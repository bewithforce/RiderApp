package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.post.requestBeans.OrderWithDishes;
import com.github.bewithforce.riderapp.post.requestBeans.Stat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private int id;

    public OrderFragment(){
        super();
    }

    public OrderFragment(int id){
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView cash = getView().findViewById(R.id.cash_received);
        TextView orders_completed = getView().findViewById(R.id.orders_completed);
        CallAPI callAPI = APIClient.getClient().create(CallAPI.class);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("session_token", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        Call<OrderWithDishes> call = callAPI.getDetailedOrder(token, id);
        call.enqueue(new Callback<OrderWithDishes>() {
            @Override
            public void onResponse(Call<OrderWithDishes> call, Response<OrderWithDishes> response) {

                switch (response.code()){
                    case 200:
                        try {
                            OrderWithDishes orderWithDishes = call.execute().body();

                        }
                        catch (Exception e){
                            Log.e("token", e.getLocalizedMessage());
                        }
                        break;
                    case 401:
                        Intent intent_finish = new Intent(OrderFragment.this.getActivity(), LoginActivity.class);
                        startActivity(intent_finish);
                        OrderFragment.this.getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<OrderWithDishes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
