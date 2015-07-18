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

    /**
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
        getUserById(_id, new SirClass() {
            @Override
            public void goIt(MrUser user) {
                _currentUser = user;
            }

            @Override
            public void failure(String error) {
                Log.e("error", error);

            }
        });

    }

    public MrUser getCurrentUser() {
        return _currentUser;
    }

    public void updateUserAsync(MrUser newUser) {
        _currentUser = newUser;
        updateRemoteData();
    }


    private void updateRemoteData() {
        JSONObject user = new JSONObject();
        try {
            user.put("email", MrUser.get_email());
            user.put("name", MrUser.get_name());
            user.put("u_name", MrUser.get_uname());
            user.put("bio", MrUser.get_bio());
            user.put("gender", MrUser.get_gender());
            user.put("pimageurl", MrUser.get_pimage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.put("/node/" + MrUser.get_id() + "/properties", user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(mcontext, "Profile updated successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(mcontext, "Could not update profile :(", Toast.LENGTH_LONG).show();
            }
        });
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


    public void getUserById(int id, final SirClass userRetriever) {

        final MrUser user = new MrUser();
        RestApi.get("/node/" + id, new RequestParams(), new JsonHttpResponseHandler() {
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
                    userRetriever.goIt(user);
                } catch (JSONException e) {
                    Toast.makeText(mcontext, "Please connect to a network", Toast.LENGTH_LONG).show();
                    userRetriever.failure("meh");
                    //TODO handle error
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //TODO handle network error
            }
        });
    }



}
