package com.example.rushapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rushapp.data.model.Offer;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.RecyclerOfferRowBinding;
import com.example.rushapp.databinding.RecyclerRowBinding;

import java.util.List;

public class MyOffersFragmentAdapter extends RecyclerView.Adapter<MyOffersFragmentAdapter.MyViewHolder> {

    private List<Offer> myOffers;
    private Context context;

    public MyOffersFragmentAdapter(List<Offer> myOffers,Context context){
        this.myOffers=myOffers;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RecyclerOfferRowBinding recyclerOfferRowBinding=RecyclerOfferRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyOffersFragmentAdapter.MyViewHolder(recyclerOfferRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.acceptOffer.setVisibility(View.GONE);
        holder.binding.serviceField.setText(myOffers.get(position).getServiceField());
        holder.binding.serviceName.setText(myOffers.get(position).getServiceName());
        Glide.with(context)
                .load(myOffers.get(position).getProviderPhoto())
                .into(holder.binding.userPhoto);
        holder.binding.userMail.setText(myOffers.get(position).getProviderMail());
        holder.binding.userNameJob.setText(myOffers.get(position).getProviderName()+"-"+myOffers.get(position).getProviderJob());


    }

    @Override
    public int getItemCount() {
        return myOffers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RecyclerOfferRowBinding binding;
        public MyViewHolder(RecyclerOfferRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
