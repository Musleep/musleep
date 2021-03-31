package com.musleep.Musleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText mEmail,mPassword;
    private Button mLoginBtn;
    private TextView mCreateBtn,forgotTextLink;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //設定隱藏標題
//        getSupportActionBar().hide();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        //抓使用者輸入的值
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.login);
        mCreateBtn = findViewById(R.id.registration);
        forgotTextLink = findViewById(R.id.ForgetPassword);

        mLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View mLoginBtn) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("請輸入 email");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("請輸入密碼");
                    return;
                }

                // authenticate the user
                //使用 google sign in api 登入
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            db = FirebaseFirestore.getInstance();
//                            Map<String,Object> space = new HashMap<>();
//                        space.put("ID",mAuth.getUid());
//                        db.collection("User")
//                                .document(mAuth.getUid())
//                                .set(space, SetOptions.merge());
                            progressBar.setVisibility(View.VISIBLE);
                            DocumentReference docRef = db.collection("User").document(mAuth.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                            Log.d("TAG", "DocumentSnapshot data: " + document.getData().size());
                                            if (document.getData().size() == 2){
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent i = new Intent(getApplicationContext(),userdata.class);
                                                        startActivity(i);
                                                    }
                                                }, 2500);
//                                                startActivity(new Intent(getApplicationContext(), userdata.class));
                                            }else {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent i = new Intent(getApplicationContext(),homescreen.class);
                                                        startActivity(i);
                                                    }
                                                }, 2500);
//                                                startActivity(new Intent(getApplicationContext(), homescreen.class));
                                            }
                                            return;

                                        } else {
                                            Log.d("TAG", "No such document");
                                        }
                                    } else {
                                        Log.d("TAG", "get failed with ", task.getException());
                                    }
                                }
                            });
                        }


//                    if (task.isSuccessful()){
//                        DocumentSnapshot document = task.getResult();
//                        if(uidref != null) {
//                            startActivity(new Intent(getApplicationContext(),homescreen.class));
//                        }else {
//                            Toast.makeText(getApplicationContext(), "Your Google Account is Connected to Our Application.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), sleep_record.class));
//                        }
//                    }else{
//                        Log.i("GG", "GG");
//                    }


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplicationContext(), "密碼錯誤了喔,請再輸入一次.", Toast.LENGTH_SHORT).show();

                        }else if (e instanceof FirebaseAuthInvalidUserException) {
                            String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                            if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                Toast.makeText(getApplicationContext(), "你還沒註冊過喔~快去註冊吧", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        //新增帳號，用於挑轉至 registration 頁面
        mCreateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });
        //忘記密碼使用 google 的忘記密碼設定，會將 link 傳至使用者的 email 進行確認
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("重設密碼");
                passwordResetDialog.setMessage("請輸入 email 取得重設密碼信件");
                passwordResetDialog.setView(resetMail);

                //頁面中送出 function
                passwordResetDialog.setPositiveButton("送出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "已傳送信件", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "輸入錯誤，請重新輸入信箱" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                //頁面中取消 function
                passwordResetDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }
}