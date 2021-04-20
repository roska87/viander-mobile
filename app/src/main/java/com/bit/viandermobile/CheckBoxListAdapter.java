package com.bit.viandermobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.models.CheckboxViewModel;

import android.util.Pair;

import java.util.List;

public class CheckBoxListAdapter extends RecyclerView.Adapter<CheckBoxListAdapter.CheckBoxHolder> {

    private Context context;
    private List<CheckboxViewModel> checkBoxList;

    public CheckBoxListAdapter(List<CheckboxViewModel> checkBoxList, Context context){
        this.context = context;
        this.checkBoxList = checkBoxList;
    }

    @NonNull
    @Override
    public CheckBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.checkbox_item, parent, false);
        return new CheckBoxHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxHolder holder, int position) {
        final CheckboxViewModel checkBox = checkBoxList.get(position);
        holder.name.setText(checkBox.getText());
        holder.selected.setChecked(checkBox.isChecked());
    }

    @Override
    public int getItemCount() {
        return checkBoxList.size();
    }

    public static class CheckBoxHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private CheckBox selected;

        public CheckBoxHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cat_name);
            selected = (CheckBox) itemView.findViewById(R.id.cat_select);
        }
    }

    public static Pair<Boolean, String> getModelValues(RecyclerView recyclerView, int position){
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        TextView text = holder.itemView.findViewById(R.id.cat_name);
        CheckBox selected = holder.itemView.findViewById(R.id.cat_select);
        return Pair.create(selected.isChecked(), text.getText().toString());
    }
}