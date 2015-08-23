/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import toa.toa.Objects.MrUser;
import toa.toa.utils.RealPathUtil;
import toa.toa.utils.TOA.SirHandler;
import toa.toa.utils.TOA.SirImageSelectorInterface;

public class EditProfileActivity extends AppCompatActivity implements SirImageSelectorInterface {

    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=archivestoa;" +
                    "AccountKey=ASFIlz+A19O/sBxAaRHWhzBce+HkSEMgA4iZ+9R08c816CWL3J+daLc3ykFW7Z2LY54IMZ7beyjHpBN/x/LeVg==";
    private static final int CHOICE_AVATAR_FROM_CAMERA_CROP = 0;
    private static final int CHOICE_AVATAR_FROM_GALLERY = 1;
    private static final int CHOICE_AVATAR_FROM_CAMERA = 2;
    private static String imagePath = "";
    ImageView pimage_imgv;
    private TextView username;
    private EditText name, bio, age, email;
    private Spinner sex;
    private MrUser _cuser;
    private SirHandler handler;
    private String cameraFileName;

    /**
     * Use for decoding camera response data.
     *
     * @param data
     * @return
     */
    public Bitmap getBitmapFromData(Intent data) {
        Bitmap photo = null;
        Uri photoUri = data.getData();
        if (photoUri != null) {

            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                imagePath = uriToFilename(photoUri);
                Log.i("newImagePath", "path: "+imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (photo == null) {
            Bundle extra = data.getExtras();
            if (extra != null) {
                photo = (Bitmap) extra.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (photo != null) {
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                }
            }
        }

        return photo;
    }

    private String uriToFilename(Uri uri) {
        String path = null;
        if (Build.VERSION.SDK_INT < 19) {
            path = RealPathUtil.getRealPathFromURI_API11to18(getApplicationContext(), uri);
        } else {
            path = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), uri);
        }

        return path;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (Build.VERSION.SDK_INT > 19) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.cnt);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = (TextView) findViewById(R.id.editProf_uname_txtv);
        name = (EditText) findViewById(R.id.editProf_name_etxtv);
        bio = (EditText) findViewById(R.id.editProf_bio_etxt);
        pimage_imgv = (ImageView) findViewById(R.id.editProf_pimage_imv);
        age = (EditText) findViewById(R.id.editProf_age_etxt);
        email = (EditText) findViewById(R.id.editProf_email_etxt);
        sex = (Spinner) findViewById(R.id.editProf_sex_spinner);
        handler = new SirHandler(getApplicationContext());
        _cuser = handler.getCurrentUser();
        username.setText(_cuser.get_name());
        name.setText(_cuser.get_uname());
        bio.setText(_cuser.get_bio());
        email.setText(_cuser.get_email());
        if (!_cuser.get_pimage().isEmpty()) {
            Picasso.with(getApplicationContext()).load(_cuser.get_pimage()).transform(new CropCircleTransformation()).into(pimage_imgv);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.defaultpimage).transform(new CropCircleTransformation()).into(pimage_imgv);
        }
        pimage_imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //age.setText(MrUser.get_age());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sex.setAdapter(adapter);
        sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        _cuser.set_gender(-1);
                        break;
                    default:
                        _cuser.set_gender(position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    choiceAvatarFromCamera();
                } else if (items[item].equals("Choose from Library")) {
                    choiceAvatarFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editprof_done) {
            do_the_thing();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void do_the_thing() {

        if (!age.getText().toString().isEmpty())
            _cuser.set_age(Integer.parseInt(age.getText().toString()));
        _cuser.set_bio(bio.getText().toString());
        _cuser.set_uname(name.getText().toString());
        _cuser.set_email(email.getText().toString());
        if(!imagePath.isEmpty()) {
            UpdateProfile task = new UpdateProfile();
            task.execute();
        } else {
            handler.updateUserAsync(_cuser);
        }
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
    public void choiceAvatarFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraFileName = getString(R.string.TOA_IMAGES_PATH) + System.currentTimeMillis()+".jpg";
        File file = new File(getString(R.string.TOA_IMAGES_PATH));
        if (!file.exists()) {
            file.mkdirs();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraFileName)));
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CHOICE_AVATAR_FROM_CAMERA);
    }

    @Override
    public void choiceAvatarFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/jpeg");
        startActivityForResult(getIntent, CHOICE_AVATAR_FROM_GALLERY);
    }

    private Intent getCropIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOICE_AVATAR_FROM_CAMERA || requestCode == CHOICE_AVATAR_FROM_GALLERY) {
                Toast.makeText(this, "CHOICE_AVATAR_FROM_CAMERA", Toast.LENGTH_SHORT).show();
                Bitmap avatar = getBitmapFromData(data);
                pimage_imgv.setImageBitmap(avatar);
                // this bitmap is the finish image
            } else if (requestCode == CHOICE_AVATAR_FROM_CAMERA_CROP) {
                Intent intent = new Intent("com.android.camera.action.CROP");
                Uri uri = Uri.fromFile(new File(cameraFileName));
                imagePath = uriToFilename(uri);
                intent.setDataAndType(uri, "image/*");
                startActivityForResult(getCropIntent(intent), CHOICE_AVATAR_FROM_CAMERA);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class UpdateProfile extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Retrieve storage account from connection-string.
                CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

                // Create the blob client.
                CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

                // Retrieve reference to a previously created container.
                CloudBlobContainer container = blobClient.getContainerReference("app-images");

                // Define the path to a local file.
                final String filePath = imagePath;
                Log.i("filePathToUpload", filePath);
                // Create or overwrite the "myimage.jpg" blob with contents from a local file.
                CloudBlockBlob blob = container.getBlockBlobReference("pimage-" + _cuser.get_id() + "-" + _cuser.get_name() + ".jpg");
                //File source = new File(filePath);
                blob.uploadFromFile(filePath);
                //blob.upload(new FileInputStream(source), source.length());
                _cuser.set_pimage("https://archivestoa.blob.core.windows.net/app-images/pimage-" + _cuser.get_id() + "-" + _cuser.get_name() + ".jpg");
                Picasso.with(getApplicationContext()).invalidate(_cuser.get_pimage());
                return "Yeah";
            } catch (Exception e) {
                e.printStackTrace();
                return e.getLocalizedMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            handler.updateUserAsync(_cuser);
        }
    }
}
