package com.bit.viandermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.VianderViewModel;

import org.apache.commons.lang3.StringUtils;

import static com.bit.viandermobile.constants.Constants.SHARED_PREFS;
import static com.bit.viandermobile.constants.Constants.TOKEN_KEY;
import static com.bit.viandermobile.constants.Constants.USERNAME_KEY;

public class ConfigurationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String username, token;

    private CheckBox chCeliac, chDiabetic, chVegan, chSunday, chMonday, chTuesday, chWednesday, chThursday, chFriday, chSaturday;

    private VianderViewModel vianderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME_KEY, null);
        token = sharedPreferences.getString(TOKEN_KEY, null);

        chCeliac = findViewById(R.id.checkboxCeliac);
        chDiabetic = findViewById(R.id.checkboxDiabetic);
        chVegan = findViewById(R.id.checkboxVegan);
        chSunday = findViewById(R.id.checkboxSunday);
        chMonday = findViewById(R.id.checkboxMonday);
        chTuesday = findViewById(R.id.checkboxTuesday);
        chWednesday = findViewById(R.id.checkboxWednesday);
        chThursday = findViewById(R.id.checkboxThursday);
        chFriday = findViewById(R.id.checkboxFriday);
        chSaturday = findViewById(R.id.checkboxSaturday);

        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnReset = findViewById(R.id.btnReset);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                List<Pair<Boolean, String>> containList = new ArrayList<>();
                if(chCeliac.isChecked()){
                    containList.add(Pair.create(true, getString(R.string.celiac)));
                }
                if(chDiabetic.isChecked()){
                    containList.add(Pair.create(true, getString(R.string.diabetic)));
                }
                if(chVegan.isChecked()){
                    containList.add(Pair.create(true, getString(R.string.vegan)));
                }
                vianderViewModel.updateProfile(token, username, containList);
                Toast.makeText(ConfigurationActivity.this, "Confirmado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfigurationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        vianderViewModel.refreshLoggedUser(token, username);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chCeliac.setChecked(false);
                chDiabetic.setChecked(false);
                chVegan.setChecked(false);
                chSunday.setChecked(false);
                chMonday.setChecked(false);
                chTuesday.setChecked(false);
                chWednesday.setChecked(false);
                chThursday.setChecked(false);
                chFriday.setChecked(false);
                chSaturday.setChecked(false);
            }
        });

        vianderViewModel.getLoggedUser().observe(ConfigurationActivity.this, new Observer<UserDto>() {
            @Override
            public void onChanged(UserDto userDto) {
                String filterStr = userDto.getProfile().getFilters();
                if(!StringUtils.isEmpty(filterStr)){
                    String[] filters = filterStr.split(",");
                    for(String filter : filters){
                        if(filter.equals(getString(R.string.celiac))){
                            chCeliac.setChecked(true);
                        }else if(filter.equals(getString(R.string.diabetic))){
                            chDiabetic.setChecked(true);
                        }else if(filter.equals(getString(R.string.vegan))){
                            chVegan.setChecked(true);
                        }else{
                            // TODO actualizar celda de tags
                        }
                    }
                }
                // TODO actualizar dias
            }
        });

    }

}