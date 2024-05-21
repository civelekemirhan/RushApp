package com.example.rushapp;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rushapp.databinding.FragmentUniversalSavesScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class UniversalSavesScreen extends Fragment {


    FragmentUniversalSavesScreenBinding binding;
    private ArrayList<ServiceCard> savedCardList;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private boolean isCustomer;
    MenuItem item;

    public UniversalSavesScreen() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUniversalSavesScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedCardList = new ArrayList<>();
        DBOperations dbo = new DBOperations();

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
                        if ((boolean) document.getData().get("isCustomer") == true) {

                            isCustomer = true;
                        } else {
                            isCustomer = false;
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        if (isCustomer) {

            dbo.getCustomerInformation(new CustomerInfoCallback() {
                @Override
                public void onCustomerReceived(Customer customer) {
                    customer.savesService(savedCardList, binding, getContext());
                }
            });

        } else {
            dbo.getProviderInformation(new ProviderInfoCallback() {
                @Override
                public void onProviderReceived(Provider provider) {

                    provider.savesService(savedCardList, binding, getContext());

                }
            });

        }


    }
}