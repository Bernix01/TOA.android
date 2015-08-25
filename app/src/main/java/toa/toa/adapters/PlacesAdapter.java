/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.adapters;

/**
 * Created by Guillermo on 7/23/2015.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import toa.toa.Objects.MrPlace;
import toa.toa.R;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private final Context contexto;
    private final ArrayList<MrPlace> places;

    public PlacesAdapter(ArrayList<MrPlace> places, Context contexto) {
        this.places = places;
        this.contexto = contexto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_place, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MrPlace place = places.get(position);
        holder.nametxtv.setText(place.getName());
        holder.organizertxtv.setText(place.getAddress());
        holder.datetxtv.setText("Lun-Vier: " + place.getWeekTime() + " Sab-Dom: " + place.getWeekendTime());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView nametxtv;
        public final TextView organizertxtv;
        public final TextView datetxtv;
        public final TextView daytxtv;

        public ViewHolder(View itemView) {
            super(itemView);
            nametxtv = (TextView) itemView.findViewById(R.id.event_name_txtv);
            daytxtv = (TextView) itemView.findViewById(R.id.event_day_txtv);
            datetxtv = (TextView) itemView.findViewById(R.id.event_date_txtv);
            organizertxtv = (TextView) itemView.findViewById(R.id.event_organizer_txtv);
        }

    }

}