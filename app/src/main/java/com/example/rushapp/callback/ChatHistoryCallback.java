package com.example.rushapp.callback;

import com.example.rushapp.data.model.MsgModel;

public interface ChatHistoryCallback {
    void onGetChatHistorySuccess(MsgModel msg);
    void onGetChatHistoryFailure(String errorMessage);
}
