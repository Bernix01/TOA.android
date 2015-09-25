package toa.toa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrCommunity;
import toa.toa.utils.CheckBox;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SirSportsListRetriever;

public class FirstTime extends AppCompatActivity {
    private adapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time2);
        final int id = SirHandler.getCurrentUser(getApplicationContext()).get_id();
        if (id == 0) {
            Intent c = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(c);
            finish();
        }
        final RecyclerView rclr = (RecyclerView) findViewById(R.id.recycler_firstTime);
        SirHandler.getAllComs(new SirSportsListRetriever() {
            @Override
            public void goIt(ArrayList<MrCommunity> sports) {
                adapt = new adapter(sports);


                rclr.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rclr.setAdapter(adapt);
                FloatingActionButton contiue = (FloatingActionButton) findViewById(R.id.finish_register);
                contiue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getApplicationContext(), LoadingSplash.class);
                        a.putExtra("SPorts", adapt.getList());
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        finish();
                    }
                });
            }

            @Override
            public void failure(String error) {

            }
        });
    }

    public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
        ArrayList<MrCommunity> list;

        public adapter(ArrayList<MrCommunity> list) {
            this.list = list;
        }

        @Override
        public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preference_sport, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final adapter.ViewHolder holder, int position) {
            final MrCommunity sport = list.get(position);
            holder.opt.setCircleColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));
            holder.opt.setUnCheckColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_dark));
            Picasso.with(getApplicationContext()).load(sport.getComunityImgAlt()).fit().centerCrop().transform(new CropCircleTransformation()).into(holder.img);
            holder.name.setText(sport.getComunityName());
            holder.cnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.opt.setChecked(!holder.opt.isChecked());
                    sport.setIsChecked(!holder.opt.isChecked());
                }
            });
        }

        public ArrayList<MrCommunity> getList() {
            return list;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox opt;
            RelativeLayout cnt;
            ImageView img;
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                opt = (CheckBox) itemView.findViewById(R.id.option);
                img = (ImageView) itemView.findViewById(R.id.img_item_preference);
                name = (TextView) itemView.findViewById(R.id.item_preference_sport_name);
                cnt = (RelativeLayout) itemView.findViewById(R.id.opt_container);
            }
        }
    }
}
