package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrdersListAdapter;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.tools.SessionTools;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends ListFragment {
    private View mView;
    private CallAPI callAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("veeeeee", "oncreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        View temp = inflater.inflate(R.layout.orders_list_fragment, container, false);
        this.mView = temp;
        return temp;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callAPI = APIClient.getClient().create(CallAPI.class);
        getListView().setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.base_fragment, new OrdersFragment())
                    .commit();
        });
        String token = SessionTools.getToken(getActivity().getBaseContext());

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
