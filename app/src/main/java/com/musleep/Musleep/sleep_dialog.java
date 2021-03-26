package com.musleep.Musleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class sleep_dialog extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_dialog);

        Intent intent = getIntent();
        String chosen_date_show2 = intent.getStringExtra(sleep_diary.EXTRA_TEXT);
        Log.i("TAG" ,chosen_date_show2);
        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        TextView date2 = findViewById(R.id.date2);
        TextView item1_1 = findViewById(R.id.item1_1);
        TextView item2_1 = findViewById(R.id.item2_1);
        TextView item3_1 = findViewById(R.id.item3_1);
        TextView item4_1 = findViewById(R.id.item4_1);
        TextView item5_1 = findViewById(R.id.item5_1);
        TextView item6_1 = findViewById(R.id.item6_1);
        DocumentReference docRef = db.collection("User")
                .document(mAuth.getUid())
                .collection("SleepDiary")
                .document(chosen_date_show2);


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("TAG", "DocumentSnapshot data: " + document.getData().get("Name").toString());
                        String date = document.getData().get("date").toString();
                        String coffee = document.getData().get("coffee").toString();
                        String drug = document.getData().get("drug").toString();
                        String nap = document.getData().get("nap").toString();
                        String sport = document.getData().get("sport").toString();
                        String wakeup = document.getData().get("wakeup").toString();
                        String wine = document.getData().get("wine").toString();

                        date2.setText(date);
                        item1_1.setText(wakeup);
                        item2_1.setText(nap);
                        item3_1.setText(coffee);
                        item4_1.setText(wine);
                        item5_1.setText(drug);
                        item6_1.setText(sport);

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
}