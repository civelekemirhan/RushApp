package com.example.rushapp.screen;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.rushapp.R;
import com.example.rushapp.adapter.MyAdapter;
import com.example.rushapp.callback.FilterServiceCardsCallback;
import com.example.rushapp.callback.ServiceCardsCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentUniversalHomeScreenBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class UniversalHomeScreen extends Fragment {


    FragmentUniversalHomeScreenBinding binding;
    private ArrayList<ServiceCard> cardList;


    public UniversalHomeScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentUniversalHomeScreenBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardList=new ArrayList<>();


        DBOperations.getServicesCardsInformation(new ServiceCardsCallback() {
            @Override
            public void onCardsReceived(ServiceCard card) {


                cardList.add(card);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MyAdapter myAdapter=new MyAdapter(cardList,getContext());
                binding.recyclerView.setAdapter(myAdapter);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 1000);

            }

        });
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

                        // DBOperations'a parse edilmiş ya da orijinal string değerleri ile çağrı yap
                        DBOperations.getServicesCardsInformation(getContext(),
                                minPrice.getText().toString(),
                                maxPrice.getText().toString(),
                                serviceField.getText().toString(),
                                new FilterServiceCardsCallback() {
                                    @Override
                                    public void onFilterCardsReceived(ArrayList<ServiceCard> filterCardList) {
                                        if (!filterCardList.isEmpty()) {
                                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            MyAdapter myAdapter = new MyAdapter(filterCardList, getContext());
                                            binding.recyclerView.setAdapter(myAdapter);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    myAdapter.notifyDataSetChanged();
                                                }
                                            }, 1000);
                                        }
                                    }
                                });

                    } catch (NumberFormatException e) {
                        // Parse işlemi başarısız olduysa mesaj göster
                        Toast.makeText(getContext(), "Lütfen düzgün bir fiyat bilgisi giriniz", Toast.LENGTH_SHORT).show();
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
                    TextInputEditText serviceField=customLayout.findViewById(R.id.serviceField);
                    TextInputEditText minPrice=customLayout.findViewById(R.id.minPrice);
                    TextInputEditText maxPrice=customLayout.findViewById(R.id.maxPrice);



                    try {
                        Integer min = null;
                        Integer max = null;


                        if (!maxPrice.getText().toString().isEmpty()) {
                            max = Integer.parseInt(maxPrice.getText().toString());
                        }
                        if (!minPrice.getText().toString().isEmpty()) {
                            min = Integer.parseInt(minPrice.getText().toString());
                        }


                        DBOperations.getServicesCardsInformation(getContext(),
                                minPrice.getText().toString(),
                                maxPrice.getText().toString(),
                                serviceField.getText().toString(),
                                new FilterServiceCardsCallback() {
                                    @Override
                                    public void onFilterCardsReceived(ArrayList<ServiceCard> filterCardList) {
                                        if (!filterCardList.isEmpty()) {
                                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            MyAdapter myAdapter = new MyAdapter(filterCardList, getContext());
                                            binding.recyclerView.setAdapter(myAdapter);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                                    myAdapter.notifyDataSetChanged();
                                                }
                                            }, 1000);
                                        }
                                    }
                                });

                    } catch (NumberFormatException e) {

                        Toast.makeText(getContext(), "Lütfen düzgün bir fiyat bilgisi giriniz", Toast.LENGTH_SHORT).show();
                    }





                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }
}