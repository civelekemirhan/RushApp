package com.example.rushapp.data.model;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rushapp.adapter.ProvidedServicesAdapter;
import com.example.rushapp.callback.FilterServiceCardsCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.adapter.SavesScreenAdapter;
import com.example.rushapp.callback.ServiceCardsCallback;
import com.example.rushapp.databinding.FragmentProvidedServicesBinding;
import com.example.rushapp.databinding.FragmentUniversalSavesScreenBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Provider extends User implements IProvider {
    public Provider(String mail, String name, String password, String job, Uri profilePhoto,boolean isCustomer,String userUid) {
        super(mail, name, password, job, profilePhoto,isCustomer,userUid);
    }

    public Provider(String mail, String name, String password, String job, boolean isCustomer,String userUid) {
        super(mail, name, password, job, isCustomer,userUid);
    }

    public Provider(String mail, String name, String password, String job, boolean isCustomer) {
        super(mail, name, password, job, isCustomer);
    }

    @Override
    public void getService(Offer offer,Context context) {
        DBOperations.makeOffer(offer,context);
    }

    public void serve(ServiceCard card) {

        DBOperations.makeServiceCard(card);
    }

    @Override
    public void savesService(ArrayList<ServiceCard> savedCardList, boolean isFilter,String minPrice,String maxPrice,String serviceField,FragmentUniversalSavesScreenBinding binding, Context context, BottomNavigationView bottomNavigationView) {

        Log.d("isFilterProvider",""+isFilter);
        if(isFilter==false){

            DBOperations.getSavedServicesCardsInformation(new ServiceCardsCallback() {
                @Override
                public void onCardsReceived(ServiceCard card) {
                    savedCardList.add(card);
                    binding.recyclerViewSaves.setLayoutManager(new LinearLayoutManager(context));
                    SavesScreenAdapter myAdapter=new SavesScreenAdapter(savedCardList,context,bottomNavigationView);
                    binding.recyclerViewSaves.setAdapter(myAdapter);

                    Log.d("FilterF","Filter False");
                }
            });

        }else{
            Log.d("isFilterProvider2","Burdayım");

            DBOperations.getSavedServicesCardsInformation(context,
                    minPrice,
                    maxPrice,
                    serviceField,
                    new FilterServiceCardsCallback() {

                        @Override
                        public void onFilterCardsReceived(ArrayList<ServiceCard> filterCardList) {

                            if (!filterCardList.isEmpty()) {
                                binding.recyclerViewSaves.setLayoutManager(new LinearLayoutManager(context));
                                SavesScreenAdapter savesScreenAdapter = new SavesScreenAdapter(filterCardList, context,bottomNavigationView);
                                binding.recyclerViewSaves.setAdapter(savesScreenAdapter);
                                savesScreenAdapter.notifyDataSetChanged();
                                Log.d("FilterT","Filter true");
                            }
                        }
                    });

        }

    }

    @Override
    public void receivedServiceHistory() {

    }

    public void providedServiceHistory(List<ServiceCard> providedCardList,FragmentProvidedServicesBinding binding,Context context) {
        // Sunulan hizmet geçmişi işlemleri
        DBOperations.getProvidedServicesCardsInformation(new ServiceCardsCallback() {
            @Override
            public void onCardsReceived(ServiceCard card) {
                providedCardList.add(card);
                binding.providedServicesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                ProvidedServicesAdapter myAdapter=new ProvidedServicesAdapter(providedCardList,context);
                binding.providedServicesRecyclerView.setAdapter(myAdapter);

            }
        });
    }

    public void activeServices() {
        // Aktif hizmetler işlemleri
    }

}