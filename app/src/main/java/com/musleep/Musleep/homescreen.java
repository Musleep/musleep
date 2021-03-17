package com.musleep.Musleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class homescreen extends AppCompatActivity {

    ImageView imageView;
    TextView name;
    TextView uid;
    FirebaseFirestore db;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button sleep1, sleep2, sleep3;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();

        Log.i("INFO",firebaseAuth.getUid()+"123123");
//        TextView uid = (TextView) findViewById(R.id.uid);

        //顯示
//        uid.setText(firebaseAuth.getUid()+"123123");


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        Log.d("tag",mAuth.getCurrentUser().getUid());
//        String current_user_id = mAuth.getCurrentUser().getUid();

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d("tag","onCreate:");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.username);
//        uid = findViewById(R.id.uid);
//        if (firebaseUser != null) {
//            String personUid = firebaseAuth.getUid();
//            uid.setText(personUid);
//        }
        // uid = findViewById(R.id.uid);


        findViewById(R.id.sleep1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), sleep_record.class));
            }
        });
        findViewById(R.id.sleep2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), alarm.class));
            }
        });
        findViewById(R.id.sleep3).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), sleep_diary.class));

            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        DocumentReference docRef = db.collection("User").document(firebaseAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("TAG", "DocumentSnapshot data: " + document.getData().get("Name").toString());
                        String personName = document.getData().get("Name").toString();
                        name.setText(personName);
                        return;

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
//        db.collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//
//                if (e !=null)
//                {
//
//                }
//
//                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
//                {
//                    String   personName =  documentChange.getDocument().getData().toString();
//                    name.setText(personName);
////                    String  isCalender   =  documentChange.getDocument().getData().get("Calender").toString();
////                    String isEnablelocation = documentChange.getDocument().getData().get("Enable Location").toString();
//
//                }
//            }
//        });
//            //Query
//            Query query = db.collection("User").document(mAuth.getUid()).collection("Name");
//            //RecyclerOptions
//            FirestoreRecyclerOptions<homescreen> options = new FirestoreRecyclerOptions.Builder<homescreen>()
//                    .setQuery(query, homescreen.class)
//                    .build();


//        if(acct != null) {
//            String personName = acct.getDisplayName();
//            Uri personPhoto = acct.getPhotoUrl();
//
//            name.setText(personName);
//            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
//        }
    }

    public void logout(final View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(view.getContext(),Loading.class));
        GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(view.getContext(),Loading.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(homescreen.this,"Signout Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public void home(final View view) {
//
//        startActivity(new Intent(view.getContext(), homescreen.class));
//    }

}