package cbr.parser;

import cbr.model.CurrencyRate;

import java.util.List;

public interface CurrencyParser {
    List<CurrencyRate> parse(String rateAsString);
}
