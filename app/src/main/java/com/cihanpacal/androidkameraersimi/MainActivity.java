package com.cihanpacal.androidkameraersimi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> permissionResultLauncher;
    ActivityResultLauncher<Uri> takePictureResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerCallback();
        accessToCamera();
    }


    private void accessToCamera(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            //goto camera
            takePictureResultLauncher.launch(getTakenPictureUri());
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                //show why
                Toast.makeText(this, "İzin gerekli", Toast.LENGTH_SHORT).show();
            }else{
                //rquest permission
                permissionResultLauncher.launch(Manifest.permission.CAMERA);
            }
        }else{
            //request permisssion
            permissionResultLauncher.launch(Manifest.permission.CAMERA);
        }
    }


    private void registerCallback(){
        permissionResultLauncher= registerForActivityResult(new ActivityResultContracts.RequestPermission(),new ActivityResultCallback<Boolean>(){

            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //goto camera
                    takePictureResultLauncher.launch(getTakenPictureUri());
                }else{
                    //request neeeded
                    Toast.makeText(MainActivity.this, "İzin gerekli", Toast.LENGTH_SHORT).show();
                }
            }
        });

        takePictureResultLauncher=registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                Toast.makeText(MainActivity.this, "Resim alındı", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Uri getTakenPictureUri(){
        File filesDir=getFilesDir();
        File imageFile=new File(filesDir,"taken_picture.jpg");
        return FileProvider.getUriForFile(this, "com.cihanpacal.androidkameraersimi.fileprovider", imageFile);
    }




}