package toa.toa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import toa.toa.Objects.MrCommunity;
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
    private ArrayList<MrCommunity> comunities;
    private int itemLayout;

    public ComunityAdapter(ArrayList<MrCommunity> comunities, int layoutID, Context contexto) {
        this.comunities = comunities;
        this.itemLayout = layoutID;
        ComunityAdapter.contexto = contexto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //par apoder inflar el objeto
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        RelativeLayout layoutContainer = (RelativeLayout) parent.findViewById(R.id.my_recycler_comunity_view);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MrCommunity com = comunities.get(position);
        holder.comunityname.setText(com.getComunityName());
        Picasso.with(contexto).load(com.getComunityImg()).into(holder.comunityImg);
        if (!com.getComunityBack().isEmpty())
            Picasso.with(contexto).load(com.getComunityBack()).fit().centerCrop().transform(new BlurTransformation(contexto, 10)).into(holder.comunityBack);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sportCrossfit = new Intent(contexto.getApplicationContext(), ComunityActivity.class);
                sportCrossfit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sportCrossfit.putExtra("sport", com);
                contexto.startActivity(sportCrossfit);
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
