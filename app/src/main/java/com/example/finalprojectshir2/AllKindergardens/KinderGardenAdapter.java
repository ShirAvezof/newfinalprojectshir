package com.example.finalprojectshir2.AllKindergardens;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;

import java.util.List;
public class KinderGardenAdapter extends ArrayAdapter<KinderGarten> {
        private Context context;
        private List<KinderGarten> kindergardens1;


        public KinderGardenAdapter(Context context, List<KinderGarten> kindergardens1) {
            super(context, R.layout.onerow, kindergardens1);
            this.context = context;
            this.kindergardens1 = kindergardens1;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;


            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.onerow, parent, false);
                holder = new ViewHolder();

                holder.imgg = convertView.findViewById(R.id.imgg);
                holder.tvname = convertView.findViewById(R.id.tvname);
                holder.tvaddress = convertView.findViewById(R.id.tvaddress);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            KinderGarten kindergarden = kindergardens1.get(position);


            // Set product name and amount
            holder.tvname.setText(kindergarden.getGanname());
            holder.tvaddress.setText("Amount: " + kindergarden.getAddress());


            // Load and set the image
            if (kindergarden.getImage() != null) {
                try {
                    byte[] decodedString = Base64.decode(kindergarden.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    holder.imgg.setImageBitmap(decodedByte);
                } catch (Exception e) {
                    Toast.makeText(context, "יש בעיה", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    holder.imgg.setImageResource(R.drawable.iconmygan); // Set default image
                }
            } else {
                Toast.makeText(context, "אין תמונה", Toast.LENGTH_LONG).show();
                holder.imgg.setImageResource(R.drawable.iconmygan);
            }


            return convertView;
        }


        private static class ViewHolder {
           ImageView imgg;
            TextView tvname;
            TextView tvaddress;
        }


        public void updateProducts(List<KinderGarten> newkindergarden) {
            kindergardens1.clear();
            kindergardens1.addAll(newkindergarden);
            notifyDataSetChanged();
        }
    }



