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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //窗口控件
    EditText userET, passET, emailET;
    Button registerBtn;

    //firebase
    FirebaseAuth auth;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化窗口控件
        userET = findViewById(R.id.userEditText);
        passET = findViewById(R.id.passEditText);
        emailET = findViewById(R.id.emailEditText);
        registerBtn = findViewById(R.id.buttonRegister);
        //注册firebase
        auth = FirebaseAuth.getInstance();

        //为注册按钮绑定事件
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userNameTxt = userET.getText().toString();
                String emailTxt = emailET.getText().toString();
                String pwdTxt = passET.getText().toString();
                if(TextUtils.isEmpty(userNameTxt) || TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(pwdTxt)){
                    Toast.makeText(RegisterActivity.this, "请将信息填写完整!", Toast.LENGTH_SHORT).show();
                }else{
                    RegisterNow(userNameTxt,emailTxt,pwdTxt);
                }
            }
        });
    }

    private void RegisterNow(final String userName, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            myRef = FirebaseDatabase.getInstance()
                                    .getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("userName",userName);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}