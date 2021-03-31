package com.musleep.Musleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private Spinner Spingender;
    private Spinner Spinage;
    public static final String TAG = "TAG";

    EditText mEmail, mPassword, mConfirm, mName;
    Button mSignUpBtn;
    TextView forgotTextLink, mLoginLink;
    TextView tvd;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userID;
    ProgressBar progressBar;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //設定隱藏標題
//        getSupportActionBar().hide();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        //抓使用者輸入的值
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirm = findViewById(R.id.pConfirm);
//        mName = findViewById(R.id.name);
        mLoginLink = findViewById(R.id.login);
        mSignUpBtn = findViewById(R.id.registration);
        forgotTextLink = findViewById(R.id.ForgetPassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

//        Spingender = (Spinner) findViewById(R.id.gender);

        //原有
//        ArrayAdapter<CharSequence> arrAdapSpn
//                = ArrayAdapter.createFromResource(Registration.this, //對應的Context
//                R.array.gender, //選項資料內容
//                android.R.layout.simple_spinner_item); //自訂getView()介面格式(Spinner介面未展開時的View)
//
//        arrAdapSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //自訂getDropDownView()介面格式(Spinner介面展開時，View所使用的每個item格式)
//        Spingender.setAdapter(arrAdapSpn); //將宣告好的 Adapter 設定給 Spinner
//        Spingender.setOnItemSelectedListener(spnRegionOnItemSelected);
//
//        Spinage = (Spinner) findViewById(R.id.age);
//
//        ArrayAdapter<CharSequence> arrAdapSpn1
//                = ArrayAdapter.createFromResource(Registration.this, //對應的Context
//                R.array.age, //選項資料內容
//                android.R.layout.simple_spinner_item); //自訂getView()介面格式(Spinner介面未展開時的View)
//
//        arrAdapSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //自訂getDropDownView()介面格式(Spinner介面展開時，View所使用的每個item格式)
//        Spinage.setAdapter(arrAdapSpn1); //將宣告好的 Adapter 設定給 Spinner
//        Spinage.setOnItemSelectedListener(spnRegionOnItemSelected);

        if(mAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),login.class));
//            finish();
        }



        //點選
        mSignUpBtn.setOnClickListener(view -> {

            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String passwordC = mConfirm.getText().toString().trim();
//            final String Name = mName.getText().toString();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("請輸入 email");
                return;
            }

            if(TextUtils.isEmpty(password)){
                mPassword.setError("請輸入密碼");
                return;
            }

            if(TextUtils.isEmpty(passwordC)){
                mConfirm.setError("請輸入確認密碼");
                return;
            }

            if(password.length() < 6){
                mPassword.setError("密碼需大於六位數");
                return;
            }

            else{
                //判斷確認密碼以及密碼是否相同
                if(password.equals(passwordC)){
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.VISIBLE);
                                // send verification link

                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Registration.this, "驗證信已送至您的信箱.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                DocumentReference documentReference = db.collection("User").document(mAuth.getUid());
                                Map<String,Object> user = new HashMap<>();
//                                user.put("Name",Name);
                                user.put("email",email);
                                documentReference.set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+ mAuth.getUid());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getApplicationContext(),Login.class);
                                        startActivity(i);
                                    }
                                }, 2500);
                            }
                            else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                                    Toast.makeText(Registration.this, "此帳號已註冊.", Toast.LENGTH_SHORT).show();
                                }

                                Log.i("LOGGGG",errorCode);

                            }
                        }
                    });
                }
                //若不相同則顯示密碼不一致
                else{
                    Toast.makeText(this, "帳號密碼不一致", Toast.LENGTH_LONG).show();
                }

            }

        });
////選擇生日
//        tvd = findViewById(R.id.tvd);
//        tvd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthofYear, int dayOfMonth) {
//                        //顯示生日（月份要+1，因為這個方法是從0開始算的）
//                        tvd.setText(String.format("%d/%d/%d", year, monthofYear + 1, dayOfMonth));
//
//                        Calendar cal = Calendar.getInstance();
//                        String strDate = year + "/" + monthofYear + "/" + dayOfMonth;
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                        Date birthDay = null;
//                        try {
//                            birthDay = sdf.parse(strDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        int birth = countAge(birthDay);
//                        if (birth < 0) {
//                            Toast.makeText(getApplicationContext(), "生日輸入有誤", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    //計算年齡
//                    private int countAge(Date birthDay) {
//                        Calendar cal = Calendar.getInstance();
//
//                        if (cal.before(birthDay)) {
//                            throw new IllegalArgumentException(
//                                    "The birthDay is before Now.It's unbelievable!");
//                        }
//                        //獲得當前日期
//                        int yearNow = cal.get(Calendar.YEAR);
//                        int monthNow = cal.get(Calendar.MONTH);
//                        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
//                        //獲得出生日期
//                        cal.setTime(birthDay);
//                        int yearBirth = cal.get(Calendar.YEAR);
//                        int monthBirth = cal.get(Calendar.MONTH) + 1;
//                        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
//
//                        int age = yearNow - yearBirth;
//                        if (monthNow <= monthBirth) {
//                            if (monthNow == monthBirth) {
//                                if (dayOfMonthNow < dayOfMonthBirth) age--;
//                            } else {
//                                age--;
//                                Map<String,Object> ggg = new HashMap<>();
//                                ggg.put("AGE",age);
//                                db.collection("User")
//                                        .document(mAuth.getUid())
//                                        .set(ggg,SetOptions.merge());
//                            }
//                        }
//                        return age;
//                    }
//                    //設定初始的顯示日期
//                }, 2000, 0, 1).show();
//
//            }
//        });
//        //上傳性別
//        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                switch(selectedId){
//                    case R.id.A0:
//                        Map<String,Object> female = new HashMap<>();
//                        female.put("Gender",0);
//                        db.collection("User")
//                                .document(mAuth.getUid())
//                                .set(female,SetOptions.merge());
//                        break;
//                    case R.id.A1:
//                        Map<String,Object> male = new HashMap<>();
//                        male.put("Gender",1);
//                        db.collection("User")
//                                .document(mAuth.getUid())
//                                .set(male,SetOptions.merge());
//                        break;
//                }
//            }
//        });
        //新增帳號，用於挑轉至 registration 頁面
        mLoginLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }


}