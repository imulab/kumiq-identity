package com.kumiq.identity.scim.path;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class PathToken {

    private String id;

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

    public abstract String pathFragment();

    public abstract String queryFreePath();

    /**
     * Clone only {@code pathFragment}, leave the {@code prev} and {@code next} as null. Note new {@code id} will be
     * generated to differentiate cloned with the original.
     *
     * @return
     */
    public abstract PathToken cloneSelfSimple();

    /**
     * Clone {@code pathFragment} and set the {@code prev}. Then call {@lin cloneSelfAndDownStream} on every {@code next}.
     *
     * @param prev
     * @return
     */
    public abstract PathToken cloneSelfAndDownStream(PathToken prev);

    public abstract PathEvaluationContext evaluate(Map<String, Object> root, Map<String, Object> cursor);

    public abstract Object evaluateSelf(Map<String, Object> cursor);

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
     * Replace all {@code next} with clones. A link list will remain a link list. A map will become a tree.
     */
    public void replaceDownstreamWithClones() {
        if (this.getNext() == null)
            return;

        Map<PathToken, PathToken> replacement = new HashMap<>();
        this.getNext().forEach(next -> {
            PathToken nextCloned = next.cloneSelfAndDownStream(this);
            replacement.put(next, nextCloned);
        });
        replacement.forEach(this::replaceTokenAndDownstream);
    }

    /**
     * Break link with {@code originalToken} entirely and append {@code newToken} to {@code this}. This method
     * will assume downstream of {@code newToken} as its own downstream.
     *
     * @param originalToken
     * @param newToken
     */
    public void replaceTokenAndDownstream(PathToken originalToken, PathToken newToken) {
        if (this.next == null || !this.next.contains(originalToken))
            return;

        this.appendToken(newToken);
        this.next.remove(originalToken);
    }

    public void replaceToken(PathToken originalToken, PathToken newToken) {
        this.replaceTokens(originalToken, Arrays.asList(newToken));
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

    /**
     * Traverse back to root following {@code prev} and make a linked list starting from root and stops at {@code this}
     *
     * @return
     */
    public PathToken clonedSubListWithSelfAsLeaf() {
        PathToken cursor = this;
        List<PathToken> list = new ArrayList<>();

        while (cursor != null) {
            list.add(0, cursor.cloneSelfSimple());
            cursor = cursor.getPrev();
        }

        linkTokenList(list);
        return list.get(0);
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
