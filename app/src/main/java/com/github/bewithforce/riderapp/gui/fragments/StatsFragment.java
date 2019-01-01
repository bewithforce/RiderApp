package com.github.bewithforce.riderapp.gui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.Stat;
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

        String token = SessionTools.getToken(getActivity().getBaseContext());
        Call<Stat> call = callAPI.getStatiscs(token);
        call.enqueue(new Callback<Stat>() {
            @Override
            public void onResponse(Call<Stat> call, Response<Stat> response) {

                switch (response.code()){
                    case 200:
                        try {
                            Stat stat = call.execute().body();
                            cash.setText(stat.getReceivedMoney().toString());
                            orders_completed.setText(stat.getDeliveryOrders());
                        }
                        catch (Exception e){
                            Log.e("token", e.getLocalizedMessage());
                        }
                        break;
                    case 401:
                        Intent intent_finish = new Intent(StatsFragment.this.getActivity(), LoginActivity.class);
                        startActivity(intent_finish);
                        StatsFragment.this.getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Stat> call, Throwable t) {
                call.cancel();
            }
        });
        Log.e("veeeee", "And we change");
    }
}