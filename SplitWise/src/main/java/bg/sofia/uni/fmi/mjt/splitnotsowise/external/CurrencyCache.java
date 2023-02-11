package bg.sofia.uni.fmi.mjt.splitnotsowise.external;

import bg.sofia.uni.fmi.mjt.splitnotsowise.external.response.CurrencyResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;

public class CurrencyCache {

    private static CurrencyCache currencyCache;
    private static final Currency BASE = Currency.BGN;
    private final Map<Currency, BigDecimal> cache;

    private CurrencyCache() {
        this.cache = new EnumMap<>(Currency.class);
        this.cache.put(BASE, BigDecimal.ONE);
    }

    public static CurrencyCache getInstance() {
        if (currencyCache == null) {
            currencyCache = new CurrencyCache();
        }
        return  currencyCache;
    }

    public boolean isConversionAvailable(Currency currency) {
        return cache.containsKey(currency);
    }

    public BigDecimal convertCurrencyToBase(BigDecimal amount, Currency currency) {
        if (cache.containsKey(currency)) {
            return amount.divide(cache.get(currency), 2, RoundingMode.FLOOR);
        }

        throw new IllegalArgumentException("Unsupported currency passed to method");
    }

    public String convertBaseToCurrency(BigDecimal amount, Currency currency) {
        if (cache.containsKey(currency)) {
            return amount.multiply(cache.get(currency)).setScale(2, RoundingMode.FLOOR)
                    + SPACE_DELIMITER
                    + currency.getLabel();
        }

        return amount.toString()
                + SPACE_DELIMITER
                + BASE.getLabel();
    }

    public void addRates(CurrencyResponse rates) {
        rates.rates().forEach((key, value) -> {
            Currency currency = Currency.parseLabel(key);
            BigDecimal rate = new BigDecimal(value);
            cache.put(currency, rate);
        });
    }

}
