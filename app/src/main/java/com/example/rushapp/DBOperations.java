package com.example.rushapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DBOperations {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private boolean isCustomer;



    public void registerCustomer(Customer customer, Context context,RegistrationCallback callback) {


        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(customer.getMail(), customer.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(context, "Kayıt Başarılı...", Toast.LENGTH_SHORT).show();

                            db = FirebaseFirestore.getInstance();
                            Map<String, Object> data = new HashMap<>();

                            data.put("NickName", customer.getName());
                            data.put("Job", customer.getJob());
                            data.put("isCustomer", true);


                            db.collection("userInformation").document(mAuth.getUid()).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Basarili firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Basarisiz firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            callback.onRegistrationSuccess();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(context, "Kayıt Başarısız!!", Toast.LENGTH_SHORT).show();
                            callback.onRegistrationFailure();

                        }
                    }
                });



    }

    public void registerProvider(Provider provider, Context context,RegistrationCallback callback) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(provider.getMail(), provider.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(context, "Kayıt Başarılı...", Toast.LENGTH_SHORT).show();


                            db = FirebaseFirestore.getInstance();
                            Map<String, Object> data = new HashMap<>();

                            data.put("NickName", provider.getName());
                            data.put("Job", provider.getJob());
                            data.put("isCustomer", false);


                            db.collection("userInformation").document(mAuth.getUid()).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Basarili firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Basarisiz firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            callback.onRegistrationSuccess();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(context, "Kayıt Başarısız!!", Toast.LENGTH_SHORT).show();
                            callback.onRegistrationFailure();

                        }
                    }
                });
    }


    public void loginSystem(String mail, String pass, Context context,LoginCallback callback) {

        //Bu sistem sayesinde girilen bilgilerin bir provider'a mı yoksa customer'a mı ait olduğu belirlenip ona göre yönlendirme yapılır

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(context, "Giriş Başarılı.",
                                    Toast.LENGTH_SHORT).show();

                            DocumentReference docRef = db.collection("userInformation").document(mAuth.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            callback.onLoginSuccess((boolean) document.getData().get("isCustomer"));
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });


                        } else {

                            Toast.makeText(context, "Giriş Başarısız.",
                                    Toast.LENGTH_SHORT).show();
                            callback.onLoginFailure();

                        }
                    }
                });

    }


}
