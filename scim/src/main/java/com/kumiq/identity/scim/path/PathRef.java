package com.kumiq.identity.scim.path;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathRef {

    private final PathToken pathToken;
    private PathRef prev;
    private PathRef next;

    public static PathRef linkList(List<PathRef> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).append(list.get(i + 1));
        }
        return list.get(0).getHead();
    }

    public PathRef(PathToken token) {
        this.pathToken = token;
    }

    public PathRef getHead() {
        if (this.isHead())
            return this;

        PathRef cursor = this;
        do {
            cursor = cursor.getPrev();
        } while (!cursor.isHead());
        return cursor;
    }

    public void append(PathRef that) {
        that.setPrev(this);
        this.setNext(that);
    }

    public boolean isHead() {
        return this.prev == null;
    }

    public boolean isTail() {
        return this.next == null;
    }

    public PathToken getPathToken() {
        return pathToken;
    }

    public PathRef getPrev() {
        return prev;
    }

    public void setPrev(PathRef prev) {
        this.prev = prev;
    }

    public PathRef getNext() {
        return next;
    }

    public void setNext(PathRef next) {
        this.next = next;
    }
}
