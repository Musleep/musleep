package com.example.Musleep;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;



public class MusicTest extends AppCompatActivity {
    //定義變量
    private ImageView diskImage;
    private SeekBar musicProgress;
    private ImageButton prevBtn, playBtn, nextBtn;
    private TextView currentTime, totalTime;
    FirebaseFirestore db;
    private ObjectAnimator animator;
    private MediaPlayer player;
    RadioButton relax,nofeel,stress;
    private int currentPlaying = 0; //用ArrayList放當前播放的歌曲
    private ArrayList<Integer> playList = new ArrayList<>();

    private boolean isPausing = false, isPlaying = false; //音樂暫停狀態, 音樂第一次播放之後變為true


//    Appraise appraise;
//    FirebaseDatabase database;
//    DatabaseReference reference;
    ImageButton save_btn;
    int i = 0;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_test);
        db = FirebaseFirestore.getInstance();
        save_btn = findViewById(R.id.btn_next);
//        appraise = new Appraise ();
        relax = findViewById(R.id.relax);
        nofeel = findViewById(R.id.nofeel);
        stress = findViewById(R.id.stress);
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
      //資料庫
//      reference = database.getInstance().getReference().child("start");
//      reference.addValueEventListener(new ValueEventListener() {
//          @Override
//          public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//              if (snapshot.exists()){
//                  i = (int)snapshot.getChildrenCount();
//              }
//          }
//
//          @Override
//          public void onCancelled(@NonNull DatabaseError error) {
//              ///
//          }
//      });
      //上傳radiogroup的ID
      RadioGroup radioGroup = (RadioGroup) findViewById(R.id.feelings);
      radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup radiogroup,@IdRes int selectId) {
              int selectedId = radioGroup.getCheckedRadioButtonId();
              switch(selectedId){
                  case R.id.relax:
                      Map<String,Object> relax = new HashMap<>();
                      relax.put("S1",0);
                      db.collection("User")
                              .document("k53CGABt5HZKkYf77DQ5k3qOHOc2")
                              .collection("week0")
                              .document("FirstScore")
                              .update(relax)
                              .addOnSuccessListener(new OnSuccessListener<Void>(){
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      Log.i("INFO", "DocumentSnapshot successfully written!");
                                  }
                              })
                              .addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Log.i("INFO", "Error writing document", e);
                                  }
                              });
                      Toast.makeText(MusicTest.this,"覺得放鬆",Toast.LENGTH_SHORT).show();
                      break;
                  case R.id.nofeel:
                      Map<String,Object> nofeel = new HashMap<>();
                      nofeel.put("S1",1);
                      db.collection("User")
                              .document("k53CGABt5HZKkYf77DQ5k3qOHOc2")
                              .collection("week0")
                              .document("FirstScore")
                              .update(nofeel)
                              .addOnSuccessListener(new OnSuccessListener<Void>(){
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      Log.i("INFO", "DocumentSnapshot successfully written!");
                                  }
                              })
                              .addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Log.i("INFO", "Error writing document", e);
                                  }
                              });


                      Toast.makeText(MusicTest.this,"覺得沒感覺",Toast.LENGTH_SHORT).show();
                      break;
                  case R.id.stress:
                      Map<String,Object> stress = new HashMap<>();
                      stress.put("S1",2);
                      db.collection("User")
                              .document("k53CGABt5HZKkYf77DQ5k3qOHOc2")
                              .collection("week0")
                              .document("FirstScore")
                              .update(stress)
                              .addOnSuccessListener(new OnSuccessListener<Void>(){
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      Log.i("INFO", "DocumentSnapshot successfully written!");
                                  }
                              })
                              .addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Log.i("INFO", "Error writing document", e);
                                  }
                              });


                      Toast.makeText(MusicTest.this,"覺得緊張",Toast.LENGTH_SHORT).show();
                      break;

              }
          }
      });

      save_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //評價
              String m1 = relax.getText().toString();
              String m2 = nofeel.getText().toString();
              String m3 = stress.getText().toString();


//              if (relax.isChecked()) {
//                  appraise.setappRaise(m1);
//                  reference.child(String.valueOf(i+1)).setValue(appraise);
//                  Log.i("INFO", "onClick: 覺得放鬆");
//              }else if (nofeel.isChecked()) {
//                  appraise.setappRaise(m2);
//                  reference.child(String.valueOf(i+1)).setValue(appraise);
//              }else {
//                  appraise.setappRaise(m3);
//                  reference.child(String.valueOf(i+1)).setValue(appraise);
//              }
          }
      });

        init();
        preparePlaylist();
      TimerTask timerTask = new TimerTask() {
          @Override
          public void run() {
              if (isPlaying) {
                  updateTimer();
              }
          }
      };
      new Timer().scheduleAtFixedRate(timerTask, 0, 500);

    }

    void init(){
        diskImage = findViewById(R.id.iv_img1);
        musicProgress = findViewById(R.id.sb_progress);
        currentTime = findViewById(R.id.tv_progress_current);
        totalTime = findViewById(R.id.tv_progress_total);
        prevBtn = findViewById(R.id.btn_prev);
        playBtn = findViewById(R.id.btn_play_pause);
        nextBtn = findViewById(R.id.btn_next);


        onClickControl onClick = new onClickControl();
        prevBtn.setOnClickListener(onClick); //傳遞參數
        playBtn.setOnClickListener(onClick);
        nextBtn.setOnClickListener(onClick);

        onSeekBarChangeControl onSbchange = new onSeekBarChangeControl();
        musicProgress.setOnSeekBarChangeListener(onSbchange);

        //照片轉動
        animator = ObjectAnimator.ofFloat(diskImage, "rotation", 0, 360.0F); //初始化狀態
        animator.setDuration(10000); //轉動時間，10秒
        animator.setInterpolator(new LinearInterpolator()); //時間函數
        animator.setRepeatCount(-1); //一直轉動
    }


    private void preparePlaylist() {
        Field[] fields = R.raw.class.getFields();
        for (int count = 0; count < fields.length; count++) {
            Log.i("Raw Asset", fields[count].getName());
            try {
                int resId = fields[count].getInt(fields[count]);
                playList.add(resId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareMedia() {
        if (isPlaying) {
            player.stop();
            player.reset();
        }
        player = MediaPlayer.create(getApplicationContext(), playList.get(currentPlaying)); //獲取當前
        int musicDuration = player.getDuration(); //當前歌曲長度
        musicProgress.setMax(musicDuration);
        int sec = musicDuration / 1000;
        int min = sec / 60;
        sec -= min * 60;
        String musicTime = String.format("%02d:%02d", min, sec); //格式化輸出
        totalTime.setText(musicTime);
        player.start(); //播放音樂
    }

    //評價上傳
    private void upload() {}

    private void updateTimer() {
        runOnUiThread(() -> {
            int currentMs = player.getCurrentPosition();
            int sec = currentMs / 1000;
            int min = sec / 60;
            sec -= min * 60;
            String time = String.format("%02d:%02d", min, sec);
            musicProgress.setProgress(currentMs);
            currentTime.setText(time); //更新目前音樂時間
        });
    }



    private class onClickControl implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_prev:
                    //重播、切上一首歌
                    Log.i("INFO", "onClick: 重播按鈕被點擊!");
                    playBtn.setImageResource(R.drawable.pause); //切換成暫停鍵
                    animator.start();
                    if (!player.isPlaying()){ //非正在播放狀態才可以切歌
                        currentPlaying = --currentPlaying % playList.size();
                    }
                    prepareMedia();
                    isPausing = false;
                    isPlaying = true;
                    break;
                case R.id.btn_play_pause:
                    //開始/暫停
                    Log.i("INFO", "onClick: 開始/暫停按鈕被點擊!");
                    if (!isPausing && !isPlaying){ //暫停狀態，且從未被播放
                        //開始播放
                        playBtn.setImageResource(R.drawable.pause); //切換成暫停鍵
                        animator.start();
                        prepareMedia();
                        isPlaying = true;
                    }else if (isPausing && isPlaying){ //暫停狀態，且被播放過一次
                        //繼續播放
                        playBtn.setImageResource(R.drawable.pause); //切換成暫停鍵
                        animator.resume();
                        player.start();

                    }else { //播放狀態
                        //暫停播放
                        playBtn.setImageResource(R.drawable.play); //切換成播放鍵
                        animator.pause();
                        player.pause();
                    }
                    isPausing = !isPausing; //切換狀態

                    break;
                case R.id.btn_next:
                    //切歌
                    Log.i("INFO", "onClick: 切歌按鈕被點擊!");
                    playBtn.setImageResource(R.drawable.pause); //切換成暫停鍵
                    currentPlaying = ++currentPlaying % playList.size();
                    prepareMedia();
                    animator.start();
                    isPausing = false;
                    isPlaying = true;
                    break;
                default:
                    Log.i("INFO", "onClick: 按鈕被點擊但有bug!");
                    //有bug了
            }
        }
    }

    private class  onSeekBarChangeControl implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                player.seekTo(progress); //musicProgress的總時長，progress到當前時間點
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            player.pause();
            animator.pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.start();
            if (seekBar.getProgress() < 10) {
                animator.start();
            } else {
                animator.resume();
            }
        }
    }


}