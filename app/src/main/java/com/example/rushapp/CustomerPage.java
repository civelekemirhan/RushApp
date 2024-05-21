package com.example.rushapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.rushapp.databinding.FragmentCustomerPageBinding;


public class CustomerPage extends Fragment{


    FragmentCustomerPageBinding binding;


    public CustomerPage() {
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

       binding=FragmentCustomerPageBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBOperations dbo=new DBOperations();
        dbo.getCustomerInformation(new CustomerInfoCallback(){

            @Override
            public void onCustomerReceived(Customer customer) {

                binding.bottomNavigationView.setOnItemSelectedListener(item -> {

                    if (item.getItemId() == R.id.home) {
                        replaceFragment(new UniversalHomeScreen());
                    }else if(item.getItemId() == R.id.savesService){
                        replaceFragment(new UniversalSavesScreen());
                    }else if(item.getItemId() == R.id.pastchat){
                        replaceFragment(new UniversalChatScreen());
                    }else{
                        replaceFragment(new CustomerProfileScreen(customer.getMail(),customer.getName(),customer.getJob(),item));
                    }

                    return true;


                });
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutCustomerPage,fragment);
        fragmentTransaction.commit();
    }
}