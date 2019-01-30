package cn.luowq.wx.common;

import cn.luowq.wx.util.JSSDKUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: rowan
 * @Date: 2019/1/24 11:39
 * @Description:
 */
public class TokenSingleton {
    //缓存accessToken 的Map  ,map中包含 一个accessToken 和 缓存的时间戳
    private Map<String, String> map = new HashMap<>();

    private TokenSingleton() {
    }

    public synchronized static TokenSingleton getInstance() {
        return TokenSingletonHolder.singleton;
    }

    private static class TokenSingletonHolder {

        private static final TokenSingleton singleton = new TokenSingleton();

        private TokenSingletonHolder() {
        }

    }

    public Map<String, String> getMap() {
        String time = map.get("time");
        String accessToken = map.get("access_token");
        Long nowDate = System.currentTimeMillis();

        if (accessToken != null && time != null && nowDate - Long.parseLong(time) < 6000 * 1000) {
            System.out.println("accessToken存在，且没有超时 ， 返回单例");
        } else {
            System.out.println("accessToken 超时 ， 或者不存在 ， 重新获取");
            String access_token = JSSDKUtil.getAccessToken();
            //这里是直接调用微信的API去直接获取 accessToken 和Jsapi_ticket 获取
            String jsapi_token = JSSDKUtil.getTicket(access_token);
            //"获取jsapi_token";
            map.put("time", nowDate + "");
            map.put("access_token", access_token);
            map.put("jsapi_token", jsapi_token);
        }
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

}
