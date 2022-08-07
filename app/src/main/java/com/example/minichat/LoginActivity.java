package com.example.minichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //窗口控件
    EditText userET, passET;
    Button loginBtn, registerBtn;
    // firebase
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart(){
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //检查用户状态，用户曾经登录过的话直接登录即可
        if(firebaseUser!=null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化窗口控件
        userET = findViewById(R.id.loginUserNameText);
        passET = findViewById(R.id.loginPwdText);
        loginBtn = findViewById(R.id.buttonLogin);
        registerBtn = findViewById(R.id.buttonToRegister);
        //firebase
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //绑定login动作
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userNameTxt = userET.getText().toString();
                String passTxt = passET.getText().toString();
                if(TextUtils.isEmpty(userNameTxt)||TextUtils.isEmpty(passTxt)){
                    Toast.makeText(LoginActivity.this, "请将信息填写完整!", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(userNameTxt, passTxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        //绑定跳转按钮事件
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i =new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}