package com.example.kaixin.elive.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.Utils.MyDB;
import com.example.kaixin.elive.bean.MarkerBean;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by kaixin on 2017/5/25.
 */

public class MarkerDetailsActivity extends SwipeBackActivity {

    private Toolbar toolbar;
    private TextView tv_event, tv_long, tv_date, tv_notes;
    private ImageButton ib_back, ib_shark;
    private Button btn_update, btn_delete;
    private MarkerBean markerBean;
    private MyDB myDB = new MyDB(MainActivity.getAppContext(), DATABASE_NAME, null, 2);
    private static final String DATABASE_NAME = "myApp.db";
    private static final String MARKER_SQL_DELETE = "delete from marker where event = ?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markerdetails);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        tv_event = (TextView)findViewById(R.id.tv_event);
        tv_long = (TextView)findViewById(R.id.tv_long);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_notes = (TextView)findViewById(R.id.tv_notes);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        Intent intent = getIntent();
        markerBean = (MarkerBean)intent.getSerializableExtra("markday");
        tv_event.setText(markerBean.getEvent());
        tv_long.setText(intent.getStringExtra("long"));
        tv_date.setText(markerBean.getDate());
        tv_notes.setText(markerBean.getNotes());

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerDetailsActivity.this.finish();
            }
        });
        ib_shark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MarkerDetailsActivity.this, "shark failed!", Toast.LENGTH_SHORT).show();
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
                        SQLiteDatabase dbDelete = myDB.getWritableDatabase();
                        dbDelete.execSQL(MARKER_SQL_DELETE, new Object[] {
                                tv_event.getText().toString()});
                        dbDelete.close();
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
                intent.putExtra("markday", markerBean);
                startActivity(intent);
                MarkerDetailsActivity.this.finish();
            }
        });
    }
}
