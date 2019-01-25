package com.github.bewithforce.riderapp.gui.LogInActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
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
import com.github.bewithforce.riderapp.post.requestBeans.JsonWebToken;
import com.github.bewithforce.riderapp.post.requestBeans.Login;
import com.github.bewithforce.riderapp.tools.SessionTools;

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
        EditText loginInput = findViewById(R.id.login_text);
        EditText passwordInput = findViewById(R.id.password_text);
        TextInputLayout loginLayout = findViewById(R.id.passwordInputLayout);

        buttonLogin.setOnClickListener((e) -> {
            buttonLogin.setClickable(false);
            loginLayout.setError(null);
            String password_value = passwordInput.getText().toString();


            String login_value = loginInput.getText().toString();
            if (password_value.length() == 0 ||
                    login_value.length() == 0) {
                return;
            }

            Login login = new Login();
            login.setPass(password_value);
            login.setName(login_value);

            callAPI = APIClient.getClient().create(CallAPI.class);

            Call<JsonWebToken> call = callAPI.login(login);
            call.enqueue(new Callback<JsonWebToken>() {
                @Override
                public void onResponse(Call<JsonWebToken> call, Response<JsonWebToken> response) {
                    JsonWebToken jsonWebToken = response.body();
                    String token;
                    if (jsonWebToken != null) {
                        try {
                            if (jsonWebToken.getToken() == null) {
                                throw new Exception("bad news");
                            }
                            token = "Bearer " + jsonWebToken.getToken();
                        } catch (Exception e) {
                            Log.e("veeeeTokenError", e.getLocalizedMessage());
                            loginLayout.setError("что-то не так");
                            return;
                        }
                        Log.d("veeeeNewToken", token);
                        SessionTools.addToken(getBaseContext(), token);
                    } else {
                        loginLayout.setError("что-то не так");
                        try {
                            Log.d("veeeeTokenErrorBody", response.errorBody().string());
                        } catch (Exception e1) {
                            Log.e("veeeeTokenVeryErrorBody", e1.getLocalizedMessage());
                        }
                    }
                    switch (response.code()) {
                        case 200:
                            Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 403:
                            loginLayout.setError("нет допуска");
                        case 404:
                            loginLayout.setError("неправильный логин или пароль");
                    }
                }

                @Override
                public void onFailure(Call<JsonWebToken> call, Throwable t) {
                    loginLayout.setError("что-то не так");
                    call.cancel();
                }
            });

            buttonLogin.setClickable(true);
        });
        buttonRegister.setOnClickListener((e) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://foodtech.by/rabota/courier"));
            startActivity(browserIntent);
        });
    }
}
