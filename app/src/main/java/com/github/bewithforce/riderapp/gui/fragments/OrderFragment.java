package com.github.bewithforce.riderapp.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrderTableAdapter;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.OrderWithDishes;
import com.github.bewithforce.riderapp.tools.SessionTools;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        this.mView = view;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Integer id = Integer.valueOf(getArguments().getString("order_number"));
        super.onActivityCreated(savedInstanceState);

        CallAPI callAPI = APIClient.getClient().create(CallAPI.class);
        String token = SessionTools.getToken(getActivity().getBaseContext());

        Button restaurant = getView().findViewById(R.id.arrivedShopOrder);
        restaurant.setOnClickListener(e -> {
            Call<Void> call = callAPI.arrivedToRestaurant(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    SessionTools.endSession(getActivity().getBaseContext());
                }
            });
        });

        Button customer = getView().findViewById(R.id.arrivedClientOrder);
        customer.setOnClickListener(e -> {
            Call<Void> call = callAPI.arrivedToCustomer(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    SessionTools.endSession(getActivity().getBaseContext());
                }
            });
        });


        Call<OrderWithDishes> call = callAPI.getDetailedOrder(token, id);
        call.enqueue(new Callback<OrderWithDishes>() {
            @Override
            public void onResponse(Call<OrderWithDishes> call, Response<OrderWithDishes> response) {

                switch (response.code()) {
                    case 200:
                        try {
                            OrderWithDishes orderWithDishes = response.body();

                            TextView order_number = getView().findViewById(R.id.orderOrder);
                            order_number.setText("Заказ №" + orderWithDishes.getId());

                            TextView restaurant = getView().findViewById(R.id.shopOrder);
                            restaurant.setText("Название не приходит");

                            TextView restaurant_address = getView().findViewById(R.id.address1Order);
                            restaurant_address.setText(orderWithDishes.getRestaurant_address());

                            TextView restaurant_time = getView().findViewById(R.id.arrivalTime1Order);
                            restaurant_time.setText(orderWithDishes.getRestaurant_arrival_time());

                            TextView restaurant_phone = getView().findViewById(R.id.phone1Order);
                            restaurant_phone.setText(orderWithDishes.getRestaurant_phone());


                            TextView restaurant_price = getView().findViewById(R.id.price_order);
                            restaurant_price.setText(Double.toString(orderWithDishes.getFull_order_sum()));

                            TextView customer_address = getView().findViewById(R.id.address2Order);
                            customer_address.setText(orderWithDishes.getDelivery_address());

                            TextView customer_phone = getView().findViewById(R.id.phone2Order);
                            customer_phone.setText(orderWithDishes.getCustomer_phone());

                            TextView customer_time = getView().findViewById(R.id.arrivalTime2Order);
                            customer_time.setText(orderWithDishes.getCustomer_arrival_time());

                            TextView customer_price = getView().findViewById(R.id.to_pay);
                            customer_price.setText(Double.toString(orderWithDishes.getOrder_sum()));

                            View includedView = getView().findViewById(R.id.table_of_dishes);
                            OrderTableAdapter adapter = new OrderTableAdapter(orderWithDishes.getDishes(),
                                    mView.getContext());
                            ListView mRecycler = includedView.findViewById(R.id.recyclerView_dishes);
                            mRecycler.setAdapter(adapter);

                            Log.e("veeeee", "order text updated");
                            Log.e("veeeee", Integer.toString(adapter.getCount()));

                        } catch (Exception e) {
                            Log.e("token", e.getLocalizedMessage());
                        }
                        break;
                    case 401:
                        SessionTools.endSession(getActivity().getBaseContext());
                }
            }

            @Override
            public void onFailure(Call<OrderWithDishes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
