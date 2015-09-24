package toa.toa.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;
import toa.toa.MainActivity;
import toa.toa.Objects.MrUser;
import toa.toa.R;
import toa.toa.utils.misc.SirUserRetrieverClass;

public class RecoverPWActivity extends AppCompatActivity {

    Random r = new Random();
    private final int key = r.nextInt(10000 - 1000) + 1000;
    private int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText email = (EditText) findViewById(R.id.email_recover);
        final CoordinatorLayout root = (CoordinatorLayout) findViewById(R.id.rootLayout);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextInputLayout input = (TextInputLayout) findViewById(R.id.input);
        final Snackbar snackbar = Snackbar.make(root, "Procesando...", Snackbar.LENGTH_INDEFINITE);
        final View.OnClickListener clickC = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disable(email);
                fab.hide();
                if (snackbar.isShown())
                    snackbar.dismiss();
                snackbar.setText("Procesando..");
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                if (email.getText().toString().isEmpty() || email.getText().toString().length() < 4) {
                    input.setError("La contraseña debe tener al menos 4 caracteres");
                    enable(email);
                    snackbar.dismiss();
                    return;
                }
                final String pw = email.getText().toString();
                Log.e("id", _id + "");
                SirHandler.getUserById(_id, new SirUserRetrieverClass() {
                    @Override
                    public void goIt(MrUser user) {
                        SirHandler.__hash = Base64.encodeToString((user.get_name() + pw).getBytes(), Base64.DEFAULT);
                        SirHandler.updateUserAsync(user);
                        if (snackbar.isShown())
                            snackbar.dismiss();

                        snackbar.setText("Contraseña cambiada satisfactoriamente");
                        snackbar.setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                        snackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {
                                super.onShown(snackbar);
                            }
                        });
                        snackbar.show();
                    }

                    @Override
                    public void failure(String error) {
                        snackbar.setText("Eror. :/");
                        snackbar.setDuration(Snackbar.LENGTH_SHORT);
                        enable(email);
                        showFAB(fab);

                    }
                });

            }
        };
        final View.OnClickListener clickB = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disable(email);
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                snackbar.setText("Procesando...");
                snackbar.show();
                if (email.getText().toString().isEmpty()) {
                    input.setError("Codigo vacío");
                    enable(email);
                    snackbar.dismiss();
                    return;
                }
                fab.hide();
                final int tmpkey = Integer.parseInt(email.getText().toString());
                if (tmpkey == key) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    snackbar.setText("Yay, ingresa tu nueva contrasela.");
                    snackbar.setDuration(Snackbar.LENGTH_LONG);
                    snackbar.show();
                    email.setFilters(new InputFilter[]{});
                    email.setText("");
                    input.setHint("Nueva contraseña");
                    email.setInputType(InputType.TYPE_CLASS_TEXT);
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_white_48dp));
                    fab.setOnClickListener(clickC);
                    showFAB(fab);
                    enable(email);
                    if (snackbar.isShown())
                        snackbar.dismiss();
                    snackbar.setText("Ingresa tu nueva contraseña");
                    snackbar.setDuration(Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }


            }
        };
        View.OnClickListener clickA =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fab.hide();
                        disable(email);
                        final String emailstr = email.getText().toString();
                        if (emailstr.isEmpty()) {
                            input.setError("");
                            enable(email);
                            showFAB(fab);
                            snackbar.dismiss();
                            return;
                        }
                        if (!emailstr.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                            input.setError("Email inválido");

                            enable(email);
                            showFAB(fab);
                            return;

                        }
                        snackbar.setText("Procesando...");
                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        if (!UtilidadesExtras.isOnline(getApplicationContext())) {
                            snackbar.setText("Conexión a internet no detectada.");
                            enable(email);
                            showFAB(fab);
                            snackbar.dismiss();
                        }
                        JSONObject cmd = new JSONObject();
                        JSONArray cmds = new JSONArray();
                        JSONObject subcmd = new JSONObject();
                        try {
                            subcmd.put("statement", "MATCH (n:user {email:\"" + emailstr + "\"}) RETURN count(*), id(n)");
                            cmds.put(subcmd);
                            cmd.put("statements", cmds);
                            RestApi.post("/transaction/commit", cmd, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    try {
                                        if (response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").getInt(0) == 0) {
                                            input.setError("Usuario no encontrado");
                                            enable(email);
                                            snackbar.dismiss();
                                            showFAB(fab);
                                            return;
                                        }
                                        setId(response.getJSONArray("results").getJSONObject(0).getJSONArray("data").getJSONObject(0).getJSONArray("row").getInt(1));
                                        snackbar.setText("En breve te enviaremos un correo");
                                        RestApi.recoverPw(key, emailstr, new TextHttpResponseHandler() {
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                snackbar.setText("Eror. :/");
                                                snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                                enable(email);
                                                snackbar.show();
                                                showFAB(fab);
                                            }

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                Log.e("response", responseString);
                                                if (!responseString.isEmpty()) {
                                                    int r = Integer.parseInt(responseString);
                                                    if (r == -1) {
                                                        snackbar.setText("Eror al enviar correo. :/");
                                                        snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                                        enable(email);
                                                        snackbar.show();
                                                        showFAB(fab);
                                                        return;
                                                    }
                                                }
                                                email.setText("");
                                                snackbar.setText("Revisa tu correo, te hemos enviado un código");
                                                input.setHint("Código de acceso");
                                                InputFilter[] FilterArray = new InputFilter[1];
                                                FilterArray[0] = new InputFilter.LengthFilter(4);
                                                email.setFilters(FilterArray);
                                                email.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                enable(email);
                                                snackbar.setDuration(Snackbar.LENGTH_LONG);
                                                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_forward_white_24dp));
                                                fab.setOnClickListener(clickB);
                                                showFAB(fab);
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        snackbar.setText("Eror. :/");
                                        snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                        enable(email);

                                        snackbar.show();
                                        showFAB(fab);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    snackbar.setText("Eror. :/");
                                    snackbar.setDuration(Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    enable(email);
                                    showFAB(fab);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            enable(email);
                            snackbar.setText("Eror. :/");
                            snackbar.setDuration(Snackbar.LENGTH_SHORT);

                            snackbar.show();
                            showFAB(fab);
                        }

                    }
                };

        fab.setOnClickListener(clickA);
    }

    private void setId(int i) {
        Log.i("new ID", i + "");
        _id = i;
        Log.i("actual", _id + "");
    }

    private void disable(EditText edit) {
        edit.setEnabled(false);
    }

    private void enable(EditText edit) {
        edit.setEnabled(true);
    }

    private void showFAB(final FloatingActionButton fab) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 1000);
    }
}
