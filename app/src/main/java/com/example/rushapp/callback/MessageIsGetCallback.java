package com.example.rushapp.callback;

import com.example.rushapp.data.model.MsgModel;

public interface MessageIsGetCallback {
    void onGetMessageSuccess(MsgModel message);
    void onGetMessageFailure();
}
