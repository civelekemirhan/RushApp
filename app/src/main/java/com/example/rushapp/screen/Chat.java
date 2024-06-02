package com.example.rushapp.screen;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.rushapp.adapter.ChatFragmentAdapter;
import com.example.rushapp.adapter.MyAdapter;
import com.example.rushapp.callback.MessageIsGetCallback;
import com.example.rushapp.callback.MessageIsSendCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.MsgModel;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentChatBinding;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Chat extends Fragment {

    FragmentChatBinding binding;
    ChatFragmentAdapter chatFragmentAdapter;
    private ArrayList<MsgModel> messageList;


    public Chat() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            binding.chatUsername.setText(ChatArgs.fromBundle(getArguments()).getProviderUserName());
            Glide.with(getContext())
                    .load(Uri.parse(ChatArgs.fromBundle(getArguments()).getProviderProfilePhoto()))
                    .into(binding.chatUserPhoto);
        }

        messageList = new ArrayList<>();

        binding.sendButton.setOnClickListener(v -> {

            MsgModel msgModel = new MsgModel(ChatArgs.fromBundle(getArguments()).getProviderID(), binding.messageContent.getText().toString(),ChatArgs.fromBundle(getArguments()).getProviderUserName(),ChatArgs.fromBundle(getArguments()).getProviderProfilePhoto(), System.currentTimeMillis());


            if (msgModel.getMessage() != null) {
                DBOperations.sendMessage(msgModel, new MessageIsSendCallback() {
                    @Override
                    public void onSendSuccess() {
                        binding.messageContent.setText(null);

                        messageList.add(msgModel);
                        chatFragmentAdapter.notifyDataSetChanged();
                        chatFragmentAdapter.notifyItemInserted(messageList.size() - 1);

                        binding.chatMessagesRecyclerRow.scrollToPosition(messageList.size() - 1);
                    }

                    @Override
                    public void onSendFailure() {
                        Toast.makeText(getContext(), "Mesaj iletilemedi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.chatMessagesRecyclerRow.setLayoutManager(new LinearLayoutManager(getContext()));
        chatFragmentAdapter = new ChatFragmentAdapter(messageList, getContext());
        binding.chatMessagesRecyclerRow.setAdapter(chatFragmentAdapter);


        DBOperations.getMessage(ChatArgs.fromBundle(getArguments()).getProviderID(), new MessageIsGetCallback() {
            @Override
            public void onGetMessageSuccess(MsgModel messages) {
                messageList.add(messages);
                chatFragmentAdapter.notifyItemInserted(messageList.size() - 1);
                binding.chatMessagesRecyclerRow.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onGetMessageFailure() {
                Toast.makeText(getContext(), "Mesajlar veritabanından çekilemedi", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

