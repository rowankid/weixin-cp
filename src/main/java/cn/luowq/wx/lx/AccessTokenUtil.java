package cn.luowq.wx.lx;

import cn.luowq.wx.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rowan
 * @Date: 2019/1/24 11:41
 * @Description:
 */
@Component
public class AccessTokenUtil {

    private static String lxAppKey;

    private static String lxAppSecret;
    private static String grantType = "client_credentials";



    public static String getAccessToken() {

        String accessToken = "";
        String tokenType = "";
        //这个url链接地址和参数皆不能变
        final String url = "https://lxapi.lexiangla.com/cgi-bin/token";
        Map params = new HashMap<>();
        params.put("grant_type",grantType);
        params.put("app_key",lxAppKey);
        params.put("app_secret",lxAppSecret);
        try {
            String responseBody = HttpUtil.getResponseBody(url, params,null);
            JSONObject response = JSON.parseObject(responseBody);
            accessToken = response.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return accessToken;
    }
    @Value("${lx.appKey}")
    public void setLxAppKey(String lxAppKey) {
        AccessTokenUtil.lxAppKey = lxAppKey;
    }

    @Value("${lx.appSecret}")
    public void setLxAppSecret(String lxAppSecret) {
        AccessTokenUtil.lxAppSecret = lxAppSecret;
    }
}
