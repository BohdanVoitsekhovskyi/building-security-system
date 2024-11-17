package com.building_security_system.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SvgToJsonParser {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JsonContent {
        public static int counter = 0;
        private String type;
        private Map<String, GeometryCollection> objects;
        private List<List<List<Integer>>> arcs;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class GeometryCollection implements Comparable<GeometryCollection> {
            private String type = "GeometryCollection";
            private List<Geometry> geometries;

            @Override
            public int compareTo(GeometryCollection o) {
                return Integer.compare(this.geometries.getFirst().properties.id,
                        o.getGeometries().getFirst().properties.id);
            }

            @Data
            public static class Geometry {
                private String type = "Polygon";
                private int[][] arcs = new int[1][1];
                private Properties properties;

                public Geometry() {
                    ++counter;
                }

                public Geometry(String type, int[][] arcs, Properties properties) {
                    this.type = type;
                    this.arcs = arcs;
                    this.properties = properties;

                    ++counter;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Properties {
                    private int id;
                    private String type;
                }
            }
        }
    }

    public static JsonContent parseToJson(String fileContent) {
        JsonContent jsonContent = new JsonContent("Topology", new HashMap<>(), new ArrayList<>());
        List<String> pathTags = extractPathTags(fileContent);

        for (String pathTag : pathTags) {

            processIdAttribute(jsonContent, pathTag);

            processCoordinates(jsonContent, pathTag);
        }

        return jsonContent;
    }

    private static List<String> extractPathTags(String fileContent) {
        Pattern pattern = Pattern.compile("<path[^>]*/>");
        Matcher matcher = pattern.matcher(fileContent);

        List<String> pathTags = new ArrayList<>();
        while (matcher.find()) {
            pathTags.add(matcher.group());
        }
        return pathTags;
    }

    private static void processIdAttribute(JsonContent jsonContent, String pathTag) {
        Pattern idPattern = Pattern.compile("id=\"([a-zA-Z]+)\\d+\"");
        Matcher idMatcher = idPattern.matcher(pathTag);

        while (idMatcher.find()) {
            String type = idMatcher.group(1);

            jsonContent.objects
                    .computeIfAbsent(type, key -> new JsonContent.GeometryCollection("GeometryCollection", new ArrayList<>()))
                    .geometries.add(new JsonContent.GeometryCollection.Geometry(
                            "Polygon",
                            new int[][]{{JsonContent.counter}},
                            new JsonContent.GeometryCollection.Geometry.Properties(JsonContent.counter, type)
                    ));
        }
    }

    private static void processCoordinates(JsonContent jsonContent, String pathTag) {
        Pattern dPattern = Pattern.compile("d=\"([^\"]+)\"");
        Matcher dMatcher = dPattern.matcher(pathTag);

        if (dMatcher.find()) {
            String dValue = dMatcher.group(1);

            List<Integer> numbers = extractNumbers(dValue);
            List<Character> symbols = extractSymbols(dValue);

            jsonContent.arcs.add(getArc(symbols, numbers));
        }
    }

    private static List<Integer> extractNumbers(String dValue) {
        Pattern numberPattern = Pattern.compile("-?\\d+(?:\\.\\d+)?");
        Matcher numberMatcher = numberPattern.matcher(dValue);

        List<Integer> numbers = new ArrayList<>();
        while (numberMatcher.find()) {
            String numberStr = numberMatcher.group();
            numbers.add(numberStr.contains(".")
                    ? Integer.parseInt(numberStr.split("\\.")[0])
                    : Integer.parseInt(numberStr));
        }
        return numbers;
    }

    private static List<Character> extractSymbols(String dValue) {
        Pattern symbolPattern = Pattern.compile("[A-Za-z]");
        Matcher symbolMatcher = symbolPattern.matcher(dValue);

        List<Character> symbols = new ArrayList<>();
        while (symbolMatcher.find()) {
            symbols.add(symbolMatcher.group().charAt(0));
        }
        return symbols;
    }



    public static String readSvgFile(String path) {
        FileReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        while(true) {
            try {
                if (!bufferedReader.ready()) break;
                builder.append(bufferedReader.readLine()).append('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return builder.toString();
    }
    public static List<List<Integer>> getArc(List<Character> symbols, List<Integer> numbers) {

        List<List<Integer>> result = new ArrayList<>();
        int x = numbers.get(0);
        int y = numbers.get(1);
        List<Integer> tmp = List.of(x, y);
        result.add(tmp);

        for (int i = 2; i < numbers.size(); i++) {
            if (symbols.get(i - 1) == 'v') {
                y += numbers.get(i);
            } else if (symbols.get(i - 1) == 'h'){
                x += numbers.get(i);
            } else if (symbols.get(i - 1) == 'V') {
                y = numbers.get(i);
            } else if (symbols.get(i - 1) == 'H') {
                x = numbers.get(i);
            }

            result.add(List.of(x, y));
        }
        result.add(tmp);

        return result;
    }
}