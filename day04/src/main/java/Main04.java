import static java.lang.IO.print;
import static java.lang.IO.println;

static final int FREE = 0;
static final int OCCUPIED = 1;
static final int VISIBLE = 2;

void part1(Stream<String> doc) {
    println("Part 1");

    List<ArrayList<Boolean>> map = doc.map(
            line ->
                    new ArrayList<>(
                            line.chars()
                                    .mapToObj(c -> (char) c).toList()
                                    .stream().map(character -> character == '@')
                                    .toList())
    ).toList();

    printMap(map);

    List<List<Integer>> visibles = buildVisibles(map);

    long visibleCount =
            visibles.stream()
                    .map(row ->
                            row.stream()
                                    .filter(cell -> cell == VISIBLE)
                                    .count()
                    ).reduce(0L, Long::sum);

    println("Visible count: " + visibleCount);
}

private List<List<Integer>> buildVisibles(List<ArrayList<Boolean>> map) {
    int colCount = map.getFirst().size();
    List<List<Integer>> visibles = new ArrayList<>(map.size());
    for (int row = 0; row < map.size(); row++) {
        List<Integer> rowCounts = new ArrayList<>(colCount);
        for (int col = 0; col < colCount; col++) {
            rowCounts.add(FREE);
        }
        visibles.add(rowCounts);
    }

    for (int y = 0; y < map.size(); y++) {
        for (int x = 0; x < colCount; x++) {
            if (!map.get(y).get(x)) {
                visibles.get(y).set(x, FREE);
            } else if (isVisible(map, y, x)) {
                visibles.get(y).set(x, VISIBLE);
            } else {
                visibles.get(y).set(x, OCCUPIED);
            }
        }
    }

    return visibles;
}

private boolean isVisible(List<ArrayList<Boolean>> map, int y, int x) {
    int visibleCount = 0;
    for (int offsetY = -1; offsetY <= 1; offsetY++) {
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            if (offsetX == 0 && offsetY == 0) {
                continue;
            }
            Boolean cellValue;
            int checkY = y + offsetY;
            int checkX = x + offsetX;
            try {
                cellValue = map.get(checkY).get(checkX);
            } catch (IndexOutOfBoundsException ex) {
                continue;
            }
            if (cellValue) {
                visibleCount += 1;
            }
        }
    }
    return visibleCount < 4;
}

private void printMap(List<ArrayList<Boolean>> map) {
    for (List<Boolean> row : map) {
        for (Boolean cell : row) {
            if (cell) {
                print("@");
            } else {
                print(".");
            }

        }
        println("");
    }
    println("");
}

void part2(Stream<String> doc) {
    println("Part 2");

    List<ArrayList<Boolean>> map = doc.map(
            line ->
                    new ArrayList<>(
                            line.chars()
                                    .mapToObj(c -> (char) c).toList()
                                    .stream().map(character -> character == '@')
                                    .toList())
    ).toList();

    int rowCount = map.size();
    int columnCount = map.getFirst().size();
    int removedCount = 0;
    boolean updated;

    do {
        updated = false;
        List<List<Integer>> visibles = buildVisibles(map);

        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++) {
                if (visibles.get(y).get(x) == VISIBLE) {
                    removedCount++;
                    updated = true;
                    map.get(y).set(x, false);
                }
            }
        }
        println("Finished cycle; updated? " + updated + "; total " + removedCount);
    } while(updated);

    println("Removed in total " + removedCount);
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
    println("Starting day 4 with [" + args.length + "]" + Arrays.toString(args));

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
