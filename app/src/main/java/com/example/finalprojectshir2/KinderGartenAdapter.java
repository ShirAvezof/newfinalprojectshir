package com.example.finalprojectshir2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectshir2.models.KinderGarten;

import java.util.List;

public class KinderGartenAdapter extends RecyclerView.Adapter<KinderGartenAdapter.ViewHolder> {
    private List<KinderGarten> kindergartens;
    private OnKindergartenClickListener listener;

    public interface OnKindergartenClickListener {
        void onKindergartenClick(KinderGarten kindergarten);
    }

    public KinderGartenAdapter(List<KinderGarten> kindergartens, OnKindergartenClickListener listener) {
        this.kindergartens = kindergartens;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kindergarten, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KinderGarten kindergarten = kindergartens.get(position);

        // Set text fields
        holder.nameTextView.setText(kindergarten.getGanname());
        holder.cityTextView.setText(kindergarten.getAboutgan());

        // Handle image
        String base64Image = kindergarten.getImage();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.ganImageView.setImageBitmap(decodedByte);
                holder.ganImageView.setVisibility(View.VISIBLE);
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        } else {

        }

        holder.itemView.setOnClickListener(v -> listener.onKindergartenClick(kindergarten));
    }

    @Override
    public int getItemCount() {
        return kindergartens.size();
    }

    public void updateData(List<KinderGarten> newKindergartens) {
        this.kindergartens = newKindergartens;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView cityTextView;
        ImageView ganImageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            ganImageView = itemView.findViewById(R.id.ganImageView);
        }
    }
}