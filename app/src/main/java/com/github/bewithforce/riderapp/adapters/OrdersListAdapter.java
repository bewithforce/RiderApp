package com.github.bewithforce.riderapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.post.requestBeans.Order;

import java.util.List;

public class OrdersListAdapter extends BaseAdapter implements ListAdapter {

    private static class ViewHolder {
        private TextView number;
        private TextView time;
        private TextView address;
        private TextView details;
    }

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
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.orders_list_element, parent, false);
            holder = new ViewHolder();
            holder.number = view.findViewById(R.id.order_number_orders_fragment);
            holder.address = view.findViewById(R.id.address_orders_fragment);
            holder.time = view.findViewById(R.id.waiting_time_orders_fragment);
            holder.details = view.findViewById(R.id.details_orders_fragment);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        int status = list.get(position).getStatus();
        holder.number.setText(String.valueOf(list.get(position).getId()));
        switch (status) {
            case 1:
                holder.address.setText(list.get(position).getRestaurant_address());
                holder.time.setText(list.get(position).getRestaurant_arrival_time());
                break;
            case 2:
                holder.address.setText(list.get(position).getDelivery_address());
                holder.time.setText(list.get(position).getCustomer_arrival_time());
                break;
        }
        holder.details.setText("ПОДРОБНЕЕ");

        Log.e("veeeee", "list adapted");
        return view;
    }
}
