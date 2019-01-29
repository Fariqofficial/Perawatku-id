package com.zundi.perawatindonesia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zindx on 09/09/2017.
 */

public class Chat {

    @SerializedName("chat_id")
    public int chatID;

    @SerializedName("user_id")
    public String userID;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("chat_msg")
    public String chatMsg;

    @SerializedName("chat_date")
    public String chatDate;

    @SerializedName("chat_date_idn")
    public String chatDateIdn;

    public Chat() {}

    public Chat(int id, String userId, String msg) {
        this.chatID = id;
        this.userID = userId;
        this.chatMsg = msg;
    }
}
