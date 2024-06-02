package com.example.rushapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rushapp.data.model.Provider;
import com.example.rushapp.screen.ActiveServiceFragment;
import com.example.rushapp.screen.IncomingOffersFragment;
import com.example.rushapp.screen.MyOffersFragment;
import com.example.rushapp.screen.ProvidedServicesFragment;
import com.example.rushapp.screen.ReceviceServicesFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    private Provider provider;
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity,Provider provider) {
        super(fragmentActivity);
        this.provider=provider;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ReceviceServicesFragment();
            case 1: return new ProvidedServicesFragment(provider);
            case 2: return new ActiveServiceFragment();
            case 3: return new IncomingOffersFragment();
            case 4: return new MyOffersFragment();
            default: return new ReceviceServicesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
