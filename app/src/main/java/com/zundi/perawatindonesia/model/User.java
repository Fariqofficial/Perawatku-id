package com.zundi.perawatindonesia.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zindx on 07/09/2017.
 */

public class User {

    @SerializedName("user_id")
    public String userID;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("user_photo")
    public String userPhoto;

    public User() {

    }

    public User(String id, String name, String passwd, String photo) {
        this.userID = id;
        this.userName = name;
        this.userPhoto = photo;
    }

}
