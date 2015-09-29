package toa.toa.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import toa.toa.R;

/**
 * Created by gbern on 9/28/2015.
 */
public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private ArrayList<String> tags;


    public TagsAdapter(ArrayList<String> tags) {
        this.tags = tags;
        Log.d(TagsAdapter.class.getSimpleName(), tags.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tag.setText(tags.get(position));
    }


    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tag;

        public ViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag_txtv);
        }

    }
}