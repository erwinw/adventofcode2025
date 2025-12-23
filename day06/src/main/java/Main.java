import static java.lang.IO.println;
import static java.lang.Long.parseLong;

String forgivingSubstring(String s, int startIndex, int endIndex) {
    int actualEndIndex = Math.min(endIndex, s.length());
    return s.substring(startIndex, actualEndIndex);
}

void part1(Stream<String> doc) {
    println("Part 1");
    List<String> lines = doc.toList();
    String operators = lines.getLast();
    List<String> linesMinusOperator = lines.subList(0, lines.size() - 1);
    int maxLineLength = lines.stream().mapToInt(String::length).sum();
    println("max line length: " + maxLineLength);
    int startColumn = 0;
    boolean startedAggregating = true;
    long totalResult = 0;
    // iterate beyond end to finish the last aggregation
    for (int column = 1; column <= maxLineLength + 1; column++) {
        int currentColumn = column;
        boolean allBlank = lines.stream().allMatch(l -> currentColumn >= l.length() || l.charAt(currentColumn) == ' ');
        if (allBlank) {
            // aggregate - if we even started
            if (startedAggregating) {
                startedAggregating = false;
                List<Long> operands = new ArrayList<>();
                for(String l : linesMinusOperator) {
                    String slice = forgivingSubstring(l, startColumn, currentColumn + 1).trim();
                    operands.add(parseLong(slice));
                }

                String operator = forgivingSubstring(operators, startColumn, currentColumn + 1).trim();
                long result = operator.equals("*")
                        ? operands.stream().reduce((a, b) -> a * b).orElseThrow()
                        : operands.stream().reduce(Long::sum).orElseThrow();
                totalResult += result;
            }
        } else if (!startedAggregating) {
            startedAggregating = true;
            startColumn = column;
        } // else nothing to do - just keep scanning
    }
    println("Total result " + totalResult);
}

void part2(Stream<String> doc) {
    println("Part 2");
}

void withInput(String inputType,
               Consumer<Stream<String>> consumer) {
    try (InputStream resource = getClass().getClassLoader().getResourceAsStream(inputType + ".txt")) {
        if (resource == null) {
            throw new RuntimeException("Resource not found: " + inputType + ".txt");
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            consumer.accept(bufferedReader.lines());
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

void main(String[] args) {
    println("Starting day 6 with [" + args.length + "]" + Arrays.toString(args));

    if (args.length != 2) {
        println("Usage: java Main part1|part2 testinput|input");
        return;
    }

    String part = args[0];
    println("Selected part: " + part);

    if (!part.equals("part1") && !part.equals("part2")) {
        println("Usage: java Main part1|part2 testinput|input");
        return;
    }

    String inputType = args[1];

    switch (part) {
        case "part1" -> withInput(inputType, this::part1);
        case "part2" -> withInput(inputType, this::part2);
    }
}
