package bg.sofia.uni.fmi.mjt.splitnotsowise.external.response;

import com.google.gson.annotations.Expose;

import java.util.Map;

public record CurrencyResponse(@Expose Map<String, String> rates) {
}
