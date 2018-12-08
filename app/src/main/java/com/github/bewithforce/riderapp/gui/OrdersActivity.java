package com.github.bewithforce.riderapp.gui;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;

public class OrdersActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        textView = findViewById(R.id.textView3);
        textView.setText("Заказы");
        BottomNavigationView view = findViewById(R.id.navigation);
        view.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_orders:
                    textView.setText("Заказы");
                    return true;
                case R.id.action_history:
                    textView.setText("История");
                    return true;
                case R.id.action_stats:
                    textView.setText("Статистика");
                    return true;
                case R.id.action_exit:
                    finish();
                    return true;
            }
            return false;

        });

    }
}
