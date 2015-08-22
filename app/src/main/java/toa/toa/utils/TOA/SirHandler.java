/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa.utils.TOA;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrEvent;
import toa.toa.Objects.MrUser;
import toa.toa.utils.RestApi;
import toa.toa.utils.UtilidadesExtras;

/**
 * Created by programador on 7/17/15.
 */
public class SirHandler {

    protected static String __hash;
    private MrUser _currentUser = new MrUser();
    private Context mcontext;
    /**
     * Crea un nuevo SirHandler, vacío.
     *
     * @param mcontext se necesita para obtener data de las sharedprefs
     */
    public SirHandler(Context mcontext) {
        this.mcontext = mcontext;
        setCurrent();
    }

    private void setCurrent() {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        _currentUser.set_id(userDetails.getInt("n_id", -1));
        _currentUser.set_bio(userDetails.getString("bio", ""));
        _currentUser.set_email(userDetails.getString("email", ""));
        _currentUser.set_name(userDetails.getString("name", ""));
        _currentUser.set_uname(userDetails.getString("uname", ""));
        _currentUser.set_pimage(userDetails.getString("pimage", ""));
    }

    public void fetchUserData(final String hash) {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        final int _id = userDetails.getInt("n_id", -1);
        getUserById(_id, new SirUserRetrieverClass() {
            @Override
            public void goIt(MrUser user) {
                Log.i("fetch", "Current updated successfully");
                _currentUser = user;
                registerCurrentUser(_currentUser, hash);
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
        fetchUserData(__hash);
    }

    private void updateRemoteData() {
        JSONObject user = new JSONObject();
        try {
            user.put("email", _currentUser.get_email());
            user.put("name", _currentUser.get_name());
            user.put("u_name", _currentUser.get_uname());
            user.put("bio", _currentUser.get_bio());
            user.put("gender", _currentUser.get_gender());
            user.put("pimageurl", _currentUser.get_pimage());
            user.put("pw", __hash);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.put("/node/" + _currentUser.get_id() + "/properties", user, new JsonHttpResponseHandler() {
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

    private void setHash(String hash) {
        __hash = hash;
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

    public float tryGetFloat(JSONObject j, String name) {
        float r = -1;
        try {
            r = (float) j.get(name);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public String tryGetString(JSONObject j, String name) {
        String r = "";
        try {
            r = j.getString(name);
            Log.i("str", r);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public int tryGetInt(JSONArray j, int pos) {
        int r = -1;
        try {
            r = j.getInt(pos);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public String tryGetString(JSONArray j, int pos) {
        String r = "";
        try {
            r = j.getString(pos);
            Log.i("str", r);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return r;
    }

    public void registerCurrentUser(MrUser user, String hash) {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putInt("n_id", user.get_id());
        editor.putString("name", user.get_name());
        editor.putString("uname", user.get_uname());
        editor.putString("bio", user.get_bio());
        editor.putInt("gender", user.get_gender());
        editor.putString("email", user.get_email());
        editor.putString("pimage", user.get_pimage());
        editor.putString("hash", hash);
        editor.apply();
        Log.i("fetch", "Shared updated successfully");
        setHash(hash);
    }

    public void getUserById(int id, final SirUserRetrieverClass userRetriever) {
        Log.i("getUserById", "start");
        final MrUser user = new MrUser();
        RestApi.get("/node/" + id, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("getUserById", "got something");
                JSONObject data;
                new JSONObject();
                try {
                    data = response.getJSONObject("data");
                    user.set_email(tryGetString(data, "email"));
                    user.set_id(tryGetInt(response.getJSONObject("metadata"), "id"));
                    user.set_name(tryGetString(data, "name"));
                    user.set_uname(tryGetString(data, "u_name"));
                    user.set_bio(tryGetString(data, "bio"));
                    user.set_gender(tryGetInt(data, "gender"));
                    user.set_pimage(tryGetString(data, "pimageurl"));

                    Log.i("getUserById", "sending");
                    userRetriever.goIt(user);
                    Log.i("getUserById", "done");
                } catch (JSONException e) {
                    Toast.makeText(mcontext, "Please connect to a network", Toast.LENGTH_LONG).show();
                    Log.i("getUserById", "failed");
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

    public void getUserSports(final MrUser user, final SirSportsListRetriever sportsListRetriever) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (a:user)-[r:Likes]-(n:Sport) WHERE id(a)=" + user.get_id() + " return n.name, n.icnurl, n.bgurl");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<MrComunity> sports = new ArrayList<MrComunity>();
                Log.e("response", response.toString());
                try {
                    JSONArray data = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = data.length();
                    for (int i = 0; i < datos; i++)
                        sports.add(new MrComunity(data.getJSONObject(i).getJSONArray("row").getString(0), data.getJSONObject(i).getJSONArray("row").getString(1), data.getJSONObject(i).getJSONArray("row").getString(2)));
                    sportsListRetriever.goIt(sports);

                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                sportsListRetriever.failure(throwable.toString());
            }
        });

    }

    public void logout(SimpleCallbackClass callback) {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.clear();
        editor.apply();

        callback.goIt();
    }

    public void getUserFriends(MrUser user, final SirFriendsRetriever retriever) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (a:user)-[r:Follows]-(n:user) WHERE id(a)=" + user.get_id() + " return n, id(n)");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    final ArrayList<MrUser> friends = new ArrayList<MrUser>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    final int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        final MrUser temp = new MrUser(dataf.getJSONObject(i).getJSONArray("row").getInt(1), tryGetString(udata, "name"), tryGetString(udata, "u_name"), tryGetString(udata, "email"), tryGetString(udata, "bio"), tryGetInt(udata, "gender"), tryGetInt(udata, "age"), tryGetString(udata, "pimageurl"));
                        getUserSports(temp, new SirSportsListRetriever() {
                            @Override
                            public void goIt(ArrayList<MrComunity> sports) {
                                MrUser finalU = temp.withSports(sports);
                                friends.add(finalU);
                                if (friends.size() == datos)
                                    retriever.goIt(friends);

                            }

                            @Override
                            public void failure(String error) {
                                super.failure(error);
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                retriever.failure(throwable.toString());
            }
        });
    }

    public void getSportMembers(MrComunity comunity, final SirFriendsRetriever retriever) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:user)-[r:Likes]-(a:Sport) WHERE a.name=\"" + comunity.getComunityName() + "\" return n, id(n)");
            Log.i("cmdMembers", subcmd.getString("statement"));
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    final ArrayList<MrUser> friends = new ArrayList<MrUser>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    final int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        final MrUser temp = new MrUser(dataf.getJSONObject(i).getJSONArray("row").getInt(1), tryGetString(udata, "name"), tryGetString(udata, "u_name"), tryGetString(udata, "email"), tryGetString(udata, "bio"), tryGetInt(udata, "gender"), tryGetInt(udata, "age"), tryGetString(udata, "pimageurl"));
                        getUserSports(temp, new SirSportsListRetriever() {
                            @Override
                            public void goIt(ArrayList<MrComunity> sports) {
                                MrUser finalU = temp.withSports(sports);
                                friends.add(finalU);
                                if (friends.size() == datos)
                                    retriever.goIt(friends);

                            }

                            @Override
                            public void failure(String error) {
                                super.failure(error);
                            }
                        });
                    }

                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                retriever.failure(throwable.toString());
            }
        });
    }

    public void getPlaces(MrComunity com) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:Place)-[r:COVERS]->(a:Sport) WHERE a.name=\"" + com.getComunityName() + "\" RETURN n");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    ArrayList<MrUser> friends = new ArrayList<MrUser>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        Log.e("udata", udata.getString("u_name"));
                        friends.add(new MrUser(dataf.getJSONObject(i).getJSONArray("row").getInt(1), tryGetString(udata, "name"), tryGetString(udata, "u_name"), tryGetString(udata, "email"), tryGetString(udata, "bio"), tryGetInt(udata, "gender"), tryGetInt(udata, "age"), tryGetString(udata, "pimageurl")));//aquí parece ser el error
                    }
                    Log.e("friends", friends.size() + "");
                    // retriever.goIt(friends);

                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                //  retriever.failure(throwable.toString());
            }
        });
    }

    public void getSportEvents(final String com, final SirEventsRetriever retriever) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:Event)-[r:isAbout]->(a:Sport) WHERE a.name=\"" + com + "\" RETURN n");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    ArrayList<MrEvent> events = new ArrayList<MrEvent>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        Log.e("udata", udata.getString("u_name"));
                        MrEvent temp = new MrEvent(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                UtilidadesExtras.convertDate(tryGetString(udata, "dateStart")),
                                UtilidadesExtras.convertDate(tryGetString(udata, "dateEnd")),
                                tryGetString(udata, "organizer"),
                                tryGetString(udata, "descr"),
                                tryGetString(udata, "address"),
                                tryGetFloat(udata, "x"),
                                tryGetFloat(udata, "y"));
                        if (com.equals("Running") || com.equals("Ciclismo") || com.equals("Natación"))
                            temp = temp.withDistance(tryGetFloat(udata, "distance"));
                        if (com.equals("Triatlón"))
                            temp = temp.withCategory(tryGetString(udata, "cat"));
                        float price = tryGetFloat(udata, "price");
                        temp = temp.withPrice((price == 0.0f) ? 0 : price);
                        events.add(temp);
                    }
                    Log.e("friends", events.size() + "");
                    retriever.gotIt(events);

                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                retriever.failure(throwable.toString());
            }
        });
    }
}
