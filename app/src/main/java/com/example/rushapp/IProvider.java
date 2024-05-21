package com.example.rushapp;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public interface IProvider {

    public void serve(ServiceCard card); //Hizmet vermek

    public void providedServiceHistory();//Verilen geçmiş hizmetler // ismi güncelle



    public void activeServices();//Aktif olarak verilecek olan servisler



}
