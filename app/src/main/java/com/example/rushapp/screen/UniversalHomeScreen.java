package com.example.rushapp.screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.rushapp.adapter.MyAdapter;
import com.example.rushapp.callback.ServiceCardsCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentUniversalHomeScreenBinding;

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
        DBOperations dbo=new DBOperations();

        dbo.getServicesCardsInformation(new ServiceCardsCallback() {
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





    }
}