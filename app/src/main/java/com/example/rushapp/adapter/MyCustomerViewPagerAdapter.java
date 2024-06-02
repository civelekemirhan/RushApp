package com.example.rushapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rushapp.data.model.Customer;
import com.example.rushapp.data.model.Provider;
import com.example.rushapp.screen.MyOffersFragment;
import com.example.rushapp.screen.ProvidedServicesFragment;
import com.example.rushapp.screen.ReceviceServicesFragment;

public class MyCustomerViewPagerAdapter extends FragmentStateAdapter {

    private Customer customer;
    public MyCustomerViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Customer customer) {
        super(fragmentActivity);
        this.customer=customer;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new ReceviceServicesFragment();
            case 1: return new MyOffersFragment();
            default: return new ReceviceServicesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
