package com.example.rushapp.screen;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.example.rushapp.R;
import com.example.rushapp.adapter.MyAdapter;
import com.example.rushapp.adapter.SavesScreenAdapter;
import com.example.rushapp.callback.CustomerInfoCallback;
import com.example.rushapp.callback.FilterServiceCardsCallback;
import com.example.rushapp.callback.ProviderInfoCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Customer;
import com.example.rushapp.data.model.Provider;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentUniversalSavesScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
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

    private boolean isFilter=false;
    private BottomNavigationView bottomNavigationView;

    public UniversalSavesScreen(BottomNavigationView bottomNavigationView) {
        // Required empty public constructor
        this.bottomNavigationView=bottomNavigationView;
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

            DBOperations.getCustomerInformation(new CustomerInfoCallback() {
                @Override
                public void onCustomerReceived(Customer customer) {

                    customer.savesService(savedCardList,isFilter,"","","", binding, getContext(),bottomNavigationView);

                }
            });

        } else {
            DBOperations.getProviderInformation(new ProviderInfoCallback() {
                @Override
                public void onProviderReceived(Provider provider) {


                    provider.savesService(savedCardList,isFilter,"","","", binding, getContext(),bottomNavigationView);

                }
            });

        }

eventHandler();
    }
    public void eventHandler(){

        binding.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Filtrele");


                final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_filter, null);
                builder.setView(customLayout);


                builder.setPositiveButton("OK", (dialog, which) -> {
                    isFilter=true;
                    TextInputEditText serviceField=customLayout.findViewById(R.id.serviceField);
                    TextInputEditText minPrice=customLayout.findViewById(R.id.minPrice);
                    TextInputEditText maxPrice=customLayout.findViewById(R.id.maxPrice);

                    try {
                        Integer min = null;
                        Integer max = null;

                        // Eğer maxPrice ve minPrice boş değilse parse etmeye çalış
                        if (!maxPrice.getText().toString().isEmpty()) {
                            max = Integer.parseInt(maxPrice.getText().toString());
                        }
                        if (!minPrice.getText().toString().isEmpty()) {
                            min = Integer.parseInt(minPrice.getText().toString());
                        }



                    } catch (NumberFormatException e) {
                        // Parse işlemi başarısız olduysa mesaj göster
                        Toast.makeText(getContext(), "Lütfen düzgün bir fiyat bilgisi giriniz", Toast.LENGTH_SHORT).show();
                    }

                    if (isCustomer) {

                        DBOperations.getCustomerInformation(new CustomerInfoCallback() {
                            @Override
                            public void onCustomerReceived(Customer customer) {
                                Log.d("isFilterBilgisi =",isFilter+"");
                                customer.savesService(savedCardList,isFilter,minPrice.getText().toString(),maxPrice.getText().toString(),serviceField.getText().toString(), binding, getContext(),bottomNavigationView);

                            }
                        });

                    } else {
                        DBOperations.getProviderInformation(new ProviderInfoCallback() {
                            @Override
                            public void onProviderReceived(Provider provider) {
                                Log.d("isFilterBilgisi =",isFilter+"");
                                provider.savesService(savedCardList,isFilter,minPrice.getText().toString(),maxPrice.getText().toString(),serviceField.getText().toString(), binding, getContext(),bottomNavigationView);

                            }
                        });

                    }







                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.filterButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Filtrele");


                final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_filter, null);
                builder.setView(customLayout);


                builder.setPositiveButton("OK", (dialog, which) -> {
                    isFilter=true;

                    Log.d("isFilterrr",""+isFilter);
                    TextInputEditText serviceField=customLayout.findViewById(R.id.serviceField);
                    TextInputEditText minPrice=customLayout.findViewById(R.id.minPrice);
                    TextInputEditText maxPrice=customLayout.findViewById(R.id.maxPrice);

                    try {
                        Integer min = null;
                        Integer max = null;

                        // Eğer maxPrice ve minPrice boş değilse parse etmeye çalış
                        if (!maxPrice.getText().toString().isEmpty()) {
                            max = Integer.parseInt(maxPrice.getText().toString());
                        }
                        if (!minPrice.getText().toString().isEmpty()) {
                            min = Integer.parseInt(minPrice.getText().toString());
                        }



                    } catch (NumberFormatException e) {
                        // Parse işlemi başarısız olduysa mesaj göster
                        Toast.makeText(getContext(), "Lütfen düzgün bir fiyat bilgisi giriniz", Toast.LENGTH_SHORT).show();
                    }

                    if (isCustomer) {

                        DBOperations.getCustomerInformation(new CustomerInfoCallback() {
                            @Override
                            public void onCustomerReceived(Customer customer) {

                                customer.savesService(savedCardList,isFilter,minPrice.getText().toString(),maxPrice.getText().toString(),serviceField.getText().toString(), binding, getContext(),bottomNavigationView);

                            }
                        });

                    } else {
                        DBOperations.getProviderInformation(new ProviderInfoCallback() {
                            @Override
                            public void onProviderReceived(Provider provider) {
                                Log.d("isFilterBilgisi =",isFilter+"");
                                provider.savesService(savedCardList,isFilter,minPrice.getText().toString(),maxPrice.getText().toString(),serviceField.getText().toString(), binding, getContext(),bottomNavigationView);

                            }
                        });

                    }







                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }
}