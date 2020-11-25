package com.aguedagg.nbateamviewer.service;

import android.provider.SyncStateContract;

import com.aguedagg.nbateamviewer.data.Team;
import com.aguedagg.nbateamviewer.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET(Constants.TEAM_DATA_ENDPOINT)
    Call<List<Team>> getTeams();

}
