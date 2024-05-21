package com.example.rushapp;

import android.content.Context;
import android.net.Uri;

import com.example.rushapp.databinding.FragmentUniversalSavesScreenBinding;

import java.util.List;

public abstract class User {
    private String name;
    private String mail;
    private String password;
    private String job;
    private Uri profilePhoto;

    private String userUid;
    private boolean isCustomer;

    public User(String mail, String name, String password, String job, Uri profilePhoto,boolean isCustomer,String userUid) {
        this.mail = mail;
        this.name = name;
        this.password = password;
        this.job = job;
        this.profilePhoto = profilePhoto;
        this.isCustomer=isCustomer;
        this.userUid=userUid;
    }
    public User(String mail, String name, String password, String job,boolean isCustomer,String userUid) {
        this.mail = mail;
        this.name = name;
        this.password = password;
        this.job = job;
        this.isCustomer=isCustomer;
        this.profilePhoto=null;
        this.userUid=userUid;
    }
    public User(String mail, String name, String password, String job,boolean isCustomer) {
        this.mail = mail;
        this.name = name;
        this.password = password;
        this.job = job;
        this.isCustomer=isCustomer;
        this.profilePhoto=null;
    }


    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getJob() {
        return job;
    }

    public Uri getProfilePhoto() {
        return profilePhoto;
    }

    public String getUserUid() {
        return userUid;
    }

    // Abstract method
    public abstract void getService();

    public abstract void savesService(List<ServiceCard> savedCardList, FragmentUniversalSavesScreenBinding binding, Context context);




    public boolean isCustomer() {
        return isCustomer;
    }


}

