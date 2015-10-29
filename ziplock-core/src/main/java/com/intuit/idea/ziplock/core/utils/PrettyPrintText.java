package com.intuit.idea.ziplock.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/28/15
 * ************************************
 */
public class PrettyPrintText {
    private static final String BLANK = " ";
    private static final String STAR = "*";
    private static final Integer BUFFER = 2;
    final List<String> secondaryLines;
    List<String> lines;

    public PrettyPrintText() {
        lines = new ArrayList<>();
        secondaryLines = new ArrayList<>();
    }

    public static void PRINT_LOGO() {
        List<String> x = new ArrayList<>();
        x.add("███████╗██╗██████╗ ██╗      ██████╗  ██████╗██╗  ██╗");
        x.add("╚══███╔╝██║██╔══██╗██║     ██╔═══██╗██╔════╝██║ ██╔╝");
        x.add("  ███╔╝ ██║██████╔╝██║     ██║   ██║██║     █████╔╝ ");
        x.add(" ███╔╝  ██║██╔═══╝ ██║     ██║   ██║██║     ██╔═██╗ ");
        x.add("███████╗██║██║     ███████╗╚██████╔╝╚██████╗██║  ██╗");
        x.add("╚══════╝╚═╝╚═╝     ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝");
        PrettyPrintText pp = new PrettyPrintText();
        pp.setTitleLine(x);
        pp.addSecondaryLine("Give Your Data the SEAL of Approval");
        pp.print();
    }

    public void addTitleLine(String line) {
        lines.add(line);
    }

    public void setTitleLine(List<String> lines) {
        this.lines = lines;
    }

    public void addSecondaryLine(String line) {
        secondaryLines.add(line);
    }

    public void print() {
        int longestTitleLine = lines.stream()
                .mapToInt(String::length)
                .max()
                .getAsInt();
        int longestSecondaryLine = secondaryLines.stream()
                .mapToInt(String::length)
                .max()
                .getAsInt();
        int width = Math.max(longestSecondaryLine, longestTitleLine);

        List<String> idk = new ArrayList<>();

        //upper
        idk.add(IntStream.range(0, width).boxed().map(i -> STAR).collect(joining()));
        idk.add(IntStream.range(0, width).boxed().map(i -> BLANK).collect(joining()));
        idk.addAll(lines);
        idk.add(IntStream.range(0, width).boxed().map(i -> BLANK).collect(joining()));
        secondaryLines.stream()
                .map(s -> {
                    if (width > s.length()) {
                        int b = (width - s.length()) / 2;
                        StringBuilder sb = new StringBuilder();
                        String spaces = IntStream.range(0, b)
                                .boxed()
                                .map(i -> BLANK)
                                .collect(joining());
                        sb.append(spaces).append(s);
                        while (sb.length() < width) {
                            sb.append(BLANK);
                        }
                        return sb.toString();
                    } else {
                        return s;
                    }
                }).forEach(idk::add);
        //lower
        idk.add(IntStream.range(0, width).boxed().map(i -> BLANK).collect(joining()));
        idk.add(IntStream.range(0, width).boxed().map(i -> STAR).collect(joining()));
        print(idk);
    }

    private void print(List<String> idk) {
        String spaces = IntStream.range(0, BUFFER).boxed().map(i -> BLANK).collect(joining());
        String stars = IntStream.range(0, BUFFER + 1).boxed().map(i -> STAR).collect(joining());
        System.out.println(stars + idk.get(0) + stars);
        idk.subList(1, idk.size() - 1).stream()
                .map(s -> STAR + spaces + s + spaces + STAR)
                .forEach(System.out::println);
        System.out.println(stars + idk.get(idk.size() - 1) + stars);

    }
}