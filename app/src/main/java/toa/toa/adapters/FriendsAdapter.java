/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrCommunity;
import toa.toa.Objects.MrUser;
import toa.toa.R;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SimpleCallbackClass;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MrUser com = comunities.get(position);
        holder.nametxtv.setText(com.get_uname());
        if (!com.get_pimage().isEmpty()) {
            Picasso.with(contexto).load(com.get_pimage()).transform(new CropCircleTransformation()).into(holder.pimage);
        } else {
            Picasso.with(contexto).load(R.drawable.ic_account_circle_white_48dp).transform(new CropCircleTransformation()).into(holder.pimage);
        }
        holder.glSports.removeAllViews();
        for (MrCommunity sport : com.get_sports())
            holder.glSports.addView(addImgv(sport));
        holder.pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contexto, ":-)", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tagline.setText(com.get_bio());

        if (com.get_id() == SirHandler.getCurrentUser(contexto).get_id()) {
            holder.friendShip.setVisibility(View.INVISIBLE);
            return;
        }
            SirHandler.isFollowing(com, new SimpleCallbackClass() {
                @Override
                public void gotBool(final Boolean bool) {
                    if (bool) {
                        holder.friendShip.setText(R.string.unFollow);
                        holder.friendShip.setBackgroundColor(ContextCompat.getColor(contexto, R.color.profesionalColor));
                    } else {
                        holder.friendShip.setText(R.string.follow);
                        holder.friendShip.setBackgroundColor(ContextCompat.getColor(contexto, R.color.my_awesome_color));

                    }

                    holder.friendShip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SirHandler.friendShip(com, bool, new SimpleCallbackClass() {
                                @Override
                                public void goIt() {
                                    notifyItemChanged(position);
                                }
                            });
                        }
                    });
                }
            });
    }

    private ImageView addImgv(MrCommunity comunity) {
        ImageView imv = new ImageView(contexto);
        Resources r = contexto.getResources();
        int wh = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
        Picasso.with(contexto).load(comunity.getComunityImgAlt()).resize(wh, wh).into(imv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imv.setElevation(1);
        }
        return imv;
    }

    @Override
    public int getItemCount() {
        return comunities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nametxtv;
        public ImageView pimage;
        public LinearLayout glSports;
        public TextView friendShip;
        public TextView tagline;

        public ViewHolder(View itemView) {
            super(itemView);
            nametxtv = (TextView) itemView.findViewById(R.id.friend_name_txtv);
            pimage = (ImageView) itemView.findViewById(R.id.friend_pimage_imv);
            glSports = (LinearLayout) itemView.findViewById(R.id.friend_sports_gl);
            friendShip = (TextView) itemView.findViewById(R.id.item_row_friend_toggleFriendship);
            tagline = (TextView) itemView.findViewById(R.id.item_row_friend_tagline);
        }

    }

}