package toa.toa;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import toa.toa.Objects.MrEvent;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SimpleCallbackClass;

public class DetailEventActivity extends AppCompatActivity {
    private TextView organizer;
    private TextView price;
    private TextView date;
    private TextView descr;
    private MrEvent event;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            Intent i = getIntent();
            event = i.getParcelableExtra("event");
        } else {
            event = savedInstanceState.getParcelable("event");
        }
        if (event == null) {
            onBackPressed();
            finish();
        } else if (event.getId() == 0) {
            onBackPressed();
            finish();

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        organizer = (TextView) findViewById(R.id.devent_organizer);
        price = (TextView) findViewById(R.id.devent_price);
        date = (TextView) findViewById(R.id.devent_date);
        descr = (TextView) findViewById(R.id.devent_descr);
        collapsingToolbarLayout.setTitle(event.getName());
        String organizerstr = event.getOrganizador();
        organizer.setText(organizerstr);
        price.setText((event.getPrice() == 0) ? getResources().getString(R.string.devent_text_price_free) : String.valueOf(event.getPrice()) + "Km");
        descr.setText(event.getDescr());
        date.setText(event.gethStartDate());
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final ImageView hback = (ImageView) findViewById(R.id.himgbackcom);
        fab.hide();
        toggleFAB(fab);
        if (event.getImgurl() != null)
            if (!event.getImgurl().isEmpty())
                Picasso.with(getApplicationContext()).load(event.getImgurl()).fit().centerCrop().into(hback);

    }

    private void showFAB(final FloatingActionButton fab) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 1000);
    }

    private void toggleFAB(final FloatingActionButton fab) {
        SirHandler.isGoingToEvent(getApplicationContext(), event, new SimpleCallbackClass() {
            @Override
            public void gotBool(Boolean bool) {
                if (bool) {
                    fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.profesionalColor)));
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_event_busy_white_24dp));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fab.hide();
                            SirHandler.deleteEvent(getApplicationContext(), event, new SimpleCallbackClass() {
                                @Override
                                public void goIt() {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.eventDeleted_txt, Snackbar.LENGTH_SHORT)
                                            .show();
                                    toggleFAB(fab);
                                }
                            });
                        }
                    });
                    showFAB(fab);
                } else {
                    fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.third_slide)));
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_alert_white_48dp));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fab.hide();
                            SirHandler.registerEvent(event, getApplicationContext(), new SimpleCallbackClass() {
                                @Override
                                public void goIt() {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.eventAdded_txt, Snackbar.LENGTH_SHORT)
                                            .show();
                                    toggleFAB(fab);
                                }
                            });
                        }
                    });
                    showFAB(fab);
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("event", event);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
