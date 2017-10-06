package edu.uark.csce.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    public static final String TAG = "networkReciever";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            Toast toast = Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            Toast toast = Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
