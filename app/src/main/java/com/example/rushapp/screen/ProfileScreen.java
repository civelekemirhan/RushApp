package com.example.rushapp.screen;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.rushapp.adapter.MyAdapter;
import com.example.rushapp.callback.ServiceCardsCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentProfileScreenBinding;

import java.util.ArrayList;


public class ProfileScreen extends Fragment {


   FragmentProfileScreenBinding binding;
    private ArrayList<ServiceCard> providedServices;

    public ProfileScreen() {
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
        binding=FragmentProfileScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.addphoto.setVisibility(View.INVISIBLE);
        String providerUid=ProfileScreenArgs.fromBundle(getArguments()).getProviderUid();
        if(getArguments()!=null){

            binding.usernickname.setText(ProfileScreenArgs.fromBundle(getArguments()).getProviderName());
            binding.useremail.setText(ProfileScreenArgs.fromBundle(getArguments()).getProviderMail());
            binding.userJob.setText(ProfileScreenArgs.fromBundle(getArguments()).getProviderJob());
            Glide.with(getContext())
                    .load(Uri.parse(ProfileScreenArgs.fromBundle(getArguments()).getProfilePhoto()))
                    .into(binding.profilephoto);
        }
        providedServices=new ArrayList<>();

        DBOperations.getProvidedServicesCardsInformation(ProfileScreenArgs.fromBundle(getArguments()).getProviderUid(),new ServiceCardsCallback() {
            @Override
            public void onCardsReceived(ServiceCard card) {


                providedServices.add(card);
                binding.universalProfileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MyAdapter myAdapter=new MyAdapter(providedServices,getContext());
                binding.universalProfileRecyclerView.setAdapter(myAdapter);

            }

        });


    }
}