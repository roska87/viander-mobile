package com.bit.viandermobile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
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

import java.util.ArrayList;
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

        vianderViewModel.getMenu().observe(ViandasActivity.this, new Observer<Map<Integer, PostDto>>() {
            @Override
            public void onChanged(Map<Integer, PostDto> postDtoMap) {
                List<ViandMenuViewModel> menuList = mapViand(postDtoMap);
                adapter = new ViandMenuViewAdapter(menuList, ViandasActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViandasActivity.this));
            }
        });

        Button confirmationBtn = findViewById(R.id.confirm_button);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViandasActivity.this, ConfirmationActivity.class);
                i.putExtra(TOTAL_AMOUNT, adapter.getMenuPrice());
                startActivity(i);
                finish();
            }
        });
    }

    private List<ViandMenuViewModel> mapViand(Map<Integer, PostDto> postMap){
        List<ViandMenuViewModel> modelList = new ArrayList<>();
        for(Map.Entry<Integer, PostDto> entry : postMap.entrySet()){
            int dayNumber = entry.getKey();
            PostDto postDto = entry.getValue();
            ViandMenuViewModel model = new ViandMenuViewModel();
            model.setTitle(postDto.getTitle());
            model.setImage(postDto.getFile());
            model.setDay(mapDay(dayNumber));
            model.setPrice(123);
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