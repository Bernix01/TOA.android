/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.utils.RestApi;
import toa.toa.utils.SirHandler;

public class LoadingSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_splash);
        Intent i = getIntent();
        Bundle bnd = i.getExtras();
        int id = SirHandler.getCurrentUser(getApplicationContext()).get_id();
        ArrayList<MrComunity> lst = bnd.getParcelableArrayList("SPorts");
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

    private void do_finish_reg(ArrayList<MrComunity> list, final int id) {
        JSONObject item_to_send = new JSONObject();
        JSONArray statements = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            MrComunity bar = list.get(i);
                JSONObject foo = new JSONObject();
                try {
                    if (bar.getIsChecked()) {
                        foo.put("statement", "MATCH (a:user),(b:Sport {name:\"" + bar.getComunityName() + "\"}) WHERE id(a)=" + id + " CREATE UNIQUE (a)-[r:Likes]->(b)");
                    } else {
                        foo.put("statement", "MATCH (a:user)-[r:Likes]->(b:Sport {name:\"" + bar.getComunityName() + "\"}) WHERE id(a)=" + id + " DELETE r");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                statements.put(foo);
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
