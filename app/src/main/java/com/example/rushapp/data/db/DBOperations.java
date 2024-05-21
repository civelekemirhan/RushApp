package com.example.rushapp.data.db;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DBOperations {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Customer customer;
    private Provider provider;

    private boolean isCustomer;

    public void registerCustomer(Customer customer, Context context, RegistrationCallback callback) {


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
                            data.put("PassWord",customer.getPassword());
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

    public void registerProvider(Provider provider, Context context, RegistrationCallback callback) {
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


    public void loginSystem(String mail, String pass, Context context, LoginCallback callback) {

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


    public void getCustomerInformation(CustomerInfoCallback callback) {

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
                            customer = new Customer(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), "", document.getData().get("Job").toString(),(boolean) document.getData().get("isCustomer"),document.getId());
                        }else{
                            String profilePhotoUrl = document.getData().get("profilePhoto").toString();
                            Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                            customer=new Customer(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), "", document.getData().get("Job").toString(),profilePhotoUri,(boolean) document.getData().get("isCustomer"),document.getId());
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

    public void getProviderInformation(ProviderInfoCallback callback) {

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
                            provider = new Provider(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), "", document.getData().get("Job").toString(), (boolean) document.getData().get("isCustomer"),document.getId());
                        }else{
                            String profilePhotoUrl = document.getData().get("profilePhoto").toString();
                            Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                            provider = new Provider(document.getData().get("Mail").toString(), document.getData().get("NickName").toString(), "", document.getData().get("Job").toString(), profilePhotoUri,(boolean) document.getData().get("isCustomer"),document.getId());
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



    public void makeServiceCard(ServiceCard card) {
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

    public void getServicesCardsInformation(ServiceCardsCallback callback) {

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

    public void getSavedServicesCardsInformation(ServiceCardsCallback callback) {

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

    public StorageReference getCurrentProfilePic(){
        mAuth = FirebaseAuth.getInstance();
        return FirebaseStorage.getInstance().getReference().child("profile_pic").child(mAuth.getUid());
    }
    public void getUserCustomerInformation(OnUserIsCustomerInformationCallback callback){
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

    public void setUserInformation(Object m,String infoHead) {
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

    public void saveService(String userId, String serviceId,Context context) {
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

    public void editProfile(String mail,String nickName,String job){


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        Map<String, Object> data2 = new HashMap<>();


        data.put("Mail", mail);
        data.put("NickName",nickName);
        data.put("Job",job);

        data2.put("Mail", mail);
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

    public void editMail(String newmail,String pass){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get auth credentials from the user for re-authentication
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass); // Current Login Credentials
            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User re-authenticated.");
                                // Now change your email address
                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    user.updateEmail(newmail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User email address updated.");
                                                    } else {
                                                        Log.e(TAG, "Error updating email", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.e(TAG, "User is null after re-authentication");
                                }
                                //----------------------------------------------------------\\
                            } else {
                                Log.e(TAG, "Re-authentication failed", task.getException());
                            }
                        }
                    });
        } else {
            Log.e(TAG, "Current user is null");
        }

    }

    public void logOut(){
        mAuth=FirebaseAuth.getInstance();

        mAuth.signOut();

    }


}
