package com.bit.viandermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hootsuite.nachos.chip.ChipInfo;

import org.apache.commons.lang3.StringUtils;

import static com.bit.viandermobile.constants.Constants.FRIDAY;
import static com.bit.viandermobile.constants.Constants.MONDAY;
import static com.bit.viandermobile.constants.Constants.SATURDAY;
import static com.bit.viandermobile.constants.Constants.SHARED_PREFS;
import static com.bit.viandermobile.constants.Constants.SUNDAY;
import static com.bit.viandermobile.constants.Constants.THURSDAY;
import static com.bit.viandermobile.constants.Constants.TOKEN_KEY;
import static com.bit.viandermobile.constants.Constants.TUESDAY;
import static com.bit.viandermobile.constants.Constants.USERNAME_KEY;
import static com.bit.viandermobile.constants.Constants.WEDNESDAY;

public class ConfigurationActivity extends AppCompatActivity {

    private static final int CHIP_LIMIT = 20;

    private SharedPreferences sharedPreferences;
    private String username, token;

    private CheckBox chCeliac, chDiabetic, chVegan;
    private CheckBox chSunday, chMonday, chTuesday, chWednesday, chThursday, chFriday, chSaturday;

    private VianderViewModel vianderViewModel;
    private TextInputEditText editText;

    // chip elements
    private EditText et;
    private FlexboxLayout chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME_KEY, null);
        token = sharedPreferences.getString(TOKEN_KEY, null);

        et = findViewById(R.id.recipient_input_ET);
        chipGroup = findViewById(R.id.recipient_group_FL);

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
                for(String value : getChipValues()){
                    containList.add(Pair.create(false, value));
                }
                List<Integer> weekDays = new ArrayList<>();
                if(chSunday.isChecked()){
                    weekDays.add(SUNDAY);
                }
                if(chMonday.isChecked()){
                    weekDays.add(MONDAY);
                }
                if(chTuesday.isChecked()){
                    weekDays.add(TUESDAY);
                }
                if(chWednesday.isChecked()){
                    weekDays.add(WEDNESDAY);
                }
                if(chThursday.isChecked()){
                    weekDays.add(THURSDAY);
                }
                if(chFriday.isChecked()){
                    weekDays.add(FRIDAY);
                }
                if(chSaturday.isChecked()){
                    weekDays.add(SATURDAY);
                }
                vianderViewModel.updateProfile(token, username, containList, weekDays);
                Toast.makeText(ConfigurationActivity.this, R.string.configuration_updated, Toast.LENGTH_SHORT).show();
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
                removeChips();
            }
        });

        vianderViewModel.getLoggedUser().observe(ConfigurationActivity.this, new Observer<UserDto>() {
            @Override
            public void onChanged(UserDto userDto) {
                String filterStr = userDto.getProfile().getFilters();
                if(!StringUtils.isEmpty(filterStr)){
                    String[] filters = filterStr.split(",");
                    List<ChipInfo> chipList = new ArrayList<>();
                    for(String filter : filters){
                        if(filter.equals(getString(R.string.celiac))){
                            chCeliac.setChecked(true);
                        }else if(filter.equals(getString(R.string.diabetic))){
                            chDiabetic.setChecked(true);
                        }else if(filter.equals(getString(R.string.vegan))){
                            chVegan.setChecked(true);
                        }else{
                            String word = filter.replace("!", "");
                            addNewChip(word, chipGroup);
                        }
                    }
                }
                String weekDaysStr = userDto.getProfile().getWeekDays();
                if(!StringUtils.isEmpty(weekDaysStr)){
                    String[] weekDays = weekDaysStr.split(",");
                    for(String weekDay : weekDays){
                        int weekDayInt = Integer.parseInt(weekDay);
                        switch(weekDayInt){ 
                            case SUNDAY:
                                chSunday.setChecked(true);
                                break;
                            case MONDAY:
                                chMonday.setChecked(true);
                                break;
                            case TUESDAY:
                                chTuesday.setChecked(true);
                                break;
                            case WEDNESDAY:
                                chWednesday.setChecked(true);
                                break;
                            case THURSDAY:
                                chThursday.setChecked(true);
                                break;
                            case FRIDAY:
                                chFriday.setChecked(true);
                                break;
                            case SATURDAY:
                                chSaturday.setChecked(true);
                                break;
                        }
                    }
                }
            }
        });

        // --------------------
        // Start Chip functions
        // --------------------
        chipGroup.setFlexWrap(FlexWrap.WRAP);
        chipGroup.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
        chipGroup.setAlignItems(AlignItems.CENTER);
        chipGroup.setJustifyContent(JustifyContent.SPACE_AROUND);
        chipGroup.setAlignContent(AlignContent.STRETCH);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String str = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (str.length() > 1) {
                        addNewChip(str, chipGroup);
                        et.setText("");
                    }
                }
                return false;
            }
        });

        et.setOnClickListener(v -> {
            if(controlChipLimit(chipGroup, CHIP_LIMIT)){
                et.setText("");
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if(TextUtils.isEmpty(str.trim())){
                    s.clear();
                    return;
                }
                if(controlChipLimit(chipGroup, CHIP_LIMIT)){
                    s.clear();
                }
                if (str.length() > 1 && (str.endsWith(" ") || str.endsWith("\n"))) {
                    addNewChip(str, chipGroup);
                    s.clear();
                    controlChipLimit(chipGroup, CHIP_LIMIT);

                }
            }
        });

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    if (et.length() == 0 && chipGroup.getChildCount() > 0) {
                        Chip chip = (Chip) chipGroup.getChildAt(chipGroup.getChildCount() - 2);
                        chipGroup.removeView(chip);
                    }
                }
                return false;
            }
        });
        // ------------------
        // End Chip functions
        // ------------------

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean controlChipLimit(FlexboxLayout chipGroup, int chipLimit){
        if(chipGroup.getChildCount() - 1 >= chipLimit){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            Snackbar.make(findViewById(R.id.recipient_input_ET), "Ha alcanzado el máximo de elementos", Snackbar.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void addNewChip(String text, FlexboxLayout chipGroup) {
        Chip chip = new Chip(ConfigurationActivity.this);
        chip.setText(text);
        chip.setChipIcon(ChipDrawable.createFromResource(ConfigurationActivity.this, R.xml.standalone_chip));
        chip.setCloseIconVisible(true);
        chipGroup.addView(chip, chipGroup.getChildCount() - 1);
        chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
    }

    private List<String> getChipValues(){
        List<String> values = new ArrayList<>();
        int childCount = chipGroup.getChildCount();
        for(int i=0; i<childCount; i++){
            View view = chipGroup.getChildAt(i);
            if(view instanceof Chip){
                Chip chip = (Chip) view;
                values.add(chip.getText().toString());
            }
        }
        return values;
    }

    private void removeChips(){
        int childCount = chipGroup.getChildCount();
        while(childCount > 1){
            chipGroup.removeViewAt(0);
            childCount = chipGroup.getChildCount();
        }
    }

}