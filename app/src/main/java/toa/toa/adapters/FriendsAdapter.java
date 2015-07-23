/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrUser;
import toa.toa.R;
import toa.toa.utils.TOA.SirHandler;
import toa.toa.utils.TOA.SirSportsListRetriever;

/**
 * Created by programador on 7/21/15.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private static Context contexto;
    private ArrayList<MrUser> comunities;

    public FriendsAdapter(ArrayList<MrUser> comunities, Context contexto) {
        this.comunities = comunities;
        FriendsAdapter.contexto = contexto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_friend, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MrUser com = comunities.get(position);
        holder.nametxtv.setText(com.get_uname());
        if (!com.get_pimage().isEmpty()) {
            Picasso.with(contexto).load(com.get_pimage()).transform(new CropCircleTransformation()).into(holder.pimage);
        } else {
            Picasso.with(contexto).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(holder.pimage);
        }
        SirHandler handler = new SirHandler(contexto);
        handler.getUserSports(com, new SirSportsListRetriever() {
            @Override
            public void goIt(ArrayList<MrComunity> sports) {
                for (MrComunity sport : sports)
                    holder.glSports.addView(addImgv(sport));
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    private ImageView addImgv(MrComunity comunity) {
        ImageView imv = new ImageView(contexto);
        Resources r = contexto.getResources();
        int wh = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()));
        Picasso.with(contexto).load(comunity.getComunityImg()).resize(wh, wh).into(imv);
        return imv;
    }

    @Override
    public int getItemCount() {
        return comunities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nametxtv;
        public ImageView pimage;
        public GridLayout glSports;

        public ViewHolder(View itemView) {
            super(itemView);
            nametxtv = (TextView) itemView.findViewById(R.id.friend_name_txtv);
            pimage = (ImageView) itemView.findViewById(R.id.friend_pimage_imv);
            glSports = (GridLayout) itemView.findViewById(R.id.friend_sports_gl);
        }

    }

}