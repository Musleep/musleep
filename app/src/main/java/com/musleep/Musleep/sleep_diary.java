package com.musleep.Musleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class sleep_diary extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.musleep.Musleep.EXTRA_TEXT";
    public static final String EXTRA_NUMBER = "com.musleep.Musleep.EXTRA_NUMBER";

    private FirebaseFirestore db;
    private RecyclerView diary_list;
    private FirestoreRecyclerAdapter adapter;
    Dialog dialog;

    Calendar today = new GregorianCalendar();
    int year = today.get(today.YEAR);
    int month = today.get(today.MONTH)+1;
    int day = today.get(today.DAY_OF_MONTH);
    int week = today.get(today.DAY_OF_WEEK);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_diary);

        db = FirebaseFirestore.getInstance();
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        diary_list = findViewById(R.id.diary_list);

        //Query
        Query query = db.collection("User").document(mAuth.getUid()).collection("SleepDiary")
                .orderBy("date", Query.Direction.DESCENDING).limit(14);
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
            protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull com.musleep.Musleep.diary_list model) {
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

        //顯示當前日期
        TextView now_date = (TextView) findViewById(R.id.now_date);
        TextView now_week = findViewById(R.id.now_week);
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date();
        now_date.setText(sdFormat.format(current));
//        String currentDate = year+"/"+month+"/"+day;
//        now_date.setText(currentDate);
        if (week == 1){
            now_week.setText("星期日");
        }
        if (week == 2){
            now_week.setText("星期一");
        }
        if (week == 3){
            now_week.setText("星期二");
        }
        if (week == 4){
            now_week.setText("星期三");
        }
        if (week == 5){
            now_week.setText("星期四");
        }
        if (week == 6){
            now_week.setText("星期五");
        }
        if (week == 7){
            now_week.setText("星期六");
        }


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
                        .document(sdFormat.format(current));
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
                                    .document(sdFormat.format(current))
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

        //選擇日期
//        TextView chosen_date = findViewById(R.id.chosen_date);
        Button chosen_date_btn = findViewById(R.id.chosen_date_btn);
//        TextView date2 = findViewById(R.id.date2);
//        TextView item1_1 = findViewById(R.id.item1_1);
//        TextView item2_1 = findViewById(R.id.item2_1);
//        TextView item3_1 = findViewById(R.id.item3_1);
//        TextView item4_1 = findViewById(R.id.item4_1);
//        TextView item5_1 = findViewById(R.id.item5_1);
//        TextView item6_1 = findViewById(R.id.item6_1);


        chosen_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(sleep_diary.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        openDialog();

                        Calendar cal = Calendar.getInstance();
//                        獲得選擇日期
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month+1);
                        cal.set(Calendar.DAY_OF_MONTH, day);
//                        String chosen_date_show = String.format("%d/%d/%d", year, month+1, day);
                        //顯示生日（月份要+1，因為這個方法是從0開始算的）
//                        chosen_date.setText(chosen_date_show);

                        String chosen_date_show2 = String.format("%d-%02d-%d", year, month+1, day);

                        DocumentReference docRef = db.collection("User")
                                .document(mAuth.getUid())
                                .collection("SleepDiary")
                                .document(chosen_date_show2);
                        Log.i("幹" ,chosen_date_show2);
                        Intent intent = new Intent(sleep_diary.this,sleep_dialog.class);
                        intent.putExtra(EXTRA_TEXT,chosen_date_show2);
//                      intent.putExtra(EXTRA_NUMBER,number);
                        startActivity(intent);
//                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
////                        Log.d("TAG", "DocumentSnapshot data: " + document.getData().get("Name").toString());
//                                        String date = document.getData().get("date").toString();
//                                        String coffee = document.getData().get("coffee").toString();
//                                        String drug = document.getData().get("drug").toString();
//                                        String nap = document.getData().get("nap").toString();
//                                        String sport = document.getData().get("sport").toString();
//                                        String wakeup = document.getData().get("wakeup").toString();
//                                        String wine = document.getData().get("wine").toString();
//
//                                        date2.setText(date);
//                                        item1_1.setText(wakeup);
//                                        item2_1.setText(nap);
//                                        item3_1.setText(coffee);
//                                        item4_1.setText(wine);
//                                        item5_1.setText(drug);
//                                        item6_1.setText(sport);
//
//                                        return;
//
//                                    } else {
//                                        Log.d("TAG", "No such document");
//                                    }
//                                } else {
//                                    Log.d("TAG", "get failed with ", task.getException());
//                                }
//                            }
//                        });

                    }

                    //設定初始的顯示日期
                }, year, month-1, day).show();
            }

        });

    }
    public void back(final View view) {
        startActivity(new Intent(view.getContext(),homescreen.class));
    }
}