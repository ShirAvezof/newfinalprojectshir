package com.example.finalprojectshir2.Parent.SearchActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.KinderGartenAdapter;
import com.example.finalprojectshir2.KindergardenProfile.KindergardenProfileActivity;
import com.example.finalprojectshir2.Parent.ParentProfile.ParentProfileActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivitySearchKinderGarten extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "SearchActivity";
    private static final int ANIMATION_DURATION = 300;

    // UI components
    private BottomNavigationView bottomNavigationView;
    private RecyclerView kindergartensRecyclerView;
    private ProgressBar searchProgressBar;
    private TextView noResultsTextView;
    private TextView searchResultsTitle;
    private TextView resultsCountBadge;
    private MaterialButton applyFiltersButton;
    private MaterialButton filterToggleButton;
    private MaterialCardView filterCard;

    private ImageButton back;

    // Filter checkboxes
    private MaterialCheckBox onlineCamerasCheckbox;
    private MaterialCheckBox closedCircuitCheckbox;
    private MaterialCheckBox fridayActiveCheckbox;

    // Adapter and data
    private KinderGartenAdapter adapter;
    private List<KinderGarten> allKindergartenResults = new ArrayList<>();
    private List<KinderGarten> filteredResults = new ArrayList<>();

    // Presenter
    private SearchKinderGartenPresenter presenter;

    // Search parameters
    private String searchCity;
    private boolean hasOnlineCameras;
    private boolean hasClosedCircuitCameras;
    private boolean isActiveOnFriday;

    // Filter panel state
    private boolean isFilterPanelVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_kinder_garten);

        // Get search parameters from intent
        Intent intent = getIntent();
        searchCity = intent.getStringExtra("city");
        if (searchCity == null) {
            searchCity = "";
        }

        hasOnlineCameras = intent.getBooleanExtra("hasOnlineCameras", false);
        hasClosedCircuitCameras = intent.getBooleanExtra("hasClosedCircuitCameras", false);
        isActiveOnFriday = intent.getBooleanExtra("isActiveOnFriday", false);

        initViews();
        setupRecyclerView();
        setupListeners();

        // Initialize presenter
        presenter = new SearchKinderGartenPresenter(this);

        // Set filter checkboxes according to intent parameters
        onlineCamerasCheckbox.setChecked(hasOnlineCameras);
        closedCircuitCheckbox.setChecked(hasClosedCircuitCameras);
        fridayActiveCheckbox.setChecked(isActiveOnFriday);

        // Update search results title
        if (searchCity.isEmpty()) {
            searchResultsTitle.setText("כל גני הילדים");
        } else {
            searchResultsTitle.setText("תוצאות חיפוש עבור: " + searchCity);
        }

        // Start search
        presenter.searchKindergartensByCity(searchCity);
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        kindergartensRecyclerView = findViewById(R.id.kindergartensRecyclerView);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        noResultsTextView = findViewById(R.id.noResultsTextView);
        searchResultsTitle = findViewById(R.id.searchResultsTitle);
        resultsCountBadge = findViewById(R.id.resultsCountBadge);
        applyFiltersButton = findViewById(R.id.applyFiltersButton);
        back = findViewById(R.id.back);
        filterToggleButton = findViewById(R.id.filterToggleButton);
        filterCard = findViewById(R.id.filterCard);

        // Initialize filter checkboxes
        onlineCamerasCheckbox = findViewById(R.id.onlineCamerasCheckbox);
        closedCircuitCheckbox = findViewById(R.id.closedCircuitCheckbox);
        fridayActiveCheckbox = findViewById(R.id.fridayActiveCheckbox);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupRecyclerView() {
        adapter = new KinderGartenAdapter(new ArrayList<>(), this::onKindergartenSelected);
        kindergartensRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        kindergartensRecyclerView.setAdapter(adapter);

        // Add animation to items
        kindergartensRecyclerView.setItemAnimator(new androidx.recyclerview.widget.DefaultItemAnimator());
    }

    private void setupListeners() {
        // Filter toggle button listener
        filterToggleButton.setOnClickListener(v -> toggleFilterPanel());

        // Apply filters button listener
        applyFiltersButton.setOnClickListener(v -> {
            applyFilters();
            // Hide filter panel after applying filters
            if (isFilterPanelVisible) {
                toggleFilterPanel();
            }
        });
    }

    private void toggleFilterPanel() {
        if (isFilterPanelVisible) {
            // Hide filter panel with animation
            filterCard.animate()
                    .alpha(0f)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            filterCard.setVisibility(View.GONE);
                        }
                    });

            // Change button text
            filterToggleButton.setText("סינון");
            filterToggleButton.setIcon(getDrawable(R.drawable.ic_filter));
            isFilterPanelVisible = false;
        } else {
            // Show filter panel with animation
            filterCard.setVisibility(View.VISIBLE);
            filterCard.setAlpha(0f);
            filterCard.animate()
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(null);

            // Change button text
            filterToggleButton.setText("סגור");
            filterToggleButton.setIcon(getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
            isFilterPanelVisible = true;
        }
    }

    public void showLoading() {
        runOnUiThread(() -> {
            searchProgressBar.setVisibility(View.VISIBLE);
            kindergartensRecyclerView.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.GONE);
            resultsCountBadge.setVisibility(View.GONE);
        });
    }

    public void hideLoading() {
        runOnUiThread(() -> {
            searchProgressBar.setVisibility(View.GONE);
        });
    }

    public void showNoResults() {
        runOnUiThread(() -> {
            noResultsTextView.setVisibility(View.VISIBLE);
            kindergartensRecyclerView.setVisibility(View.GONE);
            resultsCountBadge.setVisibility(View.GONE);
        });
    }

    public void showResults(List<KinderGarten> kindergartens) {
        allKindergartenResults = new ArrayList<>(kindergartens); // Store all results for filtering
        runOnUiThread(() -> {
            // If filters are already applied when results come in, apply them immediately
            if (hasOnlineCameras || hasClosedCircuitCameras || isActiveOnFriday) {
                applyFilters();
            } else {
                noResultsTextView.setVisibility(View.GONE);
                kindergartensRecyclerView.setVisibility(View.VISIBLE);
                adapter.updateData(kindergartens);

                // Update results count badge
                updateResultsCountBadge(kindergartens.size());
            }
        });
    }

    private void updateResultsCountBadge(int count) {
        resultsCountBadge.setText(count + " תוצאות");

        if (count > 0) {
            if (resultsCountBadge.getVisibility() == View.GONE) {
                // Animate badge entry
                resultsCountBadge.setVisibility(View.VISIBLE);
                resultsCountBadge.setScaleX(0f);
                resultsCountBadge.setScaleY(0f);
                resultsCountBadge.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        } else {
            resultsCountBadge.setVisibility(View.GONE);
        }
    }

    public void showError(String error) {
        Log.e(TAG, "Error in search: " + error);
        runOnUiThread(() -> {
            Toast.makeText(ActivitySearchKinderGarten.this,
                    "שגיאה בחיפוש: " + error,
                    Toast.LENGTH_SHORT).show();
            noResultsTextView.setVisibility(View.VISIBLE);
            resultsCountBadge.setVisibility(View.GONE);
        });
    }

    private void applyFilters() {
        // If no search has been performed yet
        if (allKindergartenResults.isEmpty()) {
            Toast.makeText(this,
                    "אין תוצאות חיפוש להחיל עליהן סינון",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean filterOnlineCameras = onlineCamerasCheckbox.isChecked();
        boolean filterClosedCircuit = closedCircuitCheckbox.isChecked();
        boolean filterFridayActive = fridayActiveCheckbox.isChecked();

        // Highlight filter button if any filters are active
        if (filterOnlineCameras || filterClosedCircuit || filterFridayActive) {
            filterToggleButton.setStrokeWidth(3);
            filterToggleButton.setStrokeColorResource(R.color.primary);
            filterToggleButton.setIconTintResource(R.color.primary);
        } else {
            filterToggleButton.setStrokeWidth(1);
            filterToggleButton.setStrokeColorResource(android.R.color.darker_gray);
            filterToggleButton.setIconTintResource(android.R.color.darker_gray);
        }

        // If no filters are selected, show all results
        if (!filterOnlineCameras && !filterClosedCircuit && !filterFridayActive) {
            filteredResults = new ArrayList<>(allKindergartenResults);
            noResultsTextView.setVisibility(View.GONE);
            kindergartensRecyclerView.setVisibility(View.VISIBLE);
            adapter.updateData(filteredResults);
            updateResultsCountBadge(filteredResults.size());
            return;
        }

        // Apply filters
        filteredResults = allKindergartenResults.stream()
                .filter(kg -> {
                    boolean passesFilter = true;

                    if (filterOnlineCameras) {
                        passesFilter = passesFilter && kg.isHasOnlineCameras();
                    }

                    if (filterClosedCircuit) {
                        passesFilter = passesFilter && kg.isHasClosedCircuitCameras();
                    }

                    if (filterFridayActive) {
                        passesFilter = passesFilter && kg.isActiveOnFriday();
                    }

                    return passesFilter;
                })
                .collect(Collectors.toList());

        if (filteredResults.isEmpty()) {
            noResultsTextView.setVisibility(View.VISIBLE);
            kindergartensRecyclerView.setVisibility(View.GONE);
            resultsCountBadge.setVisibility(View.GONE);
            Toast.makeText(this,
                    "לא נמצאו גני ילדים העונים לקריטריוני הסינון",
                    Toast.LENGTH_SHORT).show();
        } else {
            noResultsTextView.setVisibility(View.GONE);
            kindergartensRecyclerView.setVisibility(View.VISIBLE);
            adapter.updateData(filteredResults);
            updateResultsCountBadge(filteredResults.size());

            // Show toast with filter results
            Toast.makeText(this,
                    "נמצאו " + filteredResults.size() + " גני ילדים",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onKindergartenSelected(KinderGarten kindergarten) {
        Log.d(TAG, "Kindergarten selected: " + kindergarten.getGanname());
        Intent intent = new Intent(this, KindergardenProfileActivity.class);
        intent.putExtra("kindergarten_id", kindergarten.getId());
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(this, ParentProfileActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_home){
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_favorites ){
            return true;
        }
        return false;
    }
}