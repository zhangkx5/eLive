package com.example.kaixin.elive.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.Utils.LocationUtils;
import com.example.kaixin.elive.Utils.MyDB;
import com.example.kaixin.elive.Utils.WeatherUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by baoanj on 2017/5/12.
 */

public class DiaryActivity extends SwipeBackActivity implements View.OnClickListener {
    private ImageButton ib_back, ib_done, ib_edit;
    private TextView diary_time, diary_weather, diary_city, ib_title, diary_content_tv;
    private EditText diary_content_et;
    private Bundle bundle;
    private static Context mContext;
    private boolean LookAndUpdate;
    private String strDate, strCity, strWeather, strContent;
    private static final String DATABASE_NAME = "myApp.db";
    private static final String SQL_INSERT = "insert into diary (filename, time, city, weather) values (?, ?, ?, ?)";
    private static final String DIARY_SQL_SELECTONE = "select filename from diary where time = ?";

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                DiaryActivity.this.finish();
                break;
            case R.id.ib_done:
                addDiary(LookAndUpdate, "Diary_" + strDate, strDate,
                        strCity, strWeather, diary_content_et.getText().toString());
                break;
            case R.id.ib_edit:
                diary_content_et.setVisibility(View.VISIBLE);
                diary_content_tv.setVisibility(View.GONE);
                ib_done.setVisibility(View.VISIBLE);
                ib_edit.setVisibility(View.GONE);
                ib_title.setText("修改日记");
            default:
                break;
        }
    }
    public static Context getAppContext() {
        return mContext;
    }

    public void addDiary(boolean Update, String filename, String strDate, String strCity, String strWeather, String strContent) {
        if (strContent.equals("")) {
            showToast("亲，你还没写日记哦..");
        } else {
            openDiaryFile(filename, strContent);
            if (!Update) {
                addDiaryDB(filename, strDate, strCity, strWeather);
            }
            DiaryActivity.this.finish();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void openDiaryFile(String filename, String strContent) {
        try {
            FileOutputStream fos = DiaryActivity.getAppContext().openFileOutput(filename, MODE_PRIVATE);
            fos.write(strContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addDiaryDB(String filename, String strDate, String strCity, String strWeather) {
        MyDB myDB = new MyDB(DiaryActivity.getAppContext(), DATABASE_NAME, null, 1);
        SQLiteDatabase dbWrite = myDB.getWritableDatabase();
        dbWrite.execSQL(SQL_INSERT, new Object[]{filename, strDate, strCity, strWeather});
        dbWrite.close();
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

        ib_back.setOnClickListener(this);
        ib_done.setOnClickListener(this);
        ib_edit.setOnClickListener(this);
    }
}
