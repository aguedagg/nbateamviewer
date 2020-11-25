package com.aguedagg.nbateamviewer.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aguedagg.nbateamviewer.R;
import com.aguedagg.nbateamviewer.data.Team;
import com.aguedagg.nbateamviewer.databinding.ItemListPlayerBinding;
import com.aguedagg.nbateamviewer.databinding.ItemListTeamBinding;
import com.aguedagg.nbateamviewer.ui.ListItemClickListener;
import com.aguedagg.nbateamviewer.util.Sort;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TeamRecyclerViewAdapter extends RecyclerView.Adapter<TeamRecyclerViewAdapter.TeamViewHolder> {

    private List<Team> mValues;
    private final boolean mTwoPane;

    final private ListItemClickListener mOnClickListener;

    public TeamRecyclerViewAdapter(ListItemClickListener listener, boolean twoPane) {
        mOnClickListener = listener;
        mTwoPane = twoPane;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TeamViewHolder holder, int position) {
        Team t = (Team) mValues.get(position);
        holder.mTeamName.setText(t.getFullName());
        holder.mWins.setText(t.getWins());
        holder.mLosses.setText(t.getLosses());
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    public void setTeamData(List<Team> teams) {
        mValues = teams;
        notifyDataSetChanged();
    }

    public void sortData(Sort type) {
        if (type == Sort.WINS) {
            Collections.sort(mValues, Team.TeamWinsComparator);
        } else if (type == Sort.LOSSES) {
            Collections.sort(mValues, Team.TeamLossesComparator);
        } else if (type == Sort.ALPHABETICAL) {
            Collections.sort(mValues, Team.TeamAZComparator);
        }
        notifyDataSetChanged();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTeamName;
        private TextView mWins;
        private TextView mLosses;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            mTeamName= (TextView) itemView.findViewById(R.id.team_name);
            mWins = (TextView) itemView.findViewById(R.id.wins);
            mLosses = (TextView) itemView.findViewById(R.id.losses);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Team t = (Team) mValues.get(clickedPosition);
            mOnClickListener.onTeamItemClick(t);
        }
    }

}
