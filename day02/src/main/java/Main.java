import static java.lang.IO.println;
import static java.lang.Long.parseLong;

void part1(List<String> doc) {
    println("Part 1");
    String line = doc.getFirst();
    String[] ranges = line.split(",");

    long invalidIdsSum = 0;
    for ( String range : ranges ) {
        println("Part: " + range);
        String[] rangeParts = range.split("-");
        long lowerBound = parseLong(rangeParts[0]);
        long upperBound = parseLong(rangeParts[1]);
        for (long productId = lowerBound; productId <= upperBound; productId++) {
            boolean idInvalid = isProductIdInvalidPart1(productId);
            if (idInvalid) {
                invalidIdsSum += productId;
            }
        }
    }
    println("All done; invalid ids sum: " + invalidIdsSum + "\n");
}

boolean isProductIdInvalidPart1(long  productId) {
    String stringVersion = "" + productId;
    int halfLength = stringVersion.length() / 2;
    String head = stringVersion.substring(0, halfLength);
    String tail = stringVersion.substring(halfLength);
    boolean isInvalid = head.equals(tail);
//    println("Is invalid " + productId + " => " + stringVersion + " [" + head + " | " + tail + "] = " + isInvalid);
    return isInvalid;
}

void part2(List<String> doc) {
    println("Part 2");
    String line = doc.getFirst();
    String[] ranges = line.split(",");

    long invalidIdsSum = 0;
    for ( String range : ranges ) {
        println("Part: " + range);
        String[] rangeParts = range.split("-");
        long lowerBound = parseLong(rangeParts[0]);
        long upperBound = parseLong(rangeParts[1]);
        for (long productId = lowerBound; productId <= upperBound; productId++) {
            boolean idInvalid = isProductIdInvalidPart2(productId);
            if (idInvalid) {
                invalidIdsSum += productId;
            }
        }
    }
    println("All done; invalid ids sum: " + invalidIdsSum + "\n");
}

boolean isProductIdInvalidPart2(long  productId) {
    String stringVersion = "" + productId;
    int wholeLength = stringVersion.length();
    int halfLength = wholeLength / 2;
    for (int substringLength = 1; substringLength <= halfLength; substringLength += 1) {
        int times = wholeLength / substringLength;
        // Is string length a whole multiple of the substring length?
        if (substringLength * times != wholeLength) {
            continue;
        }
        String head = stringVersion.substring(0, substringLength);
        if (head.repeat(times).equals(stringVersion)) {
            println("Invalid: " + head + " x " + times + " => " + stringVersion);
            return true;
        }
    }
    return false;
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
    println("Starting day 2 with [" + args.length + "]" + Arrays.toString(args));

    if (args.length != 2) {
        println("Usage: java Main part1|part2 testinput.txt|input");
        return;
    }

    String part = args[0];
    println("Selected part: " + part);

    if (!part.equals("part1") && !part.equals("part2")) {
        println("Usage: java Main part1|part2 testinput.txt|input");
        return;
    }

    String inputType = args[1];
    List<String> input = readInput(inputType);

    switch(part) {
        case "part1" -> part1(input);
        case "part2" -> part2(input);
    }
}
