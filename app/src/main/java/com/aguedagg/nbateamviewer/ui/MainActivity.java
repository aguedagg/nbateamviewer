package com.aguedagg.nbateamviewer.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aguedagg.nbateamviewer.ApiClient;
import com.aguedagg.nbateamviewer.DetailActivity;
import com.aguedagg.nbateamviewer.R;
import com.aguedagg.nbateamviewer.TeamDetailFragment;
import com.aguedagg.nbateamviewer.data.Team;
import com.aguedagg.nbateamviewer.service.ApiInterface;
import com.aguedagg.nbateamviewer.ui.adapters.TeamRecyclerViewAdapter;
import com.aguedagg.nbateamviewer.util.Constants;
import com.aguedagg.nbateamviewer.util.Sort;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    final String TAG = this.getClass().getSimpleName();
    final static String savedState = "SORT_TYPE";

    private boolean mTwoPane = false;
    private Sort mSort = Sort.ALPHABETICAL;
    private RecyclerView recyclerView;
    private TeamRecyclerViewAdapter recyclerAdapter;
    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(savedState)) {
            int param =  savedInstanceState.getInt(savedState);
            switch(param) {
                case 1:
                    mSort = Sort.ALPHABETICAL;
                    break;
                case 2:
                    mSort = Sort.WINS;
                    break;
                case 3:
                    mSort = Sort.LOSSES;
                    break;
                default:
                    mSort = Sort.ALPHABETICAL;
            }
        }

        initViews();
    }

    private void initViews(){
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    private void initRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.team_detail_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new TeamRecyclerViewAdapter(this, mTwoPane);
        recyclerView.setAdapter(recyclerAdapter);
        loadData();
    }

    private void loadData() {

        apiService = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        Call<List<Team>> call = apiService.getTeams();
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {

                if(response.raw().networkResponse() != null){
                    Log.d(TAG, "onResponse: response is from NETWORK...");
                }
                else if(response.raw().cacheResponse() != null
                        && response.raw().networkResponse() == null){
                    Log.d(TAG, "onResponse: response is from CACHE...");
                }

                recyclerAdapter.setTeamData(response.body());
                recyclerAdapter.sortData(mSort);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                Log.d(TAG,"Response = "+t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_az:
                recyclerAdapter.sortData(Sort.ALPHABETICAL);
                return true;
            case R.id.sort_by_wins:
                recyclerAdapter.sortData(Sort.WINS);
                return true;
            case R.id.sort_by_losses:
                recyclerAdapter.sortData(Sort.LOSSES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchDetailActivity(Team team) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.ARG_TEAM, team);
        startActivity(intent);
    }

    @Override
    public void onTeamItemClick(Team team) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.ARG_TEAM, team);
            TeamDetailFragment fragment = new TeamDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            launchDetailActivity(team);
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(savedState, mSort.ordinal());
    }
}