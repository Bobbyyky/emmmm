package com.example.shinelon.notebook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shinelon.notebook.MainActivity;
import com.example.shinelon.notebook.R;
import com.example.shinelon.notebook.util.Util;
import com.example.shinelon.notebook.util.Configure;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private AlertDialog registerDialog;
    private EditText registerName;
    private EditText registerPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        nameEditText = (EditText)findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);
        loginButton.setTag(101);
        loginButton.setOnClickListener(this);
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String address = sharedPreferences.getString("password","");
        if (name.equals("")&&address.equals("")){
            register();
        }else {
            nameEditText.setText(name);
        }
    }

    private void register() {
        registerDialog = new Util().getDialog(this,registerDialogView());
        registerDialog.show();
    }

    private View registerDialogView(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView titleText = new TextView(this);
        titleText.setText("设置账号");
        titleText.setTextSize(20);
        titleText.setGravity(Gravity.CENTER);
        titleText.setPadding(0,20,0,10);
        registerName = new EditText(this);
        registerName.setHint("请输入用户名");
        registerPassword = new EditText(this);
        registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        registerPassword.setHint("请输入密码");

        LinearLayout buttonLinearLayout = new LinearLayout(this);
        buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLinearLayout.setGravity(Gravity.CENTER);

        Button registerButton = new Button(this);
        registerButton.setText("注册");
        registerButton.setOnClickListener(this);
        registerButton.setTag(102);
        Button cancelButton = new Button(this);
        cancelButton.setText("取消");
        cancelButton.setOnClickListener(this);
        cancelButton.setTag(103);

        buttonLinearLayout.addView(registerButton);
        buttonLinearLayout.addView(cancelButton);

        linearLayout.addView(titleText);
        linearLayout.addView(registerName);
        linearLayout.addView(registerPassword);
        linearLayout.addView(buttonLinearLayout);
        return linearLayout;
    }

    private void alertRegister(final String name,final String password){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("正在设置账号");
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name",name);
                editor.putString("password",password);
                editor.commit();
                registerDialog.dismiss();
                nameEditText.setText(name);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void alertLoginError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("请输入用户名，密码");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                register();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch ((Integer)v.getTag()){
            case 101:
                String loginName = nameEditText.getText().toString();
                String loginPassword = passwordEditText.getText().toString();
                if ("".equals(loginName)||"".equals(loginPassword)){
                    alertLoginError();
                    break;
                }else if (sharedPreferences.getString("name","").equals(loginName)
                        &&sharedPreferences.getString("password","").equals(loginPassword)){
                    Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    intent.putExtra("from",Configure.FROM_LOGIN_ACTIVITY);
                    startActivity(intent);
                    this.finish();
                    break;
                }else {
                    alertLoginError();
                }
                break;
            case 102:
                String name = registerName.getText().toString();
                String password = registerPassword.getText().toString();
                if("".equals(name)||"".equals(password)){
                    Toast.makeText(this,"不能为空",Toast.LENGTH_LONG).show();
                    break;
                }else {
                    alertRegister(name,password);
                }
                break;
            case 103:
                Toast.makeText(this,"取消了设置账号",Toast.LENGTH_LONG).show();
                registerDialog.dismiss();
                break;
        }

    }
}
