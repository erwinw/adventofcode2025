import static java.lang.IO.println;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

static class LongPair {
    long a;
    long b;

    LongPair(long a, long b) {
        this.a = a;
        this.b = b;
    }

    Boolean contains(long x) {
        return a <= x && x <= b;
    }
}

void part1(Stream<String> doc) {
    println("Part 1");
    ArrayList<LongPair> freshRanges = new ArrayList<>();
    int freshCount = 0;
    boolean gotBlank = false;
    for (String line : (Iterable<String>) doc::iterator) {
        if (gotBlank) {
            long ingredient = parseLong(line);
            boolean anyMatch = freshRanges.stream().anyMatch(range -> range.contains(ingredient));
            if (anyMatch) {
                freshCount++;
                println("Fresh: " + ingredient);
            }
        } else if (line.isEmpty()) {
            gotBlank = true;
        } else {
            String[] parts = line.split("-", 2);
            LongPair pair =
                    new LongPair(
                            parseLong(parts[0]),
                            parseLong(parts[1])
                    );
            freshRanges.add(pair);
        }

    };
    println("Fresh count: " + freshCount);
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
    println("Starting day 5 with [" + args.length + "]" + Arrays.toString(args));

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
