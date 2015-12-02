package com.kumiq.identity.scim.evaluation;

import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A path token with index component after the path
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PathWithIndexToken extends SimplePathToken {

    private static final Pattern pattern = Pattern.compile("(.*?)\\[([0-9]+)\\]");

    private final String pathComponent;
    private final Integer indexComponent;

    public PathWithIndexToken(String pathWithIndex) {
        super(pathWithIndex);
        Matcher matcher = pattern.matcher(pathWithIndex);
        if (matcher.find()) {
            Assert.isTrue(matcher.groupCount() == 3, pathWithIndex + " is not a valid path with index token");
            this.pathComponent = matcher.group(1);
            this.indexComponent = Integer.parseInt(matcher.group(2));
        } else {
            throw new IllegalArgumentException(pathWithIndex + " is not a valid path with index token");
        }
    }
}
