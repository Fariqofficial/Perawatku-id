package com.zundi.perawatindonesia;

import com.zundi.perawatindonesia.model.Res;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by zindx on 05/09/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<Res.authLogin> authLogin(
        @Field("user_id") String id,
        @Field("user_passwd") String passwd
    );

    @FormUrlEncoded
    @POST("users/save")
    Call<Res.addUsers> addUser(
        @Field("user_id") String id,
        @Field("user_name") String name,
        @Field("user_passwd") String passwd
    );

    @FormUrlEncoded
    @POST("users/save/{id}")
    Call<Res.saveUser> saveToken(
        @Path("id") String id,
        @Field("user_firebase_id") String token
    );

    @FormUrlEncoded
    @POST("users/save/{id}")
    Call<Res.saveUser> saveUser(
        @Path("id") String id,
        @Field("user_name") String name,
        @Field("user_passwd") String passwd,
        @Field("user_passwd_old") String passwdOld
    );

    @GET("chat/all")
    Call<Res.getChat> getChat();

    @FormUrlEncoded
    @POST("chat/send")
    Call<Res.sendChat> sendChat(
       @Field("user_id") String id,
       @Field("chat_msg") String message
    );

}
