package com.victorhom;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    static String getTaskType(String taskName) throws IOException {
        String content = findTag(taskName).get(0);
        return content.substring(10, content.length() - 11);
    }

    static String getTaskTime(String taskName) throws IOException {
        String content = findTag(taskName).get(1);
        return content.substring(6, content.length() - 7);
    }

    static String[] getTaskDays(String taskName) throws IOException {
        String content = findTag(taskName).get(2);
        return content.substring(6, content.length() - 7).split(", ");
    }

    static String getTaskDescription(String taskName) throws IOException {
        String content = "";
        List<String> taskLines = findTag(taskName);
        for (int i = 4; i < taskLines.size() - 1; i ++) {
            content += taskLines.get(i).trim();
        }
        return content;
    }

    private static List<String> findTag(String tagToFind) throws IOException {
        String TMLFile = Files.readString(Paths.get("src/main/resources/Data/Data.txt"));
        BufferedReader reader = new BufferedReader(new StringReader(TMLFile));
        String line;
        Pattern tagNamePattern = Pattern.compile("\\s*<taskName>" + tagToFind + "</taskName>");
        Pattern closeTagPattern = Pattern.compile("\\s*</task>");
        Matcher matcher;
        while ((line = reader.readLine()) != null) {
            matcher = tagNamePattern.matcher(line);
            if (matcher.matches()) {
                List<String> buffer = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    matcher = closeTagPattern.matcher(line);
                    if (matcher.matches()) {
                        return buffer;
                    } else {
                        buffer.add(line.trim());
                    }
                }
            }
        }
        return null;
    }

    static void removeTask(String taskName) throws IOException {
        String TMLFile = Files.readString(Paths.get("src/main/resources/Data/Data.txt"));
        BufferedReader reader = new BufferedReader(new StringReader(TMLFile));
        String line;
        StringBuilder newFile = new StringBuilder();
        Pattern tagNamePattern = Pattern.compile("\\s*<taskName>" + taskName + "</taskName>");
        Pattern openTagPattern = Pattern.compile("\\s*<task>");
        Pattern closeTagPattern = Pattern.compile("\\s*</task>");
        Matcher matcher;
        Matcher nameMatcher;
        while ((line = reader.readLine()) != null) {
            matcher = openTagPattern.matcher(line);
            if (matcher.matches()) {
                StringBuilder taskBuffer = new StringBuilder();
                matcher = closeTagPattern.matcher(line);
                while (!(matcher.matches())) {
                    matcher = closeTagPattern.matcher(line);
                    nameMatcher = tagNamePattern.matcher(line);
                    if (nameMatcher.matches()) {
                        taskBuffer = new StringBuilder();
                        break;
                    }
                    taskBuffer.append(line).append(System.lineSeparator());
                    if (!(matcher.matches())) {
                        line = reader.readLine();
                    }

                }
                newFile.append(taskBuffer.toString());
            }
        }
        FileWriter writer = new FileWriter("src/main/resources/Data/Data.txt");
        writer.write(newFile.toString());
        writer.close();

    }

    static List<String> getTasks() throws IOException {
        String TMLFile = Files.readString(Paths.get("src/main/resources/Data/Data.txt"));
        BufferedReader reader = new BufferedReader(new StringReader(TMLFile));
        String line;
        List<String> result = new ArrayList<>();
        Pattern tagNamePattern = Pattern.compile("\\s*<taskName>.+</taskName>");
        Matcher matcher;
        while ((line = reader.readLine()) != null) {
            matcher = tagNamePattern.matcher(line);
            if (matcher.matches()) {
                result.add(line.trim().substring(10, line.trim().length() - 11));
            }
        }
    return result;
    }

}
