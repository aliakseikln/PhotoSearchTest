package com.example.retrofitagain2.interfaces;

import android.content.Context;


public interface PhotosServiceModel {

    void loadDataOfPhotosByQuery(String query, String api_key);

    void downloadSelectedPhoto(Context context, String urlO, String photoTitle);

}