package com.musleep.Musleep;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class alarm extends AppCompatActivity {
    FirebaseFirestore db;
    String hour,minute;
    Boolean isFront;
    TextView MWake,MSleep,TWake,TSleep,WWake,WSleep,ThWake,ThSleep,FWake,FSleep,SWake,SSleep,SuWake,SuSleep;
    Float scale;
    AnimatorSet front_anim;
    AnimatorSet back_anim;
    Button flip1,flip2,flip3,flip4,flip5,flip6,flip7;
    private Object alarm;
    int MWHour,MWMin,MSHour,MSMin,TWHour,TWMin,TSHour,TSMin,WWHour,WWMin,WSHour,WSMin,ThWHour,ThWMin,ThSHour,ThSMin,FWHour,FWMin,FSHour,FSMin,SWHour,SWMin,SSHour,SSMin,SuWHour,SuWMin,SuSHour,SuSMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        MWake = findViewById(R.id.MWake);
        MSleep = findViewById(R.id.MSleep);
        TWake = findViewById(R.id.TWake);
        TSleep = findViewById(R.id.TSleep);
        WWake = findViewById(R.id.WWake);
        WSleep = findViewById(R.id.WSleep);
        ThWake = findViewById(R.id.ThWake);
        ThSleep = findViewById(R.id.ThSleep);
        FWake = findViewById(R.id.FWake);
        FSleep = findViewById(R.id.FSleep);
        SWake = findViewById(R.id.SWake);
        SSleep = findViewById(R.id.SSleep);
        SuWake = findViewById(R.id.SuWake);
        SuSleep = findViewById(R.id.SuSleep);

        //firebase time reference
        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference col =db.collection("User")
                .document(mAuth.getUid())
                .collection("time");

        //timer
        //Monday
        MWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                MWHour = hourOfDay;
                                MWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,MWHour,MWMin);
                                MWake.setText(DateFormat.format("hh:mm aa",calendar));
                                //set firebase
                                Map<String, Object> time = new HashMap<>();
                                time.put("wakeHour", String.format("%02d",MWHour));
                                time.put("wakeMin", String.format("%02d",MWMin));
                                col.document("Monday")
                                        .set(time, SetOptions.merge());
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(MWHour,MWMin);
                timePickerDialog.show();
            }
        });
        MSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                MSHour = hourOfDay;
                                MSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,MSHour,MSMin);
                                MSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(MSHour,MSMin);
                timePickerDialog.show();
            }
        });

        //Tuesday
        TWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                TWHour = hourOfDay;
                                TWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,TWHour,TWMin);
                                TWake.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(TWHour,TWMin);
                timePickerDialog.show();
            }
        });
        TSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                TSHour = hourOfDay;
                                TSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,TSHour,TSMin);
                                TSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(TSHour,TSMin);
                timePickerDialog.show();
            }
        });

        //Wednesday
        WWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                WWHour = hourOfDay;
                                WWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,WWHour,WWMin);
                                WWake.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(WWHour,WWMin);
                timePickerDialog.show();
            }
        });
        WSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                WSHour = hourOfDay;
                                WSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,WSHour,WSMin);
                                WSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(WSHour,WSMin);
                timePickerDialog.show();
            }
        });

        //Thursday
        ThWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                ThWHour = hourOfDay;
                                ThWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,ThWHour,ThWMin);
                                ThWake.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(ThWHour,ThWMin);
                timePickerDialog.show();
            }
        });
        ThSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                ThSHour = hourOfDay;
                                ThSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,ThSHour,ThSMin);
                                ThSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(ThSHour,ThSMin);
                timePickerDialog.show();
            }
        });

        //Friday
        FWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                FWHour = hourOfDay;
                                FWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,FWHour,FWMin);
                                FWake.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(FWHour,FWMin);
                timePickerDialog.show();
            }
        });
        FSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                FSHour = hourOfDay;
                                FSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,FSHour,FSMin);
                                FSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(FSHour,FSMin);
                timePickerDialog.show();
            }
        });


        //Saturday
        SWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                SWHour = hourOfDay;
                                SWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,SWHour,SWMin);
                                SWake.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(SWHour,SWMin);
                timePickerDialog.show();
            }
        });
        SSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                SSHour = hourOfDay;
                                SSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,SSHour,SSMin);
                                SSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(SSHour,SSMin);
                timePickerDialog.show();
            }
        });

        //Saturday
        SuWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                SuWHour = hourOfDay;
                                SuWMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,SuWHour,SuWMin);
                                SuWake.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(SuWHour,SuWMin);
                timePickerDialog.show();
            }
        });
        SuSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                SuSHour = hourOfDay;
                                SuSMin = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,SuSHour,SuSMin);
                                SuSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(SuSHour,SuSMin);
                timePickerDialog.show();
            }
        });


        //card animate
        //MondayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        MWake.setCameraDistance(8000 * scale);
        MSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip1 = findViewById(R.id.flip1);
        flip1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    MWake.setClickable(false);
                    front_anim.setTarget(MWake);
                    back_anim.setTarget(MSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip1.setText("就寢時間");
                }else{
                    MWake.setClickable(true);
                    front_anim.setTarget(MSleep);
                    back_anim.setTarget(MWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip1.setText("起床時間");

                }
            }
        });

        //TuesdayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        TWake.setCameraDistance(8000 * scale);
        TSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip2 = findViewById(R.id.flip2);
        flip2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    TWake.setClickable(false);
                    front_anim.setTarget(TWake);
                    back_anim.setTarget(TSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip2.setText("就寢時間");
                }else{
                    TWake.setClickable(true);
                    front_anim.setTarget(TSleep);
                    back_anim.setTarget(TWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip2.setText("起床時間");

                }
            }
        });

        //WednesdayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        WWake.setCameraDistance(8000 * scale);
        WSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip3 = findViewById(R.id.flip3);
        flip3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    WWake.setClickable(false);
                    front_anim.setTarget(WWake);
                    back_anim.setTarget(WSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip3.setText("就寢時間");
                }else{
                    WWake.setClickable(true);
                    front_anim.setTarget(WSleep);
                    back_anim.setTarget(WWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip3.setText("起床時間");

                }
            }
        });

        //ThursdayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        ThWake.setCameraDistance(8000 * scale);
        ThSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip4 = findViewById(R.id.flip4);
        flip4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    ThWake.setClickable(false);
                    front_anim.setTarget(ThWake);
                    back_anim.setTarget(ThSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip4.setText("就寢時間");
                }else{
                    ThWake.setClickable(true);
                    front_anim.setTarget(ThSleep);
                    back_anim.setTarget(ThWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip4.setText("起床時間");

                }
            }
        });

        //FridayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        FWake.setCameraDistance(8000 * scale);
        FSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip5 = findViewById(R.id.flip5);
        flip5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    FWake.setClickable(false);
                    front_anim.setTarget(FWake);
                    back_anim.setTarget(FSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip5.setText("就寢時間");
                }else{
                    FWake.setClickable(true);
                    front_anim.setTarget(FSleep);
                    back_anim.setTarget(FWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip5.setText("起床時間");

                }
            }
        });

        //SaturdayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        SWake.setCameraDistance(8000 * scale);
        SSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip6 = findViewById(R.id.flip6);
        flip6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    SWake.setClickable(false);
                    front_anim.setTarget(SWake);
                    back_anim.setTarget(SSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip6.setText("就寢時間");
                }else{
                    SWake.setClickable(true);
                    front_anim.setTarget(SSleep);
                    back_anim.setTarget(SWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip6.setText("起床時間");

                }
            }
        });

        //SundayFlip
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        SuWake.setCameraDistance(8000 * scale);
        SuSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip7 = findViewById(R.id.flip7);
        flip7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    SuWake.setClickable(false);
                    front_anim.setTarget(SuWake);
                    back_anim.setTarget(SuSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip7.setText("就寢時間");
                }else{
                    SuWake.setClickable(true);
                    front_anim.setTarget(SuSleep);
                    back_anim.setTarget(SuWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip7.setText("起床時間");

                }
            }
        });

        //Firebase抓時間
        DocumentReference docRef = db.collection("User")
                .document("jyqYBzD67eOCMCQY2j52b5aKcaH2")
                .collection("time")
                .document("Monday");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i("TAG", documentSnapshot.getString("wakeHour")+":"+documentSnapshot.getString("wakeMin"));
                Log.i("TAG", documentSnapshot.getString("wakeMin"));
                hour = documentSnapshot.getString("wakeHour");
                minute = documentSnapshot.getString("wakeMin");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                cal.set(Calendar.MINUTE, Integer.parseInt(minute));
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                setAlarm(cal.getTimeInMillis());
//                MWake.setText(documentSnapshot.getString("wakeHour")+":"+documentSnapshot.getString("wakeMin"));
            }

        });
    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, wakeup.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0, intent,0);

        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }
}