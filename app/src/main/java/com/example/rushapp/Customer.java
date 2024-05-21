package com.example.rushapp;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rushapp.databinding.FragmentUniversalSavesScreenBinding;

import java.util.List;

public class Customer extends User implements ICustomer {
    public Customer(String mail, String name, String password, String job, Uri profilePhoto,boolean isCustomer,String userUid) {
        super(mail, name, password, job, profilePhoto,isCustomer,userUid);
    }

    public Customer(String mail, String name, String password, String job, boolean isCustomer,String userUid) {
        super(mail, name, password, job, isCustomer,userUid);
    }

    public Customer(String mail, String name, String password, String job, boolean isCustomer) {
        super(mail, name, password, job, isCustomer);
    }

    @Override
    public void getService() {
        // Müşteriye özgü hizmet almak işlemleri
    }

    @Override
    public void savesService(List<ServiceCard> savedCardList, FragmentUniversalSavesScreenBinding binding, Context context) {
        DBOperations dbo=new DBOperations();
        dbo.getSavedServicesCardsInformation(new ServiceCardsCallback() {
            @Override
            public void onCardsReceived(ServiceCard card) {
                savedCardList.add(card);
                binding.recyclerViewSaves.setLayoutManager(new LinearLayoutManager(context));
                SavesScreenAdapter myAdapter=new SavesScreenAdapter(savedCardList,context);
                binding.recyclerViewSaves.setAdapter(myAdapter);
            }
        });
    }


    public void receivedServiceHistory() {
        // Alınan hizmet geçmişi işlemleri
    }

}