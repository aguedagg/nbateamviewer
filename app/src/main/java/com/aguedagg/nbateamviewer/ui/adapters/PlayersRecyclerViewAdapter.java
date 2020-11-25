package com.aguedagg.nbateamviewer.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aguedagg.nbateamviewer.R;
import com.aguedagg.nbateamviewer.data.Player;
import com.aguedagg.nbateamviewer.databinding.ItemListPlayerBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PlayersRecyclerViewAdapter extends RecyclerView.Adapter<PlayersRecyclerViewAdapter.PlayerViewHolder> {

    private List<Player> mValues = new ArrayList<>();

    public PlayersRecyclerViewAdapter(List<Player> players) {
        mValues = players;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemListPlayerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_list_player, parent, false);
        return new PlayerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position) {

        holder.binding.setPlayer(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        private final ItemListPlayerBinding binding;

        public PlayerViewHolder(final ItemListPlayerBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

}
