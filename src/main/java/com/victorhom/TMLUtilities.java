package com.victorhom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TMLUtilities {

    static String addParentTag(String content, String tag) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        StringBuilder result = new StringBuilder();
        result.append("<").append(tag).append(">").append(System.lineSeparator());
        String line;
        while ((line = reader.readLine()) != null) {
            result.append("    ").append(line).append(System.lineSeparator());
        }
        result.append("</").append(tag).append(">").append(System.lineSeparator());
        return result.toString();
    }

    static String addChildTag(String content, String tag) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("<").append(tag).append(">").append(content);
        result.append("</").append(tag).append(">").append(System.lineSeparator());
        return result.toString();
    }

    static String findTag(String TMLFile, String tagToFind) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(TMLFile));
        String line = "";
        Pattern tagNamePattern = Pattern.compile("\\s*<taskName>" + tagToFind + "</taskName>");
        Pattern openTagPattern = Pattern.compile("\\s*<task>.*");
        Pattern closeTagPattern = Pattern.compile("\\s*</task>");
        Matcher matcher;
        boolean findState = false;
        while ((line = reader.readLine()) != null) {
            matcher = tagNamePattern.matcher(line);
            if (matcher.matches()) {
                StringBuilder buffer = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    matcher = closeTagPattern.matcher(line);
                    if (matcher.matches()) {
                        return buffer.toString();
                    } else {
                        buffer.append(line.trim()).append(System.lineSeparator());
                    }
                }
            }
        }
        return null;
    }

}
