package cn.luowq.wx.util;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: rowan
 * @Date: 2018/11/7
 * @Description:
 */
public class HttpClientUtil {

    public static Map<String, String> initParamMap(String params) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        JSONObject paramsObj = new JSONObject(params);
        Iterator it = paramsObj.keys();
        while (it.hasNext()){
            String key = (String) it.next();
            map.put(key, (String) paramsObj.get(key));
        }
        return map;
    }

    //序列化
    public static String serializeToString(Object obj) throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        String str = byteOut.toString("ISO-8859-1");//此处只能是ISO-8859-1,但是不会影响中文使用
        return str;
    }
    //反序列化
    public static Object deserializeToObject(String str) throws Exception{
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Object obj =objIn.readObject();
        return obj;
    }

}
