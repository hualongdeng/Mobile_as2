package com.example.mobiledemo.ui.account;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.login.LoginActivity;
import com.example.mobiledemo.ui.password.PasswordActivity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.Calendar;
import android.Manifest;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.net.Uri;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private Button uploadButton;
    private ImageView mimage;
    Context context = this;
    private static final int REQUEST_TAKE_PHOTO = 1; // take photo identifier
    private static final int REQUEST_CHOOSE_PHOTO = 2; // Select album identifier
    // Obtain the photo permission ID
    private static final int PERMISSION_REQUEST_TAKE_PHONE = 6;
    private static final int PERMISSION_REQUEST_CHOOSE_PICTURE = 7;
    private File output;  // Set the picture file for taking pictures
    private final String Url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user?email=";//Get user information url
    private final String updateUrl = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user_update";//post user information url
    private final String locationUrl = "https://maps.googleapis.com/maps/api/geocode/json?";//google map url
    private final String mapAPI_key = "&key=AIzaSyBF2UDl9_r_TlLnjlYnGsufEbg6Xcd7oAs";// google map API key
    private String geo_location;
    private int genderIndex = 0;
    private Bitmap myBitmap;
    private String city = "Unknown";
    private String country = "Unknown";
    private String real_location = "Unknown";
    private String oldpw_me;
    private String gender_me;
    private String email_me;
    private String avatar_me = "1"; //default

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        final Button backButton = findViewById(R.id.account_back);
        final Button passwordButton = findViewById(R.id.account_password);
        final Button logoutButton = findViewById(R.id.account_logout);
        final EditText birthdayText = findViewById(R.id.birthday);
        final Button saveupdate = findViewById(R.id.savebutton);
        final Button locationinf = findViewById(R.id.getlocainf);
        mimage = findViewById(R.id.myPhoto);
        uploadButton = findViewById(R.id.account_upload);

        String login_account = this.getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        Log.d("TAG-login_account", login_account);
        initListData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        locationinf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGeoLocation();
            }
        });

        birthdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText eText = (EditText) findViewById(R.id.birthday);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datepicker = new DatePickerDialog(AccountActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });

        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcentralDialog();
            }

        });

        saveupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInf();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbottomDialog();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void setbottomDialog() {
        final Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bottom_dialog, null);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "click choose image", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CHOOSE_PICTURE);
                } else {
                    choosePhoto();
                    mCameraDialog.hide();
                }
            }
        });
        root.findViewById(R.id.btn_open_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "checkSelfPermission  camera", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_TAKE_PHONE);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission camera pass", Toast.LENGTH_SHORT).show();
                    takePhoto();
                    mCameraDialog.hide();
                }
            }
        });

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // Get the current parameter value of the dialog box
        lp.x = 0; // X coordinate of new position
        lp.y = 0; // Y coordinate of new position
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // transparency
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    public void setcentralDialog() {
        final Dialog photoDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.central_dialog, null);
        root.findViewById(R.id.photoButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("1");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("2");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("3");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("4");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("5");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("6");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("7");
                photoDialog.hide();
            }
        });
        root.findViewById(R.id.photoButton8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setphoto("8");
                photoDialog.hide();
            }
        });
        photoDialog.setContentView(root);
        Window dialogWindow = photoDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.x = 0;
        lp.y = 600;
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        photoDialog.show();
    }

    private void choosePhoto() {
        mimage = findViewById(R.id.myPhoto);
        // Select album operation
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);

    }

    private void takePhoto() {
        mimage = findViewById(R.id.myPhoto);
        Intent imagetakeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(imagetakeintent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String login_account = this.getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        switch (requestCode) {
            // Callback for taking photos
            case REQUEST_TAKE_PHOTO:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                mimage.setImageBitmap(imageBitmap);
                mimage.buildDrawingCache(true);
                mimage.buildDrawingCache();
                Bitmap bitmap = mimage.getDrawingCache();
                File file = new File(context.getFilesDir().getAbsolutePath(), "ProfilePicture");
                if (!file.exists()) {
                    // If the file path does not exist, create a folder directly
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "creat dir", Toast.LENGTH_SHORT).show();
                }
                // The temporary save path of the taken photos;
                output = new File(file, login_account + "temp.jpg");
                // If the photo already exists, delete it and create a new one
                try {
                    if (output.exists()) {
                        output.delete();
                    }
                    output.createNewFile();
                    Toast.makeText(this, "create success", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "IO error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                avatar_me = "9";
                break;

            // Call the callback of the system album
            case REQUEST_CHOOSE_PHOTO:
                Toast.makeText(this, "CHOOSE_PHOTO", Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    //Use content interface
                    ContentResolver cr = this.getContentResolver();
                    try {
                        //Get pictures
                        myBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        mimage.setImageBitmap(myBitmap);
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                } else {
                    //Operation error or no picture selected
                    Log.i("MainActivtiy", "operation error");
                }
                avatar_me = "10";
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initListData() {
        final EditText phoneNumber = (EditText) findViewById(R.id.myPhonenumber);
        final EditText myNickname = (EditText) findViewById(R.id.myNickname);
        final EditText myBirthday = (EditText) findViewById(R.id.birthday);
        final Spinner myGender = findViewById(R.id.spinner_gender);
        final EditText myLocation = (EditText) findViewById(R.id.getlocation);
        String login_account = this.getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        Log.d("TAG-login_account", login_account);
        RequestQueue mQueue = Volley.newRequestQueue(this.getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url + login_account, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), "onResponse", Toast.LENGTH_SHORT).show();
                        try {
                            int i = 3;
                            //int id = response.getJSONObject(0).getInt("id");
                            //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                            String birthday = response.getJSONObject(0).getString("birthday");
                            myBirthday.setText(birthday);
                            String nickname = response.getJSONObject(0).getString("nickname");
                            myNickname.setText(nickname);
                            String phone = response.getJSONObject(0).getString("phone");
                            phoneNumber.setText(phone);
                            String contry = response.getJSONObject(0).getString("location");
                            myLocation.setText(contry);
                            email_me = response.getJSONObject(0).getString("email");
                            avatar_me = response.getJSONObject(0).getString("avatar");
                            oldpw_me = response.getJSONObject(0).getString("password");
                            String gender = response.getJSONObject(0).getString("gender");
                            switch (gender) {
                                case "male":
                                    i = 0;
                                    break;
                                case "female":
                                    i = 1;
                                    break;
                                case "others":
                                    i = 2;
                                    break;
                                case "prefer not to say":
                                    i = 3;
                                    break;
                            }
                            myGender.setSelection(i);
                            setphoto(avatar_me);
                            // String place = response.getJSONObject(i).getString("location");
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "catch exception", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonArrayRequest);
    }

    private void updateInf() {
        final Spinner myGender = findViewById(R.id.spinner_gender);
        genderIndex = myGender.getSelectedItemPosition();
        switch (genderIndex) {
            case 0:
                gender_me = "male";
                break;
            case 1:
                gender_me = "female";
                break;
            case 2:
                gender_me = "others";
                break;
            case 3:
                gender_me = "prefer not to say";
                break;
        }
        if (avatar_me.equals("9") || (avatar_me.equals("10"))) {
            savephoto(avatar_me);
        }
        RequestQueue updateQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG-target", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG-err", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final EditText phoneNumber2 = (EditText) findViewById(R.id.myPhonenumber);
                final EditText myNickname2 = (EditText) findViewById(R.id.myNickname);
                final EditText myBirthday2 = (EditText) findViewById(R.id.birthday);
                final EditText myLocation2 = (EditText) findViewById(R.id.getlocation);
                Map<String, String> map = new HashMap<String, String>();
                map.put("nickname", myNickname2.getText().toString());
                map.put("username", "admin");
                map.put("old_password", oldpw_me);
                map.put("gender", gender_me);
                map.put("birthday", myBirthday2.getText().toString());
                map.put("phone", phoneNumber2.getText().toString());
                Log.d("TAG-phoneNumber2", phoneNumber2.getText().toString());
                map.put("location", myLocation2.getText().toString());
                map.put("avatar", avatar_me);
                map.put("email", email_me);
                map.put("new_password", oldpw_me);
                return map;
            }
        };
        updateQueue.add(stringRequest);
        Toast.makeText(this, "Account information saved", Toast.LENGTH_SHORT).show();
    }

    private void setphoto(String avatar) {
        ImageView mimage = findViewById(R.id.myPhoto);
        String login_account = this.getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        if (avatar.equals("9")) {
            File file = new File(context.getFilesDir().getAbsolutePath(), "ProfilePicture");
            File filepath = new File(file, login_account + "_final.jpg");
            Bitmap bitmap = BitmapFactory.decodeFile(filepath.toString());//Load the image bitmap from the path
            mimage.setImageBitmap(bitmap);//ImageView displays pictures
        } else {
            String photoaddress = "avatar_icon_" + avatar;
            int id = getResources().getIdentifier(photoaddress, "drawable", context.getPackageName());
            Log.d("TAG-photo", Integer.toString(id));
            mimage.setImageResource(id);
        }
        avatar_me = avatar;
    }

    private void savephoto(String avatar) {
        String login_account = this.getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        if (avatar.equals("9")) {
            Bitmap bitmap = mimage.getDrawingCache();
            File file = new File(context.getFilesDir().getAbsolutePath(), "ProfilePicture");
            if (!file.exists()) {
                file.mkdirs();
                Toast.makeText(getApplicationContext(), "creat dir", Toast.LENGTH_SHORT).show();
            }
            File output2 = new File(file, login_account + "_final.jpg");
            // If the photo already exists, delete it and create a new one
            try {
                if (output2.exists()) {
                    output2.delete();
                }
                output2.createNewFile();
                Toast.makeText(this, "create success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "IO error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output2));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                Log.e("Tag-save", "flush");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (avatar.equals("10")) {
            // Select album photo as profile picture
            File file2 = new File(context.getFilesDir().getAbsolutePath(), "ProfilePicture");
            if (!file2.exists()) {
                file2.mkdirs();
                Toast.makeText(getApplicationContext(), "creat dir", Toast.LENGTH_SHORT).show();
            }
            File output2 = new File(file2, login_account + "_final.jpg");
            try {
                if (output2.exists()) {
                    output2.delete();
                }
                output2.createNewFile();
                Toast.makeText(this, "create success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "IO error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output2));
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            avatar_me = "9";
        }

    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }};

    private void getGeoLocation() {
        //Obtain latitude and longitude information through NETWORK provider or GPS provider
        getLatLonLocation(context);
        final EditText myLocation = (EditText) findViewById(R.id.getlocation);
        RequestQueue mQueue = Volley.newRequestQueue(this.getApplicationContext());
        //Transform longitude and latitude into location information through geocodingAPI.
        //If the user is in mainland China, the information may be blocked
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, locationUrl +geo_location+ mapAPI_key, null,
                 new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Obtain the required location information from the returned json package
                            JSONArray APIresult = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                            int lenth = APIresult.length();
                            Log.e("TAG-lenth", Integer.toString(lenth));
                            for (int i = 0; i < lenth; i++) {
                                Log.e("TAG-index", Integer.toString(i));
                                JSONObject result_city = APIresult.getJSONObject(i);
                                String types = result_city.getString("types");
                                if (types.equals("[\"country\",\"political\"]")){
                                    Log.e("TAG-result", "got it  ");
                                    country = result_city.getString("short_name");
                                    Log.e("TAG-country", country);
                                }
                                if (types.equals("[\"administrative_area_level_1\",\"political\"]")){
                                    Log.e("TAG-result", "got it  ");
                                    city = result_city.getString("long_name");
                                    Log.e("TAG-city", city);
                                }
                            }
                            real_location = city+", "+country;
                            myLocation.setText(real_location);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonArrayRequest);
    }

    private void getLatLonLocation(Context context) {
        LocationManager locationManager;
        String locationProvider;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String test = locationManager.toString();
        Log.e("TAG-test", test);
        List<String> providers = locationManager.getProviders(true);
        String output = providers.toString();
        Log.e("TAG-output", output);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Toast.makeText(this, "NETWORK_PROVIDER", Toast.LENGTH_SHORT).show();
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
            Toast.makeText(this, "GPS_PROVIDER", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Location Provider, Please check permission", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location!=null){
            showLocation(location);
        }else{
            // To monitor changes in geographic location, the second and third parameters are the updated minimum time minTime and minimum distance minDistace respectively
            locationManager.requestLocationUpdates(locationProvider, 0, 0,mListener);
        }
    }

    private void showLocation(Location location){
        geo_location= "latlng="+location.getLatitude()+","+location.getLongitude();
        //test location
        //geo_location= "latlng="+"30"+","+"121";
    }



}




