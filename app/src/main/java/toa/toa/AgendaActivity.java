package toa.toa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import toa.toa.Objects.MrEvent;
import toa.toa.adapters.EventsAdapter;
import toa.toa.utils.SirHandler;
import toa.toa.utils.misc.SirEventsRetriever;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        SirHandler handler = new SirHandler(getApplicationContext());
        handler.getUserEvents(new SirEventsRetriever() {
            @Override
            public void gotIt(ArrayList<MrEvent> events) {
                ((RecyclerView) findViewById(R.id.agenda_recycler)).setAdapter(new EventsAdapter(events, getApplicationContext()));
            }

            @Override
            public void failure(String error) {
                super.failure(error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agenda, menu);
        return true;
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
}
