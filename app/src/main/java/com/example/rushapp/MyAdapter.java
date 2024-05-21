package com.example.rushapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.rushapp.databinding.FragmentCustomerPageBinding;
import com.example.rushapp.databinding.FragmentUniversalHomeScreenBinding;
import com.example.rushapp.databinding.RecyclerRowBinding;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private ArrayList<ServiceCard> arrayList;
    private Context context;

    public MyAdapter(ArrayList<ServiceCard> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ServiceCard currentCard = arrayList.get(position);

        holder.binding.serviceName.setText(currentCard.getServiceName());
        holder.binding.serviceExplain.setText(currentCard.getServiceExplain());
        holder.binding.servicePrice.setText(currentCard.getServicePrice());
        holder.binding.providerNameJob.setText(currentCard.getProviderName() + " - " + currentCard.getProviderJob());
        Glide.with(context)
                .load(currentCard.getProviderPhoto())
                .into(holder.binding.providerPhoto);

        String cardUid = currentCard.getCardUid();
        String serviceId= currentCard.getServiceId();

        holder.binding.saveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Saved Card Uid", cardUid);
                DBOperations dbo = new DBOperations();
                dbo.saveService(cardUid,serviceId, v.getContext());
            }
        });

        holder.binding.providerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action = CustomerPageDirections.actionCustomerPageToProfileScreen();
                Navigation.findNavController(v).navigate(action);


            }
        });

        holder.binding.providerNameJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = CustomerPageDirections.actionCustomerPageToProfileScreen();
                Navigation.findNavController(v).navigate(action);
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private RecyclerRowBinding binding;

        public MyHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
