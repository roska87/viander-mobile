package com.bit.viandermobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.models.CheckboxViewModel;
import com.bit.viandermobile.models.ViandMenuViewModel;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

public class ViandMenuViewAdapter extends RecyclerView.Adapter<ViandMenuViewAdapter.ViandMenuViewHolder> {

    private Context context;
    private List<ViandMenuViewModel> viandMenuViewModelList;

    public ViandMenuViewAdapter(List<ViandMenuViewModel> viandMenuViewModelList, Context context){
        this.context = context;
        this.viandMenuViewModelList = viandMenuViewModelList;
    }

    @NonNull
    @Override
    public ViandMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_viandas_row, parent, false);
        return new ViandMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViandMenuViewHolder holder, int position) {
        final ViandMenuViewModel viandMenuViewModel = viandMenuViewModelList.get(position);
        holder.title.setText(viandMenuViewModel.getTitle());
        holder.day.setText(viandMenuViewModel.getDay());
        holder.price.setText(""+viandMenuViewModel.getPrice());
        //new DownloadImageTask(holder.image).execute(viandMenuViewModel.getImage());
        Picasso.get().load(viandMenuViewModel.getImage()).into(holder.image);
        holder.check.setChecked(viandMenuViewModel.isChecked());
    }

    @Override
    public int getItemCount() {
        return viandMenuViewModelList.size();
    }

    public static class ViandMenuViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView day;
        TextView price;
        ImageView image;
        CheckBox check;

        public ViandMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.viand_title);
            price = itemView.findViewById(R.id.viand_price);
            day = itemView.findViewById(R.id.viand_day);
            image = itemView.findViewById(R.id.viand_image);
            check = itemView.findViewById(R.id.checkBox);
        }
    }

    public int getMenuPrice(){
        int price = 0;
        for(ViandMenuViewModel model : viandMenuViewModelList){
            price += model.getPrice();
        }
        return price;
    }

    public static int getModelPrice(RecyclerView recyclerView, int position){
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        TextView priceView = holder.itemView.findViewById(R.id.viand_price);
        return Integer.parseInt(priceView.getText().toString());
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView image;

        public DownloadImageTask(ImageView image) {
            this.image = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }

}