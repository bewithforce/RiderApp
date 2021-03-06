package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Stat;
import com.github.bewithforce.riderapp.post.requestBeans.ReceivedStatus;
import com.github.bewithforce.riderapp.post.requestBeans.StatusToSend;
import com.github.bewithforce.riderapp.tools.SessionTools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stats_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView cash = getView().findViewById(R.id.cash_received);
        TextView orders_completed = getView().findViewById(R.id.orders_completed);
        CallAPI callAPI = APIClient.getClient().create(CallAPI.class);
        Switch swi = getActivity().findViewById(R.id.switch1);
        try{
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
            swi.setChecked(preferences.getBoolean("switchStats", false));
        } catch (Exception e){}

        String token = SessionTools.getToken(getActivity().getBaseContext());
        Call<Stat> statCall = callAPI.getStatiscs(token);
        statCall.enqueue(new Callback<Stat>() {
            @Override
            public void onResponse(Call<Stat> call, Response<Stat> response) {
                switch (response.code()) {
                    case 200:
                        try {
                            Stat stat = response.body();
                            cash.setText(String.format("%.2f BYN", stat.getReceivedMoney()));
                            orders_completed.setText(stat.getDeliveryOrders().toString());
                        } catch (Exception e) {
                            Log.e("veeeeStatsCallBodyFail", e.getLocalizedMessage());
                        }
                        break;
                    case 401:
                        Context context = getActivity().getBaseContext();
                        SessionTools.removeToken(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Stat> call, Throwable t) {
                Log.e("veeeeStatsCallFail", t.getLocalizedMessage());
                call.cancel();
            }
        });

        Call<ReceivedStatus> call = callAPI.getStatus(token);
        call.enqueue(new Callback<ReceivedStatus>() {
            @Override
            public void onResponse(Call<ReceivedStatus> call, Response<ReceivedStatus> response) {
                switch (response.code()) {
                    case 200:
                        try {
                            ReceivedStatus stat = response.body();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                            if (stat != null) {
                                Log.e("veeeeStatsCallBodyFail", Boolean.toString(stat.getStatus()));
                                if (!stat.getStatus()) {
                                    swi.setChecked(false);
                                    preferences.edit().putBoolean("switchStats", false).apply();
                                } else {
                                    swi.setChecked(true);
                                    preferences.edit().putBoolean("switchStats", true).apply();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("veeeeStatsCallBodyFail", e.getLocalizedMessage());
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
            public void onFailure(Call<ReceivedStatus> call, Throwable t) {
                Log.e("veeeeStatsCallFail", t.getLocalizedMessage());
                call.cancel();
            }
        });

        swi.setOnCheckedChangeListener((e, t) -> {
            StatusToSend status = new StatusToSend();
            if (t) {
                status.setStatus(1);
            } else {
                status.setStatus(0);
            }
            Call<Void> voidCall = callAPI.postStatus(token, status);
            voidCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    switch (response.code()) {
                        case 401:
                            SessionTools.removeToken(getActivity().getBaseContext());
                            Context context = getActivity().getBaseContext();
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("veeeeStatsCallFail", t.getLocalizedMessage());
                    call.cancel();
                }
            });
        });
    }
}
