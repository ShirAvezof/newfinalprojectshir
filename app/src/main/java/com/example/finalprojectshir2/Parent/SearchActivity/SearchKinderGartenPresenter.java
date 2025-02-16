package com.example.finalprojectshir2.Parent.SearchActivity;

import android.util.Log;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.example.finalprojectshir2.repositories.KinderGartenRepository;

import java.util.List;

public class SearchKinderGartenPresenter {
    private static final String TAG = "KGPresenter";
    private ActivitySearchKinderGarten view;
    private KinderGartenRepository repository;

    public SearchKinderGartenPresenter(ActivitySearchKinderGarten view) {
        this.view = view;
        this.repository = new KinderGartenRepository();
        Log.d(TAG, "Presenter initialized");
    }

    public void searchKindergartensByCity(String city) {
        Log.d(TAG, "Searching for city: " + city);



        view.showLoading();
        repository.searchKinderGartensByCity(city.trim(), new FirebaseCallback<List<KinderGarten>>() {
            @Override
            public void onSuccess(List<KinderGarten> result) {
                Log.d(TAG, "Search successful. Found " + (result != null ? result.size() : 0) + " results");
                if (view != null) {
                    view.hideLoading();
                    if (result == null || result.isEmpty()) {
                        view.showNoResults();
                    } else {
                        view.showResults(result);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Search error: " + error);
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }
}