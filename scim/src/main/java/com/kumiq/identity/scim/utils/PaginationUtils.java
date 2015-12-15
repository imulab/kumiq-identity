package com.kumiq.identity.scim.utils;

import java.util.Collections;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PaginationUtils {

    public static <T> List<T> subList(List<T> input, int startIndex, int count) {
        int fromIndex = startIndex - 1;
        int toIndex = fromIndex + count;
        if (toIndex >= input.size()) {
            toIndex = input.size();
        }
        if (fromIndex >= toIndex) {
            return Collections.emptyList();
        }
        return input.subList(fromIndex, toIndex);
    }
}
