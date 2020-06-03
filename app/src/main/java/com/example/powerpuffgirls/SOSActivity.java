package com.example.powerpuffgirls;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SOSActivity extends AppCompatActivity {

    final static int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    public static String policeNumber = "0099900";

    private CameraManager mCameraManager;
    private String mCameraId;

    private String latitude, longitude, name, eContact1, eContact2;

    private static SmsManager smsManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        Intent caller = getIntent();
        name = caller.getStringExtra("name");
        eContact1 = caller.getStringExtra("eContact1");
        eContact2 = caller.getStringExtra("eContact2");
        longitude = caller.getStringExtra("longitude");
        latitude = caller.getStringExtra("latitude");

        final boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//        Toast.makeText(getApplicationContext(), "Flash Available: " + Boolean.toString(isFlashAvailable), Toast.LENGTH_SHORT).show();
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (isFlashAvailable) {
            switchFlashLight(true);
        }

        if(!checkPermission(Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            smsManager = SmsManager.getDefault();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

        alert();

        Button notifyButton = findViewById(R.id.notifyButton);
        Button safeButton = findViewById(R.id.safeButton);

        notifyButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                informPolice();
                return false;
            }
        });

        safeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isFlashAvailable) {
                    switchFlashLight(false);
                }
                startActivity(new Intent(SOSActivity.this, MenuActivity.class));
                finish();
                return false;
            }
        });
    }

    private void informPolice() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy @ hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        String smsMessage = "I ";
        if (!name.isEmpty()) smsMessage += "(" + name + ") ";
        smsMessage += "need help! My last known location is at (longitude = "
                + cleanString(longitude) + ", latitude = " + cleanString(latitude) + "), "
                + "help requested at " + cleanString(format) + " (GMT)";
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            smsManager.sendTextMessage(policeNumber, null, smsMessage, null, null);
            Toast.makeText(this, "Informed Police!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private String cleanString(String string) {
        if (string.isEmpty()) {
            return "Not Known";
        } else {
            return string;
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void alert() {
        String[] phoneNumbers = new String[]{eContact1, eContact2};
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy @ hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        String smsMessage = "I ";
        if (!name.isEmpty()) smsMessage += "(" + name + ") ";
        smsMessage += "need help! My last known location is at (longitude = "
            + cleanString(longitude) + ", latitude = " + cleanString(latitude) + "), "
            + "help requested at " + cleanString(format) + " (GMT)";

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            boolean sent = false;
            for (String phoneNumber: phoneNumbers) {
                if (isNumeric(phoneNumber)) {
                    sent = true;
                    smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                }
            }
            if (sent)
                Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "No available Emergency Contacts", Toast.LENGTH_SHORT).show();
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
