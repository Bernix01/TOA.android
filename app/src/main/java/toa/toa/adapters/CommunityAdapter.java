package toa.toa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import toa.toa.Objects.MrCommunity;
import toa.toa.R;

/**
 * Created by gbern on 10/23/2015.
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private static Context context;
    private ArrayList<MrCommunity> communities;

    public CommunityAdapter(ArrayList<MrCommunity> coms, Context contexto) {
        this.communities = coms;
        context = contexto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_community, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MrCommunity com = communities.get(position);
        holder.communityname.setText(com.getName());
        holder.communityzone.setText(com.getZone());
        holder.communitymotto.setText(com.getMotto());

    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView communityname;
        public TextView communityzone;
        public TextView communitymotto;
        public RelativeLayout content;

        public ViewHolder(View itemView) {
            super(itemView);
            communityname = (TextView) itemView.findViewById(R.id.item_com_name);
            communitymotto = (TextView) itemView.findViewById(R.id.item_com_motto);
            communityzone = (TextView) itemView.findViewById(R.id.item_com_zone);
            content = (RelativeLayout) itemView.findViewById(R.id.item_com_cnt);
        }
    }
}
