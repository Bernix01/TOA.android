package toa.toa.adapters;

/**
 * Created by Guillermo on 7/23/2015.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import toa.toa.Objects.MrEvent;
import toa.toa.R;
import toa.toa.map.MapActivity;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private final Context contexto;
    private final ArrayList<MrEvent> events;

    public EventsAdapter(ArrayList<MrEvent> comunities, Context contexto) {
        this.events = comunities;
        this.contexto = contexto;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_event, parent, false);
        return new ViewHolder(v, contexto);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MrEvent event = events.get(position);
        holder.datetxtv.setText(event.gethStartDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getDateStart());
        holder.daytxtv.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        holder.nametxtv.setText(event.getName());
        holder.organizertxtv.setText(contexto.getResources().getString(R.string.organiza_txt) + " " + event.getOrganizador());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView nametxtv;
        public final TextView organizertxtv;
        public final TextView datetxtv;
        public final TextView daytxtv;

        public ViewHolder(final View itemView, final Context contexto) {
            super(itemView);
            nametxtv = (TextView) itemView.findViewById(R.id.event_name_txtv);
            daytxtv = (TextView) itemView.findViewById(R.id.event_day_txtv);
            datetxtv = (TextView) itemView.findViewById(R.id.event_date_txtv);
            organizertxtv = (TextView) itemView.findViewById(R.id.event_organizer_txtv);
            daytxtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mapInt = new Intent(contexto.getApplicationContext(), MapActivity.class);
                    mapInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mapInt.putExtra("events", getOldPosition());
                    contexto.startActivity(mapInt);
                }
            });

        }


    }

}