package com.aguedagg.nbateamviewer;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aguedagg.nbateamviewer.data.Team;
import com.aguedagg.nbateamviewer.databinding.FragmentDetailBinding;
import com.aguedagg.nbateamviewer.ui.adapters.PlayersRecyclerViewAdapter;
import com.aguedagg.nbateamviewer.util.Constants;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
public class TeamDetailFragment extends Fragment {

    private Team mItem;
    private FragmentDetailBinding binding;

    public TeamDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.ARG_TEAM)) {
            mItem = (Team) getArguments().getParcelable(Constants.ARG_TEAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        PlayersRecyclerViewAdapter recyclerAdapter = new PlayersRecyclerViewAdapter(mItem.getPlayers());
        binding.teamDetailRecyclerView.setAdapter(recyclerAdapter);
        binding.setTeam(mItem);

        return binding.getRoot();
    }
}