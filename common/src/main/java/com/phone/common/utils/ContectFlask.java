package com.phone.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * ClassName: contect_flask
 * Function: TODO ADD FUNCTION
 *
 * @author: make wen
 * date: 2019-7-1 下午2:03:00
 */
public class ContectFlask {


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
    //-----------------------------客户端请求时需要把中文转码 ---------------------

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url 发送请求的URL
     * @return URL所代表远程资源的响应
     * @throws HttpException
     */
    public static String sendPost(String url, String param) throws HttpException {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // shuffle 打乱顺序
            Collections.shuffle(userAgent);
            conn.setRequestProperty("user-agent", userAgent.get(0));
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            //System.out.println("发送 POST 请求出现异常！" + e);
            throw new HttpException("Exception");
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * string类型 utf-8
     *
     * @throws HttpException
     */
    public static String newPost(String url, String json) throws HttpException {
        JSONObject jb = new JSONObject();
        jb.put("code", 0);
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Content-Type", "application/json;charset=utf-8");
            Collections.shuffle(userAgent);
            post.setHeader("User-Agen", userAgent.get(0));
            post.addHeader("connection", "close");
            StringEntity postingString = new StringEntity(json,
                    "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
            String content = EntityUtils.toString(response.getEntity());
            //   //System.out.println(content);
            return content;
        } catch (SocketTimeoutException e) {
            throw new HttpException("Exception");
        } catch (Exception e) {
            throw new HttpException("Exception");
        }
    }


    /**
     * 无参get请求
     *
     * @throws HttpException
     */
    public static String get(String url) throws HttpException {
        //System.err.println("发送请求");
        String result = "";
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpResponse response = httpClient.execute(get);
            result = getHttpEntityContent(response);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                result = "Exception";
            }
        } catch (Exception e) {
            //System.out.println("请求异常");
            throw new HttpException("Exception");
        } finally {
            get.abort();
        }
        return result;
    }

    /**
     * 有参get请求
     *
     * @throws HttpException
     */
    public static String get(Map<String, String> paramMap, String url) throws HttpException {
        String result = "";
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            List<NameValuePair> params = setHttpParams(paramMap);
            String param = URLEncodedUtils.format(params, "UTF-8");
            get.setURI(URI.create(url + "?" + param));
            HttpResponse response = httpClient.execute(get);
            result = getHttpEntityContent(response);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                result = "Exception";
            }
        } catch (Exception e) {
            //System.out.println("请求异常");
            throw new HttpException("Exception");
        } finally {
            get.abort();
        }
        return result;
    }

    /**
     * 参数重组
     */
    public static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
    }


    public static String getHttpEntityContent(HttpResponse response) throws UnsupportedOperationException, IOException {
        String result = "";
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream in = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                strber.append(line + '\n');
            }
            br.close();
            in.close();
            result = strber.toString();
        }

        return result;

    }

    /**
     * post请求
     *
     * @throws HttpException
     */
    public static String post(JSONObject json, String url) throws HttpException {
        String result = "";
        HttpPost post = new HttpPost(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            post.setHeader("Content-Type", "application/json;charset=utf-8");
            post.addHeader("Authorization", "Basic YWRtaW46");
            StringEntity postingString = new StringEntity(json.toString(), "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);

            InputStream in = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                strber.append(line + '\n');
            }
            br.close();
            in.close();
            result = strber.toString();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                result = "Exception";
            }
        } catch (Exception e) {
            //System.out.println("请求异常");
            throw new HttpException("Exception");
        } finally {
            post.abort();
        }
        return result;
    }


    /**
     * 无参flixedFloatget请求
     *
     * @throws HttpException
     */
    public static String flixedFloatget(String uri, String key) throws HttpException {
        //System.err.println("发送请求");
        String result = "";
        HttpGet get = new HttpGet(uri);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            get.addHeader("X-API-KEY", "GNlbgIxTvqEEeKXw7LdXo6rV3wLCZZLGKudGAuXO");
            get.addHeader("X-API-SIGN", key);

            HttpResponse response = httpClient.execute(get);
            result = getHttpEntityContent(response);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                result = "Exception";
            }
        } catch (Exception e) {
            //System.out.println("请求异常");
            throw new HttpException("Exception");
        } finally {
            get.abort();
        }
        //System.err.println(result);
        return result;
    }

    /**
     * 有参flixedFloatget请求
     *
     * @throws HttpException
     */
    public static String flixedFloatget(Map<String, String> paramMap, String url, String key) throws HttpException {
        String result = "";
        HttpGet get = new HttpGet(url);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            get.addHeader("X-API-KEY", "GNlbgIxTvqEEeKXw7LdXo6rV3wLCZZLGKudGAuXO");
            get.addHeader("X-API-SIGN", key);

            List<NameValuePair> params = setHttpParams(paramMap);
            String param = URLEncodedUtils.format(params, "UTF-8");
            get.setURI(URI.create(url + "?" + param));
            HttpResponse response = httpClient.execute(get);
            result = getHttpEntityContent(response);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                result = "Exception";
            }
        } catch (Exception e) {
            //System.out.println("请求异常");
            throw new HttpException("Exception");
        } finally {
            get.abort();
        }
        return result;
    }


    public static String flixedFloatPost(String url, Map<String, Object> paramMap,String apiKey) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("X-API-KEY", "GNlbgIxTvqEEeKXw7LdXo6rV3wLCZZLGKudGAuXO");
        httpPost.addHeader("X-API-SIGN", apiKey);
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }



}