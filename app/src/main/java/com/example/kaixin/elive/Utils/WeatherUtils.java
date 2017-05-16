package com.example.kaixin.elive.Utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by baoanj on 2017/5/12.
 */

public class WeatherUtils {

    private static final String web = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";

    private static final int UPDATE_CONTENT = 0;

    public static ArrayList<String> list = new ArrayList<String>();

    /*private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_CONTENT) {
                response = (ArrayList<String>)msg.obj;
                /*String result = response.get(0);
                if ("查询结果为空".equals(result)) {
                    Toast.makeText(MainActivity.getAppContext(), "当前城市不存在，请重新输入", Toast.LENGTH_SHORT).show();
                } else if ("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/".equals(result)){
                    Toast.makeText(MainActivity.getAppContext(), "您的点击速度过快，二次查询间隔<600ms", Toast.LENGTH_SHORT).show();
                } else if ("发现错误：免费用户 24 小时内访问超过规定数量。http://www.webxml.com.cn/".equals(result)) {
                    Toast.makeText(MainActivity.getAppContext(), "免费用户24小时内访问超过规定数量50次", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.size() > 28) {
                        String where = response.get(1);
                        name.setText(where);
                        String timestr = response.get(3);
                        time_update.setText(timestr.substring(11, timestr.length()) + " 更新");
                        showWeather();
                        showAir();
                        degrees.setText(response.get(8));
                        showList();
                        showNextFiveDay();
                    } else {
                        Toast.makeText(MainActivity.getAppContext(), response.get(0), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };*/

    public static ArrayList<String> postRequest() {
        HttpURLConnection connection = null;
        try {
            Log.i("Key", "Begin the connection");
            URL url = new URL(web);
            connection = (HttpURLConnection)(url.openConnection());
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            String request = "广州";
            request = URLEncoder.encode(request, "utf-8");
            out.writeBytes("theCityCode="+ request + "&theUserID=");

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            parseXMLWithPull(response.toString());
            Log.i("Key", response.toString());
        } catch (Exception e) {
            Log.i("Key", "Fail to connect");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
                Log.i("Key", "Disconnect the connection");
            }
        }
        return list;
    }
    private static void parseXMLWithPull(String str_xml) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(str_xml));
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                            String str = parser.nextText();
                            list.add(str);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

