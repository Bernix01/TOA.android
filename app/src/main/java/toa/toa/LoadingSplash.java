package toa.toa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toa.toa.Objects.MrSport;
import toa.toa.utils.RestApi;

public class LoadingSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_splash);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.cnt);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        Shimmer shimmer = new Shimmer();
        shimmer.start((ShimmerTextView) findViewById(R.id.shimmer_tv));
        Intent i = getIntent();
        Bundle bnd = i.getExtras();
        int id = i.getIntExtra("nid", 0);
        ArrayList<MrSport> lst = bnd.getParcelableArrayList("SPorts");
        do_finish_reg(lst, id);
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

    private void do_finish_reg(ArrayList<MrSport> list, final int id) {
        JSONObject item_to_send = new JSONObject();
        JSONArray statements = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            MrSport bar = list.get(i);
            if (bar.getIsChecked()) {
                JSONObject foo = new JSONObject();
                try {
                    foo.put("statement", "MATCH (a:user),(b:Sport) WHERE id(a)=" + id + " AND b.name =\"" + bar.getName() + "\" Create (a)-[r:Likes]->(b)");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statements.put(foo);
            }
        }
        try {
            item_to_send.put("statements", statements);
            RestApi.post("/transaction/commit", item_to_send, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e("error", response.toString());
                    try {

                        Log.e("error", response.getJSONArray("errors").length() + "");
                        if (response.getJSONArray("errors").length() == 0) {
                            Log.e("error", response.getJSONArray("errors").toString());
                            Log.i("registered", "yay :-)");
                            SharedPreferences.Editor editor = getSharedPreferences("u_data", MODE_PRIVATE).edit();
                            editor.putInt("n_id", id);
                            editor.apply();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("registered", "some error... " + e.toString());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("registered", "failure error... " + errorResponse.toString());

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading_splash, menu);
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
