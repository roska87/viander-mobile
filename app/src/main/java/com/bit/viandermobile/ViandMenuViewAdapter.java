package com.bit.viandermobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.models.ViandMenuViewModel;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViandMenuViewAdapter extends RecyclerView.Adapter<ViandMenuViewAdapter.ViandMenuViewHolder> {

    private Context context;
    private List<ViandMenuViewModel> viandMenuViewModelList;
    private List<Integer> selectedDayNumbers;
    private List<PostDto> allViands;
    private VianderViewModel vianderViewModel;

    public ViandMenuViewAdapter(Context context,
                                List<ViandMenuViewModel> viandMenuViewModelList,
                                List<PostDto> allViands,
                                VianderViewModel vianderViewModel){
        this.context = context;
        this.viandMenuViewModelList = viandMenuViewModelList;
        this.allViands = allViands;
        this.selectedDayNumbers = new ArrayList<>();
        this.vianderViewModel = vianderViewModel;
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
        holder.price.setText(String.valueOf(viandMenuViewModel.getPrice()));
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
        holder.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescription(holder.dayNumber);
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
        ImageView refresh;

        public ViandMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.viand_title);
            price = itemView.findViewById(R.id.viand_price);
            day = itemView.findViewById(R.id.viand_day);
            image = itemView.findViewById(R.id.viand_image);
            check = itemView.findViewById(R.id.checkBox);
            refresh = itemView.findViewById(R.id.refresh);
        }
    }

    private void showDescription(int dayNumber){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.change_viand);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View myView = inflater.inflate(R.layout.dialog_viand_items, null);
        dialogBuilder.setView(myView);
        Spinner checkInProviders = (Spinner) myView.findViewById(R.id.providers);
        ArrayAdapter<PostDto> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        dataAdapter.addAll(allViands);
        dataAdapter.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkInProviders.setAdapter(dataAdapter);

        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PostDto postDto = (PostDto) checkInProviders.getSelectedItem();
                //Log.i("SelectedViand", postDto.getDescription());
                List<Integer> selected = new ArrayList<>();
                selected.add(dayNumber);
                vianderViewModel.updateViand(dayNumber, postDto);
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.show();
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