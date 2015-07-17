package toa.toa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.dd.CircularProgressButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.utils.RestApi;

/**
 * Creado por : elelawliet
 * Hecha: 06/06/2015.
 * Proyecto: Toa.
 * Hora: 1:53.
 */
public class LoginActivity extends Activity {
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        // setSupportActionBar(toolbar);
        final MaterialEditText user = (MaterialEditText) findViewById(R.id.user_etxt);
        final MaterialEditText password = (MaterialEditText) findViewById(R.id.pw_etxt);
        CircularProgressButton sigIn = (CircularProgressButton) findViewById(R.id.bttn_sigIn);

        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _user = Base64.encodeToString((user.getText().toString() + password.getText().toString()).getBytes(), Base64.DEFAULT);
                if (!isReady(user.getText().toString(), password.getText().toString()))
                    return;
                JSONObject cmd = new JSONObject();
                JSONArray statements = new JSONArray();
                try {
                    JSONObject subcmd = new JSONObject();
                    subcmd.put("statement", "MATCH (n:user) WHERE n.pw=\"" + _user + "\" RETURN id(n)");
                    statements.put(subcmd);
                    cmd.put("statements", statements);
                    RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                int _id = response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").getInt(0);
                                SharedPreferences.Editor editor = getSharedPreferences("u_data", MODE_PRIVATE).edit();
                                editor.putInt("n_id", _id);
                                editor.apply();
                                Intent i = new Intent(getApplicationContext(), Tabtest.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            } catch (JSONException e) {
                                Log.e("exception", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.e("error", "code: " + statusCode + " " + throwable.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("error", throwable.getMessage() + "  " + errorResponse.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public Boolean isReady(String u, String p) {
        return !(u.isEmpty() && p.isEmpty());
    }
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, (AppCompatCallback) this);
        }
        return mDelegate;
    }

    public void dologin() {

    }
}
