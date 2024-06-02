package com.example.rushapp.screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rushapp.R;
import com.example.rushapp.callback.ServiceCardsCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Provider;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentReceviceServicesBinding;

import java.util.ArrayList;


public class ReceviceServicesFragment extends Fragment {


FragmentReceviceServicesBinding binding;
Provider provider;

private ArrayList<ServiceCard> providedServices;
    public ReceviceServicesFragment() {

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
        binding=FragmentReceviceServicesBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}