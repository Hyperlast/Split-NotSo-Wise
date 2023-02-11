package bg.sofia.uni.fmi.mjt.splitnotsowise.external.response;

public enum ErrorResponse {
    NOT_FOUND(404, "Resource not found."
            + System.lineSeparator()),
    MISSING_API(101, "No API Key was specified or an invalid API Key was specified."
            + System.lineSeparator()),
    INACTIVE_ACCOUNT(102, "The account this API request is coming from is inactive."
            + System.lineSeparator()),
    NON_EXISTENT_ENDPOINT(103, "The requested API endpoint does not exist."
            + System.lineSeparator()),
    REQUEST_CAPACITY_REACHED(104, "The maximum allowed API amount of monthly "
            + "API requests has been reached."
            + System.lineSeparator()),
    UNSUPPORTED_ENDPOINT(105, "The current subscription plan does not support this API endpoint."
            + System.lineSeparator()),
    EMPTY_RESULT(106, "The current request did not return any results."
            + System.lineSeparator()),
    INVALID_BASE(201, "An invalid base currency has been entered."
            + System.lineSeparator()),
    INVALID_SYMBOLS(202, "One or more invalid symbols have been specified."
            + System.lineSeparator()),
    UNSPECIFIED_DATE(301, "No date has been specified. [historical]"
            + System.lineSeparator()),
    INVALID_DATE(302, "An invalid date has been specified. [historical, convert]"
            + System.lineSeparator()),
    INVALID_AMOUNT(403, "No or an invalid amount has been specified. [convert]"
            + System.lineSeparator()),
    INVALID_TIMEFRAME(501, "No or an invalid timeframe has been specified. [timeseries]"
            + System.lineSeparator()),
    INVALID_START_DATE(502, "No or an invalid \"start_date\" has been specified. [timeseries, fluctuation]"
            + System.lineSeparator()),
    INVALID_END_DATE(503, "No or an invalid \"end_date\" has been specified. [timeseries, fluctuation]"
            + System.lineSeparator()),
    INVALID_TIMEFRAME_SPECIFIED(504, "An invalid timeframe has been specified. [timeseries, fluctuation]"
            + System.lineSeparator()),
    LARGE_TIMEFRAME(505, "The specified timeframe is too long, "
            + "exceeding 365 days. [timeseries, fluctuation]"
            + System.lineSeparator());

    private final int code;
    private final String message;

    private ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }


    public String getMessage() {
        return this.message;
    }
}
