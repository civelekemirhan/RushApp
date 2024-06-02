package com.example.rushapp.screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rushapp.R;
import com.example.rushapp.adapter.IncomingOffersFragmentAdapter;
import com.example.rushapp.adapter.MyOffersFragmentAdapter;
import com.example.rushapp.callback.OfferCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Offer;
import com.example.rushapp.databinding.FragmentIncomingOffersBinding;

import java.util.ArrayList;
import java.util.List;


public class IncomingOffersFragment extends Fragment {

    private FragmentIncomingOffersBinding binding;

    private List<Offer> incomingOffers;

    public IncomingOffersFragment() {
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
        binding = FragmentIncomingOffersBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        incomingOffers = new ArrayList<>();
        DBOperations.getIncomingOffers(new OfferCallback() {
            @Override
            public void onOfferReceived(Offer offer) {
                incomingOffers.add(offer);
                binding.incomingOffersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                IncomingOffersFragmentAdapter myAdapter=new IncomingOffersFragmentAdapter(incomingOffers,getContext());
                binding.incomingOffersRecyclerView.setAdapter(myAdapter);
            }
        });

    }
}