package com.github.bewithforce.riderapp.gui.LogInActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.BaseActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requests.JsonWebToken;
import com.github.bewithforce.riderapp.post.requests.LoginPOST;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    CallAPI callAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewFail = findViewById(R.id.textViewFail);

        EditText login = findViewById(R.id.login_text);
        EditText password = findViewById(R.id.password_text);

        buttonLogin.setOnClickListener((e) -> {
            String password_value = password.getText().toString();
            String login_value = login.getText().toString();
            if(password_value.length() == 0 ||
                    login_value.length() == 0){
                return;
            }

            LoginPOST loginPOST = new LoginPOST();
            loginPOST.setPassword(password_value);
            loginPOST.setLogin(login_value);

            callAPI = APIClient.getClient().create(CallAPI.class);

            Call<JsonWebToken> call = callAPI.login(loginPOST);
            call.enqueue(new Callback<JsonWebToken>() {
                @Override
                public void onResponse(Call<JsonWebToken> call, Response<JsonWebToken> response) {
                    JsonWebToken token = response.body();
                    if(token != null){
                        Log.d("token", token.getToken());
                    }
                    else{
                        Log.d("token", "nothing shit");
                    }
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<JsonWebToken> call, Throwable t) {
                    call.cancel();
                }
            });

        });
        buttonRegister.setOnClickListener((e) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        });

        textViewFail.setOnClickListener((e) -> {
            Intent browserIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "80291111111"));
            startActivity(browserIntent);
        });
    }
}
