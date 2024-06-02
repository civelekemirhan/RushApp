package com.example.rushapp.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rushapp.R;
import com.example.rushapp.callback.CustomerInfoCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Customer;
import com.example.rushapp.data.model.MsgModel;
import com.example.rushapp.databinding.ChatPastsLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UniversalChatScreenAdapter extends RecyclerView.Adapter<UniversalChatScreenAdapter.MyViewHolder> {

   private List<MsgModel> chatList;

   private FirebaseFirestore db;

   private FirebaseAuth mAuth;

   private String lastMessage;
   private Context context;

    public UniversalChatScreenAdapter(List<MsgModel> chatList,String lastMessage, Context context){
        this.chatList=chatList;
        this.context=context;
        this.lastMessage=lastMessage;
        Log.d("ChatInfo1", "Success get from firebase");
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatPastsLayoutBinding chatPastsLayoutBinding=ChatPastsLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UniversalChatScreenAdapter.MyViewHolder(chatPastsLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        DocumentReference docRef = db.collection("userInformation").document(chatList.get(position).getReceiverUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int currentPosition = holder.getAdapterPosition();

                if (task.isSuccessful()) {

                    Log.d("\nmAuth.getUid()",mAuth.getUid());
                    DocumentSnapshot document = task.getResult();
                    if (document.exists() && currentPosition != RecyclerView.NO_POSITION) {


                                            if(chatList.get(currentPosition).getSenderUid().equals(mAuth.getUid())){
                                                String profilePhotoUrl = (String) document.getData().get("profilePhoto");
                                                String nickName = (String) document.getData().get("NickName");

                                                if (profilePhotoUrl == null) {
                                                    Glide.with(context)
                                                            .load(R.drawable.person)
                                                            .into(holder.binding.senderOfMessagePhoto);
                                                } else {
                                                    Uri profilePhotoUri = Uri.parse(profilePhotoUrl);
                                                    Glide.with(context)
                                                            .load(profilePhotoUri)
                                                            .into(holder.binding.senderOfMessagePhoto);
                                                }

                                                holder.binding.senderOfMessageUsername.setText(nickName);
                                                holder.binding.lastMessage.setText(chatList.get(currentPosition).getMessage());


                                            }else{
                                                DBOperations.getUserInformationWithUid(chatList.get(currentPosition).getSenderUid(), new CustomerInfoCallback() {
                                                    @Override
                                                    public void onCustomerReceived(Customer customer) {
                                                        Uri profilePhotoUrl = customer.getProfilePhoto();
                                                        String nickName = customer.getName();

                                                        if (profilePhotoUrl == null) {
                                                            Glide.with(context)
                                                                    .load(R.drawable.person)
                                                                    .into(holder.binding.senderOfMessagePhoto);
                                                        } else {

                                                            Glide.with(context)
                                                                    .load(profilePhotoUrl)
                                                                    .into(holder.binding.senderOfMessagePhoto);
                                                        }

                                                        holder.binding.senderOfMessageUsername.setText(nickName);
                                                        holder.binding.lastMessage.setText(chatList.get(currentPosition).getMessage());
                                                    }
                                                });


                                            }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ChatPastsLayoutBinding binding;
        private MyViewHolder(ChatPastsLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
