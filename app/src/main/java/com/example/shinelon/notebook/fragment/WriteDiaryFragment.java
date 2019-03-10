package com.example.shinelon.notebook.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.shinelon.notebook.MainActivity;
import com.example.shinelon.notebook.R;
import com.example.shinelon.notebook.activity.ShowDiaryActivity;
import com.example.shinelon.notebook.database.DiaryDao;
import com.example.shinelon.notebook.database.OpenHelper;
import com.example.shinelon.notebook.entity.Diary;
import com.example.shinelon.notebook.util.Configure;
import com.example.shinelon.notebook.util.LocationUtil;
import com.example.shinelon.notebook.util.Util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Shinelon on 2019/3/9.
 */

public class WriteDiaryFragment extends Fragment implements View.OnClickListener {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Configure.LOCATION_MESSAGE_CODE:
                    address.setText("于" + msg.obj);
                    break;
            }
        }
    };

    private static AppCompatActivity activity;
    private LocationUtil locationUtil;
    private TextView address;
    private TextView date;
    private ImageView addContent;
    private android.support.v7.app.AlertDialog addContentDialog;
    private android.support.v7.app.AlertDialog addImageDialog;
    private EditText title;
    private EditText textContent;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private int imageCount = 0;
    private String utilList = "";
    private Diary diary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_diary,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initLocation();
    }

    private void initLocation() {
        locationUtil = new LocationUtil(activity,handler);
        locationUtil.getLocation(this);
    }

    public static WriteDiaryFragment newInstance(MainActivity activity) {
        WriteDiaryFragment.activity = activity;
        WriteDiaryFragment fragment = new WriteDiaryFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationUtil.removeLocationUpdates();
        locationUtil = null;
    }

    private void initView(View view) {
        address = (TextView)view.findViewById(R.id.address);
        date = (TextView)view.findViewById(R.id.date);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        date.setText(dateString);

        title = (EditText)view.findViewById(R.id.title);
        textContent = (EditText)view.findViewById(R.id.text_content);
        image1 = (ImageView)view.findViewById(R.id.image1);
        image2 = (ImageView)view.findViewById(R.id.image2);
        image3 = (ImageView)view.findViewById(R.id.image3);
        image4 = (ImageView)view.findViewById(R.id.image4);

        addContent = (ImageView)view.findViewById(R.id.add_comtent);
        addContent.setTag(201);
        addContent.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Util util = new Util();
        switch ((Integer)v.getTag()){
            case 201:
                addContentDialog = util.getDialog(activity,selcctTypeView());
                addContentDialog.show();
                break;
            case 202:
                addContentDialog.dismiss();
                addImageDialog = util.getDialog(activity,selectWhichImageView());
                addImageDialog.show();
                break;
            case 203:
                addImageDialog.dismiss();
                String[] cameraPermissions = new String[]{Manifest.permission.CAMERA};
                boolean cameraPermissionsFlag = util.checkPermission(cameraPermissions,activity);
                if (cameraPermissionsFlag){
                    WriteDiaryFragment.this.requestPermissions(cameraPermissions,Configure.CAMERA_PERMISSION_CODE);
                }else {
                    openCamera();
                }
                break;
            case 204:
                addImageDialog.dismiss();
                String[] imagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                boolean imagePermissionsFlag = util.checkPermission(imagePermissions,activity);
                if (imagePermissionsFlag){
                    WriteDiaryFragment.this.requestPermissions(imagePermissions,Configure.IMAGE_PERMISSION_CODE);
                }else {
                    openImageFile();
                }
                break;
            case 205:
                addContentDialog.dismiss();
                if (saveToSQLite()){
                    Intent intent = new Intent(activity,ShowDiaryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("diary",diary);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    activity.finish();
                }
                break;
        }

    }

    private boolean saveToSQLite() {
        diary = new Diary();
        diary.setContent(textContent.getText().toString());
        if (title.getText().toString() == null ||"".equals(title.getText().toString())){
            diary.setTitle("无题");
        }else {
            diary.setTitle(title.getText().toString());
        }
        diary.setDate(date.getText().toString());
        diary.setAddress(address.getText().toString());
        diary.setAuthor(activity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("name",""));
        diary.setUri(utilList);
        OpenHelper openHelper = new OpenHelper(activity);
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        DiaryDao diaryDao = new DiaryDao(sqLiteDatabase);
        boolean flag = diaryDao.insert(diary);
        sqLiteDatabase.close();
        return flag;
    }

    private void addImageToLinearLayout(Bitmap bitmap){
        switch (imageCount){
            case 0:
                image1.setVisibility(View.VISIBLE);
                image1.setImageBitmap(bitmap);
                imageCount++;
                break;
            case 1:
                image2.setVisibility(View.VISIBLE);
                image2.setImageBitmap(bitmap);
                imageCount++;
                break;
            case 2:
                image3.setVisibility(View.VISIBLE);
                image3.setImageBitmap(bitmap);
                imageCount++;
                break;
            case 3:
                image4.setVisibility(View.VISIBLE);
                image4.setImageBitmap(bitmap);
                imageCount++;
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(activity,"成功获取权限",Toast.LENGTH_SHORT).show();
            switch (requestCode){
                case Configure.LOCATION_PERMISSION_CODE:
                    locationUtil.getLocation(this);
                    break;
                case Configure.CAMERA_PERMISSION_CODE:
                    openCamera();
                    break;
                case Configure.IMAGE_PERMISSION_CODE:
                    openImageFile();
                    break;
            }
        }else {
            Toast.makeText(activity,"获取权限失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageFile() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19){
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent,Configure.IMAGE_ALBUM);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURRE");
        startActivityForResult(intent,Configure.IMAGE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK){
            switch (requestCode){
                case Configure.IMAGE_CAMERA:
                    setBitmap(data,Configure.IMAGE_CAMERA);
                    break;
                case Configure.IMAGE_ALBUM:
                    setBitmap(data,Configure.IMAGE_ALBUM);
                    break;
            }
        }
    }

    private void setBitmap(Intent data, int from) {
        Uri uri = data.getData();
        Bitmap bitmap = null;
        if (uri == null){
            Bundle bundle = data.getExtras();
            if (bundle != null){
                bitmap = (Bitmap)bundle.get("data");
            }
        }else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),saveBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            addImageToLinearLayout(bitmap);
        }
    }

    private Uri saveBitmap(Bitmap bitmap) {
        File f = new File(getContext().getFilesDir(),System.currentTimeMillis() + "png");
        if (f.exists()){
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
            out.flush();
            out.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        utilList += f.getPath() + ";";
        return Uri.fromFile(f);
    }

    private View selcctTypeView(){
        LinearLayout typeLayout = new LinearLayout(activity);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setGravity(Gravity.CENTER);

        if (imageCount < 4){
            Button imageButton = new Button(activity);
            imageButton.setText("插入图片");
            imageButton.setTag(202);
            imageButton.setOnClickListener(this);
            typeLayout.addView(imageButton);
        }

        Button complete = new Button(activity);
        complete.setText("完成");
        complete.setTag(205);
        complete.setOnClickListener(this);
        typeLayout.addView(complete);
        return typeLayout;
    }

    private View selectWhichImageView(){
        LinearLayout typeLayout = new LinearLayout(activity);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setGravity(Gravity.CENTER);

        Button textButton = new Button(activity);
        textButton.setText("从相机获取");
        textButton.setTag(203);
        textButton.setOnClickListener(this);
        Button imageButton = new Button(activity);
        imageButton.setText("从相册获取");
        imageButton.setTag(204);
        imageButton.setOnClickListener(this);

        typeLayout.addView(textButton);
        typeLayout.addView(imageButton);
        return typeLayout;
    }

}
