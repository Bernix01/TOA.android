/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import toa.toa.utils.RestApi;
import toa.toa.utils.TOA.SirHandler;


public class RegisterActivity extends ActionBarActivity {

    CircularProgressButton btn;
    MaterialEditText name, surname, usr, pw, mail;
    Boolean isDoingReg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        btn = (CircularProgressButton) findViewById(R.id.go_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.conatiner_view);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }
        btn.setIndeterminateProgressMode(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "Click");
                do_the_reg();

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
        // getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public boolean isFormReady() {
        name = (MaterialEditText) findViewById(R.id.reg_name);
        usr = (MaterialEditText) findViewById(R.id.reg_usr);
        surname = (MaterialEditText) findViewById(R.id.reg_last_name);
        pw = (MaterialEditText) findViewById(R.id.reg_pw);
        mail = (MaterialEditText) findViewById(R.id.reg_email);
        return (!name.getText().toString().equals("") & !usr.getText().toString().equals("") & !pw.getText().toString().equals("") & !mail.getText().toString().equals(""));
    }

    // Register the user to db
    public void do_the_reg() {
        if (isDoingReg)
            return;
        btn.setProgress(50);
        isDoingReg = true;
        if (!isFormReady()) {
            Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show(); //One of the form's items is/are empty
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setProgress(-1);//change to error button.
                }
            }, 600);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setProgress(0);//change to initial button.
                }
            }, 900);
            return;
        }
        JSONObject data = new JSONObject();
        try {
            data.put("u_name", name.getText().toString() + " " + surname.getText().toString());
            data.put("pw", Base64.encodeToString((usr.getText().toString() + pw.getText().toString()).getBytes(), Base64.DEFAULT));
            data.put("email", mail.getText().toString());
            data.put("name", usr.getText().toString());
            StringEntity se;
            try {
                se = new StringEntity(data.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data == null) {
            isDoingReg = false;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setProgress(-1);//change to error button.
                }
            }, 600);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setProgress(0);//change to initial button.
                }
            }, 900);
            return;
        }
        RestApi.post("/node/", data, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                Log.e("statuscode", "" + statusCode);
                try {
                    JSONObject meta = responseBody.getJSONObject("metadata");
                    final int _id = meta.getInt("id");
                    RestApi.post("/node/" + _id + "/labels", "\"user\"", new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                            if (statusCode == 204) {
                                SharedPreferences.Editor editor = getSharedPreferences("u_data", MODE_PRIVATE).edit();
                                editor.putInt("n_id", _id);
                                editor.apply();
                                SirHandler handler = new SirHandler(getApplicationContext());
                                handler.fetchUserData(Base64.encodeToString((usr.getText().toString() + pw.getText().toString()).getBytes(), Base64.DEFAULT));
                                btn.setProgress(100);
                                Intent i = new Intent(getApplicationContext(), FirstTime.class);
                                i.putExtra("nid", _id);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            btn.setProgress(-1);
                            try {
                                if (errorResponse.getJSONObject("cause").getString("exception").equals("ConstraintViolationException")) {
                                    deleteBadNode(_id);
                                    usr.setError("User already exists");
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn.setProgress(0);//change to error button.
                                        }
                                    }, 400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void deleteBadNode(int id) {
                RestApi._delete(id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        isDoingReg = false;
                        Log.e("delete", "deleted");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.e("delete", "oh oh");
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("error", "code: " + statusCode + " " + errorResponse.toString());
            }
        });
    }
}












