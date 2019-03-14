package moe.nyako.NIMS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends NFCRead {

    public static String userIdString;
    public static APICall api = new APICall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcInit();

        if(nfc_enabled)
            findViewById(R.id.nfc_info).setVisibility(View.VISIBLE);

        //设置标题
        setTitle("南京大学信息管理系统");
        getSupportActionBar().hide();

        View decorView = this.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(option);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);

        //允许主线程访问网络
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //登录按钮调用此方法
    public void logIn(View view) {
        EditText userIdEditText = findViewById(R.id.userId);
        EditText userPwdEditText = findViewById(R.id.userPwd);

        userIdString = userIdEditText.getText().toString();
        String userPWdString = userPwdEditText.getText().toString();

        if (api.auth(userIdString, userPWdString))
            output(userIdString);
        else {
            Toast.makeText(this, "学工号或密码错误", Toast.LENGTH_SHORT).show();
            userPwdEditText.setText("");
        }
    }

    //读取到校园卡中的学号，跳过登陆部分，直接继续
    @Override
    protected void output(String out) {
        userIdString = out;
        Intent intent = new Intent(this, TagLists.class);
        startActivity(intent);
    }
}
