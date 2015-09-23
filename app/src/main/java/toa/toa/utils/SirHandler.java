/*
 * Copyright TOA Inc. 2015. 
 */

package toa.toa.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toa.toa.Objects.MrComunity;
import toa.toa.Objects.MrEvent;
import toa.toa.Objects.MrPlace;
import toa.toa.Objects.MrUser;
import toa.toa.agenda.AgendaMan;
import toa.toa.utils.misc.SimpleCallbackClass;
import toa.toa.utils.misc.SirEventsRetriever;
import toa.toa.utils.misc.SirFriendsRetriever;
import toa.toa.utils.misc.SirPlacesRetriever;
import toa.toa.utils.misc.SirSportsListRetriever;
import toa.toa.utils.misc.SirUserRetrieverClass;

import static toa.toa.utils.UtilidadesExtras.convertDate;
import static toa.toa.utils.UtilidadesExtras.tryGetFloat;
import static toa.toa.utils.UtilidadesExtras.tryGetInt;
import static toa.toa.utils.UtilidadesExtras.tryGetString;

/**
 * Created by programador on 7/17/15.
 */
public class SirHandler {

    protected static String __hash;
    private static MrUser _currentUser;
    private static Context mcontext;

    /**
     * Crea un nuevo SirHandler, vacío.
     *
     * @param mcontext se necesita para obtener data de las sharedprefs
     */
    public SirHandler(Context mcontext) {
        SirHandler.mcontext = mcontext;
        SirHandler._currentUser = new MrUser();
        setCurrent();
    }

    public static MrUser getCurrentUser(Context mcontext) {
        SirHandler.mcontext = mcontext;
        SirHandler._currentUser = new MrUser();
        setCurrent();
        return SirHandler._currentUser;
    }

    public static void initialize(Context mcontext) {
        SirHandler.mcontext = mcontext;
        SirHandler._currentUser = new MrUser();
        setCurrent();
    }

    public static void friendShip(MrUser user, boolean bool, final SimpleCallbackClass simpleCallbackClass) {

        String _del = "MATCH (h:user)-[r:Follows]->(n:user) WHERE id(n)=" + user.get_id() + " AND id(h)=" + _currentUser.get_id() + " DELETE r RETURN -1";
        String _add = "MATCH (n:user),(h:user) WHERE id(n)=" + user.get_id() + " AND id(h)=" + _currentUser.get_id() + " CREATE UNIQUE (h)-[r:Follows]->(n) RETURN 1";
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", bool ? _del : _add);
            Log.i("statementFriendship", subcmd.toString() + " ");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.i("friendship", response.toString() + " ");
                simpleCallbackClass.goIt();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }

        });
    }

    public static void searchPeople(String query, final SirFriendsRetriever retriever) {
        // =~ "\S*eg+\S*"
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:user),(h:user {name: \'" + _currentUser.get_name() + "\'}) WHERE n.name=~ \".*" + query + ".*\"  OR n.u_name=~ \".*" + query + ".*\" RETURN n, id(n)");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                Log.e("response", response.toString());
                try {
                    final ArrayList<MrUser> friends = new ArrayList<MrUser>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    final int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        final MrUser temp = new MrUser(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                tryGetString(udata, "u_name"),
                                tryGetString(udata, "email"),
                                tryGetString(udata, "bio"),
                                tryGetInt(udata, "gender"),
                                tryGetInt(udata, "age"),
                                tryGetString(udata, "pimageurl"));
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
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }
        });
    }

    public static void isFollowing(final MrUser user, final SimpleCallbackClass simpleCallback) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:user {name: '" + _currentUser.get_name() + "'})-[Follows]->(h:user {name: '" + user.get_name() + "'}) RETURN h.name");
            Log.d("statementFollow", subcmd.getString("statement"));
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                Log.e("response", response.toString());
                try {
                    JSONArray array = response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row");
                    String dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").getString(0);
                    simpleCallback.gotBool(dataf.equals(user.get_name()));
                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                    simpleCallback.gotBool(false);
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }
        });
    }

    public static void getAllComs(final SirSportsListRetriever retriever) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:Sport) RETURN n.name, n.icnurl, n.bgurl, n.icnurl_alt");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                ArrayList<MrComunity> sports = new ArrayList<MrComunity>();
                try {
                    JSONArray data = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    int datos = data.length();
                    for (int i = 0; i < datos; i++)
                        sports.add(new MrComunity(data.getJSONObject(i).getJSONArray("row").getString(0), data.getJSONObject(i).getJSONArray("row").getString(1), data.getJSONObject(i).getJSONArray("row").getString(2), data.getJSONObject(i).getJSONArray("row").getString(3)));
                    retriever.goIt(sports);
                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                retriever.failure(throwable.toString());
            }
        });

    }

    private static void setCurrent() {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        SirHandler._currentUser.set_id(userDetails.getInt("n_id", -1));
        SirHandler._currentUser.set_bio(userDetails.getString("bio", ""));
        SirHandler._currentUser.set_email(userDetails.getString("email", ""));
        SirHandler._currentUser.set_name(userDetails.getString("name", ""));
        SirHandler._currentUser.set_uname(userDetails.getString("uname", ""));
        SirHandler._currentUser.set_pimage(userDetails.getString("pimage", ""));
        SirHandler._currentUser.set_age(userDetails.getInt("age", 0));
        __hash = userDetails.getString("hash", "");
    }

    private static void setHash(String hash) {
        __hash = hash;
    }

    public static void registerCurrentUser(MrUser user, String hash) {
        Log.e("register user", "starting up things..");
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.putInt("n_id", user.get_id());
        editor.putString("name", user.get_name());
        editor.putString("uname", user.get_uname());
        editor.putString("bio", user.get_bio());
        editor.putInt("gender", user.get_gender());
        editor.putString("email", user.get_email());
        editor.putInt("age", user.get_age());
        editor.putString("pimage", user.get_pimage());
        editor.putString("hash", hash);
        editor.apply();
        Log.i("fetch", "Shared updated successfully");
        setHash(hash);
    }

    public static void logout(SimpleCallbackClass callback) {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        editor.clear();
        editor.apply();
        callback.goIt();
    }

    public static void getUserSports(final MrUser user, final SirSportsListRetriever sportsListRetriever) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (a:user)-[r:Likes]-(n:Sport) WHERE id(a)=" + user.get_id() + " return n.name, n.icnurl, n.bgurl, n.icnurl_alt");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                ArrayList<MrComunity> sports = new ArrayList<MrComunity>();
                try {
                    JSONArray data = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    int datos = data.length();
                    ArrayList<String> sportsl = new ArrayList<String>();
                    for (int a = 0; a < datos; a++) {
                        JSONObject i = data.getJSONObject(a);
                        sports.add(new MrComunity(i.getJSONArray("row").getString(0), i.getJSONArray("row").getString(1), i.getJSONArray("row").getString(2), i.getJSONArray("row").getString(3)));
                        sportsl.add(i.getJSONArray("row").getString(0));
                    }
                    sportsListRetriever.goIt(sports);
                    sportsl.add("android");
                    sportsListRetriever.gotString(sportsl);
                } catch (JSONException e) {
                    Log.e("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                sportsListRetriever.failure(throwable.toString());
            }
        });

    }

    public void fetchUserData(final String hash) {
        SharedPreferences userDetails = mcontext.getSharedPreferences("u_data", Context.MODE_PRIVATE);
        final int _id = userDetails.getInt("n_id", -1);
        getUserById(_id, new SirUserRetrieverClass() {
            @Override
            public void goIt(MrUser user) {
                Log.i("fetch", "gotIt");
                SirHandler._currentUser = user;
                Log.i("fetch", "pimage: " + user.get_pimage());
                Picasso.with(mcontext).invalidate(user.get_pimage());
                registerCurrentUser(_currentUser, hash);
            }

            @Override
            public void failure(String error) {
                Log.e("error", error);
            }
        });

    }

    public void updateUserAsync(MrUser newUser) {
        SirHandler._currentUser = newUser;
        updateRemoteData();
        fetchUserData(__hash);
    }

    private void updateRemoteData() {
        JSONObject user = new JSONObject();
        try {
            user.put("email", SirHandler._currentUser.get_email());
            user.put("name", SirHandler._currentUser.get_name());
            user.put("u_name", SirHandler._currentUser.get_uname());
            user.put("bio", SirHandler._currentUser.get_bio());
            user.put("gender", SirHandler._currentUser.get_gender());
            user.put("age", SirHandler._currentUser.get_age());
            user.put("pimageurl", SirHandler._currentUser.get_pimage());
            user.put("pw", (__hash.trim() + "\n").trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.e("userProps", user.toString(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.put("/node/" + SirHandler._currentUser.get_id() + "/properties", user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Toast.makeText(mcontext, "Profile updated successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(mcontext, "Could not update profile :(", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserById(int id, final SirUserRetrieverClass userRetriever) {
        Log.i("getUserById", "start");
        final MrUser user = new MrUser();
        RestApi.get("/node/" + id, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
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
                    user.set_age(tryGetInt(data, "age"));
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
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                //TODO handle network error
            }
        });
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
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    final ArrayList<MrUser> friends = new ArrayList<MrUser>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    final int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        final MrUser temp = new MrUser(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                tryGetString(udata, "u_name"),
                                tryGetString(udata, "email"),
                                tryGetString(udata, "bio"),
                                tryGetInt(udata, "gender"),
                                tryGetInt(udata, "age"),
                                tryGetString(udata, "pimageurl"));
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
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
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
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                try {
                    final ArrayList<MrUser> friends = new ArrayList<MrUser>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    final int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        final MrUser temp = new MrUser(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                tryGetString(udata, "u_name"),
                                tryGetString(udata, "email"),
                                tryGetString(udata, "bio"),
                                tryGetInt(udata, "gender"),
                                tryGetInt(udata, "age"),
                                tryGetString(udata, "pimageurl"));
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
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                retriever.failure(throwable.toString());
            }
        });
    }

    public void getPlaces(MrComunity com, final SirPlacesRetriever retriever) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:Place)-[r:COVERS]->(a:Sport) WHERE a.name=\"" + com.getComunityName() + "\" RETURN n, id(n)");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.e("response", response.toString());
                try {
                    ArrayList<MrPlace> places = new ArrayList<MrPlace>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    Log.e("respuesta", response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").toString());
                    int datos = dataf.length();
                    if (datos == 0) {
                        retriever.gotIt(places);
                        return;
                    }
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        places.add(new MrPlace(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                tryGetString(udata, "email"),
                                tryGetString(udata, "phone"),
                                tryGetString(udata, "address"),
                                tryGetFloat(udata, "X"),
                                tryGetFloat(udata, "Y"),
                                tryGetString(udata, "zone"),
                                tryGetString(udata, "FB"),
                                tryGetString(udata, "TW"),
                                tryGetString(udata, "IG"),
                                tryGetString(udata, "weekTime"),
                                tryGetString(udata, "weekendTime"),
                                tryGetString(udata, "website")));
                    }
                    Log.e("friends", places.size() + "");
                    retriever.gotIt(places);

                } catch (JSONException e) {
                    Log.e("exceptionPlace", e.getMessage());
                    retriever.failure(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("errorPlace", "code: " + statusCode + " " + throwable.toString());
                //  retriever.failure(throwable.toString());
            }
        });
    }

    public void getSportEvents(final String com, final SirEventsRetriever retriever) {

        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:Event)-[r:ABOUT]->(a:Sport) WHERE a.name=\"" + com + "\" RETURN n,id(n)");
            Log.i("statement", "MATCH (n:Event)-[r:isAbout]->(a:Sport) WHERE a.name=\"" + com + "\" RETURN n,id(n)");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.i("resonse", response.toString());
                try {
                    ArrayList<MrEvent> events = new ArrayList<MrEvent>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        MrEvent temp = new MrEvent(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                convertDate(tryGetString(udata, "dateStart")),
                                convertDate(tryGetString(udata, "dateEnd")),
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
                    retriever.failure(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
                retriever.failure(throwable.toString());
            }
        });
    }

    public void registerEvent(MrEvent event) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:user),(a:Event) WHERE id(a)=" + event.getId() + " AND id(n)=" + _currentUser.get_id() + " CREATE UNIQUE (n)-[r:isGoing]->(a) RETURN id(r),r");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                if (statusCode == 201) {
                    try {

                        JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                        JSONObject udata = dataf.getJSONObject(0).getJSONArray("row").getJSONObject(1);
                        AgendaMan.saveEvent(dataf.getJSONObject(0).getJSONArray("row").getInt(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mcontext, "Failed to register event, please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(mcontext, "Failed to register event, please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteEvent(final MrEvent event) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:user)-[r:isGoing]->(a:Event) WHERE id(n)=\"" + _currentUser.get_id() + "\" AND id(a)=\"" + event.getId() + "\" DELETE r RETURN 0");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d("EventDel", "No longer going to event: " + event.getId());
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }
        });
    }

    public void getUserEvents(final SirEventsRetriever retriever) {
        JSONObject cmd = new JSONObject();
        JSONArray cmds = new JSONArray();
        JSONObject subcmd = new JSONObject();
        try {
            subcmd.put("statement", "MATCH (n:user)-[r:isGoing]->(a:Event) WHERE id(n)=" + _currentUser.get_id() + " RETURN a,id(a)");
            cmds.put(subcmd);
            cmd.put("statements", cmds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                try {
                    ArrayList<MrEvent> events = new ArrayList<MrEvent>();
                    JSONArray dataf = response.getJSONArray("results").getJSONObject(0).getJSONArray("data");
                    int datos = dataf.length();
                    for (int i = 0; i < datos; i++) {
                        JSONObject udata = dataf.getJSONObject(i).getJSONArray("row").getJSONObject(0);
                        MrEvent temp = new MrEvent(dataf.getJSONObject(i).getJSONArray("row").getInt(1),
                                tryGetString(udata, "name"),
                                convertDate(tryGetString(udata, "dateStart")),
                                convertDate(tryGetString(udata, "dateEnd")),
                                tryGetString(udata, "organizer"),
                                tryGetString(udata, "descr"),
                                tryGetString(udata, "address"),
                                tryGetFloat(udata, "X"),
                                tryGetFloat(udata, "Y"));
                        String sportsA = tryGetString(udata, "esports");
                        if (!sportsA.isEmpty()) {
                            String[] sports = sportsA.split(",");
                            for (String sport : sports) {
                                if (sport.equals("Running") || sport.equals("Ciclismo") || sport.equals("Natación"))
                                    temp = temp.withDistance(tryGetFloat(udata, "distance"));
                                if (sport.equals("Triatlón"))
                                    temp = temp.withCategory(tryGetString(udata, "cat"));
                            }
                        }
                        float price = tryGetFloat(udata, "price");
                        temp = temp.withPrice((price == 0.0f) ? 0 : price);
                        events.add(temp);
                    }
                    retriever.gotIt(events);

                } catch (JSONException e) {
                    retriever.failure(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("error", "code: " + statusCode + " " + throwable.toString());
            }
        });

    }
}
