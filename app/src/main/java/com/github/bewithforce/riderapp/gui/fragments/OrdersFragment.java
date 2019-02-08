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
import android.widget.AdapterView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrdersListAdapter;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.tools.SessionTools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends ListFragment implements AdapterView.OnItemClickListener {
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
        startTimer(SessionTools.getToken(getActivity().getBaseContext()));
        return temp;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callAPI = APIClient.getClient().create(CallAPI.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Order>orders = readFromFile();
        if(orders != null) {
            OrdersListAdapter adapter = new OrdersListAdapter(orders, mView.getContext());
            getListView().setAdapter(adapter);
            getListView().setOnItemClickListener(OrdersFragment.this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                                        getListView().setOnItemClickListener(OrdersFragment.this);
                                        writeToFile(response);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle arguments = new Bundle();
        arguments.putString("order_number", String.valueOf(id));
        orderFragment.setArguments(arguments);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment, orderFragment)
                .addToBackStack(null)
                .commit();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private void writeToFile(Response<List<Order>> response){
        try {
            FileOutputStream fos = getContext().openFileOutput("config.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(response.body());
            out.close();
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private List<Order> readFromFile(){
        try {
            FileInputStream fis = getContext().openFileInput("config.txt");
            ObjectInputStream is = new ObjectInputStream(fis);
            List<Order> list = (List<Order>)is.readObject();
            is.close();
            fis.close();
            Log.e("list size", String.valueOf(list.size()));
            return list;
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return null;
        }
    }
}
