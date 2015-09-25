package toa.toa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import toa.toa.Objects.MrNutrition;

/**
 * Created by Junior on 18/07/2015.
 */
public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {
    private ArrayList<MrNutrition> nutrition;
    private int itemLayout;
    private Context contexto;
    private RelativeLayout layoutContainer;

    public NutritionAdapter(ArrayList<MrNutrition> nutrition, int layoutID, Context contexto) {
        this.nutrition = nutrition;
        this.itemLayout = layoutID;
        this.contexto = contexto;
        this.layoutContainer = layoutContainer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //par apoder inflar el objeto
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        // layoutContainer = (RelativeLayout) parent.findViewById(R.id.my_recycler_comunity_view);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NutritionAdapter.ViewHolder holder, int position) {
        MrNutrition com = nutrition.get(position);
        holder.nutritionname.setText(com.getNutritionName());
        Picasso.with(contexto).load(com.getNutritionImg()).into(holder.nutritionImg);
        if (!com.getNutritionBack().isEmpty())
            Picasso.with(contexto).load(com.getNutritionBack()).fit().centerCrop().into(holder.nutritionBack);
    }


    @Override
    public int getItemCount() {
        return nutrition.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nutritionname;
        public ImageView nutritionImg;
        public ImageView nutritionBack;

        public ViewHolder(View itemView) {
            super(itemView);
            //nutritionname = (TextView) itemView.findViewById(R.id.nutritionSportName);
            // nutritionBack = (ImageView) itemView.findViewById(R.id.nutrition_bck_imv);
            // nutritionImg = (ImageView) itemView.findViewById(R.id.nutrition_image);
        }
    }
}
