package info.sjd;

import info.sjd.model.Product;
import info.sjd.service.FilesManager;
import info.sjd.service.NavigationPageParser;
import info.sjd.service.ProductPageParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ApplicationRunner {

    public static final Logger LOG = Logger.getLogger(ApplicationRunner.class.getName());

    public static void main( String[] args ) throws UnsupportedEncodingException {
        String keyword = "";

        if (args.length > 0){
            keyword = args[0];
        } else {
            keyword = "hp omen 15 dc0047ur";
        }


        LOG.info("Parser started");

        List<Product> products = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = Collections.synchronizedList(new ArrayList<>());

        String url = "https://prom.ua/search?search_term=" + URLEncoder.encode(keyword, "UTF-8");
        NavigationPageParser navigationPageParser = new NavigationPageParser(threads, products, url);

        threads.add(navigationPageParser);
        navigationPageParser.start();

        do {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (threadsStillWork(threads));

        products.forEach(it -> FilesManager.write(it));

        LOG.info("Parser finished");

    }

    private static boolean threadsStillWork(List<Thread> threads) {
        for (Thread thread : threads) {
            if (thread.getState().equals(Thread.State.NEW) || thread.isAlive()){
                return true;
            }
        }

        return false;
    }
}
