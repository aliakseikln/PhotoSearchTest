package com.example.retrofitagain2.photoList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.retrofitagain2.Photo;
import com.example.retrofitagain2.services.PhotosService;
import com.example.retrofitagain2.services.PhotosServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class PhotoListPresenterImpl implements PhotoListPresenter, PhotoListServiceListener {

    private static final String TAG = "PhotoListActivity";
    private final PhotoListView view;
    private final PhotosService photosService;
    ArrayList<String> searchPhotoList = new ArrayList<>();

    public PhotoListPresenterImpl(PhotoListView photoListActivity) {
        view = photoListActivity;
        photosService = new PhotosServiceImpl((Context) view, this);
    }

    public void handleHistoryButtonClick() {
        view.showSearchHistoryActivity(searchPhotoList);
    }

    public void handleSearchViewQuery(String query) {
        if (query != null) {
            searchPhotoList.add(query);
            view.showProgressBar();
            view.showToast("Ищем фото по запросу: " + query);
            photosService.loadDataOfPhotosByQuery(query);
        }
    }

    public void handleDownloadButtonClick(String urlO, String photoTitle) {
        try {
            photosService.downloadSelectedPhoto(urlO, photoTitle);
            view.showToast("Загрузка изображения началась.");
        } catch (Exception e) {
            Log.e(TAG, "onFailedDownload: " + e.getMessage());
            view.showToast("Автор не дал разрешения на загрузку фото.");
        }
    }

    public void onPhotosServiceSuccess(List<Photo> photoListResponse) {
        view.showToast("Вот что мы нашли по вашему запросу");
        view.hideProgressBar();
        view.showRecyclerView(photoListResponse);
    }

    public void handleImageButtonClick(Bitmap bitmap) {
        view.hideKeyboard();
        view.showPhotoDetailsActivity(bitmap);
    }
}