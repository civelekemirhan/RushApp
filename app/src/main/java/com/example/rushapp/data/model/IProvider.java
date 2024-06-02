package com.example.rushapp.data.model;

import android.content.Context;

import com.example.rushapp.databinding.FragmentProvidedServicesBinding;

import java.util.List;

public interface IProvider {

    public void serve(ServiceCard card); //Hizmet vermek

    public void providedServiceHistory(List<ServiceCard> providedCardList, FragmentProvidedServicesBinding binding, Context context);//Verilen geçmiş hizmetler // ismi güncelle



    public void activeServices();//Aktif olarak verilecek olan servisler



}
