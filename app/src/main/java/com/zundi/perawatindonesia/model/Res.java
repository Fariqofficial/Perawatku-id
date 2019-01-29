package com.zundi.perawatindonesia.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zindx on 07/09/2017.
 */

public class Res {

    public static class addUsers {
        public boolean success;

        @SerializedName("msg")
        public String message;
    }

    public static class saveUser extends addUsers {}

    public static class authLogin extends addUsers {
        public User rows;
    }

    public static class getChat {
        public boolean success;
        public ArrayList<Chat> rows;
    }

    public static class sendChat {
        public boolean success;
    }

    public static class getNotification {
        @SerializedName("msg")
        public String message;

        @SerializedName("timestamp")
        public String timeStamp;

        public String title;
        public boolean success;
        public List<Chat> rows;
    }
}
