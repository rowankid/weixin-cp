package cn.luowq.wx.lx.service;

import cn.luowq.wx.lx.LxTokenSingleton;
import cn.luowq.wx.util.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: rowan
 * @Date: 2019/1/24 15:32
 * @Description:
 */
@Service
public class LxMessageService {

    public String parseMessage(WxCpXmlMessage inMessage){
        if("text".equals(inMessage.getMsgType())){
            JSONObject document = new JSONObject();
            Map dataMap = new ConcurrentHashMap();
            dataMap.put("type","doc");
            Map attributesMap = new ConcurrentHashMap();
            //前20个字符作为标题
            if (inMessage.getContent().length()>20){
                attributesMap.put("title",inMessage.getContent().substring(0,20));
            }else{
                attributesMap.put("title",inMessage.getContent());
            }
            // 内容
            attributesMap.put("content",inMessage.getContent());
            // 是否markdown 0 为html ； 1 为markdown
            attributesMap.put("is_markdown",0);
            dataMap.put("attributes",attributesMap);
            document.put("data",dataMap);
            return postDocument(document);
        }
        return "不是文字信息,暂不支持自动发布！";
    }

    private String postDocument(JSONObject document){
        final String url = "https://lxapi.lexiangla.com/cgi-bin/v1/docs";
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, document.toJSONString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("StaffID", "LuoWenQi")
            .addHeader("Authorization",LxTokenSingleton.getInstance().getMap().get("access_token"))
            .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject responseBody = JSON.parseObject(response.body().string());
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

}
