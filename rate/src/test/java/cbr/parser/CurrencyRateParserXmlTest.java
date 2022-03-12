package cbr.parser;


import cbr.model.CurrencyRate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyRateParserXmlTest {

    @Test
    void parseTest() throws IOException, URISyntaxException {
        //given
        var parser = new CurrencyRateParserXml();
        var uri = ClassLoader.getSystemResource("cbr.xml").toURI();
        var ratesXml = Files.readString(Paths.get(uri), StandardCharsets.UTF_16);

        //when
        var rates = parser.parse(ratesXml);
        System.out.println(rates);

        //then
        assertThat(rates.size()).isEqualTo(34);
        assertThat(rates.contains(getUSDrate())).isTrue();
        assertThat(rates.contains(getEURrate())).isTrue();
        assertThat(rates.contains(getJPYrate())).isTrue();
    }

    CurrencyRate getUSDrate() {
        return CurrencyRate.builder()
                .numCode("840")
                .charCode("USD")
                .nominal("1")
                .name("Доллар США")
                .value("30,9436")
                .build();
    }

    CurrencyRate getEURrate() {
        return CurrencyRate.builder()
                .numCode("978")
                .charCode("EUR")
                .nominal("1")
                .name("Евро")
                .value("89,4461")
                .build();
    }

    CurrencyRate getJPYrate() {
        return CurrencyRate.builder()
                .numCode("392")
                .charCode("JPY")
                .nominal("100")
                .name("Японских иен")
                .value("69,4702")
                .build();
    }
}