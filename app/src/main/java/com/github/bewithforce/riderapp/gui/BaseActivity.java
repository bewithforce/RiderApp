package com.github.bewithforce.riderapp.gui;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        BottomNavigationView view = findViewById(R.id.navigation);
        view.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_orders:
                    return true;
                case R.id.action_history:
                    return true;
                case R.id.action_stats:
                    return true;
                case R.id.action_exit:
                    finish();
                    return true;
            }
            return false;

        });

    }
}
