package com.bit.viandermobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    String desc_menu, precio_menu;
    int dia_menu;
    // images[];
    Context context;
    public RecyclerViewAdapter(Context ct,String desc_menu, String precio_menu, int dia/*int image[]*/){

        context = ct; //Activity
        this.desc_menu = desc_menu;
        this.precio_menu = precio_menu;
        this.dia_menu = dia;
        //   images = image;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_viandas_rows, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.desc_menu.setText(desc_menu);
        holder.precio_menu.setText(precio_menu);
        if (dia_menu == 2){
            holder.dia.setText("Lunes");
        }
        holder.imagen_menu.setImageResource(R.drawable.logo);


    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView desc_menu, precio_menu, dia;
        ImageView imagen_menu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            desc_menu = itemView.findViewById(R.id.descripcion_vianda);
            precio_menu = itemView.findViewById(R.id.precio_vianda);
            dia = itemView.findViewById(R.id.dia_vianda);
            imagen_menu = itemView.findViewById(R.id.image_vianda);
        }
    }
}