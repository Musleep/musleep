package com.example.Musleep;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class dairy_survey extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_survey);
        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

        Calendar today = new GregorianCalendar();
        int year = today.get(today.YEAR);
        int month = today.get(today.MONTH) + 1;
        int day = today.get(today.DAY_OF_MONTH);

        //wakeup上傳radiogroup的ID
        RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.wakeup);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radiogroup, @IdRes int selectId) {
                int selectedId = radioGroup1.getCheckedRadioButtonId();

                Log.i("INFO", String.valueOf(year));
                Log.i("INFO", String.valueOf(month));
                Log.i("INFO", String.valueOf(day));
                switch (selectedId) {
                    case R.id.A1:
                        Map<String, Object> yes = new HashMap<>();
                        yes.put("wakeup", 1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year + "-" + month + "-" + day)
                                .update(yes);
                        Toast.makeText(dairy_survey.this, "是", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.A0:
                        Map<String, Object> no = new HashMap<>();
                        no.put("wakeup", 0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year + "-" + month + "-" + day)
                                .update(no);
                        Toast.makeText(dairy_survey.this, "否", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

        //nap上傳radiogroup的ID
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.nap);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
                int selectedId = radioGroup2.getCheckedRadioButtonId();
                switch(selectedId){
                    case R.id.B1:
                        Map<String,Object> yes = new HashMap<>();
                        yes.put("nap",1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(yes);
                        Toast.makeText(dairy_survey.this,"是",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.B0:
                        Map<String,Object> no = new HashMap<>();
                        no.put("nap",0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(no);
                        Toast.makeText(dairy_survey.this,"否",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

        //coffee上傳radiogroup的ID
        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.coffee);
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
                int selectedId = radioGroup3.getCheckedRadioButtonId();

                Log.i("INFO", String.valueOf(year));
                Log.i("INFO", String.valueOf(month));
                Log.i("INFO", String.valueOf(day));
                switch(selectedId){
                    case R.id.C1:
                        Map<String,Object> yes = new HashMap<>();
                        yes.put("coffee",1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(yes);
                        Toast.makeText(dairy_survey.this,"是",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.C0:
                        Map<String,Object> no = new HashMap<>();
                        no.put("coffee",0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(no);
                        Toast.makeText(dairy_survey.this,"否",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        //wine上傳radiogroup的ID
        RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.wine);
        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
                int selectedId = radioGroup4.getCheckedRadioButtonId();

                Log.i("INFO", String.valueOf(year));
                Log.i("INFO", String.valueOf(month));
                Log.i("INFO", String.valueOf(day));
                switch(selectedId){
                    case R.id.D1:
                        Map<String,Object> yes = new HashMap<>();
                        yes.put("wine",1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(yes);
                        Toast.makeText(dairy_survey.this,"是",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.D0:
                        Map<String,Object> no = new HashMap<>();
                        no.put("wine",0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(no);
                        Toast.makeText(dairy_survey.this,"否",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

        //drug上傳radiogroup的ID
        RadioGroup radioGroup5 = (RadioGroup) findViewById(R.id.drug);
        radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
                int selectedId = radioGroup5.getCheckedRadioButtonId();

                Log.i("INFO", String.valueOf(year));
                Log.i("INFO", String.valueOf(month));
                Log.i("INFO", String.valueOf(day));
                switch(selectedId){
                    case R.id.E1:
                        Map<String,Object> yes = new HashMap<>();
                        yes.put("drug",1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(yes);
                        Toast.makeText(dairy_survey.this,"是",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.E0:
                        Map<String,Object> no = new HashMap<>();
                        no.put("drug",0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(no);
                        Toast.makeText(dairy_survey.this,"否",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

        //sport上傳radiogroup的ID
        RadioGroup radioGroup6 = (RadioGroup) findViewById(R.id.sport);
        radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
                int selectedId = radioGroup6.getCheckedRadioButtonId();

                Log.i("INFO", String.valueOf(year));
                Log.i("INFO", String.valueOf(month));
                Log.i("INFO", String.valueOf(day));
                switch(selectedId){
                    case R.id.F1:
                        Map<String,Object> yes = new HashMap<>();
                        yes.put("sport",1);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(yes);
                        Toast.makeText(dairy_survey.this,"是",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.F0:
                        Map<String,Object> no = new HashMap<>();
                        no.put("sport",0);
                        db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(year+"-"+month+"-"+day)
                                .update(no);
                        Toast.makeText(dairy_survey.this,"否",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }
}