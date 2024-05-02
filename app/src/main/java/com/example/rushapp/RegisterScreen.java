package com.example.rushapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgument;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rushapp.databinding.FragmentRegisterScreenBinding;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterScreen extends Fragment {


    FragmentRegisterScreenBinding binding;
    FirebaseFirestore db;

    public RegisterScreen() {
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
        binding = FragmentRegisterScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerMail=binding.registerMail.getText().toString();
                String registerPass=binding.registerPass.getText().toString();
                String registerPassAgain=binding.registerPassAgain.getText().toString();
                String registerName=binding.registerName.getText().toString();
                String registerJob=binding.registerJob.getText().toString();

                if (!registerMail.equals("") && !registerPass.equals("") && !registerPassAgain.equals("")
                        && !registerName.equals("") && !registerJob.equals("")) {
                    if (registerPassAgain.equals(registerPass)) {


                        DBOperations dbo=new DBOperations();

                        if(binding.checkbox.isChecked()){
                            Provider provider = new Provider(registerMail, registerName,registerPass,registerJob);
                            dbo.registerProvider(provider, getContext(), new RegistrationCallback() {
                                @Override
                                public void onRegistrationSuccess() {
                                    NavDirections action = RegisterScreenDirections.actionRegisterScreenToProviderHomePage();
                                    Navigation.findNavController(view).navigate(action);
                                }

                                @Override
                                public void onRegistrationFailure() {

                                }
                            });
                        }else{
                            Customer customer = new Customer(registerMail,registerName,registerPass,registerJob);
                            dbo.registerCustomer(customer, getContext(), new RegistrationCallback() {
                                @Override
                                public void onRegistrationSuccess() {
                                    NavDirections action = RegisterScreenDirections.actionRegisterScreenToCustomerHomePage();
                                    Navigation.findNavController(view).navigate(action);
                                }

                                @Override
                                public void onRegistrationFailure() {

                                }
                            });

                        }

                    } else {
                        Toast.makeText(getContext(), "Girilen şifreler eşleşmiyor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}