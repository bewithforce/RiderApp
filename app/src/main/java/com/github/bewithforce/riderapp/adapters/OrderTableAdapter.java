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
import com.github.bewithforce.riderapp.post.requestBeans.Dish;

import java.util.List;

public class OrderTableAdapter extends BaseAdapter implements ListAdapter {

    private static class ViewHolder {
        private TextView number;
        private TextView dish;
        private TextView amount;
    }

    private List<Dish> list;
    private Context context;


    public OrderTableAdapter(List<Dish> list, Context context) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.table_element, parent, false);
            holder = new ViewHolder();
            holder.number = view.findViewById(R.id.number_table_element);
            holder.dish = view.findViewById(R.id.dish_table_element);
            holder.amount = view.findViewById(R.id.amount_table_element);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        holder.number.setText(Integer.toHexString(position + 1));
        holder.dish.setText(list.get(position).getName());
        holder.amount.setText(list.get(position).getCount());
        view.setTag(holder);
        return view;
    }
}
