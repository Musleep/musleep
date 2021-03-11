package com.musleep.Musleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class init_time extends AppCompatActivity {

    FirebaseFirestore db;
    TextView timeHour,timeHour1,timeMinute,timeMinute1;
    LinearLayout setTime, setTime1;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    Button next_btn;
    int currentHour;
    int currentMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_time);
        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        //起床
        timeHour = findViewById(R.id.timeHour);
        timeMinute = findViewById(R.id.timeMinute);
        setTime = findViewById(R.id.setTime);
        //就寢
        timeHour1 = findViewById(R.id.timeHour1);
        timeMinute1 = findViewById(R.id.timeMinute1);
        setTime1 = findViewById(R.id.setTime1);
        //起床
        setTime.setOnClickListener((v) -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(init_time.this,(timePicker, hourOfDay, minutes) -> {
                timeHour.setText(String.format("%02d",hourOfDay));
                timeMinute.setText(String.format("%02d",minutes));
                Map<String, Object> time = new HashMap<>();
                time.put("wakeHour", String.format("%02d",hourOfDay));
                time.put("wakeMin", String.format("%02d",minutes));
                CollectionReference col =db.collection("User")
                        .document(mAuth.getUid())
                        .collection("time");
                        col.document("Monday")
                        .set(time, SetOptions.merge());
                        col.document("Tuesday")
                        .set(time, SetOptions.merge());
                        col.document("Wednesday")
                        .set(time, SetOptions.merge());
                        col.document("Thursday")
                        .set(time, SetOptions.merge());
                        col.document("Friday")
                        .set(time, SetOptions.merge());
                        col.document("Saturday")
                        .set(time, SetOptions.merge());
                        col.document("Sunday")
                        .set(time, SetOptions.merge());
            },currentHour, currentMinute, false);


            timePickerDialog.show();
        });
        //就寢
        setTime1.setOnClickListener((v) -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(init_time.this,(timePicker, hourOfDay1, minutes1) -> {
                timeHour1.setText(String.format("%02d",hourOfDay1));
                timeMinute1.setText(String.format("%02d",minutes1));
                Map<String, Object> time1 = new HashMap<>();
                time1.put("sleepHour", String.format("%02d",hourOfDay1));
                time1.put("sleepMin", String.format("%02d",minutes1));
                CollectionReference col =db.collection("User")
                        .document(mAuth.getUid())
                        .collection("time");
                col.document("Monday")
                        .set(time1, SetOptions.merge());
                col.document("Tuesday")
                        .set(time1, SetOptions.merge());
                col.document("Wednesday")
                        .set(time1, SetOptions.merge());
                col.document("Thursday")
                        .set(time1, SetOptions.merge());
                col.document("Friday")
                        .set(time1, SetOptions.merge());
                col.document("Saturday")
                        .set(time1, SetOptions.merge());
                col.document("Sunday")
                        .set(time1, SetOptions.merge());
            },currentHour, currentMinute, false);

            timePickerDialog.show();
        });
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                DocumentReference docIdRef = rootRef.collection("User")
                        .document(mAuth.getUid())
                        .collection("time")
                        .document("Monday");
                //判斷文件是否存在
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.getData().size() == 4){
                                    startActivity(new Intent(getApplicationContext(), MusicTest.class));
                                }else {
                                    Toast.makeText(getApplicationContext(), "請設定作息時間！", Toast.LENGTH_SHORT).show();
                                }
                                return;

                            } else {
                                Toast.makeText(getApplicationContext(), "請設定作息時間！", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.d("INFO", "Failed with: ", task.getException());
                        }
                    }
                });

            }
        });
    }
}