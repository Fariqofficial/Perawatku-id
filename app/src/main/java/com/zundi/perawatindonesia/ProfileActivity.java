package com.zundi.perawatindonesia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.zundi.inc.R;
import com.zundi.perawatindonesia.db.DBAccess;
import com.zundi.perawatindonesia.model.Res;
import com.zundi.perawatindonesia.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText mInputFullName;
    private TextView mInputId;
    private EditText mPasswdOld;
    private EditText mPasswd;
    private EditText mPasswdConf;

    private LinearLayout mPassGroup;
    private CheckBox mPassCheck;
    private TextView mBtnSave;
    private ProgressDialog mProgress;

    private Validator validator;
    private DBAccess dba;
    private User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dba = DBAccess.getInstance(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInputFullName = (EditText) findViewById(R.id.input_fullname);
        mInputId = (TextView) findViewById(R.id.input_email);
        mPassCheck = (CheckBox) findViewById(R.id.passwd_check);
        mPassGroup = (LinearLayout) findViewById(R.id.passwd_group);
        mPasswd = (EditText) findViewById(R.id.input_passwd);
        mPasswdOld = (EditText) findViewById(R.id.input_passwd_old);
        mPasswdConf = (EditText) findViewById(R.id.input_passwd_conf);
        mBtnSave = (TextView) findViewById(R.id.btn_save);
        mProgress = Util.ShowProgDialog(this, false);

        mPasswdOld.setTransformationMethod(new PasswordTransformationMethod());
        mPasswd.setTransformationMethod(new PasswordTransformationMethod());
        mPasswdConf.setTransformationMethod(new PasswordTransformationMethod());

        mPassCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()) {
                    mPassGroup.setVisibility(View.VISIBLE);
                }  else {
                    mPassGroup.setVisibility(View.GONE);
                    clearAllPassError();
                    Util.clearVal(mPasswd, mPasswdConf, mPasswdOld);
                }
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_signout :
                App.getInstance().destroySession(this);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(App.TOPIC_GLOBAL);
                return true;
            case R.id.act_about :
                Intent intent = new Intent(ProfileActivity.this, CommonPreviewActivity.class);
                intent.putExtra("title", "Tentang Kami");
                intent.putExtra("html", "about");
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        if(mPassCheck.isChecked()) {
            if(!Util.isEmpty(mPasswdOld, mPasswd, mPasswdConf)) {
                if(!Util.val(mPasswd).equals(Util.val(mPasswdConf))) {
                    mPasswdConf.setError("Password tidak sama!");
                    return;
                }
            } else return;
        } else {
            clearAllPassError();
        }

        onUpdateUser();
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

    private void onUpdateUser() {
        mProgress.setTitle("Menyimpan perubahan");
        mProgress.show();

        Call<Res.saveUser> serv = App.getInstance().getApi()
                .saveUser(me.userID,
                        Util.val(mInputFullName), Util.val(mPasswd), Util.val(mPasswdOld));
        serv.enqueue(new Callback<Res.saveUser>() {
            @Override
            public void onResponse(Call<Res.saveUser> call, Response<Res.saveUser> response) {
                if(response.isSuccessful()) {
                    if(response.body().success) {
                        dba.open();
                        User update = new User();
                        update.userID = me.userID;
                        update.userName = Util.val(mInputFullName);
                        dba.saveUser(update, true);
                        dba.close();

                        if(mPassCheck.isChecked()) {
                            mPassCheck.performClick();
                        }
                    }

                    Util.showToast(getApplicationContext(), response.body().message);
                    mProgress.hide();
                }
            }

            @Override
            public void onFailure(Call<Res.saveUser> call, Throwable t) {
                mProgress.hide();
            }
        });
    }

    private void fillData() {
        dba.open();
        me = dba.getUser();
        mInputFullName.setText(me.userName);
        mInputId.setText(me.userID);
        dba.close();
    }

    private void clearAllPassError() {
        Util.clearErrors(mPasswd, mPasswdOld, mPasswdConf);
    }
}
