package info.sjd.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtil {

    public static String getEncodedString(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
