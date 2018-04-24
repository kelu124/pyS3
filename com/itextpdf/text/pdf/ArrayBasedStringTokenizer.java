package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayBasedStringTokenizer {
    private final Pattern regex;

    public ArrayBasedStringTokenizer(String[] tokens) {
        this.regex = Pattern.compile(getRegexFromTokens(tokens));
    }

    public String[] tokenize(String text) {
        List<String> tokens = new ArrayList();
        Matcher matcher = this.regex.matcher(text);
        int endIndexOfpreviousMatch = 0;
        while (matcher.find()) {
            String previousToken = text.substring(endIndexOfpreviousMatch, matcher.start());
            if (previousToken.length() > 0) {
                tokens.add(previousToken);
            }
            tokens.add(matcher.group());
            endIndexOfpreviousMatch = matcher.end();
        }
        String tail = text.substring(endIndexOfpreviousMatch, text.length());
        if (tail.length() > 0) {
            tokens.add(tail);
        }
        return (String[]) tokens.toArray(new String[0]);
    }

    private String getRegexFromTokens(String[] tokens) {
        StringBuilder regexBuilder = new StringBuilder(100);
        for (String token : tokens) {
            regexBuilder.append("(").append(token).append(")|");
        }
        regexBuilder.setLength(regexBuilder.length() - 1);
        return regexBuilder.toString();
    }
}
