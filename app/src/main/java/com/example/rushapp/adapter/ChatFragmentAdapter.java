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
import com.example.rushapp.callback.OnUserIsCustomerInformationCallback;
import com.example.rushapp.data.model.MsgModel;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.databinding.MessagesLoyutChatBinding;
import com.example.rushapp.databinding.RecyclerRowBinding;
import com.example.rushapp.screen.CustomerPageDirections;
import com.example.rushapp.screen.ProviderPageDirections;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatFragmentAdapter extends RecyclerView.Adapter<ChatFragmentAdapter.MyHolder> {

    private ArrayList<MsgModel> arrayList;

    private FirebaseAuth mAuth;
    private Context context;

    public ChatFragmentAdapter(ArrayList<MsgModel> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessagesLoyutChatBinding messagesLoyutChatBinding = MessagesLoyutChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyHolder(messagesLoyutChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        mAuth=FirebaseAuth.getInstance();
        MsgModel currentMsg = arrayList.get(position);


        if(currentMsg.getReceiverUid()!=mAuth.getUid()){
            holder.binding.getmessage.setVisibility(View.INVISIBLE);
            holder.binding.sendmessage.setVisibility(View.VISIBLE);

            holder.binding.sendmessage.setText(currentMsg.getMessage());


        }else{
            holder.binding.getmessage.setVisibility(View.VISIBLE);
            holder.binding.sendmessage.setVisibility(View.INVISIBLE);
            holder.binding.getmessage.setText(currentMsg.getMessage());

        }


    }

    @Override
    public int getItemCount() {
        Log.d("ArraySize",""+arrayList.size());
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private MessagesLoyutChatBinding binding;

        public MyHolder(MessagesLoyutChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
