package com.kumiq.identity.scim.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generic tokenizer providing functionality for {@link com.kumiq.identity.scim.path.Tokenizer.PathTokenizer}
 *
 *
 * Subclass can call {@code nextSequence} to obtain the next token and call {@link i}
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class Tokenizer {

    private static final String PERIOD = ".";
    private static final String SPACE = " ";

    private final CharSequence charSequence;
    private List<CharSequence> delimiters;
    private List<CharSequence> keywordTokens;
    private int cursorStartPos;
    private int cursorEndPos;
    private boolean lastMatchWasDelimiter = true;
    private boolean sequenceHasExhausted = false;

    protected Tokenizer(CharSequence charSequence) {
        this.charSequence = charSequence;
        this.delimiters = Arrays.asList(PERIOD);
        this.keywordTokens = new ArrayList<>();
        this.cursorEndPos = 0;
        syncStartPosition();
    }

    public CharSequence nextSequence() throws NoMoreSequenceException {
        if (sequenceHasExhausted) {
            throw new NoMoreSequenceException();
        }

        try {
            while (isEndPosInBounds()) {
                CharSequence current = currentSubSequence();

                for (CharSequence delimiter : delimiters) {
                    if (currentMatches(delimiter)) {
                        lastMatchWasDelimiter = true;
                        incrementPosition(delimiter.length());
                        syncStartPosition();

                        if (current.length() > 0)
                            return current;
                        else
                            continue;
                    }
                }

                for (CharSequence keywordToken : keywordTokens) {
                    if ((currentMatches(keywordToken) && lastMatchWasDelimiter) ||
                            keywordToken.equals(current)) {
                        lastMatchWasDelimiter = false;
                        syncStartPosition();

                        if (current.length() > 0)
                            return current;
                        else
                            continue;
                    }
                }

                incrementPosition(1);
            }

            sequenceHasExhausted = true;
            CharSequence remaining = this.charSequence.subSequence(this.cursorStartPos, this.charSequence.length());
            if (remaining.length() > 0)
                return remaining;
            else
                throw new NoMoreSequenceException();
        } catch (IndexOutOfBoundsException ex) {
            throw new NoMoreSequenceException();
        }
    }

    public void syncStartPosition() {
        this.cursorStartPos = this.cursorEndPos;
    }

    public int incrementPosition(int count) {
        this.cursorEndPos += count;
        return this.cursorEndPos;
    }

    public CharSequence currentSubSequence() {
        return this.charSequence.subSequence(this.cursorStartPos, this.cursorEndPos);
    }

    public boolean currentMatches(CharSequence symbol) {
        if (!inBounds(this.cursorEndPos + symbol.length())) {
            return false;
        }

        return symbol.equals(this.charSequence.subSequence(this.cursorEndPos, this.cursorEndPos + symbol.length()));
    }

    public boolean isEndPosInBounds() {
        return inBounds(this.cursorEndPos);
    }

    public boolean inBounds(int index) {
        return (index >= 0) && (index <= this.charSequence.length());
    }

    public List<CharSequence> getDelimiters() {
        return delimiters;
    }

    public void setDelimiters(List<CharSequence> delimiters) {
        this.delimiters = delimiters;
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public List<CharSequence> getKeywordTokens() {
        return keywordTokens;
    }

    public void setKeywordTokens(List<CharSequence> keywordTokens) {
        this.keywordTokens = keywordTokens;
    }

    /**
     * Tokenizer for SCIM paths. For example, path like <code>name.firstName</code> will be tokenized
     * to <code>name</code> and <code>firstName</code>
     *
     */
    public static class PathTokenizer extends Tokenizer {

        public PathTokenizer(CharSequence charSequence) {
            super(charSequence);
            super.setDelimiters(Arrays.asList(PERIOD));
        }
    }

    /**
     * Tokenizer for SCIM filters. For example, <code>(value eq 100) and (name sw "A")</code>.
     */
    public static class FilterTokenizer extends Tokenizer {

        public static final String LEFT_BRACKET = "(";
        public static final String RIGHT_BRACKET = ")";
        public static final String LEFT_SQUARE_BRACKET = "[";
        public static final String RIGHT_SQUARE_BRACKET = "]";

        public FilterTokenizer(CharSequence charSequence) {
            super(charSequence);
            super.setDelimiters(Arrays.asList(SPACE, LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET));
            super.setKeywordTokens(Arrays.asList(
                    LEFT_BRACKET,
                    RIGHT_BRACKET
            ));
        }
    }

    /**
     * Placeholder exception thrown when token sequence is exhausted.
     */
    public static class NoMoreSequenceException extends Exception {
    }
}
