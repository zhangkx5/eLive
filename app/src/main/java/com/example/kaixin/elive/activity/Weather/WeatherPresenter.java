package com.example.kaixin.elive.activity.Weather;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.kaixin.elive.Utils.WeatherUtils;
import com.example.kaixin.elive.activity.Main.MainActivity;

import java.util.ArrayList;

/**
 * Created by kaixin on 2017/6/13.
 */

public class WeatherPresenter implements IWeatherPresenter{
    private IWeatherView weatherView;
    private IWeatherModel weatherModel;

    public WeatherPresenter(IWeatherView weatherView) {
        this.weatherView = weatherView;
        weatherModel = new WeatherModel();
    }

    private static final int UPDATE_CONTENT = 0;
    private ArrayList<String> response;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:
                    response = (ArrayList<String>)msg.obj;
                    String result = response.get(0);
                    if ("查询结果为空".equals(result)) {
                        Toast.makeText(MainActivity.getAppContext(), "当前城市不存在，请重新输入", Toast.LENGTH_SHORT).show();
                    } else if ("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/".equals(result)){
                        Toast.makeText(MainActivity.getAppContext(), "您的点击速度过快，二次查询间隔<600ms", Toast.LENGTH_SHORT).show();
                    } else if ("发现错误：免费用户 24 小时内访问超过规定数量。http://www.webxml.com.cn/".equals(result)) {
                        Toast.makeText(MainActivity.getAppContext(), "免费用户24小时内访问超过规定数量50次", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.size() > 28) {
                            weatherView.setVisibility(response.get(1), response.get(3), response.get(8));
                            weatherView.showWeather();
                            weatherView.showAir();
                            weatherView.showList();
                            weatherView.showNextFiveDay();
                        } else {
                            Toast.makeText(MainActivity.getAppContext(), response.get(0), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void postRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_CONTENT;
                message.obj = WeatherUtils.postRequest();
                handler.sendMessage(message);
            }
        }).start();
    }
    @Override
    public ArrayList<String> getResponse() {
        return response;
    }
    @Override
    public int setImage(String gif) {
        return weatherModel.findImage(gif);
    }
}
