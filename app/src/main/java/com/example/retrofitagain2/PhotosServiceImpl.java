package com.example.retrofitagain2;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.retrofitagain2.interfaces.ApiInterfaceFlickr;
import com.example.retrofitagain2.interfaces.PhotosServiceModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosServiceImpl implements PhotosServiceModel {

    private static final String TAG = "PhotoSearchViewImpl";
    ApiInterfaceFlickr apiInterfaceFlickr;
    List<Photo> photoListFromResponse = new ArrayList<Photo>();
    PhotoListPresenterImpl photoListPresenterImpl;

    public void loadDataOfPhotosByQuery(String query, String api_key) {
        apiInterfaceFlickr = ApiClientFlickr.getClient().create(ApiInterfaceFlickr.class);
        Call<PhotosResponse> callGetAll = apiInterfaceFlickr.getAllBySearch(query,api_key);
        callGetAll.enqueue(new Callback<PhotosResponse>() {

            @Override
            public void onResponse(@NonNull Call<PhotosResponse> call, @NonNull Response<PhotosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(TAG, "onResponse: " + response.body());
                    photoListFromResponse.clear();
                    photoListFromResponse.addAll(response.body().getPhotos().getPhoto());
                    photoListPresenterImpl.handleRecyclerView(photoListFromResponse);

                }
            }

            @Override
            public void onFailure(@NonNull Call<PhotosResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });

    }

    public void downloadSelectedPhoto(Context context, String urlO, String photoTitle) {

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(urlO);
        DownloadManager.Request request = new DownloadManager.Request((Uri) downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Photo")
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "FlickrPhotos" + File.separator + photoTitle + System.currentTimeMillis() + ".jpg");
        dm.enqueue(request);

    }

}