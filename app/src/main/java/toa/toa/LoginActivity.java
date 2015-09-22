/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.Objects.MrUser;
import toa.toa.utils.RestApi;
import toa.toa.utils.SirHandler;
import toa.toa.utils.UtilidadesExtras;

import static toa.toa.utils.UtilidadesExtras.tryGetInt;
import static toa.toa.utils.UtilidadesExtras.tryGetString;

/**
 * Creado por : elelawliet
 * Hecha: 06/06/2015.
 * Proyecto: Toa.
 * Hora: 1:53.
 */
public class LoginActivity extends Activity {
    EditText user;
    EditText password;
    CircularProgressButton sigIn;
    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.user_etxt);
        password = (EditText) findViewById(R.id.pw_etxt);
        sigIn = (CircularProgressButton) findViewById(R.id.bttn_sigIn);
        sigIn.setIndeterminateProgressMode(true);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilidadesExtras.isOnline(getApplicationContext())) {
                    dologin();
                } else {
                    Toast.makeText(getApplicationContext(), "No network connection :-(", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public Boolean isReady(String u, String p) {
        return !(u.isEmpty() && p.isEmpty());
    }

    public void dologin() {

        final String _user = Base64.encodeToString((user.getText().toString() + password.getText().toString()).getBytes(), Base64.DEFAULT);
        if (!isReady(user.getText().toString(), password.getText().toString()))
            return;
        sigIn.setProgress(50);
        JSONObject cmd = new JSONObject();
        final JSONArray statements = new JSONArray();
        try {
            JSONObject subcmd = new JSONObject();
            subcmd.put("statement", "MATCH (n:user) WHERE n.pw=\"" + _user + "\" RETURN id(n), n.u_name, n.name, n.bio, n.gender, n.email, n.pimageurl");
            statements.put(subcmd);
            cmd.put("statements", statements);
            RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.i("got it!", statusCode + " " + response + "  " + _user);
                        sigIn.setProgress(100);
                        SirHandler handler = new SirHandler(getApplicationContext());
                        JSONArray udata = response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row");
                        MrUser _cuser = new MrUser();
                        _cuser.set_id(tryGetInt(udata, 0));
                        _cuser.set_uname(tryGetString(udata, 1));
                        _cuser.set_name(tryGetString(udata, 2));
                        _cuser.set_bio(tryGetString(udata, 3));
                        _cuser.set_gender(tryGetInt(udata, 4));
                        _cuser.set_email(tryGetString(udata, 5));
                        _cuser.set_pimage(tryGetString(udata, 6));
                        SirHandler.registerCurrentUser(_cuser, _user);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
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
}
