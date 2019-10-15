package info.sjd.service;

import info.sjd.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ProductPageParser extends Thread {

    private List<Product> products;
    private final String url;

    public ProductPageParser(List<Product> products, String url) {
        this.products = products;
        this.url = url;
    }

    @Override
    public void run() {

        try {
            Document document = Jsoup.connect(url).get();
            Element productInfo = document.getElementsByAttributeValue("data-qaid", "main_product_info").first();

            String code = getCode(productInfo);
            String name = getProductName(productInfo);
            BigDecimal price = getPrice(productInfo);
            BigDecimal initPrice = getInitPrice(productInfo, price);
            String avialability = getAvialability(productInfo);
            String url = this.url;

            Product product = new Product(code, name, price, initPrice, avialability, url);

            products.add(product);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal getInitPrice(Element productInfo, BigDecimal currentPrice) {
        Elements priceElements = productInfo.getElementsByAttributeValue("data-qaid", "price_without_discount");
        if (!priceElements.isEmpty()) {
            Element priceElement = priceElements.first();
            String priceAsText = priceElement.attr("data-qaprice");
            if (priceAsText != null && !(priceAsText.replaceAll("\\D", "").isEmpty())) {
                double price = Double.valueOf(priceAsText);
                return new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return currentPrice;
    }

    private BigDecimal getPrice(Element productInfo) {
        Elements priceElements = productInfo.getElementsByAttributeValue("data-qaid", "product_price");
        if (!priceElements.isEmpty()) {
            Element priceElement = priceElements.first();
            String priceAsText = priceElement.attr("data-qaprice");
            if (priceAsText != null && !(priceAsText.replaceAll("\\D", "").isEmpty())) {
               double price = Double.valueOf(priceAsText);
               return new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    private String getAvialability(Element productInfo) {
        Elements codeElements = productInfo.getElementsByAttributeValue("data-qaid", "product_presence");
        if (codeElements.isEmpty()) {
            return "";
        }
        return codeElements.first().text();
    }

    private String getProductName(Element productInfo) {
        Elements codeElements = productInfo.getElementsByAttributeValue("data-qaid", "product_name");
        if (codeElements.isEmpty()) {
            return "";
        }
        return codeElements.first().text();
    }

    private String getCode(Element productInfo) {
        Elements codeElements = productInfo.getElementsByAttributeValue("data-qaid", "product-sku");
        if (codeElements.isEmpty()) {
            return "";
        }
        return codeElements.first().text();
    }
}
