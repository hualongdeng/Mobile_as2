package com.example.mobiledemo.ui.account;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import java.io.BufferedOutputStream;
import android.provider.DocumentsContract;
import android.database.Cursor;
import java.io.FileOutputStream;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import android.content.ContentUris;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
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
    private Uri photoUri;  // 拍摄照片的路径

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        final Button backButton = findViewById(R.id.account_back);
        final Button passwordButton = findViewById(R.id.account_password);
        final Button logoutButton = findViewById(R.id.account_logout);
        uploadButton = findViewById(R.id.account_upload);
        final EditText birthdayText = findViewById(R.id.birthday);

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
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
        mimage = findViewById(R.id.myphoto);
        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, ProfilePhotoActivity.class);
                startActivity(intent);
            }

        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
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

    public void setDialog() {
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

    private void choosePhoto() {
        // 选择相册操作
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Toast.makeText(getApplicationContext(), "chose", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);

    }

    private void takePhoto() {
        mimage = findViewById(R.id.myphoto);
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

}


