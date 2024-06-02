package com.example.rushapp.data.model;

import android.net.Uri;

import java.sql.Timestamp;

public class MsgModel {

    private String receiverUid;

    private String message;

    private String senderUid;

    private String receiverName;

    private String receiverPic;

    private long timestamp;

    public MsgModel(String receiverUid, String message, String receiverName, String receiverPic, long timestamp) {
        this.receiverUid = receiverUid;
        this.message = message;
        this.receiverName = receiverName;
        this.receiverPic = receiverPic;
        this.timestamp = timestamp;
    }
    public MsgModel(String receiverUid,String senderUid, String message, String receiverName, String receiverPic, long timestamp) {
        this.receiverUid = receiverUid;
        this.senderUid=senderUid;
        this.message = message;
        this.receiverName = receiverName;
        this.receiverPic = receiverPic;
        this.timestamp = timestamp;
    }
    public MsgModel(String receiverUid, String message, String receiverName, long timestamp) {
        this.receiverUid = receiverUid;
        this.message = message;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
    }

    public MsgModel() {

    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPic() {
        return receiverPic;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
