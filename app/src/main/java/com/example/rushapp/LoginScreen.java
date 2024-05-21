package com.example.rushapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.rushapp.databinding.FragmentLoginScreenBinding;


public class LoginScreen extends Fragment {


    FragmentLoginScreenBinding binding;

    public LoginScreen() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding=FragmentLoginScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation bottomAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bottom_animation);
        binding.contentLinearLayout.setAnimation(bottomAnim);
        binding.goRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = LoginScreenDirections.actionLoginScreenToRegisterScreen();
                Navigation.findNavController(view).navigate(action);
            }
        });



        binding.loginButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String loginMail=binding.textInputEditTextMail.getText().toString();
                String loginPass=binding.textInputEditTextPass.getText().toString();



                if(!loginMail.equals("") && !loginPass.equals("")){

                    DBOperations dbo=new DBOperations();
                    dbo.loginSystem(loginMail,loginPass, getContext(), new LoginCallback() {
                        @Override
                        public void onLoginSuccess(boolean isCustomer) {
                            binding.loginButton.setEnabled(false);
                            if(isCustomer){
                                NavDirections action = LoginScreenDirections.actionLoginScreenToCustomerPage();
                                Navigation.findNavController(view).navigate(action);
                            }else{
                                NavDirections action=LoginScreenDirections.actionLoginScreenToProviderPage();
                                Navigation.findNavController(view).navigate(action);
                            }


                        }

                        @Override
                        public void onLoginFailure() {

                        }
                    });


                }else{
                    Toast.makeText(getContext(), "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}