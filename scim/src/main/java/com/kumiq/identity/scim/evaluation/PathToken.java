package com.kumiq.identity.scim.evaluation;

import java.util.*;

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

    public void appendToken(PathToken pathToken) {
        if (this.next == null)
            this.next = new LinkedList<>();
        this.next.add(pathToken);
        pathToken.setPrev(this);
    }

    public void replaceToken(PathToken originalToken, PathToken newToken) {
        this.replaceTokens(originalToken, Arrays.asList(newToken));
    }

    public void replaceTokens(PathToken originalToken, List<PathToken> newTokens) {
        if (this.next == null || !this.next.contains(originalToken))
            return;

        for (PathToken newToken : newTokens) {
            newToken.setPrev(this);
            newToken.setNext(originalToken.getNext());
        }

        next.addAll(newTokens);
        next.remove(originalToken);
    }

    public List<PathToken> getNext() {
        return next;
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
