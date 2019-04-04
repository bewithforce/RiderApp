package com.github.bewithforce.riderapp.gui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrdersListAdapter;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.tools.FileTools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrdersFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private View mView;
    private Timer timer;
    private TimerTask task;
    private List<Order> oldOrders;
    private Handler mTimerHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View temp = inflater.inflate(R.layout.orders_list_fragment, container, false);
        this.mView = temp;
        startTimer();
        return temp;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(oldOrders == null) {
            oldOrders = FileTools.readFromFile(mView.getContext());
        }
        if(oldOrders != null) {
            OrdersListAdapter adapter = new OrdersListAdapter(oldOrders, mView.getContext());
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

    private void startTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.post(() -> {
                    oldOrders = FileTools.readFromFile(mView.getContext());
                    if (oldOrders != null) {
                        OrdersListAdapter adapter = new OrdersListAdapter(oldOrders, mView.getContext());
                        try {
                            getListView().setAdapter(adapter);
                            getListView().setOnItemClickListener(OrdersFragment.this);
                        } catch (Exception e){}
                    }
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
}
