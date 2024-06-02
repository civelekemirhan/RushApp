package com.example.rushapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.RecyclerRowBinding;

import java.util.List;

public class ProvidedServicesUniversalAdapter extends RecyclerView.Adapter<ProvidedServicesUniversalAdapter.MHolder> {

    private List<ServiceCard> providedCardList;
    private Context context;

    public ProvidedServicesUniversalAdapter(List<ServiceCard> providedCardList , Context context){
        this.providedCardList=providedCardList;
        this.context=context;
    }


    @NonNull
    @Override
    public ProvidedServicesUniversalAdapter.MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ProvidedServicesUniversalAdapter.MHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvidedServicesUniversalAdapter.MHolder holder, int position) {
        ServiceCard card = providedCardList.get(position);

        holder.binding.serviceName.setText(card.getServiceName());
        holder.binding.serviceExplain.setText(card.getServiceExplain());
        holder.binding.servicePrice.setText(card.getServicePrice());
        holder.binding.serviceField.setText(card.getServiceField());
        holder.binding.providerNameJob.setText(card.getProviderName() + " - " + card.getProviderJob());
        Glide.with(context)
                .load(card.getProviderPhoto())
                .into(holder.binding.providerPhoto);

        holder.binding.saveService.setVisibility(View.GONE);
        holder.binding.chatWithProvider.setVisibility(View.GONE);
        holder.binding.quickOfferButton.setVisibility(View.GONE);

        holder.binding.deleteSavedService.setVisibility(View.VISIBLE);
        int itemPosition = position;
        holder.binding.deleteSavedService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations.deleteProvidedCard(card.getServiceId(), card.getCardUid(), v.getContext(), new Runnable() {
                    @Override
                    public void run() {
                        // Öğeyi listeden kaldır
                        providedCardList.remove(itemPosition);
                        // RecyclerView'ı güncelle
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return providedCardList.size();
    }

    public class MHolder extends RecyclerView.ViewHolder{

        RecyclerRowBinding binding;
        public MHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
