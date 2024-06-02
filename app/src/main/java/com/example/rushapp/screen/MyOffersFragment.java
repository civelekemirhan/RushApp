package com.example.rushapp.screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rushapp.R;
import com.example.rushapp.adapter.MyOffersFragmentAdapter;
import com.example.rushapp.adapter.ProvidedServicesAdapter;
import com.example.rushapp.callback.OfferCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Offer;
import com.example.rushapp.databinding.FragmentMyOffersBinding;

import java.util.ArrayList;
import java.util.List;


public class MyOffersFragment extends Fragment {


    private FragmentMyOffersBinding binding;
    private List<Offer> myOffers;

    public MyOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMyOffersBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myOffers=new ArrayList<>();
        Log.d("myOffer1","burda");
        DBOperations.getMyOffers(new OfferCallback() {
            @Override
            public void onOfferReceived(Offer offer) {
                Log.d("myOffer2","burda");
                myOffers.add(offer);
                binding.myOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MyOffersFragmentAdapter myAdapter=new MyOffersFragmentAdapter(myOffers,getContext());
                binding.myOffersRecyclerView.setAdapter(myAdapter);
            }
        });
    }
}