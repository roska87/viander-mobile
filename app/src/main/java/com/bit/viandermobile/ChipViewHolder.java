package com.bit.viandermobile;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.chip.Chip;

public class ChipViewHolder extends RecyclerView.ViewHolder {

    private Chip chip;

    public ChipViewHolder(@NonNull View itemView) {
        super(itemView);
        this.chip = (Chip) itemView;
    }

    public void bindTo(String content) {
        chip.setText(content);
        ViewGroup.LayoutParams lp = chip.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            ((FlexboxLayoutManager.LayoutParams) lp).setFlexGrow(1f);
        }
    }

}