package com.infiniterealm.achievers.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

public class NetworkUtils {

    private final Context context;
    private ConnectivityManager.NetworkCallback networkCallback;

    public NetworkUtils(Context context) {
        this.context = context;
    }

    public interface OnNetworkChangeListener {
        void onNetworkAvailable();
        void onNetworkUnavailable();
    }

    public void checkNetworkStatus(OnNetworkChangeListener listener) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                listener.onNetworkAvailable();
            }

            @Override
            public void onLost(@NonNull Network network) {
                listener.onNetworkUnavailable();
            }
        };
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback);
    }

    public void unregisterNetworkCallback() {
        if (networkCallback != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

}
