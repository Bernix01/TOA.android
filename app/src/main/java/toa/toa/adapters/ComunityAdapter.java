package toa.toa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import toa.toa.Objects.MrComunity;
import toa.toa.R;
import toa.toa.activities.ComunityActivity;

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
        final MrComunity com = comunities.get(position);
        holder.comunityname.setText(com.getComunityName());
        Picasso.with(contexto).load(com.getComunityImg()).into(holder.comunityImg);
        Log.d("comunitiBack", com.getComunityBack() + " ");
        // if (!com.getComunityBack().isEmpty())
        //    Picasso.with(contexto).load(com.getComunityBack()).fit().centerCrop().transform(new BlurTransformation(contexto, 10)).into(holder.comunityBack);
        Picasso.with(contexto).setLoggingEnabled(true);
        Picasso.with(contexto).load(com.getComunityBack()).fit().centerCrop().transform(new BlurTransformation(contexto, 10)).into(holder.comunityBack);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sportCrossfit = new Intent(contexto.getApplicationContext(), ComunityActivity.class);
                sportCrossfit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sportCrossfit.putExtra("sport", com);
                contexto.startActivity(sportCrossfit);
               /* String comunitySelected = com.getComunityName().trim();
                comunitySelected = comunitySelected.toUpperCase();
                switch (comunitySelected) {
                    case "CROSSFIT":
                        Intent sportCrossfit = new Intent(contexto.getApplicationContext(), ComunityActivity.class);
                        sportCrossfit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sportCrossfit.putExtra("sport",com);
                        contexto.startActivity(sportCrossfit);
                        break;
                    case "RUNNING":
                        Intent sportRunning = new Intent(contexto.getApplicationContext(), RunningActivity.class);
                        sportRunning.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contexto.startActivity(sportRunning);
                        break;

                    case "FÚTBOL":
                        Intent sportFootbol = new Intent(contexto.getApplicationContext(), FootballActivity.class);
                        sportFootbol.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contexto.startActivity(sportFootbol);
                        break;
                    case "NATACIÓN":
                        Intent sportSwimming = new Intent(contexto.getApplicationContext(), SwimmingActivity.class);
                        sportSwimming.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contexto.startActivity(sportSwimming);
                        break;

                    case "CICLISMO":
                        Intent sportCycling = new Intent(contexto.getApplicationContext(), CyclingActivity.class);
                        sportCycling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        contexto.startActivity(sportCycling);
                        break;
                    case "TRIATLÓN":
                        Intent sportTriathlon = new Intent(contexto.getApplicationContext(), TriathlonActivity.class);
                        sportTriathlon.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contexto.startActivity(sportTriathlon);
                        break;
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return comunities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comunityname;
        public ImageView comunityImg;
        public ImageView comunityBack;
        public RelativeLayout content;

        public ViewHolder(View itemView) {
            super(itemView);
            comunityname = (TextView) itemView.findViewById(R.id.comunityName);
            comunityBack = (ImageView) itemView.findViewById(R.id.comunity_bck_imv);
            comunityImg = (ImageView) itemView.findViewById(R.id.comunity_image);
            content = (RelativeLayout) itemView.findViewById(R.id.content_comunity);
        }
    }
}
