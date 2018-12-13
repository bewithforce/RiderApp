package com.github.bewithforce.riderapp.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.bewithforce.riderapp.R;

    public class OrdersFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orders_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int a[] = {1, 2, 3, 4, 6};
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.orders_list_element);
        setListAdapter(adapter);
    }
}
