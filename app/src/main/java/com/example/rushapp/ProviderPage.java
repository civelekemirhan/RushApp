package com.example.rushapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.rushapp.databinding.FragmentProviderPageBinding;


public class ProviderPage extends Fragment {

    FragmentProviderPageBinding binding;
    ProgressDialog progressDialog;

    public ProviderPage() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.appColor));
        getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.appColor));

        replaceFragment(new UniversalHomeScreen());

    }

    @Override
    public void onResume() {
        super.onResume();
        replaceFragment(new UniversalHomeScreen());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentProviderPageBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBOperations dbo=new DBOperations();
        dbo.getProviderInformation(new ProviderInfoCallback(){

            @Override
            public void onProviderReceived(Provider provider) {
                binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                    if (item.getItemId() == R.id.home) {
                        replaceFragment(new UniversalHomeScreen());
                    }else if(item.getItemId() == R.id.savesService){
                        replaceFragment(new UniversalSavesScreen());
                    }else if(item.getItemId() == R.id.pastchat){
                        replaceFragment(new UniversalChatScreen());
                    }else if(item.getItemId() == R.id.profile){
                        replaceFragment(new ProviderProfileScreen(provider.getMail().toString(),provider.getName().toString(),provider.getJob(),item));
                    }else{

                        replaceFragment(new ProviderServeScreen(item));
                    }

                    return true;


                });
            }
        });



    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.providerFrameLayout,fragment);
        fragmentTransaction.commit();
    }
}