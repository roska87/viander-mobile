package com.bit.viandermobile;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;

public class ChipAdapter extends RecyclerView.Adapter<ChipViewHolder> {

    private String[] CHIP_IDS = {"abc", "def", "afsgdshdshsd"};

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Chip chip = new Chip(parent.getContext());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(chip);
            }
        });
        return new ChipViewHolder(chip);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        int pos = position % CHIP_IDS.length;
        holder.bindTo(CHIP_IDS[pos]);
    }

    @Override
    public int getItemCount() {
        return CHIP_IDS.length * 10;
    }
}