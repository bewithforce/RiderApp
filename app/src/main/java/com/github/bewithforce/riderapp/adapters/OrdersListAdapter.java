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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersListAdapter extends BaseAdapter implements ListAdapter {

    private static class ViewHolder {
        private TextView number;
        private TextView time;
        private TextView address;
        private TextView status;
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
        if (view == null) {
            view = inflater.inflate(R.layout.orders_list_element, parent, false);
            holder = new ViewHolder();
            holder.number = view.findViewById(R.id.order_number_orders_fragment);
            holder.address = view.findViewById(R.id.address_orders_fragment);
            holder.time = view.findViewById(R.id.waiting_time_orders_fragment);
            holder.status = view.findViewById(R.id.order_status_orders_fragment);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        int status = list.get(position).getStatus();
        holder.number.setText("№" + String.valueOf(list.get(position).getId()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        Date now = Calendar.getInstance().getTime();
        StringBuilder time = new StringBuilder();
        long diff = 0, minutes, hours;
        Date date = null;
        boolean fail = false;
        switch (status) {
            case 0:
            case 2:
            case 3:
                holder.status.setText("Ресторан");
                holder.address.setText(list.get(position).getRestaurant_address());

                try {
                    date = format.parse(list.get(position).getRestaurant_arrival_time());
                } catch (Exception e) {
                    Log.e("veeeeListAdapter", e.getMessage());
                    fail = true;
                }
                break;
            case 1:
            case 4:
            case 5:
                holder.address.setText(list.get(position).getDelivery_address());
                holder.status.setText("Клиент");
                try {
                    date = format.parse(list.get(position).getCustomer_arrival_time());
                } catch (Exception e) {
                    Log.e("veeeeListAdapter", e.getMessage());
                    fail = true;
                }
                break;
            default:
                view.setTag(holder);
                return view;
        }
        if(!fail) {
            diff = date.getTime() - now.getTime();
            hours = diff / (1000 * 60 * 60) % 24;
            minutes = diff / (1000 * 60) % 60;
            if (diff > 0) {
                if (hours != 0) {
                    time.append(hours);
                    if (hours == 1) {
                        time.append(" час ");
                    } else if (hours >= 5) {
                        time.append(" часов ");
                    } else {
                        time.append(" часа ");
                    }
                }
                if (minutes != 0) {
                    time.append(minutes);
                    if (minutes == 1) {
                        time.append(" минута");
                    } else if (minutes < 5) {
                        time.append(" минуты");
                    } else {
                        time.append(" минут");
                    }
                } else {
                    time.append("<1 минуты");
                }
            } else {
                time.append("<1 минуты");
            }
            holder.time.setText(time);
        }
        view.setTag(holder);
        return view;
    }
}
