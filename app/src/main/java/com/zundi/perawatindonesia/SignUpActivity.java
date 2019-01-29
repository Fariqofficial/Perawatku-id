package com.zundi.perawatindonesia;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.zundi.inc.R;
import com.zundi.perawatindonesia.model.Res;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText mInputFullName;

    @NotEmpty
    @Email
    private EditText mInputEmail;

    @Password(min = 5)
    private EditText mInputPasswd;

    @ConfirmPassword
    private EditText mInputRePasswd;

    private Validator validator;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        validator = new Validator(this);
        validator.setValidationListener(this);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInputFullName = (EditText) findViewById(R.id.input_fullname);
        mInputEmail = (EditText) findViewById(R.id.input_email);
        mInputPasswd = (EditText) findViewById(R.id.input_passwd);
        mInputRePasswd = (EditText) findViewById(R.id.input_passwd_conf);
        mProgress = Util.ShowProgDialog(this, false);

        mInputPasswd.setTransformationMethod(new PasswordTransformationMethod());
        mInputRePasswd.setTransformationMethod(new PasswordTransformationMethod());

        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
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
        onSaveUsers();
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

    private void onSaveUsers() {
        mProgress.setTitle("Please wait..");
        mProgress.show();

        Call<Res.addUsers> serv = App.getInstance().getApi()
                .addUser(Util.val(mInputEmail), Util.val(mInputFullName), Util.val(mInputPasswd));
        serv.enqueue(new Callback<Res.addUsers>() {
            @Override
            public void onResponse(@NonNull Call<Res.addUsers> call, @NonNull Response<Res.addUsers> response) {
                if(response.isSuccessful()) {
                    if (response.body().success) {
                        finish();
                    }
                    Util.showToast(getApplicationContext(), response.body().message);
                } else {
                    Util.showToast(getApplicationContext(), "Opps Terdapat Error");
                }
                mProgress.hide();
            }

            @Override
            public void onFailure(Call<Res.addUsers> call, Throwable t) {
                mProgress.hide();
            }
        });
    }
}
