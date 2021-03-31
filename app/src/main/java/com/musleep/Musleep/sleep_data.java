package com.musleep.Musleep;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Color;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sleep_data#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sleep_data extends Fragment {
    PieChart pieChart;
    private TextToSpeech tts;
    private static final String REQUEST_CODE_TTS = "TextToSpeech";
    private static final String TAG = sleep_data.class.getSimpleName();
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public sleep_data() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sleep_data.
     */
    // TODO: Rename and change types and number of parameters
    public static sleep_data newInstance(String param1, String param2) {
        sleep_data fragment = new sleep_data();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTTS();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();

        if (getArguments() != null) {
            pieChart.invalidate();
        }

//        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                  if(status == TextToSpeech.SUCCESS){
//                      int result = mTTS.setLanguage(Locale.CHINA);
//
//                      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
//                          Log.e("TTS", "不支援");
//                      }else {
//                          Log.e("TTS", "不成功");
//                      }
//                  }else {
//                      Log.e("TTS", "不成功");
//                  }
//            }
//        });


    }

    private void setTTS(){
        if (tts != null) return;

        //建立TTS物件
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) { // TTS 初始化成功

                    tts.setLanguage(Locale.CHINESE);
                    Log.d(TAG, "isLanguageAvailable(Locale.CHINESE): " + tts.isLanguageAvailable(Locale.CHINESE));


                    Log.d(TAG, "isLanguageAvailable(Locale.US): " + tts.isLanguageAvailable(Locale.US));

                    tts.speak("昨晚總計睡眠時間是8小時16分鐘", TextToSpeech.QUEUE_FLUSH, null);
                    tts.speak("", TextToSpeech.QUEUE_ADD, null);

                    tts.setSpeechRate(1); // 設定語速
                    tts.setPitch(1); // 設定音調
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        // 釋放 TTS
        if (tts != null) tts.shutdown();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleep_data, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);
        pieChart.setNoDataText("Touch me.");
        pieChart.setNoDataTextColor(Color.rgb(89, 114, 113));
//        setupPiechart();
//        loadPiechart();

        DocumentReference docRef = db.collection("User").document(firebaseAuth.getUid()).collection("SleepData").document("2021-3-4");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    String lightSleep = document.getData().get("lightSleep").toString();
                    String deepSleep = document.getData().get("deepSleep").toString();
                    String wakeUp = document.getData().get("wakeUp").toString();
                    String REM = document.getData().get("REM").toString();
//                        Log.i("fucking give me sleepdata", (String) sleepDataList.get(0));


                    pieChart.setUsePercentValues(true); //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
                    pieChart.getDescription().setEnabled(false);
                    pieChart.animateY(1000);// 設定pieChart圖表展示動畫效果
                    pieChart.setExtraOffsets(5, 10, 5, 5); //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
                    //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    //設定中間字型
                    pieChart.setCenterText("實際睡眠時間\n\n7小時56分鐘");
                    pieChart.setCenterTextSize(23);
                    pieChart.setCenterTextColor(Color.parseColor("#7c7877"));
                    pieChart.setRotationEnabled(false);
                    pieChart.setDrawHoleEnabled(true); //設定為true將餅中心清空
                    pieChart.setHoleColor(Color.argb(60,255,255,255)); //繪製在PieChart中心的顏色
                    pieChart.setTransparentCircleRadius(60f); //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
                    pieChart.setEntryLabelTextSize(16); //控制label大小9
                    pieChart.setHighlightPerTapEnabled(false);

                    ArrayList<PieEntry> yValues = new ArrayList<>();
                    yValues.add(new PieEntry(Float.parseFloat(wakeUp), "清醒"));
                    yValues.add(new PieEntry(Float.parseFloat(REM), "快速動眼期"));
                    yValues.add(new PieEntry(Float.parseFloat(lightSleep), "淺層"));
                    yValues.add(new PieEntry(Float.parseFloat(deepSleep), "深層"));


                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(20f);
                    //add many colors
                    ArrayList<Integer> colors = new ArrayList<Integer>();

                    for (int c : ColorTemplate.VORDIPLOM_COLORS)
                        colors.add(c);
                    for (int c : ColorTemplate.JOYFUL_COLORS)
                        colors.add(c);
                    for (int c : ColorTemplate.COLORFUL_COLORS)
                        colors.add(c);
                    for (int c : ColorTemplate.LIBERTY_COLORS)
                        colors.add(c);
                    for (int c : ColorTemplate.PASTEL_COLORS)
                        colors.add(c);
//        colors.add(ColorTemplate.getHoloBlue();
                    dataSet.setColors(ColorTemplate.PASTEL_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(18);
                    data.setValueTextColor(Color.WHITE);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter(pieChart));
                    data.setDrawValues(true);


                    pieChart.setData(data);
                    pieChart.invalidate();
                    pieChart.requestLayout();

                    Legend l = pieChart.getLegend();
                    l.setEnabled(true);
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setForm(Legend.LegendForm.DEFAULT);
                    l.setYOffset(10);
                    l.setFormSize(15);
                    l.setFormToTextSpace(10);
                    l.setYEntrySpace(25);
                    l.setDrawInside(false);
                    l.setTextSize(18);
                    l.setTextColor(Color.parseColor("#7c7877"));

                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });
        return view;
    }
}
//    private void setupPiechart(){
//        pieChart.setDrawHoleEnabled(true);
//        pieChart.setUsePercentValues(true);
//        pieChart.setEntryLabelTextSize(12);
//        pieChart.setEntryLabelColor(Color.BLACK);
//        pieChart.setCenterText("123123");
//        pieChart.setCenterTextSize(24);
//        pieChart.getDescription().setEnabled(false);
//
//        Legend l = pieChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setEnabled(true);
//    }
//
//    private void loadPiechart(){
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(0.2f, "A"));
//        entries.add(new PieEntry(0.15f, "B"));
//        entries.add(new PieEntry(0.10f, "C"));
//        entries.add(new PieEntry(0.25f, "D"));
//        entries.add(new PieEntry(0.3f, "E"));
//
//        ArrayList<Integer> colors = new ArrayList<>();
//        for(int c: ColorTemplate.VORDIPLOM_COLORS){
//            colors.add(c);
//        }
//        for(int c: ColorTemplate.JOYFUL_COLORS){
//            colors.add(c);
//        }
//        PieDataSet dataSet = new PieDataSet(entries, "555555");
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(dataSet);
//        data.setDrawValues(true);
//        data.setValueFormatter(new PercentFormatter(pieChart));
//        data.setValueTextSize(12f);
//        data.setValueTextColor(Color.BLACK);
//
//        pieChart.setData(data);
//        pieChart.invalidate();
//    }


