package com.example.kaixin.elive.activity.Diary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.elive.R;
import com.example.kaixin.elive.activity.Main.MainActivity;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by baoanj on 2017/6/1.
 */

public class SetDiaryLock extends SwipeBackActivity {
    private TextInputLayout oldPasText;
    private TextInputLayout newPasText;
    private TextInputLayout conPasText;
    private EditText et_oldPas, et_newPas, et_conPas;
    private Button btn_save, btn_abandon;
    private ImageButton ib_back, ib_done, ib_edit;
    private TextView ib_title;

    private SharedPreferences pref;
    private String lock;
    private Boolean hasSetLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_diary_lock);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_done = (ImageButton)findViewById(R.id.ib_done);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        ib_title = (TextView) findViewById(R.id.ib_title);

        ib_done.setVisibility(View.GONE);
        ib_edit.setVisibility(View.GONE);
        ib_title.setText("设置/修改日记锁");

        pref = MainActivity.getAppContext().getSharedPreferences("diaryLock", MODE_PRIVATE);
        lock = pref.getString("lock", "");
        hasSetLock = pref.getBoolean("hasSetLock", false);

        oldPasText = (TextInputLayout) findViewById(R.id.pas_old);
        newPasText = (TextInputLayout) findViewById(R.id.pas_new);
        conPasText = (TextInputLayout) findViewById(R.id.pas_confirm);
        et_oldPas = oldPasText.getEditText();
        et_newPas = newPasText.getEditText();
        et_conPas = conPasText.getEditText();
        btn_save = (Button) findViewById(R.id.save);

        if (!hasSetLock) {
            oldPasText.setVisibility(View.GONE);
            newPasText.setVisibility(View.VISIBLE);
            conPasText.setVisibility(View.VISIBLE);
        } else {
            oldPasText.setVisibility(View.VISIBLE);
            newPasText.setVisibility(View.VISIBLE);
            conPasText.setVisibility(View.VISIBLE);

            et_oldPas.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(et_oldPas.getText().toString())) {
                        oldPasText.setErrorEnabled(true);
                        oldPasText.setError("请输入原密码");
                    } else if (!et_oldPas.getText().toString().equals(lock)) {
                        oldPasText.setErrorEnabled(true);
                        oldPasText.setError("原密码不正确，请重新输入");
                    } else {
                        oldPasText.setError(null);
                    }
                }
            });
        }
        et_newPas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_newPas.getText().toString())) {
                    newPasText.setErrorEnabled(true);
                    newPasText.setError("请输入新密码");
                } else {
                    newPasText.setError(null);
                }
            }
        });
        et_conPas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(et_conPas.getText().toString())) {
                    conPasText.setErrorEnabled(true);
                    conPasText.setError("请确认密码");
                } else if (!et_conPas.getText().toString().equals(et_newPas.getText().toString())) {
                    newPasText.setErrorEnabled(true);
                    conPasText.setError("密码不一致，请重新输入");
                } else {
                    conPasText.setError(null);
                }
            }
        });
        btn_abandon = (Button) findViewById(R.id.abandon);
        btn_abandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_oldPas.setText("");
                et_newPas.setText("");
                et_conPas.setText("");
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasSetLock) {
                    if (et_conPas.getText().toString().equals(et_newPas.getText().toString())) {
                        Toast.makeText(MainActivity.getAppContext(), "开启日记功能，日记锁设置成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = MainActivity.getAppContext().getSharedPreferences("diaryLock", MODE_PRIVATE).edit();
                        editor.putString("lock", et_conPas.getText().toString());
                        editor.putBoolean("hasSetLock", true);
                        editor.commit();
                        lock = et_conPas.getText().toString();
                        hasSetLock = true;
                        SetDiaryLock.this.finish();
                    } else {
                        Toast.makeText(MainActivity.getAppContext(), "设置失败", Toast.LENGTH_SHORT).show();
                    }
                } else if (hasSetLock){
                    if (et_conPas.getText().toString().equals(et_newPas.getText().toString())
                            && et_oldPas.getText().toString().equals(lock)) {
                        Toast.makeText(MainActivity.getAppContext(), "日记锁密码修改成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = MainActivity.getAppContext().getSharedPreferences("diaryLock", MODE_PRIVATE).edit();
                        editor.putString("lock", et_conPas.getText().toString());
                        editor.putBoolean("hasSetLock", true);
                        editor.commit();
                        lock = et_conPas.getText().toString();
                        hasSetLock = true;
                        SetDiaryLock.this.finish();
                    } else {
                        Toast.makeText(MainActivity.getAppContext(), "设置失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.getAppContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetDiaryLock.this.finish();
            }
        });
    }
}
