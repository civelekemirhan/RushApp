package com.example.rushapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import com.example.rushapp.R;
import com.example.rushapp.callback.CustomerInfoCallback;
import com.example.rushapp.callback.OnUserIsCustomerInformationCallback;
import com.example.rushapp.callback.ProviderInfoCallback;
import com.example.rushapp.data.model.Customer;
import com.example.rushapp.data.model.Offer;
import com.example.rushapp.data.model.Provider;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.databinding.RecyclerRowBinding;
import com.example.rushapp.screen.CustomerPageDirections;
import com.example.rushapp.screen.ProviderPageDirections;

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
        holder.binding.serviceField.setText(currentCard.getServiceField());
        holder.binding.providerNameJob.setText(currentCard.getProviderName() + " - " + currentCard.getProviderJob());
        Glide.with(context)
                .load(currentCard.getProviderPhoto())
                .into(holder.binding.providerPhoto);

        String cardUid = currentCard.getCardUid();
        String serviceId= currentCard.getServiceId();

        holder.binding.saveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView=holder.binding.saveService;
                Log.d("Saved Card Uid", cardUid);

                DBOperations.saveService(cardUid,serviceId, v.getContext(),imageView);

            }
        });
        holder.binding.quickOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations.getUserCustomerInformation(new OnUserIsCustomerInformationCallback() {
                    @Override
                    public void onComplete(boolean isCustomer) {
                        if(isCustomer){
                            DBOperations.getCustomerInformation(new CustomerInfoCallback() {
                                @Override
                                public void onCustomerReceived(Customer customer) {
                                    Log.d("currentCard.getProviderMail",currentCard.getProviderMail().toString());
                                    Offer offer=new Offer(customer.getName(),customer.getUserUid(),customer.getJob(),customer.getMail(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getServiceName(),currentCard.getServiceField(),currentCard.getCardUid(),currentCard.getProviderPhoto(),customer.getProfilePhoto());
                                    customer.getService(offer, v.getContext());
                                }
                            });

                        }else{
                            DBOperations.getProviderInformation(new ProviderInfoCallback() {
                                @Override
                                public void onProviderReceived(Provider provider) {
                                    Log.d("currentCard.getProviderMail",currentCard.getProviderMail().toString());
                                    Offer offer=new Offer(provider.getName(),provider.getUserUid(),provider.getJob(),provider.getMail(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getServiceName(),currentCard.getServiceField(),currentCard.getCardUid(),currentCard.getProviderPhoto(),provider.getProfilePhoto());
                                    provider.getService(offer, v.getContext());
                                }
                            });

                        }
                    }
                });
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
                            ProviderPageDirections.ActionProviderPageToProfileScreen action = ProviderPageDirections.actionProviderPageToProfileScreen(currentCard.getProviderPhoto().toString(),currentCard.getProviderName(),currentCard.getProviderJob(),currentCard.getProviderMail(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }

                    }
                });
            }
        });


        holder.binding.chatWithProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBOperations.getUserCustomerInformation(new OnUserIsCustomerInformationCallback() {
                    @Override
                    public void onComplete(boolean isCustomer) {

                        if(isCustomer){

                            CustomerPageDirections.ActionCustomerPageToChat action= CustomerPageDirections.actionCustomerPageToChat(currentCard.getProviderName(),currentCard.getProviderPhoto().toString(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }else{
                            ProviderPageDirections.ActionProviderPageToChat action = ProviderPageDirections.actionProviderPageToChat(currentCard.getProviderName(),currentCard.getProviderPhoto().toString(),currentCard.getCardUid());
                            Navigation.findNavController(v).navigate(action);

                        }

                    }
                });

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
