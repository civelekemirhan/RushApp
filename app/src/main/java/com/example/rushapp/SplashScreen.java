package com.example.rushapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.rushapp.databinding.FragmentSplashScreenBinding;


public class SplashScreen extends Fragment {

    FragmentSplashScreenBinding binding;

    public SplashScreen() {
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

        binding=FragmentSplashScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation topAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bottom_animation);
        binding.appLogo.setAnimation(topAnim);
        binding.appName.setAnimation(bottomAnim);

       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavDirections action=SplashScreenDirections.actionSplashScreenToLoginScreen2();
                Navigation.findNavController(view).navigate(action);


            }
        },3000);


    }
}