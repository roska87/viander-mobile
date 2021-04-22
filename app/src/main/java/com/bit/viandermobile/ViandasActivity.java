package com.bit.viandermobile;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.ViandMenuViewModel;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.bit.viandermobile.constants.Constants.*;
import static com.bit.viandermobile.constants.Constants.SHARED_PREFS;
import static com.bit.viandermobile.constants.Constants.TOKEN_KEY;
import static com.bit.viandermobile.constants.Constants.TOTAL_AMOUNT;
import static com.bit.viandermobile.constants.Constants.USERNAME_KEY;

public class ViandasActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String username, token;
    private VianderViewModel vianderViewModel;
    private RecyclerView recyclerView;
    private ViandMenuViewAdapter adapter;
    private TextView menuPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viandas);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME_KEY, null);
        token = sharedPreferences.getString(TOKEN_KEY, null);
        vianderViewModel.getMenu(token, username);
        recyclerView = findViewById(R.id.recyclerView);
        menuPrice = findViewById(R.id.menu_price_amount);

        vianderViewModel.getMenu().observe(ViandasActivity.this, new Observer<Map<Integer, PostDto>>() {
            @Override
            public void onChanged(Map<Integer, PostDto> postDtoMap) {
                List<ViandMenuViewModel> menuList = mapViand(postDtoMap);
                adapter = new ViandMenuViewAdapter(menuList, ViandasActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViandasActivity.this));
                menuPrice.setText(""+getTotalAmount(menuList));
            }
        });

        Button changeBtn = findViewById(R.id.change_button);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> selectedList = adapter.getSelectedViands();
                if(CollectionUtils.isEmpty(selectedList)){
                    Snackbar.make(findViewById(R.id.change_button), getString(R.string.not_selected_viands), Snackbar.LENGTH_LONG).show();
                    return;
                }
                vianderViewModel.changeViand(token, username, selectedList);
                Snackbar.make(findViewById(R.id.change_button), getString(R.string.updated_viands), Snackbar.LENGTH_LONG).show();
            }
        });

        Button confirmationBtn = findViewById(R.id.confirm_button);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vianderViewModel.updateViandCount(token, adapter.getViandIds());
                Intent i = new Intent(ViandasActivity.this, ConfirmationActivity.class);
                i.putExtra(TOTAL_AMOUNT, adapter.getMenuPrice());
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }
        return super.onOptionsItemSelected(item);
    }

    private int getTotalAmount(List<ViandMenuViewModel> menuList){
        int sum = 0;
        for(ViandMenuViewModel model : menuList){
            sum += model.getPrice();
        }
        return sum;
    }

    private List<ViandMenuViewModel> mapViand(Map<Integer, PostDto> postMap){
        List<ViandMenuViewModel> modelList = new LinkedList<>();
        for(Map.Entry<Integer, PostDto> entry : postMap.entrySet()){
            int dayNumber = entry.getKey();
            PostDto postDto = entry.getValue();
            ViandMenuViewModel model = new ViandMenuViewModel();
            model.setId(postDto.getId());
            model.setDayNumber(dayNumber);
            model.setTitle(postDto.getTitle());
            model.setContent(postDto.getContent());
            model.setImage(postDto.getFile());
            model.setDay(mapDay(dayNumber));
            model.setPrice(postDto.getPrice());
            model.setChecked(false);
            modelList.add(model);
        }
        return modelList;
    }

    private String mapDay(int dayNumber){
        switch(dayNumber){
            case MONDAY:
                return getString(R.string.monday);
            case TUESDAY:
                return getString(R.string.tuesday);
            case WEDNESDAY:
                return getString(R.string.wednesday);
            case THURSDAY:
                return getString(R.string.thursday);
            case FRIDAY:
                return getString(R.string.friday);
            case SATURDAY:
                return getString(R.string.saturday);
            case SUNDAY:
                return getString(R.string.sunday);
        }
        return "";
    }

}