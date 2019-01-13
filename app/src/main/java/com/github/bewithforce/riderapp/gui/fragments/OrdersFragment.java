package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrdersListAdapter;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.tools.SessionTools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends ListFragment {
    private View mView;
    private CallAPI callAPI;
    private Timer timer;
    private TimerTask task;
    private Handler mTimerHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View temp = inflater.inflate(R.layout.orders_list_fragment, container, false);
        this.mView = temp;
        return temp;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callAPI = APIClient.getClient().create(CallAPI.class);
        startTimer(SessionTools.getToken(getActivity().getBaseContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("veeeeOrdersFragmentDie","destroyed");
        stopTimer();
    }

    private void startTimer(String token) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.post(() -> {
                    Call<List<Order>> call = callAPI.getOrders(token);
                    call.enqueue(new Callback<List<Order>>() {
                        @Override
                        public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                            switch (response.code()) {
                                case 200:
                                    try {
                                        List<Order> orders = response.body();
                                        OrdersListAdapter adapter = new OrdersListAdapter(orders, mView.getContext());
                                        getListView().setAdapter(adapter);
                                    } catch (Exception e) {
                                        Log.e("veeeeCallOrdersBodyFail", e.getMessage());
                                    }
                                    break;
                                case 401:
                                    SessionTools.removeToken(getActivity().getBaseContext());
                                    Context context = getActivity().getBaseContext();
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                    getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Order>> call, Throwable t) {
                            Log.e("veeeeCallOrdersFail", t.getLocalizedMessage());
                            call.cancel();
                        }
                    });
                });
            }
        };
        timer.schedule(task, 1, 10000);
    }


    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
