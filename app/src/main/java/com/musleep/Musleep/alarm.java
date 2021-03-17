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
    TextView MWake,MSleep;
    Float scale;
    AnimatorSet front_anim;
    AnimatorSet back_anim;
    Button flip;
    private Object alarm;
    int MWHour,MWMin,MSHour,MSMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        MWake = findViewById(R.id.MWake);
        MSleep = findViewById(R.id.MSleep);

        //timer
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
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(MWHour,MWMin);
                timePickerDialog.show();
            }
        });

        //card animate
        isFront = Boolean.TRUE;
        scale = getApplicationContext().getResources().getDisplayMetrics().density;
        MWake.setCameraDistance(8000 * scale);
        MSleep.setCameraDistance(8000 * scale);
        front_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.front_animator);
        back_anim = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.back_animator);

        flip = findViewById(R.id.flip1);
        flip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isFront){
                    front_anim.setTarget(MWake);
                    back_anim.setTarget(MSleep);
                    front_anim.start();
                    back_anim.start();
                    isFront = Boolean.FALSE;
                }else{
                    front_anim.setTarget(MSleep);
                    back_anim.setTarget(MWake);
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