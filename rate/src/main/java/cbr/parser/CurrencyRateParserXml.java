package cbr.parser;

import cbr.model.CurrencyRate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRateParserXml implements CurrencyParser{
    @Override
    public List<CurrencyRate> parse(String ratesAsString) {
        List<CurrencyRate> rates=new ArrayList<>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,"");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA,"");

        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            try (StringReader reader = new StringReader(ratesAsString)){
                Document document = documentBuilder.parse(new InputSource(reader));
                document.getDocumentElement().normalize();

                NodeList valute = document.getElementsByTagName("Valute");

                for (int i = 0; i < valute.getLength(); i++) {
                    Node node = valute.item(i);
                    if (node.getNodeType()==Node.ELEMENT_NODE){
                        Element element = (Element) node;

                        CurrencyRate rate = CurrencyRate.builder()
                                .numCode(element.getElementsByTagName("NumCode").item(0).getTextContent())
                                .charCode(element.getElementsByTagName("CharCode").item(0).getTextContent())
                                .nominal(element.getElementsByTagName("Nominal").item(0).getTextContent())
                                .name(element.getElementsByTagName("Name").item(0).getTextContent())
                                .value(element.getElementsByTagName("Value").item(0).getTextContent())
                                .build();

                        rates.add(rate);

                    }
                }


            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return rates;
    }
}
