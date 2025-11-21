package com.phone.module.utils;

import com.alibaba.fastjson.JSONObject;
import com.phone.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class ModuleFilter implements Filter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final int MAX_COUNT = 50; // 单位时间最大访问次数
    private static final long UNIT_TIME_MS = 60 * 1000L; // 单位时间毫秒
    private static final long BLACK_TIME_MIN = 10L; // 黑名单持续时间（分钟）
    // IP 白名单
    private static final String[] WHITE_IPS = {
            "127.0.0.1",
            "192.168.0.27",
            "192.168.150.147",
            "192.168.150.183",
//            "192.168.1.141"
            "192.168.1.155"
    };

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

    /** 判断 IP 是否在白名单中 */
    private boolean isWhiteIp(String ip) {
        if (StringUtils.isEmpty(ip)) return false;
        for (String whiteIp : WHITE_IPS) {
            if (whiteIp.equals(ip)) return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getServletPath();
        String ip = getIPAddress(request);

        // 白名单 IP 或 URL 直接放行
        if (isWhiteListUrl(url) || isWhiteIp(ip)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // IP 无效
        if (StringUtils.isEmpty(ip) || "unknown".equals(ip) || "0.0.0.0".equals(ip)) {
            renderString(response, "invalid ip");
            return;
        }

        // 黑名单判断
        if (redisTemplate.hasKey("filter:ip:black:" + ip)) {
            long expire = redisTemplate.getExpire("filter:ip:black:" + ip, TimeUnit.SECONDS);
            renderString(response, "IP in blacklist: " + ip + ", countdown " + expire + " seconds");
            return;
        }

        // 访问频率限制
        checkIpFrequency(ip, response);

        // Token 校验
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token) || !checkToken(token, response)) {
            return;
        }

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteListUrl(String url) {
        return "/favicon.ico".equals(url)
                || url.contains("/module/merge/createUs")
                || url.contains("/module/merge/getCodeList")
                || url.contains("/module/merge/getAddPrice")
                || url.contains("/module/merge/getPrice")
                || url.contains("/module/merge/charges")
                || url.contains("/module/merge/renewal")
                || url.contains("/module/merge/added")
                || url.contains("/module/order/list")
                || url.contains("/module/merge/getOrder");
    }

    private void checkIpFrequency(String ip, HttpServletResponse response) throws IOException {
        String countKey = "count:" + ip;
        String timeKey = "time:" + ip;

        Object countObj = redisTemplate.opsForHash().get("filter:ip:normal", countKey);
        Object timeObj = redisTemplate.opsForHash().get("filter:ip:normal", timeKey);

        long now = System.currentTimeMillis();

        if (countObj != null && timeObj != null) {
            int count = Integer.parseInt(countObj.toString());
            long lastTime = Long.parseLong(timeObj.toString());
            if (now - lastTime < UNIT_TIME_MS) {
                if (count >= MAX_COUNT) {
                    redisTemplate.opsForValue().set("filter:ip:black:" + ip, "1", BLACK_TIME_MIN, TimeUnit.MINUTES);
                    redisTemplate.opsForHash().delete("filter:ip:normal", countKey, timeKey);
                    renderString(response, "IP access too frequently: " + ip);
                    throw new IOException("IP blocked"); // 阻止继续执行
                } else {
                    redisTemplate.opsForHash().put("filter:ip:normal", countKey, String.valueOf(count + 1));
                }
            } else {
                initVisitsIP(ip);
            }
        } else {
            initVisitsIP(ip);
        }
    }

    private boolean checkToken(String token, HttpServletResponse response) throws IOException {
        try {
//            String resToken = Aes128Utils.decrypt(token);
            JSONObject json = JSONObject.parseObject("");
            long oldTimestamp = Long.parseLong(json.getString("timestamp"));
            long now = System.currentTimeMillis();
            long diffMinutes = (now - oldTimestamp) / (1000 * 60);

            if (diffMinutes > 1) {
                renderString(response, "token expired");
                return false;
            }
            return true;
        } catch (Exception e) {
            renderString(response, "token invalid");
            return false;
        }
    }

    private void initVisitsIP(String ip) {
        redisTemplate.opsForHash().put("filter:ip:normal", "count:" + ip, "1");
        redisTemplate.opsForHash().put("filter:ip:normal", "time:" + ip, String.valueOf(System.currentTimeMillis()));
    }

    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static void renderString(HttpServletResponse response, String message) {
        try {
            response.setStatus(401);
            response.getWriter().print(message);
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
