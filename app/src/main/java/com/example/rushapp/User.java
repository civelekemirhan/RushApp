package com.example.rushapp;

public abstract class User {

    private String name;

    private String mail;
    private String password;
    private String job;


    public User(String mail,String name, String password, String job) {
        this.mail=mail;
        this.name = name;
        this.password = password;
        this.job = job;

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

    public abstract void getService();// Tüm kullanıcı tipleri aynı zamanda hizmet alabilir


}

