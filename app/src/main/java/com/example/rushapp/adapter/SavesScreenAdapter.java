package com.example.rushapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rushapp.R;
import com.example.rushapp.callback.OnUserIsCustomerInformationCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.RecyclerRowBinding;
import com.example.rushapp.screen.CustomerPageDirections;
import com.example.rushapp.screen.ProviderPageDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.lock.qual.LockHeld;

import java.util.ArrayList;
import java.util.List;

public class SavesScreenAdapter extends RecyclerView.Adapter<SavesScreenAdapter.SavesScreenHolder> {


    private List<ServiceCard> savedCardList;
    private BottomNavigationView bottomNavigationView;

    Context context;
    public SavesScreenAdapter(ArrayList<ServiceCard> savedCardList , Context context, BottomNavigationView bottomNavigationView) {
        this.savedCardList=savedCardList;
        this.context=context;
        this.bottomNavigationView=bottomNavigationView;
    }

    @NonNull
    @Override
    public SavesScreenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SavesScreenHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SavesScreenHolder holder, int position) {
        ServiceCard currentCard = savedCardList.get(position);
        String currentCardServiceId = savedCardList.get(position).getServiceId();
   ;

        holder.binding.serviceName.setText(savedCardList.get(position).getServiceName());
        holder.binding.serviceExplain.setText(savedCardList.get(position).getServiceExplain());
        holder.binding.servicePrice.setText(savedCardList.get(position).getServicePrice());
        holder.binding.serviceField.setText(savedCardList.get(position).getServiceField());
        holder.binding.providerNameJob.setText(savedCardList.get(position).getProviderName()+" - "+savedCardList.get(position).getProviderJob());
        Glide.with(context)
                .load(savedCardList.get(position).getProviderPhoto())
                .into(holder.binding.providerPhoto);

        holder.binding.saveService.setVisibility(View.GONE);
        holder.binding.deleteSavedService.setVisibility(View.VISIBLE);
        int itemPosition = position;
        holder.binding.deleteSavedService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBOperations.deleteSavedCard(currentCardServiceId, v.getContext(), new Runnable() {
                    @Override
                    public void run() {
                        savedCardList.remove(itemPosition);

                        notifyDataSetChanged();
                    }
                });


            }

        });

        holder.binding.chatWithProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.binding.providerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DBOperations.getUserCustomerInformation(new OnUserIsCustomerInformationCallback() {
                    @Override
                    public void onComplete(boolean isCustomer) {

                        if(isCustomer){
                            NavDirections action = CustomerPageDirections.actionCustomerPageToProfileScreen(currentCard.getProviderPhoto().toString(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }else{
                            NavDirections action = ProviderPageDirections.actionProviderPageToProfileScreen(currentCard.getProviderPhoto().toString(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }

                    }
                });




            }
        });

        holder.binding.providerNameJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBOperations.getUserCustomerInformation(new OnUserIsCustomerInformationCallback() {
                    @Override
                    public void onComplete(boolean isCustomer) {

                        if(isCustomer){
                            //NavDirections action = CustomerPageDirections.actionCustomerPageToProfileScreen();
                            CustomerPageDirections.ActionCustomerPageToProfileScreen action= CustomerPageDirections.actionCustomerPageToProfileScreen(currentCard.getProviderPhoto().toString(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }else{
                            NavDirections action = ProviderPageDirections.actionProviderPageToProfileScreen(currentCard.getProviderPhoto().toString(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }

                    }
                });
            }
        });


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
