package toa.toa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.R;

/**
 * Creado por : lawliet
 * Cecha: 17/06/2015.
 * Proyecto: Toa.
 * Hora: 19:31.
 */
public class ComunityAdapter extends RecyclerView.Adapter<ComunityAdapter.ViewHolder> {
    private ArrayList<MrComunity> comunities;
    private int itemLayout;

    public ComunityAdapter(ArrayList<MrComunity> comunities, int layoutID) {
        this.comunities = comunities;
        this.itemLayout = layoutID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //par apoder inflar el objeto
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MrComunity com = comunities.get(position);

        holder.comunityname.setText(com.getComunityName());
       /* holder.comunityBack.set(com.getComunityBack());
        holder.comunityImg.setText(com.getComunityImg());*/

        //TODO Investigar como capturar la imagen
        //TODO  *hacer que trabaje con datos del WS pedir ayuda a Google
        /*holder.comunityname.setR(com.getComunityName());
        holder.comunityname.setText(com.getComunityName());*/

    }

    @Override
    public int getItemCount() {
        return comunities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comunityname;
        public ImageView comunityImg;
        public RelativeLayout comunityBack;

        public ViewHolder(View itemView) {
            super(itemView);
            comunityname = (TextView) itemView.findViewById(R.id.comunityName);
            comunityBack = (RelativeLayout) itemView.findViewById(R.id.content_comunity);
            comunityImg = (ImageView) itemView.findViewById(R.id.comunity_image);
        }
    }


}
