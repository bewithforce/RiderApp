package com.github.bewithforce.riderapp.gui.LogInActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.OrdersActivity;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.ServiceGenerator;
import com.github.bewithforce.riderapp.post.requests.LoginPOST;


import java.io.IOException;

import butterknife.BindView;
import io.reactivex.Observable;
import retrofit2.Call;

import static com.github.bewithforce.riderapp.post.ServiceGenerator.createService;

public class LoginActivity extends AppCompatActivity {

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

            CallAPI userService = ServiceGenerator.createService(CallAPI.class);
            Call<Void> call = userService.login(loginPOST);
            try {
                Void void_value = call.execute().body();
            }
            catch (Exception exception){

            }

            Intent browserIntent = new Intent(this, OrdersActivity.class);
            startActivity(browserIntent);
            finish();
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
