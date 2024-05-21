package com.example.rushapp;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rushapp.databinding.FragmentUniversalSavesScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Provider extends User implements IProvider{
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
    public void getService() {
        // Sağlayıcıya özgü hizmet vermek işlemleri
    }

    public void serve(ServiceCard card) {
        // Hizmet verme işlemleri
        DBOperations dbo = new DBOperations();
        dbo.makeServiceCard(card);
    }

    @Override
    public void savesService(List<ServiceCard> savedCardList, FragmentUniversalSavesScreenBinding binding,Context context) {
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

    public void providedServiceHistory() {
        // Sunulan hizmet geçmişi işlemleri
    }

    public void activeServices() {
        // Aktif hizmetler işlemleri
    }

}