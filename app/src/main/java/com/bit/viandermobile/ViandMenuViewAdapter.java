package com.bit.viandermobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.models.CheckboxViewModel;
import com.bit.viandermobile.models.ViandMenuViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ViandMenuViewAdapter extends RecyclerView.Adapter<ViandMenuViewAdapter.ViandMenuViewHolder> {

    private Context context;
    private List<ViandMenuViewModel> viandMenuViewModelList;
    private List<Integer> selectedDayNumbers;

    public ViandMenuViewAdapter(List<ViandMenuViewModel> viandMenuViewModelList, Context context){
        this.context = context;
        this.viandMenuViewModelList = viandMenuViewModelList;
        this.selectedDayNumbers = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViandMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_viandas_row, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = (TextView) v.findViewById(R.id.viand_title);
                Snackbar.make(parent.findViewById(R.id.viandsActivityRow), R.string.selected_viand, Snackbar.LENGTH_LONG)
                        .setText(title.getText())
                        .show();
            }
        });
        return new ViandMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViandMenuViewHolder holder, int position) {
        final ViandMenuViewModel viandMenuViewModel = viandMenuViewModelList.get(position);
        holder.id = viandMenuViewModel.getId();
        holder.dayNumber = viandMenuViewModel.getDayNumber();
        holder.title.setText(viandMenuViewModel.getTitle());
        holder.day.setText(viandMenuViewModel.getDay());
        holder.price.setText(""+viandMenuViewModel.getPrice());
        //new DownloadImageTask(holder.image).execute(viandMenuViewModel.getImage());
        Picasso.get()
                .load(viandMenuViewModel.getImage())
                .resize(146, 86)
                .centerCrop()
                .into(holder.image);
        holder.check.setChecked(viandMenuViewModel.isChecked());
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedDayNumbers.add((Integer) holder.dayNumber);
                }else{
                    selectedDayNumbers.remove((Integer) holder.dayNumber);
                }
                Collections.sort(selectedDayNumbers);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescription(viandMenuViewModel.getContent());
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescription(viandMenuViewModel.getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return viandMenuViewModelList.size();
    }

    public static class ViandMenuViewHolder extends RecyclerView.ViewHolder {

        int id;
        int dayNumber;
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

    private void showDescription(String desc){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.description));
        builder.setMessage(desc);
        builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public List<Integer> getSelectedViands(){
        return selectedDayNumbers;
    }

    public int getMenuPrice(){
        int price = 0;
        for(ViandMenuViewModel model : viandMenuViewModelList){
            price += model.getPrice();
        }
        return price;
    }

    public List<Integer> getViandIds(){
        List<Integer> ids = new ArrayList<>();
        for(ViandMenuViewModel model : viandMenuViewModelList){
            ids.add(model.getId());
        }
        return ids;
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