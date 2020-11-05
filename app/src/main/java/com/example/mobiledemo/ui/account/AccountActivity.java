package com.example.mobiledemo.ui.account;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.login.LoginActivity;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.password.PasswordActivity;
import com.example.mobiledemo.ui.todo.TodoEditActivity;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AccountActivity extends AppCompatActivity {

    private AccountViewModel accountViewModel;
    private Button uploadButton;
    private ImageView mimage;
    Context context = this;
    private static final int REQUEST_CAPTURE = 101;
    private static final int REQUEST_TAKE_PHOTO = 1; // 拍照标识
    private static final int REQUEST_CHOOSE_PHOTO = 2; // 选择相册标示符
    // 获取拍照权限标识
    private static final int PERMISSION_REQUEST_TAKE_PHONE = 6;
    private static final int PERMISSION_REQUEST_CHOOSE_PICTURE = 7;
    private File output;  // 设置拍照的图片文件
    private String oldpw_me;
    private String gender_me;
    private String email_me;
    private String avatar_me = "1";
    private String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user?email=";
    private String updateurl = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user_update";
    private int genderindex = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        final Button backButton = findViewById(R.id.account_back);
        final Button passwordButton = findViewById(R.id.account_password);
        final Button logoutButton = findViewById(R.id.account_logout);
        uploadButton = findViewById(R.id.account_upload);
        final EditText birthdayText = findViewById(R.id.birthday);
        final Button getinfor = findViewById(R.id.getinf);
        final Button saveupdate = findViewById(R.id.savebutton);
        //String photoind = "1";
        //try { getIntent().getStringExtra("photoIndex");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
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
        mimage = findViewById(R.id.myPhoto);
        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcentralDialog();
//                Intent intent = new Intent(AccountActivity.this, ProfilePhotoActivity.class);
//                startActivity(intent);
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

        getinfor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                initListData();
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
        //初始化视图
        root.findViewById(R.id.btn_choose_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click choose image", Toast.LENGTH_SHORT).show();
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
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }


    public void setcentralDialog() {
        final Dialog photoDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.central_dialog, null);
        //初始化视图
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
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.x = 0; // 新位置X坐标
        lp.y = 600; // 新位置Y坐标
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        photoDialog.show();
    }

    private void choosePhoto() {
        // 选择相册操作
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Toast.makeText(getApplicationContext(), "chose", Toast.LENGTH_SHORT).show();
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
        switch (requestCode) {
            // 拍摄照片的回调
            case REQUEST_TAKE_PHOTO:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                mimage.setImageBitmap(imageBitmap);
                mimage.buildDrawingCache(true);
                mimage.buildDrawingCache();
                Bitmap bitmap = mimage.getDrawingCache();
                File file = new File(context.getFilesDir().getAbsolutePath(), "takePhoto");
                if (!file.exists()) {
                    // 如果文件路径不存在则直接创建一个文件夹
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "creat dir", Toast.LENGTH_SHORT).show();
                }
                // 把时间作为拍摄照片的保存路径;
                output = new File(file, System.currentTimeMillis() + ".jpg");
                String cx = output.toString();
                Toast.makeText(getApplicationContext(), cx, Toast.LENGTH_SHORT).show();
                if (file.exists()) {
                    // 如果文件路径不存在则直接创建一个文件夹
                    Toast.makeText(getApplicationContext(), "exist dir", Toast.LENGTH_SHORT).show();
                }
                // 如果该照片已经存在就删除它,然后新创建一个
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
                break;
            // 调用系统相册的回调
            case REQUEST_CHOOSE_PHOTO:
                Toast.makeText(this, "选择完毕", Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "进入验证", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    Toast.makeText(this, "uri 完毕", Toast.LENGTH_SHORT).show();
                    //使用content的接口
                    ContentResolver cr = this.getContentResolver();
                    Toast.makeText(this, "ContentResolver 完毕", Toast.LENGTH_SHORT).show();
                    try {
                        //获取图片
                        Bitmap bitmap2 = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        mimage.setImageBitmap(bitmap2);
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                } else {
                    //操作错误或没有选择图片
                    Log.i("MainActivtiy", "operation error");
                }
                //mCameraDialog.cancel();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListData() {
        final EditText phoneNumber = (EditText) findViewById(R.id.myPhonenumber);
        final EditText myNickname = (EditText) findViewById(R.id.myNickname);
        final EditText myBirthday = (EditText) findViewById(R.id.birthday);
        final Spinner myGender = findViewById(R.id.spinner_gender);
        final EditText myLocation = (EditText) findViewById(R.id.getlocation);
        RequestQueue mQueue = Volley.newRequestQueue(this.getApplicationContext());
        Toast.makeText(getApplicationContext(), "sent request", Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "123", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getApplicationContext(), "onResponse", Toast.LENGTH_SHORT).show();
                        try {
                            String ddd = response.getJSONObject(0).toString();
                            Toast.makeText(getApplicationContext(), ddd, Toast.LENGTH_SHORT).show();
                            int i = 0;
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
                            Log.d("TAG-photoavatar_me",avatar_me);
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
                            Log.d("TAG-photoavatarset",avatar_me);
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

    private void updateInf()  {
        final Spinner myGender = findViewById(R.id.spinner_gender);
        genderindex = myGender.getSelectedItemPosition();
        switch (genderindex) {
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
        RequestQueue updateQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateurl, new Response.Listener<String>() {
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
    }

    public void setphoto(String avatar) {
        ImageView mimage = findViewById(R.id.myPhoto);
        String photoaddress = "avatar_icon_"+avatar;
        int id=getResources().getIdentifier(photoaddress, "drawable", context.getPackageName());
        //int id=getResources().getIdentifier("avatar_icon_2", "drawable", context.getPackageName());
        Log.d("TAG-photo",Integer.toString(id));
        mimage.setImageResource(id);
    }
}



