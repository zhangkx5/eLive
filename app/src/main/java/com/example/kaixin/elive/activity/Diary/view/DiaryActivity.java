package com.example.kaixin.elive.activity.Diary.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.Utils.LocationUtils;
import com.example.kaixin.elive.Utils.WeatherUtils;
import com.example.kaixin.elive.activity.Diary.presenter.DiaryPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by baoanj on 2017/5/12.
 */

public class DiaryActivity extends SwipeBackActivity implements IDiaryView {
    private ImageButton ib_back, ib_done, ib_edit;
    private TextView diary_time, diary_weather, diary_city, ib_title, diary_content_tv;
    private EditText diary_content_et;
    private Bundle bundle;
    private static Context mContext;
    private boolean LookAndUpdate;
    private String strDate, strCity, strWeather, strContent;
    private DiaryPresenter diaryPresenter;

    private ArrayList<String> response;
    private final int UPDATE_CONTENT = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:
                    response = (ArrayList<String>)msg.obj;
                    String result = response.get(0);
                    if ("查询结果为空".equals(result)) {
                        strWeather = "多云";
                    } else if ("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/".equals(result)){
                        strWeather = "多云";
                    } else if ("发现错误：免费用户 24 小时内访问超过规定数量。http://www.webxml.com.cn/".equals(result)) {
                        strWeather = "多云";
                    } else {
                        if (response.size() > 28) {
                            strWeather = response.get(7).substring(response.get(7).indexOf(" ")+1, response.get(7).length());
                        } else {
                            strWeather = "多云";
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        initViews();
        diaryPresenter = new DiaryPresenter(this);
        showView();

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryActivity.this.finish();
            }
        });
        ib_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaryPresenter.addDiary();
                DiaryActivity.this.finish();
            }
        });
        ib_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diary_content_et.setVisibility(View.VISIBLE);
                diary_content_tv.setVisibility(View.GONE);
                ib_done.setVisibility(View.VISIBLE);
                ib_edit.setVisibility(View.GONE);
                ib_title.setText("修改日记");
            }
        });
    }
    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void initViews() {
        mContext = getApplicationContext();
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_done = (ImageButton)findViewById(R.id.ib_done);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        ib_title = (TextView) findViewById(R.id.ib_title);
        diary_content_et = (EditText)findViewById(R.id.diary_content_et);
        diary_content_tv = (TextView) findViewById(R.id.diary_content_tv);
        diary_city = (TextView)findViewById(R.id.diary_city);
        diary_time = (TextView)findViewById(R.id.diary_time);
        diary_weather = (TextView)findViewById(R.id.diary_weather);
    }
    @Override
    public String getStrDate() {
        return strDate;
    }
    @Override
    public String getStrCity() {
        return strCity;
    }
    @Override
    public String getStrWeather() {
        return strWeather;
    }
    @Override
    public String getStrContent() {
        return diary_content_et.getText().toString();
    }
    @Override
    public Boolean getUpdate() {
        return LookAndUpdate;
    }
    public void showView() {
        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            LookAndUpdate = true;
            strCity = bundle.getString("city");
            strWeather = bundle.getString("weather");
            strDate = bundle.getString("time");
            strContent = bundle.getString("content");

            diary_content_et.setVisibility(View.GONE);
            diary_content_tv.setVisibility(View.VISIBLE);
            diary_content_et.setText(strContent);
            diary_content_tv.setText(strContent);
            ib_done.setVisibility(View.GONE);
            ib_edit.setVisibility(View.VISIBLE);
            ib_title.setText("日记");
        } else {
            LookAndUpdate = false;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            strDate = simpleDateFormat.format(curDate);
            strWeather = "晴";
            strCity = "广州";
            PackageManager pm = getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.example.kaixin.elive"));
            if (permission) {
                LocationUtils.getCNBylocation(DiaryActivity.this);
                strCity = LocationUtils.cityName;
            }
            postRequest();
            diary_content_et.setVisibility(View.VISIBLE);
            diary_content_tv.setVisibility(View.GONE);
            ib_done.setVisibility(View.VISIBLE);
            ib_edit.setVisibility(View.GONE);
            ib_title.setText("写日记");
        }
        diary_time.setText(strDate.substring(0, strDate.indexOf("日") + 1));
        diary_weather.setText(strWeather);
        diary_city.setText(strCity);
    }
    private void postRequest() {
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
}
