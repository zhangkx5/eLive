package com.example.kaixin.elive.activity.Marker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.bean.MarkerBean;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerDetailsActivity extends SwipeBackActivity implements
        IMarkerDetailsView {

    private Toolbar toolbar;
    private TextView tv_event, tv_long, tv_date, tv_notes;
    private ImageButton ib_back;
    private Button btn_update, btn_delete;
    private MarkerBean markerBean;

    private MarkerDetailsPresenter markerDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markerdetails);

        init();
        markerDetailsPresenter = new MarkerDetailsPresenter(this);
        showMarkerDetails();

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerDetailsActivity.this.finish();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MarkerDetailsActivity.this);
                builder.setTitle("是否删除？");
                builder.setMessage("此操作不可逆！");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        markerDetailsPresenter.deleteMark();
                        dialogInterface.dismiss();
                        MarkerDetailsActivity.this.finish();
                    }
                });
                builder.create().show();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkerDetailsActivity.this, MarkerActivity.class);
                intent.putExtra("marker", markerBean);
                startActivity(intent);
                MarkerDetailsActivity.this.finish();
            }
        });
    }
    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        tv_event = (TextView)findViewById(R.id.tv_event);
        tv_long = (TextView)findViewById(R.id.tv_long);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_notes = (TextView)findViewById(R.id.tv_notes);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);
    }
    @Override
    public void showMarkerDetails() {
        Intent intent = getIntent();
        markerBean = markerDetailsPresenter.getMarkerDetails(intent);
        tv_event.setText(markerBean.getEvent());
        tv_long.setText(intent.getStringExtra("long"));
        tv_date.setText(markerBean.getDate());
        tv_notes.setText(markerBean.getNotes());
    }
    @Override
    public void showToast(String text) {}

    @Override
    public String getEvent() {
        String event = tv_event.getText().toString();
        return event;
    }
    @Override
    public String getDate() {
        String date = tv_date.getText().toString();
        return date;
    }
    @Override
    public String getNotes() {
        String notes = ""+tv_notes.getText().toString();
        return notes;
    }
}
