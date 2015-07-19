package toa.toa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.R;
import toa.toa.activities.AtletismoActivity;

/**
 * Creado por : lawliet
 * Cecha: 17/06/2015.
 * Proyecto: Toa.
 * Hora: 19:31.
 */
public class ComunityAdapter extends RecyclerView.Adapter<ComunityAdapter.ViewHolder> {
    private static Context contexto;
    private ArrayList<MrComunity> comunities;
    private int itemLayout;
    private RelativeLayout layoutContainer;

    public ComunityAdapter(ArrayList<MrComunity> comunities, int layoutID, Context contexto) {
        this.comunities = comunities;
        this.itemLayout = layoutID;
        ComunityAdapter.contexto = contexto;
        this.layoutContainer = layoutContainer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //par apoder inflar el objeto
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        layoutContainer = (RelativeLayout) parent.findViewById(R.id.my_recycler_comunity_view);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MrComunity com = comunities.get(position);
        holder.comunityname.setText(com.getComunityName());
        Picasso.with(contexto).load(com.getComunityImg()).into(holder.comunityImg);
        if (!com.getComunityBack().isEmpty())
            Picasso.with(contexto).load(com.getComunityBack()).fit().centerCrop().into(holder.comunityBack);

        String comunitySelected = com.getComunityName().trim();
        comunitySelected = comunitySelected.toUpperCase();
        switch (comunitySelected) {
            case "CROSSFIT":
                Intent sportAtletismo = new Intent(contexto.getApplicationContext(), AtletismoActivity.class);
                sportAtletismo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                contexto.startActivity(sportAtletismo);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return comunities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

        public TextView comunityname;
        public ImageView comunityImg;
        public ImageView comunityBack;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            comunityname = (TextView) itemView.findViewById(R.id.comunityName);
            comunityBack = (ImageView) itemView.findViewById(R.id.comunity_bck_imv);
            comunityImg = (ImageView) itemView.findViewById(R.id.comunity_image);
        }

        @Override
        public void onClick(View view) {
            MrComunity com =???
            Toast.makeText(view.getContext(), "este evento funciona MS ", Toast.LENGTH_SHORT).show();
            String comunitySelected = com.getComunityName().trim();
            comunitySelected = comunitySelected.toUpperCase();
            switch (comunitySelected) {
                case "CROSSFIT":
                    Intent sportAtletismo = new Intent(contexto.getApplicationContext(), AtletismoActivity.class);
                    sportAtletismo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    contexto.startActivity(sportAtletismo);
                    break;
            }
        }
    }

}
