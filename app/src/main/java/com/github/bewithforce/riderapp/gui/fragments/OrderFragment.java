package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrderTableAdapter;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.OrderWithDishes;
import com.github.bewithforce.riderapp.tools.SessionTools;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

        TextView back = getView().findViewById(R.id.backButton);
        back.setOnClickListener(e->{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.base_fragment, new OrdersFragment())
                    .commit();
        });


        Button restaurant = getView().findViewById(R.id.arrivedShopOrder);
        restaurant.setOnClickListener(e -> {
            Call<Void> call = callAPI.arrivedToRestaurant(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
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

                            switch (orderWithDishes.getStatus()){
                                case 0: case 2: case 3:
                                    restaurant.setClickable(true);
                                    customer.setClickable(false);
                                    break;
                                case 1: case 4: case 5:
                                    restaurant.setClickable(false);
                                    customer.setClickable(true);
                                    break;
                            }

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                            SimpleDateFormat createBeautifulDate = new SimpleDateFormat("HH:mm", Locale.US);

                            TextView order_number = getView().findViewById(R.id.orderOrder);
                            order_number.setText("Заказ №" + orderWithDishes.getId());

                            TextView restaurant = getView().findViewById(R.id.shopOrder);
                            restaurant.setText("Название не приходит");

                            TextView restaurant_address = getView().findViewById(R.id.address1Order);
                            restaurant_address.setText(orderWithDishes.getRestaurant_address());

                            Date restaurant_date = format.parse(orderWithDishes.getRestaurant_arrival_time());
                            TextView restaurant_time = getView().findViewById(R.id.arrivalTime1Order);
                            restaurant_time.setText(createBeautifulDate.format(restaurant_date));

                            TextView restaurant_phone = getView().findViewById(R.id.phone1Order);
                            if(orderWithDishes.getRestaurant_phone() != null) {
                                StringBuilder builder = new StringBuilder();
                                if (orderWithDishes.getRestaurant_phone().charAt(0) != '+') {
                                    builder.append('+');
                                }
                                builder.append(orderWithDishes.getRestaurant_phone());
                                restaurant_phone.setText(builder.toString());
                            }else{
                                restaurant_phone.setText(orderWithDishes.getRestaurant_phone());
                            }

                            TextView restaurant_price = getView().findViewById(R.id.price_order);
                            restaurant_price.setText(Double.toString(orderWithDishes.getFull_order_sum()) + " BYN");

                            TextView customer_address = getView().findViewById(R.id.address2Order);
                            customer_address.setText(orderWithDishes.getDelivery_address());

                            TextView customer_phone = getView().findViewById(R.id.phone2Order);
                            if(orderWithDishes.getCustomer_phone() != null) {
                                StringBuilder builder = new StringBuilder();
                                if (orderWithDishes.getCustomer_phone().charAt(0) != '+') {
                                    builder.append('+');
                                }
                                builder.append(orderWithDishes.getCustomer_phone());
                                customer_phone.setText(builder.toString());
                            }else{
                                customer_phone.setText(orderWithDishes.getCustomer_phone());
                            }

                            Date customer_date = format.parse(orderWithDishes.getCustomer_arrival_time());
                            TextView customer_time = getView().findViewById(R.id.arrivalTime2Order);
                            customer_time.setText(createBeautifulDate.format(customer_date));

                            TextView customer_price = getView().findViewById(R.id.to_pay);
                            customer_price.setText(Double.toString(orderWithDishes.getOrder_sum()) + " BYN");

                            View includedView = getView().findViewById(R.id.table_of_dishes);
                            OrderTableAdapter adapter = new OrderTableAdapter(orderWithDishes.getDishes(),
                                    mView.getContext());
                            ListView mRecycler = includedView.findViewById(R.id.recyclerView_dishes);
                            mRecycler.setAdapter(adapter);
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", e.getLocalizedMessage());
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
            public void onFailure(Call<OrderWithDishes> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
