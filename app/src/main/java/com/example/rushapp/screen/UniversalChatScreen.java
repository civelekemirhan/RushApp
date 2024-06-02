package com.example.rushapp.screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rushapp.adapter.UniversalChatScreenAdapter;
import com.example.rushapp.callback.ChatHistoryCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.MsgModel;
import com.example.rushapp.databinding.FragmentUniversalChatScreenBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class UniversalChatScreen extends Fragment {

    private FragmentUniversalChatScreenBinding binding;
    private BottomNavigationView bottomNavigationView;

    private List<MsgModel> chatList;


    public UniversalChatScreen(BottomNavigationView bottomNavigationView) {
        // Required empty public constructor
        this.bottomNavigationView=bottomNavigationView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentUniversalChatScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatList=new ArrayList<>();
        DBOperations.getChatHistory(new ChatHistoryCallback() {
            @Override
            public void onGetChatHistorySuccess(MsgModel msg) {

                chatList.add(msg);
                String lastMessage=msg.getMessage();
                binding.messageListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                UniversalChatScreenAdapter universalChatScreenAdapter=new UniversalChatScreenAdapter(chatList,lastMessage,getContext());
                binding.messageListRecyclerView.setAdapter(universalChatScreenAdapter);
            }

            @Override
            public void onGetChatHistoryFailure(String errorMessage) {

            }
        });


    }
}