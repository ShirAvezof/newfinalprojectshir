package com.example.finalprojectshir2.KindergardenAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.FavoriteKindergartenRepository;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class KinderGardenAdapter extends RecyclerView.Adapter<KinderGardenAdapter.KinderGartenViewHolder> {
    private static final String TAG = "KinderGartenAdapter";
    private List<KinderGarten> kindergartenList;
    private final OnKindergartenClickListener listener;
    private FavoriteKindergartenRepository favoriteRepository;
    private Context context;

    public interface OnKindergartenClickListener {
        void onKindergartenClick(KinderGarten kindergarten);
    }

    public KinderGardenAdapter(List<KinderGarten> kindergartenList, OnKindergartenClickListener listener, Context context) {
        this.kindergartenList = kindergartenList;
        this.listener = listener;
        this.favoriteRepository = new FavoriteKindergartenRepository();
        this.context = context;
    }
    public void removeItem(KinderGarten kindergarten) {
        int position = kindergartenList.indexOf(kindergarten);
        if (position != -1) {
            kindergartenList.remove(position);
            notifyItemRemoved(position);
        }
    }


    @NonNull
    @Override
    public KinderGartenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kindergarten, parent, false);
        return new KinderGartenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KinderGartenViewHolder holder, int position) {
        KinderGarten kindergarten = kindergartenList.get(position);

        holder.bind(kindergarten, listener, favoriteRepository, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    favoriteRepository.checkIfFavorite(userId, kindergarten.getId(), isFavorite -> {
                        if (isFavorite) {
                            // Already a favorite, remove it
                            favoriteRepository.removeFromFavorites(userId, kindergarten.getId(), success -> {
                                if (success) {
                                    // Update the button appearance immediately
                                    ((ImageButton)v).setImageResource(android.R.drawable.btn_star_big_off);
                                    Toast.makeText(context,
                                            "הוסר מהמועדפים", Toast.LENGTH_SHORT).show();
                                    if (context instanceof FavoriteKindergarndsActivity) {
                                        ((FavoriteKindergarndsActivity) context).removeFromFavoritesList(kindergarten);
                                    }
                                } else {
                                    Toast.makeText(context,
                                            "שגיאה בהסרה מהמועדפים", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Not a favorite, add it
                            favoriteRepository.addToFavorites(userId, kindergarten.getId(), success -> {
                                if (success) {
                                    // Update the button appearance immediately
                                    ((ImageButton)v).setImageResource(android.R.drawable.btn_star_big_on);
                                    Toast.makeText(context,
                                            "התווסף למועדפים", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context,
                                            "שגיאה בהוספה למועדפים", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(context,
                            "יש להתחבר כדי להוסיף למועדפים", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //כמה פריטיים יש ברשימה אם ריק מחזירה 0
    @Override
    public int getItemCount() {
        return kindergartenList != null ? kindergartenList.size() : 0;
    }

    public void updateData(List<KinderGarten> newData) {
        this.kindergartenList = newData;
        notifyDataSetChanged();
        Log.d(TAG, "Data updated with " + newData.size() + " items");
    }
//אחראית להחזיק את האלמנטים של כל כרטיס (CardView) ברשימה.
//🔹 כל שורה ב־RecyclerView מיוצגת ע״י אובייקט מהמחלקה הזו.
    static class KinderGartenViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView nameTextView;
        private final TextView addressTextView;
        private final TextView ownerTextView;
        private final TextView hoursTextView;
        private final ImageView kindergartenImageView;
        private final ImageView onlineCameraIcon;
        private final ImageView closedCircuitIcon;
        private final ImageView fridayActiveIcon;
        private final ImageButton favoriteButton;


        public KinderGartenViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.kindergartenCardView);
            nameTextView = itemView.findViewById(R.id.kindergartenNameTextView);
            addressTextView = itemView.findViewById(R.id.kindergartenAddressTextView);
            ownerTextView = itemView.findViewById(R.id.ownerNameTextView);
            hoursTextView = itemView.findViewById(R.id.hoursTextView);
            kindergartenImageView = itemView.findViewById(R.id.kindergartenImageView);
            onlineCameraIcon = itemView.findViewById(R.id.onlineCameraIcon);
            closedCircuitIcon = itemView.findViewById(R.id.closedCircuitIcon);
            fridayActiveIcon = itemView.findViewById(R.id.fridayActiveIcon);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }

        public void bind(KinderGarten kindergarten, OnKindergartenClickListener listener,
                         FavoriteKindergartenRepository favoriteRepository, View.OnClickListener OnFavorite) {
            nameTextView.setText(kindergarten.getGanname());
            addressTextView.setText(kindergarten.getAddress());
            ownerTextView.setText("בעלים: " + kindergarten.getOwnerName());

            // Set hours if available
            if (kindergarten.getHours() != null && !kindergarten.getHours().isEmpty()) {
                hoursTextView.setText("שעות: " + kindergarten.getHours());
                hoursTextView.setVisibility(View.VISIBLE);
            } else {
                hoursTextView.setVisibility(View.GONE);
            }

            // Load image from base64 if available
            if (kindergarten.getImage() != null && !kindergarten.getImage().isEmpty()) {
                try {
                    byte[] decodedString = Base64.decode(kindergarten.getImage(), Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    kindergartenImageView.setImageBitmap(decodedBitmap);
                    kindergartenImageView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading image: " + e.getMessage());
                }
            }

            onlineCameraIcon.setVisibility(kindergarten.isHasOnlineCameras() ? View.VISIBLE : View.GONE);
            closedCircuitIcon.setVisibility(kindergarten.isHasClosedCircuitCameras() ? View.VISIBLE : View.GONE);
            fridayActiveIcon.setVisibility(kindergarten.isActiveOnFriday() ? View.VISIBLE : View.GONE);



            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                Log.d(TAG, "logged in");
                String userId = currentUser.getUid();
                favoriteRepository.checkIfFavorite(userId, kindergarten.getId(), isFavorite -> {
                    if (isFavorite) {
                        Log.d(TAG, "favorite");
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    } else {
                        Log.d(TAG, "not favorite");
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                });
            } else {
                Log.d(TAG, "not logged in");
            }

            favoriteButton.setOnClickListener(OnFavorite);//מצרף לפעולה הזו את הקוד שהוגדר קודם – מה קורה בלחיצה (הוספה/הסרה ממועדפים).
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onKindergartenClick(kindergarten);
                }
            });
        }
    }
}