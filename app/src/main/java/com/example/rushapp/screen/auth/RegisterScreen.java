package com.example.rushapp.screen.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.rushapp.R;
import com.example.rushapp.callback.RegistrationCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Customer;
import com.example.rushapp.data.model.Provider;
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

        Animation bottomAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.bottom_animation);
        binding.contentLinearLayoutRegister.setAnimation(bottomAnim);

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




                        if(binding.checkbox.isChecked()){
                            Provider provider = new Provider(registerMail, registerName,registerPass,registerJob,false);
                            DBOperations.registerProvider(provider, getContext(), new RegistrationCallback() {
                                @Override
                                public void onRegistrationSuccess() {
                                    NavDirections action = RegisterScreenDirections.actionRegisterScreenToProviderPage();
                                    Navigation.findNavController(view).navigate(action);
                                }

                                @Override
                                public void onRegistrationFailure() {

                                }
                            });
                        }else{
                            Customer customer = new Customer(registerMail,registerName,registerPass,registerJob,true);
                            DBOperations.registerCustomer(customer, getContext(), new RegistrationCallback() {
                                @Override
                                public void onRegistrationSuccess() {
                                    NavDirections action = RegisterScreenDirections.actionRegisterScreenToCustomerPage();
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