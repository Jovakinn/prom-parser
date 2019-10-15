package info.sjd.service;

import info.sjd.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class NavigationPageParser extends Thread {

    private List<Thread> threads;
    private List<Product> products;

    public final String url;

    public NavigationPageParser(List<Thread> threads, List<Product> products, String url) {
        this.threads = threads;
        this.products = products;
        this.url = url;
    }

    @Override
    public void run() {

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Elements productLinkElements = document.getElementsByAttribute("data-product-url");
            for (Element element : productLinkElements ) {
                String productUrl = element.attr("data-product-url");
                if (productUrl != null && !productUrl.isEmpty()){
                    ProductPageParser productPageParser = new ProductPageParser(products, productUrl);
                    threads.add(productPageParser);
                    productPageParser.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (url.contains("page=")) {
            Integer lastPage = 13;

            for (int i = 2; i <= lastPage; i++) {

                String nextPageUrl =  url + "&page=" + i;
                NavigationPageParser navigationPageParser = new NavigationPageParser(threads, products, nextPageUrl);

                threads.add(navigationPageParser);
                navigationPageParser.start();
            }
        }

    }
}
