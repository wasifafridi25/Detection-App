package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int requestCamera = 1;
    int request = 0;
    private ZXingScannerView zxingScannerVeiw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        zxingScannerVeiw = new ZXingScannerView(this);
        setContentView(zxingScannerVeiw);

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(MainActivity.this, "Permission has been granted", Toast.LENGTH_SHORT).show();
            }
            else {
                requestPermission();
            }

        }*/

    }

    /*private boolean checkPermission(){
        return(ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, requestCamera);
    }*/
    /*public void onRequestPermissionsResult(int requestCode, String permission[], int acceptResults[]){
        switch(requestCode){
            case requestCamera:
                if(acceptResults.length > 0){
                    boolean cameraAccepted = acceptResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted)
                        Toast.makeText(MainActivity.this, "camera usage allowed", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(MainActivity.this, "camera usage NOT allowed", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                alertMsg("Need to allow access to camera", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA}, requestCamera);
                                    }
                                });
                               return;
                            }
                        }
                    }
                }
                break;
        }
    }*/

    protected void onPause() {
        super.onPause();
        zxingScannerVeiw.stopCamera();
    }
    //@Override
   /* public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(zxingScannerVeiw == null){
                    zxingScannerVeiw = new ZXingScannerView(this);
                    setContentView(zxingScannerVeiw);
                }
                zxingScannerVeiw.setResultHandler(this);
                zxingScannerVeiw.startCamera();
            }
        }
        else{
            requestPermission();
        }
    }*/
    /*@Override
    public void onDestroy(){
        super.onDestroy();
        zxingScannerVeiw.stopCamera();
    }*/
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    request);
        }
        zxingScannerVeiw.setResultHandler(this);
        zxingScannerVeiw.startCamera();
    }
   /* public void alertMsg(String msg, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(msg)
                .setPositiveButton("Allow", listener)
                .setNegativeButton("Cancel", null)
                .create().show();


    }*/

    @Override
    public void handleResult(final Result result) {
        final String barOrQRcodeResult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Result after scanned");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int was){
                zxingScannerVeiw.resumeCameraPreview(MainActivity.this);
            }
        });
        builder.setNeutralButton("Visit Site", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent site = new Intent(Intent.ACTION_VIEW, Uri.parse(barOrQRcodeResult));
                startActivity(site);

            }
        });
        builder.setMessage(barOrQRcodeResult);
        AlertDialog alert =builder.create();
        alert.show();
    }
}