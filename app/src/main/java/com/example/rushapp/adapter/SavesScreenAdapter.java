package com.example.rushapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.RecyclerRowBinding;

import java.util.List;

public class SavesScreenAdapter extends RecyclerView.Adapter<SavesScreenAdapter.SavesScreenHolder> {


    List<ServiceCard> savedCardList;

    Context context;
    public SavesScreenAdapter(List<ServiceCard> savedCardList ,Context context) {
        this.savedCardList=savedCardList;
        this.context=context;
    }

    @NonNull
    @Override
    public SavesScreenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SavesScreenHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavesScreenHolder holder, int position) {

        holder.binding.serviceName.setText(savedCardList.get(position).getServiceName());
        holder.binding.serviceExplain.setText(savedCardList.get(position).getServiceExplain());
        holder.binding.servicePrice.setText(savedCardList.get(position).getServicePrice());
        holder.binding.providerNameJob.setText(savedCardList.get(position).getProviderName()+" - "+savedCardList.get(position).getProviderJob());
        Glide.with(context)
                .load(savedCardList.get(position).getProviderPhoto())
                .into(holder.binding.providerPhoto);

        holder.binding.saveService.setVisibility(View.INVISIBLE);



    }

    @Override
    public int getItemCount() {
        return savedCardList.size();
    }

    public class SavesScreenHolder extends RecyclerView.ViewHolder{

        private RecyclerRowBinding binding;
        public SavesScreenHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
