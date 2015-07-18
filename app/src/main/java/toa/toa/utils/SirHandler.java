package toa.toa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import toa.toa.Objects.MrUser;

/**
 * Created by programador on 7/17/15.
 */
public class SirHandler {

    private static MrUser _currentUser;
    private Context mcontext;
    /*
     * Crea un nuevo SirHandler, vacío.
     *
     * @param mcontext se necesita para obtener data de las sharedprefs
     */

    public SirHandler(Context mcontext) {
        this.mcontext = mcontext;
    }


    private void fetchUserData() {

        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        final int _id = userDetails.getInt("n_id", -1);
        final MrUser user = new MrUser();
        RestApi.get("/node/" + _id, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject data;
                new JSONObject();
                try {
                    data = response.getJSONObject("data");
                    MrUser.set_email(tryGetString(data, "email"));
                    MrUser.set_id(tryGetInt(response.getJSONObject("metadata"), "id"));
                    MrUser.set_name(tryGetString(data, "name"));
                    MrUser.set_uname(tryGetString(data, "u_name"));
                    MrUser.set_bio(tryGetString(data, "bio"));
                    MrUser.set_gender(tryGetInt(data, "gender"));
                    MrUser.set_pimage(tryGetString(data, "pimageurl"));
                } catch (JSONException e) {
                    Toast.makeText(mcontext, "Please connect to a network", Toast.LENGTH_LONG).show();
                    //TODO handle error
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //TODO handle network error
            }
        });
        _currentUser = user;

    }

    public MrUser getCurrentUser() {
        return _currentUser;
    }

    public void updateUserAsync(MrUser newUser) {
        _currentUser = newUser;
        updateRemoteData();
    }


    private void updateRemoteData() {
        Log.d("Final user", _currentUser.toString());
        JSONObject user = new JSONObject();
        //RestApi.put("/node/"+MrUser.get_id()+"/properties",_currentUser,new JsonHttpResponseHandler(){});
    }


    public int tryGetInt(JSONObject j, String name) {
        int r = -1;
        try {
            r = j.getInt(name);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public String tryGetString(JSONObject j, String name) {
        String r = "";
        try {
            r = j.getString(name);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }


   /* public void getUserById(int id, ){


    }*/
}
