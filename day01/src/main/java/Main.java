import static java.lang.IO.println;

void part1(List<String> doc) {
    println("Part 1, have " + doc.size() + " lines");
    int position = 50;
    int zeroCount = 0;
    for (String line : doc) {
        char direction = line.charAt(0);
        int amount = Integer.parseInt(line.substring(1));
        println("Line : " + line + " Direction: " + direction + " Amount: " + amount);
        switch (direction) {
            case 'L' -> position -= amount;
            case 'R' -> position += amount;
        }

        // Normalize position to be within 0-99
        while (position < 0) {
            position += 100;
        }
        position = position % 100;

        if (position == 0) {
            zeroCount++;
        }
    }
    println("Final position: " + position);
    println("Zero crossings: " + zeroCount);
}

void part2(List<String> doc) {
    println("Part 2");
}

List<String> readInput(String inputType) {
    try (InputStream resource = getClass().getClassLoader().getResourceAsStream(inputType + ".txt")) {
        if (resource == null) {
            throw new RuntimeException("Resource not found: " + inputType + ".txt");
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.lines().toList();
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

void main(String[] args) {
    println("Starting day 1 with [" + args.length + "]" + Arrays.toString(args));

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
    List<String> doc = readInput(inputType);

    switch(part) {
        case "part1" -> part1(doc);
        case "part2" -> part2(doc);
    }
}
