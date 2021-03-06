package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.adapters.OrderTableAdapter;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.post.requestBeans.OrderWithDishes;
import com.github.bewithforce.riderapp.post.requestBeans.Phone;
import com.github.bewithforce.riderapp.tools.LocationTools;
import com.github.bewithforce.riderapp.tools.SessionTools;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private View mView;
    private Button customer;
    private CallAPI callAPI;
    private Button restaurant;
    private String token;
    private TextView back;
    private Integer id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        this.mView = view;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        id = Integer.valueOf(getArguments().getString("order_number"));
        callAPI = APIClient.getClient().create(CallAPI.class);
        token = SessionTools.getToken(getActivity().getBaseContext());
        restaurant = getView().findViewById(R.id.arrivedShopOrder);
        customer = getView().findViewById(R.id.arrivedClientOrder);
        back = getView().findViewById(R.id.backButton);


        back.setOnClickListener(e -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment, new OrdersFragment())
                .commit()
        );
        restaurant.setOnClickListener(e -> {
            Call<Void> smallCall = callAPI.arrivedToRestaurant(token, id);
            smallCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> litCall, Response<Void> response) {
                    if(response.isSuccessful()) {
                        restaurant.setBackground(getResources().getDrawable(R.drawable.drawable_rectangle_fill_gray));
                        restaurant.setClickable(false);
                        customer.setBackground(getResources().getDrawable(R.drawable.drawable_rectangle_fill_yellow));
                        customer.setClickable(true);
                    }
                }

                @Override
                public void onFailure(Call<Void> litCall, Throwable t) {
                    litCall.cancel();
                }
            });
        });
        customer.setOnClickListener(e -> {
            Call<Void> smallCall = callAPI.arrivedToCustomer(token, id);
            smallCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> litCall, Response<Void> response) {
                    if(response.isSuccessful()) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.base_fragment, new OrdersFragment())
                                .commit();
                    }
                }

                @Override
                public void onFailure(Call<Void> litCall, Throwable t) {
                    litCall.cancel();
                }
            });
        });

        Button sayProblemsOrder = getView().findViewById(R.id.sayProblemsOrder);
        sayProblemsOrder.setOnClickListener(e -> {
            Call<Phone> problemsCall = callAPI.getPhone(token);
            problemsCall.enqueue(new Callback<Phone>() {
                @Override
                public void onResponse(Call<Phone> call, Response<Phone> response) {
                    Intent browserIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + response.body().getPhone()));
                    startActivity(browserIntent);
                }

                @Override
                public void onFailure(Call<Phone> call, Throwable t) {

                }
            });
        });
        makeCall();
    }

    private void makeCall() {
        Call<OrderWithDishes> call = callAPI.getDetailedOrder(token, id);
        call.enqueue(new Callback<OrderWithDishes>() {
            @Override
            public void onResponse(Call<OrderWithDishes> call, Response<OrderWithDishes> response) {
                switch (response.code()) {
                    case 200:
                        OrderWithDishes orderWithDishes = response.body();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                        SimpleDateFormat createBeautifulDate = new SimpleDateFormat("HH:mm", Locale.US);

                        try {
                            switch (orderWithDishes.getStatus()) {
                                case 0:
                                case 2:
                                case 3:
                                    restaurant.setBackground(getResources().getDrawable(R.drawable.drawable_rectangle_fill_yellow));
                                    restaurant.setClickable(true);
                                    break;
                                case 1:
                                case 4:
                                case 5:
                                    customer.setBackground(getResources().getDrawable(R.drawable.drawable_rectangle_fill_yellow));
                                    customer.setClickable(true);
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView order_number = getView().findViewById(R.id.orderOrder);
                            order_number.setText("Заказ №" + orderWithDishes.getId());
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView restaurant = getView().findViewById(R.id.shopOrder);
                            restaurant.setText(orderWithDishes.getRestaurant_name());
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView restaurant_address = getView().findViewById(R.id.address1Order);
                            restaurant_address.setText(orderWithDishes.getRestaurant_address());
                            restaurant_address.setOnClickListener(e -> {
                                StringBuilder line = new StringBuilder("yandexnavi://build_route_on_map");
                                CourierLocation location = LocationTools.getToken(mView.getContext());
                                line.append("?lat_from=")
                                        .append(location.getLatitude())
                                        .append("&lon_from=")
                                        .append(location.getLongitude())
                                        .append("&lat_to=")
                                        .append(orderWithDishes.getRestaurant_latitude())
                                        .append("&lon_to=")
                                        .append(orderWithDishes.getRestaurant_longitude());
                                Uri uri = Uri.parse(line.toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage("ru.yandex.yandexnavi");
                                PackageManager packageManager = getActivity().getPackageManager();
                                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                                boolean isIntentSafe = activities.size() > 0;
                                if (isIntentSafe) {
                                    startActivity(intent);
                                } else {

                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://details?id=ru.yandex.yandexnavi"));
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            if (orderWithDishes.getRestaurant_arrival_time() != null) {
                                Date restaurant_date = format.parse(orderWithDishes.getRestaurant_arrival_time());
                                TextView restaurant_time = getView().findViewById(R.id.arrivalTime1Order);
                                restaurant_time.setText(createBeautifulDate.format(restaurant_date));
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView restaurant_phone = getView().findViewById(R.id.phone1Order);
                            if (orderWithDishes.getRestaurant_phone() != null) {
                                if (orderWithDishes.getRestaurant_phone().length() != 0) {
                                    StringBuilder builder = new StringBuilder();
                                    if (orderWithDishes.getRestaurant_phone().charAt(0) != '+') {
                                        builder.append('+');
                                    }
                                    builder.append(orderWithDishes.getRestaurant_phone());
                                    restaurant_phone.setText(builder.toString());
                                    restaurant_phone.setOnClickListener(e -> {
                                        Intent browserIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + builder.toString()));
                                        startActivity(browserIntent);
                                    });
                                }
                            } else {
                                restaurant_phone.setText(orderWithDishes.getRestaurant_phone());
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView restaurant_price = getView().findViewById(R.id.price_order);
                            if (orderWithDishes.getOrder_sum() != null) {
                                restaurant_price.setText(String.format("%.2f BYN", orderWithDishes.getFull_order_sum()));
                            } else {
                                restaurant_price.setText("0 BYN");
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView customer_address = getView().findViewById(R.id.address2Order);
                            customer_address.setText(orderWithDishes.getDelivery_address());
                            customer_address.setOnClickListener(e -> {
                                StringBuilder line = new StringBuilder("yandexnavi://build_route_on_map");
                                CourierLocation location = LocationTools.getToken(mView.getContext());
                                line.append("?lat_from=")
                                        .append(location.getLatitude())
                                        .append("&lon_from=")
                                        .append(location.getLongitude())
                                        .append("&lat_to=")
                                        .append(orderWithDishes.getDelivery_latitude())
                                        .append("&lon_to=")
                                        .append(orderWithDishes.getDelivery_longitude());
                                Uri uri = Uri.parse(line.toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage("ru.yandex.yandexnavi");
                                PackageManager packageManager = getActivity().getPackageManager();
                                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                                boolean isIntentSafe = activities.size() > 0;
                                if (isIntentSafe) {
                                    startActivity(intent);
                                } else {

                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://details?id=ru.yandex.yandexnavi"));
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView customer_phone = getView().findViewById(R.id.phone2Order);
                            if (orderWithDishes.getCustomer_phone() != null) {
                                if (orderWithDishes.getCustomer_phone().length() != 0) {
                                    StringBuilder builder = new StringBuilder();
                                    if (orderWithDishes.getCustomer_phone().charAt(0) != '+') {
                                        builder.append('+');
                                    }
                                    builder.append(orderWithDishes.getCustomer_phone());
                                    customer_phone.setText(builder.toString());
                                    customer_phone.setOnClickListener(e -> {
                                        Intent browserIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + builder.toString()));
                                        startActivity(browserIntent);
                                    });
                                }
                            } else {
                                customer_phone.setText(orderWithDishes.getCustomer_phone());
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            if (orderWithDishes.getCustomer_arrival_time() != null) {
                                Date customer_date = format.parse(orderWithDishes.getCustomer_arrival_time());
                                TextView customer_time = getView().findViewById(R.id.arrivalTime2Order);
                                customer_time.setText(createBeautifulDate.format(customer_date));
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView customer_price = getView().findViewById(R.id.to_pay);
                            if (orderWithDishes.getFull_order_sum() != null) {
                                customer_price.setText(String.format("%.2f BYN", orderWithDishes.getOrder_sum()));
                            } else {
                                customer_price.setText("0 BYN");
                            }
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            TextView comment = getView().findViewById(R.id.comment);
                            comment.setText(orderWithDishes.getDelivery_comment());
                        }catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

                        }

                        try {
                            View includedView = getView().findViewById(R.id.table_of_dishes);
                            OrderTableAdapter adapter = new OrderTableAdapter(orderWithDishes.getDishes(),
                                    mView.getContext());
                            ListView mRecycler = includedView.findViewById(R.id.recyclerView_dishes);
                            mRecycler.setAdapter(adapter);
                            View view = getView().findViewById(R.id.view);
                            mRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    int height = 60;
                                    for (int i = 0; i < mRecycler.getCount(); i++) {
                                        View view = mRecycler.getChildAt(i);
                                        if (view != null) {
                                            height += view.getHeight();
                                        } else {
                                            height += adapter.getView(i, view, mRecycler).getHeight();
                                        }
                                    }
                                    height += mRecycler.getDividerHeight() * (mRecycler.getCount() - 1);
                                    view.setLayoutParams(new ConstraintLayout.LayoutParams(view.getWidth(), height));
                                    getView().requestLayout();
                                    mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });
                        } catch (Exception e) {
                            Log.e("veeeeOrderFragmentFail", Log.getStackTraceString(e));

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
