package com.example.finalprojectshir2.Parent.SearchActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.KinderGartenAdapter;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearchKinderGarten extends AppCompatActivity {
    private static final String TAG = "SearchKinderGarten";
    private EditText cityEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private KinderGartenAdapter adapter;
    private SearchKinderGartenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_kinder_garten);
        Log.d(TAG, "onCreate: Initializing activity");

        initializeViews();
        setupRecyclerView();
        setupPresenter();
        setupListeners();
    }

    private void setupPresenter() {
        presenter = new SearchKinderGartenPresenter(this);
        Log.d(TAG, "Presenter setup complete");
    }

    private void initializeViews() {
        cityEditText = findViewById(R.id.cityEditText);
        searchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView not found in layout");
            Toast.makeText(this, "Error initializing view", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d(TAG, "Views initialized successfully");
    }

    private void setupRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView");
        adapter = new KinderGartenAdapter(new ArrayList<>(), this::onKindergartenSelected);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setupListeners() {
        searchButton.setOnClickListener(v -> {
            String city = cityEditText.getText().toString();
            Log.d(TAG, "Search clicked for city: " + city);
            presenter.searchKindergartensByCity(city);
        });
    }

    public void showLoading() {
        Log.d(TAG, "Showing loading indicator");
        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            searchButton.setEnabled(false);
            recyclerView.setVisibility(View.GONE);
        });
    }

    public void hideLoading() {
        Log.d(TAG, "Hiding loading indicator");
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            searchButton.setEnabled(true);
            recyclerView.setVisibility(View.VISIBLE);
        });
    }

    public void showResults(List<KinderGarten> kindergartens) {
        Log.d(TAG, "Showing results. Found " + kindergartens.size() + " kindergartens");
        runOnUiThread(() -> {
            if (adapter != null) {
                adapter.updateData(kindergartens);
                for (KinderGarten kg : kindergartens) {
                    Log.d(TAG, "Kindergarten: " + kg.getGanname());
                }
            } else {
                Log.e(TAG, "Adapter is null");
                showError("Internal error occurred");
            }
        });
    }

    public void showNoResults() {
        Log.d(TAG, "No results found");
        runOnUiThread(() -> {
            Toast.makeText(this, "No kindergartens found in this city", Toast.LENGTH_SHORT).show();
            if (adapter != null) {
                adapter.updateData(new ArrayList<>());
            }
        });
    }

    public void showError(String error) {
        Log.e(TAG, "Error occurred: " + error);
        runOnUiThread(() -> {
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        });
    }

    private void onKindergartenSelected(KinderGarten kindergarten) {
        Log.d(TAG, "Kindergarten selected: " + kindergarten.getGanname());
        Toast.makeText(this, "Selected: " + kindergarten.getGanname(), Toast.LENGTH_SHORT).show();
        // TODO: Navigate to kindergarten details
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
        adapter = null;
    }
}