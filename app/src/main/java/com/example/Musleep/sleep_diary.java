package com.example.Musleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class sleep_diary extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView diary_list;
    private FirestoreRecyclerAdapter adapter;

    Calendar today = new GregorianCalendar();
    int year = today.get(today.YEAR);
    int month = today.get(today.MONTH)+1;
    int day = today.get(today.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_diary);

        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        diary_list = findViewById(R.id.diary_list);

        //Query
        Query query =db.collection("User").document(mAuth.getUid()).collection("SleepDiary");
        //RecyclerOptions
        FirestoreRecyclerOptions<diary_list> options = new FirestoreRecyclerOptions.Builder<diary_list>()
                .setQuery(query, diary_list.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<diary_list, ListViewHolder>(options) {
            @NonNull
            @Override
            public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
                return new ListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull com.example.Musleep.diary_list model) {
                holder.list_date.setText(model.getDate());
                holder.list_wakeup.setText(model.getWakeup()+ "");
                holder.list_coffee.setText(model.getCoffee()+ "");
                holder.list_nap.setText(model.getNap()+ "");
                holder.list_wine.setText(model.getWine()+ "");
                holder.list_drug.setText(model.getDrug()+ "");
                holder.list_sport.setText(model.getSport()+ "");

            }
        };
        diary_list.setHasFixedSize(true);
        diary_list.setLayoutManager(new LinearLayoutManager(this));
        diary_list.setAdapter(adapter);
        //橫向的list
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView myList = (RecyclerView) findViewById(R.id.diary_list);
        myList.setLayoutManager(layoutManager);

        //創建當日睡眠日誌之文件
        findViewById(R.id.data_pick).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                DocumentReference docIdRef = rootRef.collection("User")
                        .document(mAuth.getUid())
                        .collection("SleepDiary")
                        .document(year+"-"+month+"-"+day);
                //判斷文件是否存在
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                startActivity(new Intent(getApplicationContext(), dairy_survey.class));
                                Log.d("INFO", "Document exists!");
                            } else {
                                startActivity(new Intent(getApplicationContext(), dairy_survey.class));
                                db = FirebaseFirestore.getInstance();
                                Map<String,Object> dairy = new HashMap<>();
                                dairy.put("date",month+"/"+day);
                                db.collection("User")
                                    .document(mAuth.getUid())
                                    .collection("SleepDiary")
                                    .document(year+"-"+month+"-"+day)
                                    .set(dairy);
                                Log.d("INFO", "Document does not exist!");
                            }
                        } else {
                            Log.d("INFO", "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });//睡眠日誌結束

    }

    private class ListViewHolder extends RecyclerView.ViewHolder{

        private TextView list_date;
        private TextView list_wakeup;
        private TextView list_coffee;
        private TextView list_nap;
        private TextView list_wine;
        private TextView list_drug;
        private TextView list_sport;

        public ListViewHolder(@NonNull View itemView){
            super(itemView);

            list_wakeup = itemView.findViewById(R.id.list_wakeup);
            list_date = itemView.findViewById(R.id.list_date);
            list_coffee = itemView.findViewById(R.id.list_coffee);
            list_nap = itemView.findViewById(R.id.list_nap);
            list_wine = itemView.findViewById(R.id.list_wine);
            list_drug = itemView.findViewById(R.id.list_drug);
            list_sport = itemView.findViewById(R.id.list_sport);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}