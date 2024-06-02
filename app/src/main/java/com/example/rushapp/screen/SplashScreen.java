package com.example.rushapp.screen;

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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.rushapp.R;
import com.example.rushapp.callback.OnUserIsCustomerInformationCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.databinding.FragmentSplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;


public class SplashScreen extends Fragment {

    FragmentSplashScreenBinding binding;
    private FirebaseAuth mAuth;
    public SplashScreen() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //En tepede FragmentSplashScreenBinding binding; şeklinde tanımladık
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

                if(mAuth.getCurrentUser()!=null){



                    DBOperations.getUserCustomerInformation(new OnUserIsCustomerInformationCallback() {
                        @Override
                        public void onComplete(boolean isCustomer) {
                            if(isCustomer){
                                NavDirections action = SplashScreenDirections.actionSplashScreenToCustomerPage();
                                Navigation.findNavController(view).navigate(action);
                            }else{
                                NavDirections action = SplashScreenDirections.actionSplashScreenToProviderPage();
                                Navigation.findNavController(view).navigate(action);
                            }
                        }


                    });
                }else{
                    NavDirections action = SplashScreenDirections.actionSplashScreenToLoginScreen2();
                    Navigation.findNavController(view).navigate(action);

                }


            }
        }, 3000);


    }

}
