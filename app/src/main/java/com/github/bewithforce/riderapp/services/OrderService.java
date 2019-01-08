package com.github.bewithforce.riderapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.bewithforce.riderapp.adapters.OrdersListAdapter;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.gui.fragments.OrdersFragment;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.tools.SessionTools;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
public class OrderService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        Call<List<Order>> call = callAPI.getOrders(token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                switch (response.code()){
                    case 200:
                        try {
                            List<Order> orders = response.body();
                            OrdersListAdapter adapter = new OrdersListAdapter(orders, mView.getContext());
                            getListView().setAdapter(adapter);
                        }
                        catch (Exception e){
                            Log.e("veeeeee", e.getMessage());
                        }
                        break;
                    case 401:
                        Intent intent_finish = new Intent(OrdersFragment.this.getActivity(), LoginActivity.class);
                        SessionTools.removeToken(getContext());
                        OrdersFragment.this.getActivity().finish();
                        startActivity(intent_finish);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("veeeeee", t.getLocalizedMessage());
                call.cancel();
            }
        });
    }
}
*/