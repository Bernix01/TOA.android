package toa.toa;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import toa.toa.Objects.MrSport;
import toa.toa.utils.CheckBox;

public class FirstTime extends AppCompatActivity {
    private ArrayList<MrSport> Sports = new ArrayList<MrSport>();
    private Picasso picasso;
    private adapter adapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time2);
        final int id = getIntent().getIntExtra("nid", 0);
        if (id == 0) {
            Intent c = new Intent(getApplicationContext(), Splash_Activity.class);
            c.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(c);
            finish();
        }
        final RecyclerView rclr = (RecyclerView) findViewById(R.id.recycler_firstTime);
        addObjectToList(new MrSport(R.drawable.deprun, "Running"));
        addObjectToList(new MrSport(R.drawable.depswi, "Natación"));
        addObjectToList(new MrSport(R.drawable.depcross, "Crossfit"));
        addObjectToList(new MrSport(R.drawable.depsoc, "Fútbol"));
        addObjectToList(new MrSport(R.drawable.depbike, "Ciclismo"));
        addObjectToList(new MrSport(R.drawable.deptri, "Triatlón"));
        adapt = new adapter(Sports);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.opt_container);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        GridLayoutManager gm = new GridLayoutManager(getApplicationContext(),2);
        rclr.setLayoutManager(gm);
        rclr.setAdapter(adapt);
        ButtonFloat contiue = (ButtonFloat) findViewById(R.id.finish_register);
        contiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(), LoadingSplash.class);
                a.putExtra("nid", id);
                a.putExtra("SPorts", adapt.getList());
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_time, menu);
        return true;
    }

    private void addObjectToList(MrSport sport) {
        Sports.add(sport);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
        ArrayList<MrSport> list;
        public adapter(ArrayList<MrSport> list) {
            this.list = list;
        }

        @Override
        public adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preference_sport, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final adapter.ViewHolder holder, int position) {
            final MrSport sport = list.get(position);
            holder.opt.setCircleColor(getResources().getColor(R.color.primary));
            holder.opt.setUnCheckColor(getResources().getColor(R.color.primary_dark));
            ViewGroup.LayoutParams lp = holder.img.getLayoutParams();
            lp.height = holder.img.getWidth();
            holder.img.setLayoutParams(lp);
            holder.title.setText(sport.getName());
            Picasso.with(getApplicationContext()).load(sport.getImg()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.img);
            holder.cnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.opt.setChecked(!holder.opt.isChecked());
                    sport.setIsChecked(!holder.opt.isChecked());
                }
            });
        }

        public ArrayList<MrSport> getList() {
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
            TextView title;
            public ViewHolder(View itemView) {
                super(itemView);
                opt = (CheckBox) itemView.findViewById(R.id.option);
                img = (ImageView) itemView.findViewById(R.id.img_item_preference);
                cnt = (RelativeLayout) itemView.findViewById(R.id.opt_container);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }
    }
}
