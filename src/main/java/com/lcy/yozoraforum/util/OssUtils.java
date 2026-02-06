package com.lcy.yozoraforum.util;

import java.net.URI;
import java.net.URISyntaxException;

public class OssUtils {

    /**
     * 拆分url 链接
     * @param url
     * @return
     */
    public static String extractObjectName(String url){
        try {
            URI uri = new URI(url);
            return uri.getPath().substring(1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("非法 OSS URL");
        }
    }
}
