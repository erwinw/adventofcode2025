import static java.lang.IO.println;
import static java.lang.Long.parseLong;

void part1(Stream<String> doc) {
    println("Part 1");

    int sumJoltage =
            doc.map(this::lineJoltage1)
                    .reduce(0, Integer::sum);
    println("Sum joltage: " + sumJoltage);
}

int lineJoltage1(String line) {
    int lineLength = line.length();
    int maxJoltage = -1;

    for (int leftCharOffset = 0; leftCharOffset < lineLength - 1; leftCharOffset++) {
        int leftCharJoltage = line.charAt(leftCharOffset) - '0';
        for (int rightCharOffset = leftCharOffset + 1; rightCharOffset < lineLength; rightCharOffset++) {
            int rightCharJoltage = line.charAt(rightCharOffset) - '0';
            int currentJoltage = leftCharJoltage * 10 + rightCharJoltage;
            if (currentJoltage > maxJoltage) {
                maxJoltage = currentJoltage;
            }
        }
    }

    println("Joltage line " + line + " = " + maxJoltage);

    return maxJoltage;
}

void part2(Stream<String> doc) {
    println("Part 2");

    long sumJoltage =
            doc.map(this::lineJoltage2)
                    .reduce(0L, Long::sum);
    println("Sum joltage: " + sumJoltage);
}

long lineJoltage2(String line) {
    String maxJoltage = buildJoltage(line, 0, 12);

    println("Joltage line " + line + " = " + maxJoltage);

    return parseLong(maxJoltage);
}

String buildJoltage(String line, int startOffset, int chars) {
    int foundOffset = -1;
    int foundCharacterJoltage = -1;
    for(int offset = startOffset; offset < line.length() - chars + 1; offset++) {
        var currentCharacter = line.charAt(offset) - '0';
        if (currentCharacter > foundCharacterJoltage) {
            foundCharacterJoltage = currentCharacter;
            foundOffset = offset;
        }
        if (foundCharacterJoltage == 9) {
            break;
        }
    }
    String tail = "";
    if (chars > 1) {
        tail = buildJoltage(line, foundOffset+1, chars - 1);
    }
    return foundCharacterJoltage + tail;
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
    println("Starting day 3 with [" + args.length + "]" + Arrays.toString(args));

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
