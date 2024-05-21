package com.example.rushapp.data.model;

import android.net.Uri;
import android.widget.ImageView;

public class ServiceCard {

    private String serviceName;
    private String providerName;

    private String providerMail;

    private String serviceExplain;

    private String serviceField;

    private String servicePrice;

    private String providerJob;

    private Uri providerPhoto;

    private String cardUid;

    private String serviceId;

    public ServiceCard(String serviceName, String providerName, String providerMail, String serviceExplain, String serviceField,String servicePrice,String providerJob,Uri providerPhoto,String serviceId,String cardUid) {
        this.serviceName = serviceName;
        this.providerName = providerName;
        this.providerMail = providerMail;
        this.serviceExplain = serviceExplain;
        this.serviceField = serviceField;
        this.servicePrice=servicePrice;
        this.providerJob=providerJob;
        this.providerPhoto=providerPhoto;
        this.cardUid=cardUid;
        this.serviceId=serviceId;
    }
    public ServiceCard(String serviceName, String providerName, String providerMail, String serviceExplain, String serviceField,String servicePrice,String providerJob,Uri providerPhoto,String cardUid) {
        this.serviceName = serviceName;
        this.providerName = providerName;
        this.providerMail = providerMail;
        this.serviceExplain = serviceExplain;
        this.serviceField = serviceField;
        this.servicePrice=servicePrice;
        this.providerJob=providerJob;
        this.providerPhoto=providerPhoto;
        this.cardUid=cardUid;
    }

    public Uri getProviderPhoto() {
        return providerPhoto;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderMail() {
        return providerMail;
    }

    public String getServiceExplain() {
        return serviceExplain;
    }

    public String getServiceField() {
        return serviceField;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getProviderJob() {
        return providerJob;
    }

    public String getCardUid() {
        return cardUid;
    }

    public String getServiceId() {
        return serviceId;
    }


}
