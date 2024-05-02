package com.example.rushapp;

public class Customer extends User implements ICustomer{

    public Customer(String mail,String name, String password, String job) {
        super(mail,name, password, job);
    }


    @Override
    public void getService() {

    }


    @Override
    public void searchService() {

    }

    @Override
    public void receivedServiceHistory() {

    }



}
 