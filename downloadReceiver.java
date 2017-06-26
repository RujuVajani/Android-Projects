package com.rujuvajani.scheduledownload;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class downloadReceiver extends BroadcastReceiver {

    DownloadManager downloadManager;
    public downloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        String url = "http://example.com/large.zip";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // only download via WIFI
        request.setAllowedNetworkTypes(DownloadManager.PAUSED_UNKNOWN);
        request.setTitle("Example");
        request.setDescription("Downloading a very large zip");

        // we just want to download silently
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalFilesDir(context, null, "large.zip");

        // enqueue this request
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Long downloadID = downloadManager.enqueue(request);

        /*
        SharedPreferences settings = context.getSharedPreferences("DownloadIDS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("savedDownloadIds", your download id);
        editor.commit();
        dm.enqueue(Request request);
        DownloadManager.enqueue(android.app.DownloadManager.Request);
    */
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
