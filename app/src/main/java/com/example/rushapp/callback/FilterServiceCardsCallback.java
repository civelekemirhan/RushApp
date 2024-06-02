package com.example.rushapp.callback;

import com.example.rushapp.data.model.ServiceCard;

import java.util.ArrayList;

public interface FilterServiceCardsCallback {
    void onFilterCardsReceived(ArrayList<ServiceCard> filterCardList);
}
