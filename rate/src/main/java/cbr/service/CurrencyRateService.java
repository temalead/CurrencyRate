package cbr.service;

import cbr.config.CbrConfig;
import cbr.model.CachedCurrencyRates;
import cbr.model.CurrencyRate;
import cbr.parser.CurrencyParser;
import cbr.requester.CbrRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ehcache.Cache;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyRateService {
    public static final String DATE_FORMAT = "dd/mm/yyyy";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private final CbrRequester requester;
    private final CurrencyParser parser;
    private final CbrConfig cbrConfig;
    private final Cache<LocalDate, CachedCurrencyRates> currencyRatesCache;

    public CurrencyRate getCurrencyRate(String currency, LocalDate date) throws Exception {
        List<CurrencyRate> rates;

        CachedCurrencyRates cachedCurrencyRates = currencyRatesCache.get(date);
        if (cachedCurrencyRates == null) {
            String uri = String.format("%s?date_req=%s", cbrConfig.getUrl(), DATE_TIME_FORMATTER.format(date));
            String xml = requester.getRatesAsXml(uri);
            rates = parser.parse(xml);
            currencyRatesCache.put(date, new CachedCurrencyRates(rates));

        } else {
            rates = cachedCurrencyRates.getCurrencyRates();
        }

        return rates.stream().filter(rate->currency.equals(rate.getCharCode()))
                .findFirst().orElseThrow(Exception::new);
    }
}
