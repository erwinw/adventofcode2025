import java.util.ArrayList;
import java.util.stream.Stream;

import static java.lang.IO.println;
import static java.lang.Long.parseLong;

static class LongPair {
    long start;
    long finish;

    LongPair(long start, long finish) {
        this.start = start;
        this.finish = finish;
    }

    Boolean contains(long x) {
        return start <= x && x <= finish;
    }

    Boolean updateIfOverlaps(long otherStart, long otherFinish) {
        if (otherStart > finish) {
            return false;
        }
        if (otherFinish < start) {
            return false;
        }
        println("Overlaps with " + toDisplayString()+": "+otherStart + "|" + otherFinish);
        if (otherStart < start) {
            start = otherStart;
        }
        if (otherFinish > finish) {
            finish = otherFinish;
        }
        return true;
    }

    long size() {
        return finish - start + 1L;
    }

    String toDisplayString() {
        return start + "-" + finish;
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

    }

    println("Fresh count: " + freshCount);
}

void part2(Stream<String> doc) {
    println("Part 2");
    ArrayList<LongPair> freshRanges = new ArrayList<>();
    for (String line : (Iterable<String>) doc::iterator) {
        if (line.isEmpty()) {
            break;
        } else {
            println("> " + line);
            String[] parts = line.split("-", 2);
            long start = parseLong(parts[0]);
            long finish = parseLong(parts[1]);
            boolean anyOverlaps =
                    freshRanges.stream().anyMatch(r -> r.updateIfOverlaps(start, finish));
            if (!anyOverlaps) {
                LongPair pair =
                        new LongPair(
                                start,
                                finish
                        );
                freshRanges.add(pair);
            }
        }
    }
    println("\n--- Overview");
    freshRanges.forEach( r -> println(r.toDisplayString()));
    println("\n--- Cleaning");
    // now we need to merge any that overlap anyway
    ArrayList<LongPair> toRemoveRanges = new ArrayList<>();
    for (int iterTarget = 1; iterTarget < freshRanges.size(); iterTarget++) {
        LongPair rTarget = freshRanges.get(iterTarget);
        println("Checking rTarget " + rTarget.toDisplayString());
        for (int iterCandidate = 0; iterCandidate < iterTarget; iterCandidate++) {
            LongPair rCandidate  = freshRanges.get(iterCandidate);
//            println("Candidate " + rCandidate.toDisplayString());
            if (rCandidate.updateIfOverlaps(rTarget.start, rTarget.finish)) {
                println("Overlapped; scheduling to remove target " + rTarget.toDisplayString());
                toRemoveRanges.add(rTarget);
                break;
            }
        }
    }

    println("To remove " + toRemoveRanges.size());
    freshRanges.removeAll(toRemoveRanges);
    println("\nResult:");
    freshRanges.forEach( r -> println(r.toDisplayString() + ": " + r.size()));
    long ingredientCount = freshRanges.stream().mapToLong(LongPair::size).sum();
    println("Ingredient count: " + ingredientCount);
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
