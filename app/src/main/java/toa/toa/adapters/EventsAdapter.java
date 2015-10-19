package toa.toa.adapters;

/**
 * Created by Guillermo on 7/23/2015.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import toa.toa.DetailEventActivity;
import toa.toa.Objects.MrEvent;
import toa.toa.R;
import toa.toa.utils.UtilidadesExtras;

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
        if (position == 0) {
            String monthstr = new SimpleDateFormat("MMMM", Locale.getDefault()).format(event.getDateStart());
            holder.monthtxtv.setText(monthstr);
            holder.monthtxtv.getLayoutParams().height = Math.round(UtilidadesExtras.dipToPixels(contexto, 35));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(event.getDateStart());
            int thisEM = cal.get(Calendar.MONTH);
            cal.setTime(events.get(position - 1).getDateStart());
            int nextEM = cal.get(Calendar.MONTH);
            if (thisEM != nextEM) {

                String monthstr = new SimpleDateFormat("MMMM", Locale.getDefault()).format(event.getDateStart());
                holder.monthtxtv.setText(monthstr);
                holder.monthtxtv.getLayoutParams().height = Math.round(UtilidadesExtras.dipToPixels(contexto, 35));
            } else {
                holder.monthtxtv.getLayoutParams().height = 0;
            }
        }
        Log.e("height", position + ": " + holder.monthtxtv.getHeight() + "");
        holder.address.setText(event.getAddress());
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getDateStart());
        holder.daytxtv.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        holder.nametxtv.setText(event.getName());
        String organizastr = contexto.getResources().getString(R.string.organiza_txt) + " " + event.getOrganizador();
        holder.organizertxtv.setText(organizastr);
        holder.daytxtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent devent = new Intent(contexto.getApplicationContext(), DetailEventActivity.class);
                devent.putExtra("event", event);
                devent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                contexto.startActivity(devent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView nametxtv;
        public final TextView organizertxtv;
        public final TextView address;
        public final TextView monthtxtv;
        public final TextView daytxtv;

        public ViewHolder(final View itemView, final Context contexto) {
            super(itemView);
            nametxtv = (TextView) itemView.findViewById(R.id.event_name_txtv);
            daytxtv = (TextView) itemView.findViewById(R.id.event_day_txtv);
            address = (TextView) itemView.findViewById(R.id.event_address_txtv);
            organizertxtv = (TextView) itemView.findViewById(R.id.event_organizer_txtv);
            monthtxtv = (TextView) itemView.findViewById(R.id.event_month_name);


        }


    }

}