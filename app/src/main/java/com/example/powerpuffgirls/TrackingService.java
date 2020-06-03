package com.example.powerpuffgirls;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    private static final String PROXIMITY_ALERT = "com.example.powerpuffgirls.ProximityAlert";
    private static final float POINT_RADIUS = 100;
    private static final long PROX_ALERT_EXPIRATION = -1;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        requestLocationUpdates();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.powerpuffgirls";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.tracking_enabled)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Unregister the BroadcastReceiver when the notification is tapped//

            unregisterReceiver(stopReceiver);

            //Stop the Service//

            stopSelf();
        }
    };


    //Initiate the request to track the device's location//

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

        //Specify how often your app should request the device’s location//

        request.setInterval(10000);

        //Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "location";
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

            //...then request location updates//
            getUserDetails();

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    //Get a reference to the database, so your app can perform read and write operations//

                    Location location = locationResult.getLastLocation();
                    if (location != null && (mAuth.getCurrentUser() != null)) {
                        //Save the location data to the database//

                        mDatabase.child("users").child(mAuth.getUid()).child(path).setValue(location);
                    }
                }
            }, null);

            checkIfNearInfectedPlaces();

        }
    }

    private void checkIfNearInfectedPlaces() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("clusters/coordinates.csv");
        File localFile = null;

        try {
            localFile = File.createTempFile("clusters", "csv");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Success get Coords", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getApplicationContext(), "Fail get Coords", Toast.LENGTH_SHORT).show();

            }
        });

        List<String[]> rows = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(localFile));
            String line;
            String csvSplitBy = ",";

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                rows.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Intent intent = new Intent(PROXIMITY_ALERT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        for (int i = 0; i < rows.size(); i++) {
            double lat = Double.parseDouble(rows.get(i)[0]);
            double longitude = Double.parseDouble(rows.get(i)[1]);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                lm.addProximityAlert(lat, longitude, POINT_RADIUS, PROX_ALERT_EXPIRATION, proximityIntent);
            }
        }

    }

    protected void getUserDetails() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

}

