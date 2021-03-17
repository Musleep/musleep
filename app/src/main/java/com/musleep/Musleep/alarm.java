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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

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
    int MWHour,MWMin,MSHour,MSMin;

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
                                MSleep.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(MWHour,MWMin);
                timePickerDialog.show();
            }
        });
        MSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(
                        alarm.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view1, int hourOfDay1, int minute1) {
                                MSHour = hourOfDay1;
                                MSMin = minute1;
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(0,0,0,MSHour,MSMin);
                                MWake.setText(DateFormat.format("hh:mm aa",calendar1));
                            }
                        },12,0,false
                );
                timePickerDialog1.updateTime(MSHour,MSMin);
                timePickerDialog1.show();
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
                    MSleep.setClickable(false);
                    front_anim.setTarget(MWake);
                    back_anim.setTarget(MSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                    flip1.setText("鬧鐘");
                }else{
                    front_anim.setTarget(MSleep);
                    back_anim.setTarget(MWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                    flip1.setText("");

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
                    front_anim.setTarget(TWake);
                    back_anim.setTarget(TSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(TSleep);
                    back_anim.setTarget(TWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
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
                    front_anim.setTarget(WWake);
                    back_anim.setTarget(WSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(WSleep);
                    back_anim.setTarget(WWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
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
                    front_anim.setTarget(ThWake);
                    back_anim.setTarget(ThSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(ThSleep);
                    back_anim.setTarget(ThWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
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
                    front_anim.setTarget(FWake);
                    back_anim.setTarget(FSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(FSleep);
                    back_anim.setTarget(FWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
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
                    front_anim.setTarget(SWake);
                    back_anim.setTarget(SSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(SSleep);
                    back_anim.setTarget(SWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
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
                    front_anim.setTarget(SuWake);
                    back_anim.setTarget(SuSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(SuSleep);
                    back_anim.setTarget(SuWake);
                    back_anim.start();
                    front_anim.start();
                    isFront = Boolean.TRUE;
                }
            }
        });

        //Firebase抓時間
        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
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