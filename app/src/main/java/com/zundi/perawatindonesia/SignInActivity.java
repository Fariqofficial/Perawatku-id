package com.zundi.perawatindonesia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.zundi.inc.R;
import com.zundi.perawatindonesia.db.DBAccess;
import com.zundi.perawatindonesia.model.Res;
import com.zundi.perawatindonesia.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty
    @Email
    private EditText mInputEmail;

    @NotEmpty
    private EditText mInputPasswd;

    private Validator validator;
    private ProgressDialog mProgress;
    private DBAccess dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        dba = DBAccess.getInstance(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInputEmail = (EditText) findViewById(R.id.input_email);
        mInputPasswd = (EditText) findViewById(R.id.input_passwd);
        mProgress = Util.ShowProgDialog(this, false);

        mInputPasswd.setTransformationMethod(new PasswordTransformationMethod());
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        onAuth();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Util.showToast(this, message);
            }
        }
    }

    private void onAuth() {
        mProgress.setTitle("");
        mProgress.show();

        Call<Res.authLogin> serv = App.getInstance().getApi()
                .authLogin(Util.val(mInputEmail), Util.val(mInputPasswd));
        serv.enqueue(new Callback<Res.authLogin>() {
            @Override
            public void onResponse(Call<Res.authLogin> call, Response<Res.authLogin> response) {
                if(response.isSuccessful()) {
                    if (response.body().success) {
                        dba.open();
                        if(dba.saveUser(response.body().rows, false) != -1) {
                            FirebaseMessaging.getInstance().subscribeToTopic(App.TOPIC_GLOBAL);
                            String myToken = FirebaseInstanceId.getInstance().getToken();
                            onSaveToken(response.body().rows, myToken);
                        }
                        dba.close();
                    } else {
                        Util.showToast(getApplicationContext(), response.body().message);
                    }
                } else {
                    Util.showToast(getApplicationContext(), "Opps Terdapat Error");
                }
                mProgress.hide();
            }

            @Override
            public void onFailure(Call<Res.authLogin> call, Throwable t) {
                mProgress.hide();
            }
        });
    }

    private void onSaveToken(User u, String firebaseID) {
        Call<Res.saveUser> serv = App.getInstance().getApi()
                .saveToken(u.userID, firebaseID);
        serv.enqueue(new Callback<Res.saveUser>() {
            @Override
            public void onResponse(Call<Res.saveUser> call, Response<Res.saveUser> response) {
                if(response.isSuccessful()) {
                    if(response.body().success) {
                        mProgress.dismiss();
                        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        dba.deleteUser();
                    }
                }
            }

            @Override
            public void onFailure(Call<Res.saveUser> call, Throwable t) {

            }
        });
    }
}
