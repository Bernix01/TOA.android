package toa.toa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import toa.toa.Objects.MrCommunity;
import toa.toa.R;

/**
 * Created by Guillermo on 7/19/2015.
 */
public class ProfileSportsAdapter extends RecyclerView.Adapter<ProfileSportsAdapter.ViewHolder> {

    private static Context contexto;
    private ArrayList<MrCommunity> comunities;

    public ProfileSportsAdapter(ArrayList<MrCommunity> comunities, Context contexto) {
        this.comunities = comunities;
        ProfileSportsAdapter.contexto = contexto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_sport_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MrCommunity com = comunities.get(position);
        holder.comunityname.setText(com.getComunityName());
        Picasso.with(contexto).load(com.getComunityImgAlt()).into(holder.comunityIcn);
    }

    @Override
    public int getItemCount() {
        return comunities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comunityname;
        public ImageView comunityIcn;

        public ViewHolder(View itemView) {
            super(itemView);
            comunityname = (TextView) itemView.findViewById(R.id.profile_sport_item_title_txtv);
            comunityIcn = (ImageView) itemView.findViewById(R.id.profile_sports_item_imgv);
        }

    }

}
