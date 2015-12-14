package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.resource.misc.Schema;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class PathToken {

    private String id;
    private Schema.Attribute attribute;
    private PathToken prev;
    private List<PathToken> next;

    protected PathToken() {
        this.id = UUID.randomUUID().toString();
    }

    public boolean isLeaf() {
        return next == null || next.size() == 0;
    }

    public boolean isRoot() {
        return prev == null;
    }

    public PathToken getRoot() {
        PathToken cursor = this;
        while (!cursor.isRoot())
            cursor = cursor.getPrev();
        return cursor;
    }

    public String apiAttributeName() {
        return (this.attribute != null) ? this.attribute.getName() : this.queryFreePath();
    }

    public String modelAttributeName() {
        return (this.attribute != null) ?
                (this.attribute.getProperty() != null ?
                        this.attribute.getProperty() :
                        apiAttributeName()) :
                apiAttributeName();
    }

    public String attributeName(Configuration configuration) {
        return configuration.getOptions().contains(Configuration.Option.API_ATTR_NAME_PREF) ?
                apiAttributeName() :
                modelAttributeName();
    }

    public abstract String pathFragment();

    public abstract String queryFreePath();

    public abstract Object evaluate(Object cursor, Configuration configuration);

    public boolean isSimplePath() {
        return false;
    }

    public boolean isPathWithIndex() {
        return false;
    }

    public boolean isPathWithFilter() {
        return false;
    }

    /**
     * Set {@code this} as the {@code prev} of the {@code pathToken} and {@pathToken} as one of the {@code next}
     * of {@code this}.
     *
     * @param pathToken
     */
    public void appendToken(PathToken pathToken) {
        if (this.next == null)
            this.next = new LinkedList<>();
        this.next.add(pathToken);
        pathToken.setPrev(this);
    }

    /**
     * Setup {@code prev} and {@code next} relationships for all tokens in {@code tokenList} in order.
     *
     * @param tokenList
     */
    public static void linkTokenList(List<PathToken> tokenList) {
        for (int i = 0; i < tokenList.size() - 1; i++) {
            tokenList.get(i).appendToken(tokenList.get(i+1));
        }
    }

    /**
     * Replace {@code originalToken} with a list of new tokens in {@code newTokens}. This method assume the downstream
     * of {@code originalToken} as the downstream of very new token in {@code newTokens}.
     *
     * @param originalToken
     * @param newTokens
     */
    public void replaceTokens(PathToken originalToken, List<? extends PathToken> newTokens) {
        if (this.next == null || !this.next.contains(originalToken))
            return;

        for (PathToken newToken : newTokens) {
            newToken.setPrev(this);
            newToken.setNext(originalToken.getNext());
        }

        next.addAll(newTokens);
        next.remove(originalToken);
    }

    /**
     * Traverse and report every possible paths
     */
    public List<PathRef> traverse() {
        List<List<PathToken>> paths = new ArrayList<>();
        traverse(new ArrayList<>(), paths);

        List<PathRef> heads = new ArrayList<>();
        for (List<PathToken> path : paths) {
            PathRef head = PathRef.linkList(path.stream().map(PathRef::new).collect(Collectors.toList()));
            heads.add(head);
        }

        return heads;
    }

    private void traverse(List<PathToken> path, List<List<PathToken>> paths) {
        path.add(this);
        if (this.isLeaf()) {
            paths.add(path);
        } else {
            for (PathToken next : this.getNext()) {
                next.traverse(new ArrayList<>(path), paths);
            }
        }
    }

    public String getId() {
        return id;
    }

    public List<PathToken> getNext() {
        return next;
    }

    public PathToken firstNext() {
        return (next != null && next.size() > 0) ? next.get(0) : null;
    }

    public void setNext(List<PathToken> next) {
        this.next = next;
    }

    public PathToken getPrev() {
        return prev;
    }

    public void setPrev(PathToken prev) {
        this.prev = prev;
    }

    public Schema.Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Schema.Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathToken)) return false;

        PathToken pathToken = (PathToken) o;

        return id.equals(pathToken.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
