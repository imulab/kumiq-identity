package com.kumiq.identity.scim.evaluation;

import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A path token with filter component after the path
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathWithFilterToken extends SimplePathToken {

    private static final Pattern pattern = Pattern.compile("(.*?)\\[(.*?)\\]");

    private final String pathComponent;
    private final String filterComponent;

    public PathWithFilterToken(String pathWithFilter) {
        super(pathWithFilter);
        Matcher matcher = pattern.matcher(pathWithFilter);
        if (matcher.find()) {
            Assert.isTrue(matcher.groupCount() == 3, pathWithFilter + " is not a valid path with filter token");
            this.pathComponent = matcher.group(1);
            this.filterComponent = matcher.group(2);
        } else {
            throw new IllegalArgumentException(pathWithFilter + " is not a valid path with filter token");
        }
    }
}
