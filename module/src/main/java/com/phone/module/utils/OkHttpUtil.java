package com.phone.module.utils;


import com.alibaba.fastjson.JSONObject;

import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class OkHttpUtil {
    @Resource
    private OkHttpClient okHttpClient;



    public static List<String> userAgent=Arrays.asList(
            "Mozilla/5.0 (Amiga; U; AmigaOS 1.3; en; rv:1.8.1.19) Gecko/20081204 SeaMonkey/1.1.14",
            "Mozilla/5.0 (AmigaOS; U; AmigaOS 1.3; en-US; rv:1.8.1.21) Gecko/20090303 SeaMonkey/1.1.15",
            "Mozilla/5.0 (AmigaOS; U; AmigaOS 1.3; en; rv:1.8.1.19) Gecko/20081204 SeaMonkey/1.1.14",
            "Mozilla/5.0 (Android 2.2; Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.19.4 (KHTML, like Gecko) Version/5.0.3 Safari/533.19.4",
            "Mozilla/5.0 (BeOS; U; BeOS BeBox; fr; rv:1.9) Gecko/2008052906 BonEcho/2.0",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.8.1.1) Gecko/20061220 BonEcho/2.0.0.1",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.8.1.10) Gecko/20071128 BonEcho/2.0.0.10",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.8.1.17) Gecko/20080831 BonEcho/2.0.0.17",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.8.1.6) Gecko/20070731 BonEcho/2.0.0.6",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.8.1.7) Gecko/20070917 BonEcho/2.0.0.7",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.8.1b2) Gecko/20060901 Firefox/2.0b2",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.9a1) Gecko/20051002 Firefox/1.6a1",
            "Mozilla/5.0 (BeOS; U; BeOS BePC; en-US; rv:1.9a1) Gecko/20060702 SeaMonkey/1.5a",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
            "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24",
            "Mozilla/5.0 (Macintosh; U; Mac OS X Mach-O; en-US; rv:2.0a) Gecko/20040614 Firefox/3.0.0 ",
            "Mozilla/5.0 (Macintosh; U; PPC Mac OS X 10.5; en-US; rv:1.9.0.3) Gecko/2008092414 Firefox/3.0.3",
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.1) Gecko/20090624 Firefox/3.5",
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.14) Gecko/20110218 AlexaToolbar/alxf-2.0 Firefox/3.6.14",
            "Mozilla/5.0 (Macintosh; U; PPC Mac OS X 10.5; en-US; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
            "Opera/9.80 (Android 2.3.4; Linux; Opera mobi/adr-1107051709; U; zh-cn) Presto/2.8.149 Version/11.10",
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/531.21.8 (KHTML, like Gecko) Version/4.0.4 Safari/531.21.10",
            "Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.19.4 (KHTML, like Gecko) Version/5.0.2 Safari/533.18.5",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)"
    );
    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
//    @Retryable(value = Exception.class)
    public String get(String url, Map<String, String> queries, Map<String, String> headerMap) throws Exception {
        String responseBody = "";
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        Builder build=new Builder()
                .url(sb.toString());
        if(headerMap!=null) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = build.build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            //if (status == 200) {
            return response.body().string();
            //}
        } catch (Exception e) {
//            System.out.println("okhttp put error >> ex = {}"+ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }



    public String getByProxy(String url, Map<String, String> queries, Map<String, String> headerMap,String proxyIp,String proxyPort)throws Exception {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, Integer.parseInt(proxyPort)));
        OkHttpClient client=okHttpClient.newBuilder().proxy(proxy).build();

        String responseBody = "";
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        Builder build=new Builder()
                .url(sb.toString());

        Collections.shuffle(userAgent);
        build.addHeader("user-agent", userAgent.get(0));
        if(headerMap!=null) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = build.build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            int status = response.code();
            //if (status == 200) {
            return response.body().string();
            //}
        } catch (Exception e) {
//            System.out.println("okhttp put error >> ex = {}"+ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String post(String url, Map<String, Object> params, Map<String, String> headerMap) {
        String responseBody = "";
//        FormBody.Builder builder = new FormBody.Builder();
//        //添加参数
//        if (params != null && params.keySet().size() > 0) {
//            for (String key : params.keySet()) {
//                builder.add(key, params.get(key));
//            }
//        }
        JSONObject jsonObject = new JSONObject(params);
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json"),jsonObject.toJSONString().getBytes(Charset.forName("UTF-8")));

        Builder build= new Builder()
                .url(url);
        if(headerMap!=null) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = build
                .post(requestBodyJson)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
//            if (status == 200) {
            return response.body().string();
//            }
        } catch (Exception e) {
            System.out.println("okhttp post error >> ex = {}"+ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }


    public String postString(String url,String entity, Map<String, String> headerMap) {
        String responseBody = "";
//        FormBody.Builder builder = new FormBody.Builder();
//        //添加参数
//        if (params != null && params.keySet().size() > 0) {
//            for (String key : params.keySet()) {
//                builder.add(key, params.get(key));
//            }
//        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json"),entity.getBytes(Charset.forName("UTF-8")));

        Builder build= new Builder()
                .url(url);
        if(headerMap!=null) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = build
                .post(requestBodyJson)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
//            if (status == 200) {
            return response.body().string();
//            }
        } catch (Exception e) {
            System.out.println("okhttp post error >> ex = {}"+ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }
    /**
     * post 上传文件
     *
     * @param url
     * @param params
     * @param fileType
     * @return
     */
    public String postFile(String url, Map<String, Object> params, String fileType) {
        String responseBody = "";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
                    continue;
                }
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }
        Request request = new Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
            System.out.println("okhttp postFile error >> ex = {}"+ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

}