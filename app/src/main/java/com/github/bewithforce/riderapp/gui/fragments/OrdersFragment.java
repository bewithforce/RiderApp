package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrdersListAdapter;
import com.github.bewithforce.riderapp.gui.BaseActivity;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Orders;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends ListFragment {
    private View mView;
    private CallAPI callAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("veeeeee", "oncreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        View temp = inflater.inflate(R.layout.orders_list_fragment, container, false);
        this.mView = temp;
        getListView().setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.base_fragment, new OrdersFragment())
                    .commit();
        });
        return temp;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callAPI = APIClient.getClient().create(CallAPI.class);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("session_token", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        Call<Orders> call = callAPI.getOrders(token);
        call.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {

                switch (response.code()){
                    case 200:
                        try {
                            Orders orders = call.execute().body();
                            OrdersListAdapter adapter = new OrdersListAdapter(orders.getOrders(), mView.getContext());
                            getListView().setAdapter(adapter);
                        }
                        catch (Exception e){
                            Log.e("token", e.getLocalizedMessage());
                        }
                        break;
                    case 401:
                        Intent intent_finish = new Intent(OrdersFragment.this.getActivity(), LoginActivity.class);
                        startActivity(intent_finish);
                        OrdersFragment.this.getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
