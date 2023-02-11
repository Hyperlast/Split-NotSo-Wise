package bg.sofia.uni.fmi.mjt.splitnotsowise.utils;

public final class Validator {

    private static final String NULL_ARGUMENT_MESSAGE = "Null argument passed";

    private Validator() { }

    public static void checkIfNullArray(Object[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_MESSAGE);
        }

        for (Object obj: arr) {
            if (obj == null) {
                throw new IllegalArgumentException(NULL_ARGUMENT_MESSAGE);
            }
        }

    }
    public static void checkIfNull(Object... obj) {
        if (obj == null) {
            throw new IllegalArgumentException(NULL_ARGUMENT_MESSAGE);
        }
        for (Object o : obj) {
            if (o == null) {
                throw new IllegalArgumentException(NULL_ARGUMENT_MESSAGE);
            }
        }
    }

    public static void isBlank(String... str) {
        for (String s : str) {
            if (s.isBlank()) {
                throw new IllegalArgumentException("Empty or Blank arg passed");
            }
        }
    }

    public static boolean checkSpecificArgCount(String[] args , int expectedCount) {
        return args.length == expectedCount;
    }

    public static boolean checkArgCount(String[] args , int expectedCount) {
        return args.length >= expectedCount;
    }
}
