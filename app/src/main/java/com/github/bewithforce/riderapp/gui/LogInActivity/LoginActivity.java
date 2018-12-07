package com.github.bewithforce.riderapp.gui.LogInActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.OrdersActivity;
import com.github.bewithforce.riderapp.post.RetrofitClient;

import butterknife.BindView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewFail = findViewById(R.id.textViewFail);

        buttonLogin.setOnClickListener((e) -> {
            /*RetrofitClient.getClient().*/
            Intent browserIntent = new Intent(this, OrdersActivity.class);
            startActivity(browserIntent);
            finish();
        });
        buttonRegister.setOnClickListener((e) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        });

        textViewFail.setOnClickListener((e) -> {
            Intent browserIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "80291111111"));
            startActivity(browserIntent);
        });
    }
}
