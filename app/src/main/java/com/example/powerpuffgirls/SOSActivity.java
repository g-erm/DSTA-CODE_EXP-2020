package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SOSActivity extends AppCompatActivity {

    final static int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    private CameraManager mCameraManager;
    private String mCameraId;
    private ToggleButton toggleButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String latitude = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(mAuth.getUid()).child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showLatitude(dataSnapshot);
                showLongitude(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Retrieve NRIC Fail", Toast.LENGTH_SHORT).show();
            }
        });

        final boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Toast.makeText(getApplicationContext(), Boolean.toString(isFlashAvailable), Toast.LENGTH_SHORT).show();
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        toggleButton = findViewById(R.id.onOffFlashlight);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isFlashAvailable) {
                    switchFlashLight(isChecked);
                }
                if (isChecked) {
                    alert();
                }
            }
        });

        if(checkPermission(Manifest.permission.SEND_SMS)) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    protected void showLatitude (DataSnapshot dataSnapshot) {
        latitude = dataSnapshot.child("latitude").getValue().toString();
    }

    protected void showLongitude (DataSnapshot dataSnapshot) {
        longitude = dataSnapshot.child("longitude").getValue().toString();
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void alert() {
        String phoneNumber = "1000";
        String smsMessage = "I need help!";

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            if (phoneNumber == null || phoneNumber.length() == 0) {
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void switchFlashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
