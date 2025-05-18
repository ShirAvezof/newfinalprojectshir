package com.example.finalprojectshir2.FavoriteKindergarnds;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectshir2.AllKindergardens.KinderGardenAdapter;
import com.example.finalprojectshir2.FavoriteKindergartenRepository;
import com.example.finalprojectshir2.Home.HomeActivity;

import com.example.finalprojectshir2.KindergardenProfile.KindergardenProfileActivity;
import com.example.finalprojectshir2.Parent.ParentProfile.ParentProfileActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.example.finalprojectshir2.repositories.KinderGartenRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteKindergarndsActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        KinderGardenAdapter.OnKindergartenClickListener {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private KinderGardenAdapter adapter;
    private List<KinderGarten> favoriteKindergartens;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;

    private FavoriteKindergartenRepository favoriteRepository;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_kindergarnds);

        // Initialize UI components
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_favorites);

        recyclerView = findViewById(R.id.favoritesRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);

        // Set up RecyclerView
        favoriteKindergartens = new ArrayList<>();
        // Use the activity itself as the click listener since it implements the interface
        adapter = new KinderGardenAdapter(favoriteKindergartens, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        favoriteRepository = new FavoriteKindergartenRepository();

        // Get current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            loadFavoriteKindergartens();
        } else {
            // Handle not logged in
            showEmptyState("Please log in to view your favorites");
        }
    }

    private void loadFavoriteKindergartens() {
        showLoading(); // Start with loading state

        favoriteRepository.getAllFavorites(currentUserId, new FavoriteKindergartenRepository.OnFavoritesLoadedListener() {
            @Override
            public void onFavoritesLoaded(ArrayList<String> favoriteKindergartenIds) {
                if (favoriteKindergartenIds.isEmpty()) {
                    showEmptyState("No favorite kindergartens yet");
                    return;
                }

                fetchKindergartenDetails(favoriteKindergartenIds);
            }

            @Override
            public void onError(String errorMessage) {
                showEmptyState("Error loading favorites: " + errorMessage);
            }
        });
    }

    private void fetchKindergartenDetails(List<String> kindergartenIds) {
        favoriteKindergartens.clear();

        if (kindergartenIds.isEmpty()) {
            showEmptyState("No favorite kindergartens found");
            return;
        }

        KinderGartenRepository repository = new KinderGartenRepository();
        final int[] loadedCount = {0}; // Track completion
        final int totalToLoad = kindergartenIds.size();

        for (String id : kindergartenIds) {
            repository.getKinderGartenById(id, new FirebaseCallback<KinderGarten>() {
                @Override
                public void onSuccess(KinderGarten kinderGarten) {
                    if (!containsKindergarten(favoriteKindergartens, kinderGarten.getId())) {
                        favoriteKindergartens.add(kinderGarten);
                    }
                    loadedCount[0]++;

                    // Check if all kindergartens are loaded
                    if (loadedCount[0] == totalToLoad) {
                        // Update UI on the main thread
                        runOnUiThread(() -> {
                            adapter.updateData(favoriteKindergartens);
                            hideLoading();
                            showContent();
                        });
                    }
                }
                private boolean containsKindergarten(List<KinderGarten> list, String id) {
                    for (KinderGarten g : list) {
                        if (g.getId().equals(id)) {
                            return true;
                        }
                    }
                    return false;
                }
                @Override
                public void onError(String error) {
                    loadedCount[0]++;
                    // Still check if all requests are complete
                    if (loadedCount[0] == totalToLoad) {
                        runOnUiThread(() -> {
                            if (favoriteKindergartens.isEmpty()) {
                                showEmptyState("Could not load kindergartens");
                            } else {
                                adapter.updateData(favoriteKindergartens);
                                hideLoading();
                                showContent();
                            }
                        });
                    }
                }
            });
        }
    }

    // UI state management methods
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyStateTextView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);
    }

    private void showEmptyState(String message) {
        hideLoading();
        recyclerView.setVisibility(View.GONE);
        emptyStateTextView.setVisibility(View.VISIBLE);
        emptyStateTextView.setText(message);
    }

    @Override
    public void onKindergartenClick(KinderGarten kindergarten) {
        Intent intent = new Intent(this, KindergardenProfileActivity.class);
        intent.putExtra(KindergardenProfileActivity.EXTRA_KINDERGARTEN_ID, kindergarten.getId());
        startActivity(intent);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(this, ParentProfileActivity.class);
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.nav_home){
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.nav_favorites){
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload favorites when returning to this activity
        if (currentUserId != null) {
            loadFavoriteKindergartens();
        }
    }

    public void removeFromFavoritesList(KinderGarten kindergarten) {
        favoriteKindergartens.remove(kindergarten);
        adapter.updateData(favoriteKindergartens);

        if (favoriteKindergartens.isEmpty()) {
            showEmptyState("אין גנים מועדפים");
        }
    }

}