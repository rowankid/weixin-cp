package cn.luowq.wx.lx;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rowan
 * @Date: 2019/1/24 11:39
 * @Description:
 */
@Component
public class LxTokenSingleton {


    //缓存accessToken 的Map  ,map中包含 一个accessToken 和 缓存的时间戳
    private Map<String, String> map = new HashMap<>();

    private LxTokenSingleton() {
    }

    public synchronized static LxTokenSingleton getInstance() {
        return LxTokenSingletonHolder.singleton;
    }

    private static class LxTokenSingletonHolder {

        private static final LxTokenSingleton singleton = new LxTokenSingleton();

        private LxTokenSingletonHolder() {
        }

    }

    public Map<String, String> getMap() {
        String time = map.get("time");
        String accessToken = map.get("access_token");
        Long nowDate = System.currentTimeMillis();

        if (accessToken != null&&""!=accessToken && time != null && nowDate - Long.parseLong(time) < 6000 * 1000) {
            System.out.println("accessToken存在，且没有超时 ， 返回单例");
        } else {
            System.out.println("accessToken 超时 ， 或者不存在 ， 重新获取");
            String access_token = AccessTokenUtil.getAccessToken();
            map.put("time", nowDate + "");
            map.put("access_token", access_token);
        }
        System.out.println(map.get("access_token"));
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }


}
