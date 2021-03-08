package com.example.Musleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE1 = 10005;
    public static final int GOOGLE_SIGN_IN_CODE = REQUEST_CODE1;
    // Set the dimensions of the sign-in button.
    SignInButton signIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    DocumentReference uidref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn = findViewById(R.id.sign_in_button);

        firebaseAuth = firebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null || firebaseAuth.getCurrentUser() != null) {

            startActivity(new Intent(this,userdata.class));
            Toast.makeText(getApplicationContext(), "歡迎進入", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "未登入", Toast.LENGTH_SHORT).show();
        }

        signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent sign = signInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
//              GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
//              db = FirebaseFirestore.getInstance();
//              firebaseAuth = firebaseAuth.getInstance();
//              uidref = db.collection("User").document(firebaseAuth.getUid());
//              Log.i("INFO", firebaseAuth.getUid());
//              Log.i("GG", String.valueOf(uidref));
//              uidref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    db = FirebaseFirestore.getInstance();
                    Map<String,Object> space = new HashMap<>();
                    space.put("ID",firebaseAuth.getUid());
                    db.collection("User")
                            .document(firebaseAuth.getUid())
                            .set(space, SetOptions.merge());
//                    uidref = db.collection("User").document(firebaseAuth.getUid());
                    Toast.makeText(getApplicationContext(), "Your Google Account is Connected to Our Application.", Toast.LENGTH_SHORT).show();
                    DocumentReference docRef = db.collection("User").document(firebaseAuth.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                    Log.d("TAG", "DocumentSnapshot data: " + document.getData().size());
                                    if (document.getData().size() == 1){
                                        startActivity(new Intent(getApplicationContext(), userdata.class));
                                    }else {
                                        startActivity(new Intent(getApplicationContext(), homescreen.class));
                                    }
                                    return;

                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });

//                    if (task.isSuccessful()){
//                        DocumentSnapshot document = task.getResult();
//                        if(uidref != null) {
//                            startActivity(new Intent(getApplicationContext(),homescreen.class));
//                        }else {
//                            Toast.makeText(getApplicationContext(), "Your Google Account is Connected to Our Application.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), sleep_record.class));
//                        }
//                    }else{
//                        Log.i("GG", "GG");
//                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        } catch (ApiException e){
            e.printStackTrace();
        }


        }
    }
}