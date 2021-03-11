package com.musleep.Musleep;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class userdata extends AppCompatActivity {

    TextView tvd;
    EditText et_name;
    DatePickerDialog.OnDateSetListener setListener;
    MaterialButton Registerbtn;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);


        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

        //設定隱藏標題


        //上傳性別
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch(selectedId){
                    case R.id.A0:
                        Map<String,Object> female = new HashMap<>();
                        female.put("Gender",0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .update(female);
                        break;
                    case R.id.A1:
                        Map<String,Object> male = new HashMap<>();
                        male.put("Gender",1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .update(male);
                        break;

                }
            }
        });
        //下一步觸發
        Registerbtn = findViewById(R.id.Registerbtn);
        et_name = findViewById(R.id.et_name);
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //名字寫入
                String Name = et_name.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("Name",Name);
                db.collection("User")
                        .document(mAuth.getUid())
                        .update(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db = FirebaseFirestore.getInstance();
                                Map<String,Object> week0 = new HashMap<>();
                                week0.put("ID",mAuth.getUid());
                                db.collection("User")
                                        .document(mAuth.getUid())
                                        .collection("week0")
                                        .document("FirstScore")
                                        .set(week0);
                                Toast.makeText(userdata.this,"Successfull",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), init_time.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(userdata.this,"Failed",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

//選擇生日
        tvd = findViewById(R.id.tvd);
        tvd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(userdata.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofYear, int dayOfMonth) {
                        //顯示生日（月份要+1，因為這個方法是從0開始算的）
                        tvd.setText(String.format("%d/%d/%d", year, monthofYear + 1, dayOfMonth));

                        Calendar cal = Calendar.getInstance();
                        String strDate = year + "/" + monthofYear + "/" + dayOfMonth;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Date birthDay = null;
                        try {
                            birthDay = sdf.parse(strDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int birth = countAge(birthDay);
                        if (birth < 0) {
                            Toast.makeText(getApplicationContext(), "生日輸入有誤", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //計算年齡
                    private int countAge(Date birthDay) {
                        Calendar cal = Calendar.getInstance();

                        if (cal.before(birthDay)) {
                            throw new IllegalArgumentException(
                                    "The birthDay is before Now.It's unbelievable!");
                        }
                        //獲得當前日期
                        int yearNow = cal.get(Calendar.YEAR);
                        int monthNow = cal.get(Calendar.MONTH);
                        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
                        //獲得出生日期
                        cal.setTime(birthDay);
                        int yearBirth = cal.get(Calendar.YEAR);
                        int monthBirth = cal.get(Calendar.MONTH) + 1;
                        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

                        int age = yearNow - yearBirth;
                        if (monthNow <= monthBirth) {
                            if (monthNow == monthBirth) {
                                if (dayOfMonthNow < dayOfMonthBirth) age--;
                            } else {
                                age--;
                                Map<String,Object> ggg = new HashMap<>();
                                ggg.put("AGE",age);
                                db.collection("User")
                                        .document(mAuth.getUid())
                                        .update(ggg);
                            }
                        }
                        return age;
                    }
                    //設定初始的顯示日期
                }, 2000, 0, 1).show();

            }
        });


    }

}