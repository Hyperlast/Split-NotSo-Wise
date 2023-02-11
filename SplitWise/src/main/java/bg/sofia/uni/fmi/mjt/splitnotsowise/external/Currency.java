package bg.sofia.uni.fmi.mjt.splitnotsowise.external;

public enum Currency {
    USD("USD"),
    EUR("EUR"),
    BGN("BGN"),
    UNKNOWN("Unsupported or Unknown");

    private final String label;

    private Currency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Currency parseLabel(String currency) {
        return switch(currency) {
            case "USD" -> USD;
            case "EUR" -> EUR;
            case "BGN" -> BGN;
            default -> UNKNOWN;
        };
    }
}
