package com.example.rushapp.callback;

import com.example.rushapp.data.model.Offer;
import com.example.rushapp.data.model.ServiceCard;

public interface OfferCallback {
    void onOfferReceived(Offer offer);
}
