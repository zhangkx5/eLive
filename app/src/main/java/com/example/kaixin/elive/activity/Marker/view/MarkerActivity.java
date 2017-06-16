package com.example.kaixin.elive.activity.Marker.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.activity.Marker.presenter.MarkerPresenter;
import com.example.kaixin.elive.bean.MarkerBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerActivity extends SwipeBackActivity implements IMarkerView {
    private Toolbar toolbar;
    private EditText et_event, et_notes;
    private TextView tv_date;
    private ImageButton ib_back, ib_ok;
    private MarkerBean markerBean;
    private MarkerPresenter markerDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        init();
        markerDetailsPresenter = new MarkerPresenter(this);
        showMarkerDetails();

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerActivity.this.finish();
            }
        });
        ib_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(getEvent())) {
                    markerDetailsPresenter.EventError();
                } else if (TextUtils.isEmpty(getDate())){
                    markerDetailsPresenter.DateError();
                } else {
                    if (markerBean != null) {
                        markerDetailsPresenter.updateMark(markerBean);
                    } else {
                        markerDetailsPresenter.addMark();
                    }
                    MarkerActivity.this.finish();
                }
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
    }
    private DatePickerDialog.OnDateSetListener dataSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            setDate(year, month, day);
        }
    };
    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (id) {
            case 0:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, dataSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(true);
                datePickerDialog.setTitle("选择日期");
                datePickerDialog.show();
                break;
            default:
                break;
        }
        return null;
    }
    private void init() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        et_event = (EditText)findViewById(R.id.event);
        et_notes = (EditText)findViewById(R.id.notes);
        tv_date = (TextView) findViewById(R.id.date);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_ok = (ImageButton)findViewById(R.id.ib_done);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date ymd = new Date(System.currentTimeMillis());
        String str = formatter.format(ymd);
        tv_date.setText(str);
    }
    public void setDate(int year, int month, int day) {
        String YY, MM, DD;
        YY = year+"";
        if (month + 1 < 10) {
            MM = "0" + (month + 1);
        } else {
            MM = (month+1)+"";
        }
        if (day < 10) {
            DD = "0" + day;
        }else {
            DD = day+"";
        }
        tv_date.setText(YY+"年"+MM+"月"+DD+"日");
    }
    @Override
    public void showMarkerDetails() {
        Intent intent = getIntent();
        markerBean = (MarkerBean)intent.getSerializableExtra("marker");
        if (markerBean != null) {
            et_event.setText(markerBean.getEvent());
            tv_date.setText(markerBean.getDate());
            et_notes.setText(markerBean.getNotes());
        }
    }
    @Override
    public void showToast(String text) {
        Toast.makeText(MarkerActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public String getEvent() {
        String event = et_event.getText().toString();
        return event;
    }
    @Override
    public String getDate() {
        String date = tv_date.getText().toString();
        return date;
    }
    @Override
    public String getNotes() {
        String notes = ""+et_notes.getText().toString();
        return notes;
    }
}
