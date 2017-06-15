package com.example.kaixin.elive.activity.Jokes.model;

import android.util.Log;
import com.example.kaixin.elive.bean.JokesBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaixin on 2017/6/13.
 */

public class JokesModel implements IJokesModel{

    String url = "http://japi.juhe.cn/joke/content/text.from";
    String apikey = "e0e928f4afcc0997f574aece6c351bde";

    @Override
    public String getUrl() {
        Map params = new HashMap();
        params.put("page", "");
        params.put("pagesize", "15");
        params.put("key", apikey);
        url = url+"?"+urlencode(params);
        return url;
    }
    @Override
    public List<JokesBean> getJsonData(String url) {
        List<JokesBean> jokesBeanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            JokesBean jokesBean;
            try {
                jsonObject = new JSONObject(jsonString);
                jsonObject = jsonObject.getJSONObject("result");
                Log.d("JOKES", jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    jokesBean = new JokesBean();
                    jokesBean.setJokesPtime(jsonObject.getString("updatetime"));
                    String regexnbsp = "(&nbsp;)";
                    Pattern patnbsp = Pattern.compile(regexnbsp);
                    Matcher matchernbsp = patnbsp.matcher(jsonObject.getString("content"));
                    jokesBean.setJokesContent(matchernbsp.replaceAll(""));
                    jokesBeanList.add(jokesBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jokesBeanList;
    }
    @Override
    public String readStream(InputStream is) {
        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=")
                        .append(URLEncoder.encode(i.getValue()+"", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
