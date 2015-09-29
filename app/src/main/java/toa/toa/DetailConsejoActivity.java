package toa.toa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import toa.toa.Objects.MrConsejo;

public class DetailConsejoActivity extends AppCompatActivity {

    private String[] imgurlshc = {
            "http://www.sharesinv.com/wp-content/uploads/articles/healthcare-center-qlook.jpg"
    };
    private MrConsejo consejo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_consejo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            consejo = savedInstanceState.getParcelable("consejo");
        } else {
            consejo = getIntent().getParcelableExtra("consejo");
        }
        if (consejo == null) {
            onBackPressed();
            finish();
        }
        TextView text = (TextView) findViewById(R.id.dconsejo_consejo_txtv);
        TextView author = (TextView) findViewById(R.id.dconsejo_autor_txtv);
        text.setText(consejo.getDescripcion());
        author.setText(consejo.getAutor());
        ImageView back = (ImageView) findViewById(R.id.back_conseo_imgv);
        Picasso.with(getApplicationContext()).load(imgurlshc[0]).fit().centerCrop().into(back);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
