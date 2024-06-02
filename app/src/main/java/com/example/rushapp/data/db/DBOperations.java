package com.example.rushapp.data.db;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rushapp.callback.ChatHistoryCallback;
import com.example.rushapp.callback.FilterServiceCardsCallback;
import com.example.rushapp.callback.MessageIsGetCallback;
import com.example.rushapp.callback.MessageIsSendCallback;
import com.example.rushapp.callback.OfferCallback;
import com.example.rushapp.data.model.MsgModel;
import com.example.rushapp.data.model.Offer;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.callback.CustomerInfoCallback;
import com.example.rushapp.callback.LoginCallback;
import com.example.rushapp.callback.OnUserIsCustomerInformationCallback;
import com.example.rushapp.callback.ProviderInfoCallback;
import com.example.rushapp.callback.RegistrationCallback;
import com.example.rushapp.callback.ServiceCardsCallback;
import com.example.rushapp.data.model.Customer;
import com.example.rushapp.data.model.Provider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBOperations {
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore db;

    private static Customer customer;
    private static Provider provider;

    private static ArrayList<ServiceCard> filterList;
    private static boolean isCustomer;

    public static void registerCustomer(Customer customer, Context context, RegistrationCallback callback) {


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
                            data.put("Mail", customer.getMail());
                            data.put("Password",customer.getPassword());
                            data.put("NickName", customer.getName());
                            data.put("Job", customer.getJob());
                            data.put("isCustomer", true);
                            data.put("profilePhoto",customer.getProfilePhoto());



                            db.collection("userInformation").document(mAuth.getUid()).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Basarisiz Bilgi Aktarımı", Toast.LENGTH_SHORT).show();

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

    public static void registerProvider(Provider provider, Context context, RegistrationCallback callback) {
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
                            data.put("Mail", provider.getMail());
                            data.put("Password",provider.getPassword());
                            data.put("NickName", provider.getName());
                            data.put("Job", provider.getJob());
                            data.put("isCustomer", false);
                            data.put("profilePhoto",provider.getProfilePhoto());



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


    public static void loginSystem(String mail, String pass, Context context, LoginCallback callback) {

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


    public static void getCustomerInformation(CustomerInfoCallback callback) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("userInformation").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("UserInfo", "Success get from firebase");
                        if(document.getData().get("profilePhoto")== null){
                            customer = new Customer(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(),(boolean) document.getData().get("isCustomer"),document.getId());
                        }else{
                            String profilePhotoUrl = document.getData().get("profilePhoto").toString();
                            Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                            customer=new Customer(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(),profilePhotoUri,(boolean) document.getData().get("isCustomer"),document.getId());
                        }
                        callback.onCustomerReceived(customer);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static void getProviderInformation(ProviderInfoCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("userInformation").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("UserInfo", "Success get from firebase");
                        if(document.getData().get("profilePhoto") == null){
                            provider = new Provider(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(), (boolean) document.getData().get("isCustomer"),document.getId());
                        }else{
                            String profilePhotoUrl = document.getData().get("profilePhoto").toString();
                            Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                            provider = new Provider(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(), profilePhotoUri,(boolean) document.getData().get("isCustomer"),document.getId());
                        }
                        callback.onProviderReceived(provider);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    public static void getUserInformationWithUid(String uid, ProviderInfoCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("userInformation").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("UserInfo", "Success get from firebase");
                        if(document.getData().get("profilePhoto") == null){
                            provider = new Provider(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(), (boolean) document.getData().get("isCustomer"),document.getId());
                        }else{
                            String profilePhotoUrl = document.getData().get("profilePhoto").toString();
                            Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                            provider = new Provider(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(), profilePhotoUri,(boolean) document.getData().get("isCustomer"),document.getId());
                        }
                        callback.onProviderReceived(provider);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public static void getUserInformationWithUid(String uid, CustomerInfoCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("userInformation").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("UserInfo", "Success get from firebase");
                        if(document.getData().get("profilePhoto")== null){
                            customer = new Customer(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(),(boolean) document.getData().get("isCustomer"),document.getId());
                        }else{
                            String profilePhotoUrl = document.getData().get("profilePhoto").toString();
                            Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                            customer=new Customer(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), document.getData().get("Password").toString(), document.getData().get("Job").toString(),profilePhotoUri,(boolean) document.getData().get("isCustomer"),document.getId());
                        }
                        callback.onCustomerReceived(customer);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }



    public static void makeServiceCard(ServiceCard card) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("Mail", card.getProviderMail());
        data.put("NickName", card.getProviderName());
        data.put("serviceName", card.getServiceName());
        data.put("serviceExplain", card.getServiceExplain());
        data.put("servicePrice", card.getServicePrice());
        data.put("serviceField", card.getServiceField());
        data.put("providerJob", card.getProviderJob());
        data.put("providerPhoto", card.getProviderPhoto());
        data.put("userId", card.getCardUid());


        db.collection("serviceCards").document().set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Hizmet Yayınla", "Basarili");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Hizmet Yayınla", "Hata");
                    }
                });


    }

    public static void getServicesCardsInformation(ServiceCardsCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        db.collection("serviceCards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                        .addOnCompleteListener(t -> {
                                            if(t.isSuccessful()){
                                                Uri uri=t.getResult();
                                                ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                        document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                        document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                callback.onCardsReceived(card);
                                            }
                                        });


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public static void getServicesCardsInformation(Context context, String minPrice, String maxPrice, String serviceField, FilterServiceCardsCallback callback) {
        filterList=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(minPrice.isEmpty() && maxPrice.isEmpty()){
            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }

                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }else if(serviceField.isEmpty() && minPrice.isEmpty()){


            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice)){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }

                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        }else if(serviceField.isEmpty() && maxPrice.isEmpty()){

            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice)){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }else if(serviceField.isEmpty()){
            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice) && Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice)){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        } else if (minPrice.isEmpty()) {
            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        if(Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice)){
                                                            filterList.add(card);
                                                            callback.onFilterCardsReceived(filterList);
                                                        }
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }else if(maxPrice.isEmpty()){
            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        if(Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice)){
                                                            filterList.add(card);
                                                            callback.onFilterCardsReceived(filterList);
                                                        }
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }else{
            db.collection("serviceCards")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        if(Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice) && Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice)){
                                                            filterList.add(card);
                                                            callback.onFilterCardsReceived(filterList);
                                                        }
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }


    }
    public static void getProvidedServicesCardsInformation(ServiceCardsCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        db.collection("serviceCards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                        .addOnCompleteListener(t -> {
                                            if(t.isSuccessful()){
                                                Uri uri=t.getResult();
                                                if(document.getData().get("userId").toString().equals(mAuth.getUid())){
                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    callback.onCardsReceived(card);
                                                }

                                            }
                                        });


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public static void getProvidedServicesCardsInformation(String providerUid,ServiceCardsCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        db.collection("serviceCards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                        .addOnCompleteListener(t -> {
                                            if(t.isSuccessful()){
                                                Uri uri=t.getResult();
                                                if(document.getData().get("userId").toString().equals(providerUid)){
                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    callback.onCardsReceived(card);
                                                }

                                            }
                                        });


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public static void getSavedServicesCardsInformation(ServiceCardsCallback callback) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                        .addOnCompleteListener(t -> {
                                            if(t.isSuccessful()){
                                                Uri uri=t.getResult();
                                                ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                        document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                        document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                callback.onCardsReceived(card);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public static void getSavedServicesCardsInformation(Context context,String minPrice,String maxPrice,String serviceField,FilterServiceCardsCallback callback) {
        filterList=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        if(minPrice.isEmpty() && maxPrice.isEmpty()){

            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){

                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }

                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }else if(serviceField.isEmpty() && minPrice.isEmpty()){


            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice)){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }

                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        }else if(serviceField.isEmpty() && maxPrice.isEmpty()){

            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice)){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }else if(serviceField.isEmpty()){
            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice) && Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice)){
                                                        filterList.add(card);
                                                        callback.onFilterCardsReceived(filterList);
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        } else if (minPrice.isEmpty()) {
            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        if(Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice)){
                                                            filterList.add(card);
                                                            callback.onFilterCardsReceived(filterList);
                                                        }
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }else if(maxPrice.isEmpty()){
            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        if(Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice)){
                                                            filterList.add(card);
                                                            callback.onFilterCardsReceived(filterList);
                                                        }
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }else{
            db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    FirebaseStorage.getInstance().getReference().child("profile_pic").child(document.getData().get("userId").toString()).getDownloadUrl()
                                            .addOnCompleteListener(t -> {
                                                if(t.isSuccessful()){
                                                    Uri uri=t.getResult();


                                                    ServiceCard card = new ServiceCard(document.getData().get("serviceName").toString(), document.getData().get("NickName").toString(), document.getData().get("Mail").toString(),
                                                            document.getData().get("serviceExplain").toString(),document.getData().get("serviceField").toString(),
                                                            document.getData().get("servicePrice").toString(),document.getData().get("providerJob").toString(),uri,document.getId(),document.getData().get("userId").toString());
                                                    Log.d("Card Info", "card.getProviderMail()"+",card.getServiceExplain()");
                                                    if(card.getServiceField().toLowerCase().equals(serviceField.toLowerCase())){
                                                        if(Integer.parseInt(card.getServicePrice())>=Integer.parseInt(minPrice) && Integer.parseInt(card.getServicePrice())<=Integer.parseInt(maxPrice)){
                                                            filterList.add(card);
                                                            callback.onFilterCardsReceived(filterList);
                                                        }
                                                    }
                                                }
                                            });


                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }


    }

    public static StorageReference getCurrentProfilePic(){
        mAuth = FirebaseAuth.getInstance();
        return FirebaseStorage.getInstance().getReference().child("profile_pic").child(mAuth.getUid());
    }
    public static void getUserCustomerInformation(OnUserIsCustomerInformationCallback callback){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("userInformation").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean control=(boolean)document.getData().get("isCustomer");
                    Log.d("Control",control+"");
                    if(control){
                        isCustomer=true;
                        callback.onComplete(isCustomer);
                    }else{
                        isCustomer=false;
                        callback.onComplete(isCustomer);
                    }


                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



    }

    public static void setUserInformation(Object m,String infoHead) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put(infoHead, m);

        FirebaseFirestore.getInstance()
                .collection("userInformation") // Belgenin bulunduğu koleksiyon adı
                .document(mAuth.getUid()) // Güncellemek istediğiniz belgenin ID'si
                .update(data) // Belgeyi güncelleyin veya oluşturun
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Belge başarıyla güncellendi.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Belge güncelleme hatası: ", e);
                    }
                });


    }

    public static void saveService(String userId, String serviceId, Context context,ImageView imageView) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("serviceCards").document(serviceId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && document.getString("userId").equals(userId)) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("Mail", document.getString("Mail"));
                                data.put("NickName", document.getString("NickName"));
                                data.put("serviceName", document.getString("serviceName"));
                                data.put("serviceExplain", document.getString("serviceExplain"));
                                data.put("servicePrice", document.getString("servicePrice"));
                                data.put("serviceField", document.getString("serviceField"));
                                data.put("providerJob", document.getString("providerJob"));
                                data.put("providerPhoto", document.getString("providerPhoto"));
                                data.put("userId", document.getString("userId"));

                                db.collection("savesCards")
                                        .document(mAuth.getUid())
                                        .collection("currentUserSavesCard")
                                        .document(serviceId)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Kaydetme", "Başarılı");
                                                Toast.makeText(context,"Bu hizmeti başarıyla kaydettiniz",Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Kaydetme", "Hata");
                                                Toast.makeText(context,"Bu hizmeti kaydederken bir hata oluştu",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Log.d("Card UID", "Hata: Eşleşme yok, userId=" + userId + ", document userId=" + document.getString("userId"));
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    public static void editProfile(String nickName,String job){


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> data2 = new HashMap<>();



        data.put("NickName",nickName);
        data.put("Job",job);


        data2.put("NickName",nickName);
        data2.put("providerJob",job);

        FirebaseFirestore.getInstance()
                .collection("userInformation") // Belgenin bulunduğu koleksiyon adı
                .document(mAuth.getUid()) // Güncellemek istediğiniz belgenin ID'si
                .update(data) // Belgeyi güncelleyin veya oluşturun
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Belge başarıyla güncellendi.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Belge güncelleme hatası: ", e);
                    }
                });


        db.collection("serviceCards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               if(document.getData().get("userId").toString().equals(mAuth.getUid())){

                                db.collection("serviceCards").document(document.getId()).update(data2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                               }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });








    }

    public static void editMail(String oldMail,String newmail,String pass){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();




        AuthCredential credential = EmailAuthProvider.getCredential(oldMail, pass);


        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Reauthenticate", "User re-authenticated.");
                        user.updateEmail(newmail)
                                .addOnCompleteListener(t -> {
                                    if (t.isSuccessful()) {
                                        Log.d("UpdateEmail", "User email address updated.");
                                    } else {
                                        Log.d("UpdateEmail", "Failed to update email address: " + t.getException().getMessage());
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("UpdateEmail", "Error updating email address:", e);
                                });
                    } else {
                        Log.d("Reauthenticate", "Re-authentication failed: " + task.getException().getMessage());
                    }
                });


    }

    public static void logOut(){
        mAuth=FirebaseAuth.getInstance();

        mAuth.signOut();

    }

    public static void deleteSavedCard(String documentId,Context context,Runnable onSucces){

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        db.collection("savesCards").document(mAuth.getUid()).collection("currentUserSavesCard").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Servis basariyla silindi",Toast.LENGTH_SHORT).show();
                        onSucces.run();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }
    public static void deleteProvidedCard(String documentId,String cardUid,Context context,Runnable onSuccess){

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        db.collection("serviceCards").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Servis basariyla silindi",Toast.LENGTH_SHORT).show();
                        onSuccess.run();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });



            db.collection("savesCards").document(cardUid).collection("currentUserSavesCard").document(documentId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });



    }
    public static void sendMessage(MsgModel msgModel, MessageIsSendCallback callback) {
        mAuth = FirebaseAuth.getInstance();

        db=FirebaseFirestore.getInstance();
        HashMap<String  ,Object> data=new HashMap<>();
        data.put("message",msgModel.getMessage());
        data.put("senderUid",mAuth.getUid());
        data.put("receiverUid",msgModel.getReceiverUid());
        data.put("timestamp",msgModel.getTimestamp());
        data.put("reciverPp",msgModel.getReceiverPic());
        data.put("receiverName",msgModel.getReceiverName());


      db.collection("chats").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {

              callback.onSendSuccess();

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

          }
      });

    }


   /* public static void getMessage(String opposingUserUid,MessageIsGetCallback callback) {
        mAuth = FirebaseAuth.getInstance();

        db=FirebaseFirestore.getInstance();

        db.collection("chats").document(mAuth.getUid()).collection("conversations").document(opposingUserUid).collection("sendedMessage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> sendedMessagesTask) {
                        if (sendedMessagesTask.isSuccessful()) {
                            List<DocumentSnapshot> sendedMessages = sendedMessagesTask.getResult().getDocuments();

                            db.collection("chats").document(mAuth.getUid()).collection("conversations").document(opposingUserUid).collection("receivedMessage")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> receivedMessagesTask) {
                                            if (receivedMessagesTask.isSuccessful()) {
                                                List<DocumentSnapshot> receivedMessages = receivedMessagesTask.getResult().getDocuments();


                                                List<DocumentSnapshot> allMessages = new ArrayList<>();
                                                allMessages.addAll(sendedMessages);
                                                allMessages.addAll(receivedMessages);


                                                for (DocumentSnapshot document : allMessages) {
                                                    String timestampString = document.getData().get("timestamp").toString();
                                                    long timestamp = Long.parseLong(timestampString);
                                                    MsgModel msg=new MsgModel(document.getData().get("opposingUserUid").toString(),document.getData().get("message").toString(),timestamp);
                                                    callback.onGetMessageSuccess(msg);
                                                }
                                            } else {

                                            }
                                        }
                                    });
                        } else {

                        }
                    }
                });




    }
    */

    public static void getMessage(String opposingUserUid,MessageIsGetCallback callback){
        db.collection("chats")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getData().get("reciverPp").toString().isEmpty()){
                                    MsgModel msg = new MsgModel(document.getData().get("receiverUid").toString(), document.getData().get("message").toString(), document.getData().get("receiverName").toString(), (long) document.getData().get("timestamp"));
                                    callback.onGetMessageSuccess(msg);
                                }else{
                                    MsgModel msg = new MsgModel(document.getData().get("receiverUid").toString(), document.getData().get("message").toString(), document.getData().get("receiverName").toString(), document.getData().get("reciverPp").toString(), (long) document.getData().get("timestamp"));
                                    callback.onGetMessageSuccess(msg);
                                }




                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    }
    public static void getChatHistory(ChatHistoryCallback callback) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        db.collection("chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(mAuth.getUid().equals(document.getData().get("receiverUid")) || mAuth.getUid().equals(document.getData().get("senderUid"))){
                                    if(document.getData().get("reciverPp").toString().isEmpty()){
                                        MsgModel msg = new MsgModel(document.getData().get("receiverUid").toString(), document.getData().get("senderUid").toString(),document.getData().get("message").toString(), document.getData().get("receiverName").toString(), (long) document.getData().get("timestamp"));
                                        callback.onGetChatHistorySuccess(msg);
                                    }else{
                                        MsgModel msg = new MsgModel(document.getData().get("receiverUid").toString(),document.getData().get("senderUid").toString(), document.getData().get("message").toString(), document.getData().get("receiverName").toString(), document.getData().get("reciverPp").toString(), (long) document.getData().get("timestamp"));
                                        callback.onGetChatHistorySuccess(msg);
                                    }
                                }


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public static void makeOffer(Offer offer,Context context){

        HashMap<String,Object> offerData=new HashMap<>();
        offerData.put("customerName",offer.getCustomerName());
        offerData.put("customerUid",offer.getCustomerUid());
        offerData.put("customerJob",offer.getCustomerJob());
        offerData.put("customerMail",offer.getCustomerMail());
        offerData.put("providerName",offer.getProviderName());
        offerData.put("providerJob",offer.getProviderJob());
        offerData.put("providerMail",offer.getProviderMail());
        offerData.put("serviceName",offer.getServiceName());
        offerData.put("serviceField",offer.getServiceField());
        offerData.put("providerUid",offer.getProviderUid());
        offerData.put("providerPhoto",offer.getProviderPhoto());
        offerData.put("customerPhoto",offer.getCustomerPhoto());


        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        db.collection("offers").add(offerData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Toast.makeText(context,"Basariyla teklif gönderildi",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context,"Teklif gönderilirken bir hata oluştu",Toast.LENGTH_SHORT).show();

            }
        });



    }
    public static void getMyOffers(OfferCallback callback){



        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

       db.collection("offers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       if(mAuth.getUid().equals(document.getData().get("customerUid").toString())){
                           Uri providerPhoto=Uri.parse(document.getData().get("providerPhoto").toString());
                           Uri customerPhoto=Uri.parse(document.getData().get("customerPhoto").toString());
                           Offer offer=new Offer(document.getData().get("customerName").toString(),document.getData().get("customerUid").toString(),
                                   document.getData().get("customerJob").toString(),document.getData().get("customerMail").toString(),document.getData().get("providerName").toString(),
                                   document.getData().get("providerJob").toString(),document.getData().get("providerMail").toString(),
                                   document.getData().get("serviceName").toString(),document.getData().get("serviceField").toString(),
                                   document.getData().get("providerUid").toString(),providerPhoto,customerPhoto);
                           callback.onOfferReceived(offer);
                       }

                   }

               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });



    }
    public static void getIncomingOffers(OfferCallback callback){



        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        db.collection("offers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(mAuth.getUid().equals(document.getData().get("providerUid").toString())){
                            Uri providerPhoto=Uri.parse(document.getData().get("providerPhoto").toString());
                            Uri customerPhoto=Uri.parse(document.getData().get("customerPhoto").toString());
                            Offer offer=new Offer(document.getData().get("customerName").toString(),document.getData().get("customerUid").toString(),
                                    document.getData().get("customerJob").toString(),document.getData().get("customerMail").toString(),document.getData().get("providerName").toString(),
                                    document.getData().get("providerJob").toString(),document.getData().get("providerMail").toString(),
                                    document.getData().get("serviceName").toString(),document.getData().get("serviceField").toString(),
                                    document.getData().get("providerUid").toString(),providerPhoto,customerPhoto);
                            callback.onOfferReceived(offer);
                        }
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }











}
