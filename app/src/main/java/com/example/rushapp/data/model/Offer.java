package com.example.rushapp.data.model;

import android.net.Uri;

public class Offer {

    private String customerName;

    private String customerUid;

    private String customerJob;

    private String customerMail;
    private String providerName;


    private String providerJob;

    private String providerMail;

    private String providerUid;

    private String serviceName;

    private String serviceField;

    private Uri providerPhoto;

    private Uri customerPhoto;

    public Offer(String customerName, String customerUid, String customerJob, String customerMail,String providerName,String providerJob ,String providerMail, String serviceName,String serviceField,String providerUid, Uri providerPhoto, Uri customerPhoto) {
        this.customerName = customerName;
        this.customerUid = customerUid;
        this.customerJob = customerJob;
        this.customerMail = customerMail;
        this.providerName=providerName;
        this.providerJob=providerJob;
        this.providerMail=providerMail;
        this.serviceField=serviceField;
        this.serviceName=serviceName;
        this.providerUid = providerUid;
        this.providerPhoto = providerPhoto;
        this.customerPhoto = customerPhoto;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public String getCustomerJob() {
        return customerJob;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderJob() {
        return providerJob;
    }

    public String getProviderMail() {
        return providerMail;
    }

    public String getServiceField() {
        return serviceField;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public String getProviderUid() {
        return providerUid;
    }

    public Uri getProviderPhoto() {
        return providerPhoto;
    }

    public Uri getCustomerPhoto() {
        return customerPhoto;
    }
}
