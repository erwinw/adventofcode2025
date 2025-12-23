import static java.lang.IO.println;

enum Field {
    FREE,
    SPLITTER,
    START,
}

void part1(Stream<String> doc) {
    println("Part 1");

    List<List<Field>> space =
            doc.map(
                    l -> l.chars()
                            .mapToObj(c ->
                                    switch (c) {
                                        case '.' -> Field.FREE;
                                        case '^' -> Field.SPLITTER;
                                        case 'S' -> Field.START;
                                        default -> throw new RuntimeException("Unexpected character " + c);
                                    }
                            )
                            .toList()
            ).toList();

    int startX = -1;
    List<Field> spaceFirst = space.getFirst();
    for (int i = 0; i < spaceFirst.size(); i++) {
        if (spaceFirst.get(i) == Field.START) {
            startX = i;
            break;
        }
    }

    println("Start X: " + startX);
    int rowCount = space.size();
    int colCount = space.getFirst().size();
    Set<Integer> beams = new HashSet<>();
    beams.add(startX);
    int splits = 0;

    for (int y=1; y<rowCount; y++) {
        Set<Integer> nextBeamsGeneration = new HashSet<>();
        for (int x: beams) {
            switch (space.get(y).get(x)) {
                case Field.FREE -> nextBeamsGeneration.add(x);
                case Field.SPLITTER -> {
                    splits++;
                    if (x>0) {
                        nextBeamsGeneration.add(x-1);
                    }
                    if (x<colCount-1) {
                        nextBeamsGeneration.add(x+1);
                    }
                }

            }
        }
        beams = nextBeamsGeneration;
    }
    println("Splits: "+splits);
}
void part2(Stream<String> doc) {
    println("Part 1");

    List<List<Field>> space =
            doc.map(
                    l -> l.chars()
                            .mapToObj(c ->
                                    switch (c) {
                                        case '.' -> Field.FREE;
                                        case '^' -> Field.SPLITTER;
                                        case 'S' -> Field.START;
                                        default -> throw new RuntimeException("Unexpected character " + c);
                                    }
                            )
                            .toList()
            ).toList();

    int startX = -1;
    List<Field> spaceFirst = space.getFirst();
    for (int i = 0; i < spaceFirst.size(); i++) {
        if (spaceFirst.get(i) == Field.START) {
            startX = i;
            break;
        }
    }

    println("Start X: " + startX);
    int rowCount = space.size();
    int colCount = space.getFirst().size();
    Map<Integer, Integer> beams = new HashMap<>();
    beams.put(startX, 1);

    for (int y=1; y<rowCount; y++) {
        List<Integer> keySet = new ArrayList<>(beams.keySet());
        for (int x: keySet) {
            int pathCount = beams.get(x);
            switch (space.get(y).get(x)) {
                case Field.FREE -> {
                    // nothing to do
                }
                case Field.SPLITTER -> {
                    if (x>0) {
                        if (beams.containsKey(x-1)) {
                            beams.put(x-1, beams.get(x-1) + pathCount);
                        } else {
                            beams.put(x-1, pathCount);
                        }
                    }
                    if (x<colCount-1) {
                        if (beams.containsKey(x+1)) {
                            beams.put(x+1, beams.get(x+1) + pathCount);
                        } else {
                            beams.put(x+1, pathCount);
                        }
                    }
                    beams.remove(x);
                }

            }
        }
    }
    int pathCount = beams.values().stream().mapToInt(i -> i).sum();
    println("Path count: "+pathCount);
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
    println("Starting day TEMPLATE with [" + args.length + "]" + Arrays.toString(args));

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
