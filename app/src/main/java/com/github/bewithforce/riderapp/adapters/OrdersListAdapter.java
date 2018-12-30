package com.github.bewithforce.riderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.BaseActivity;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.gui.fragments.OrdersFragment;
import com.github.bewithforce.riderapp.post.requestBeans.Order;

import java.util.ArrayList;
import java.util.List;

public class OrdersListAdapter extends BaseAdapter implements ListAdapter {

    private List<Order> list;
    private Context context;


    public OrdersListAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.orders_list_fragment, null);
        }

        int status = list.get(position).getStatus();

        TextView number = view.findViewById(R.id.order_number_orders_fragment);
        number.setText(list.get(position).getCustomerAdress());

        TextView time = view.findViewById(R.id.waiting_time_orders_fragment);
        TextView address = view.findViewById(R.id.address_orders_fragment);
        switch (status) {
            case 1:
                address.setText(list.get(position).getRestaurantAdress());
                time.setText(list.get(position).getRestaurantArivalTime());
                break;
            case 2:
                address.setText(list.get(position).getCustomerAdress());
                time.setText(list.get(position).getCustomerArivalTime());
                break;
        }

        TextView details = view.findViewById(R.id.details_orders_fragment);
        details.setText("ПОДРОБНЕЕ");
        //Handle TextView and display string from your list

        return view;
    }
}
