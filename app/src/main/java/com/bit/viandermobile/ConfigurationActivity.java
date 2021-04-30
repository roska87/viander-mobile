package com.bit.viandermobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.CheckboxViewModel;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.hootsuite.nachos.chip.ChipInfo;

import org.apache.commons.lang3.StringUtils;

import static com.bit.viandermobile.constants.Constants.SHARED_PREFS;
import static com.bit.viandermobile.constants.Constants.*;
import static com.bit.viandermobile.constants.Constants.TOKEN_KEY;
import static com.bit.viandermobile.constants.Constants.USERNAME_KEY;

public class ConfigurationActivity extends AppCompatActivity {

    public static final String WEEK_DAYS = "weekDays";
    public static final String FILTERS = "filters";
    public static final String TAGS_STATE_NAME = "tags";
    public static final String CATEGORIES_STATE_NAME = "categories";
    public static final String WEEK_DAYS_STATE_NAME = "weekDays";
    private static final int CHIP_LIMIT = 20;

    private SharedPreferences sharedPreferences;
    private String username, token;

    private VianderViewModel vianderViewModel;

    // chip elements
    private EditText et;
    private FlexboxLayout chipGroup;

    private RecyclerView categoryLstView, weekLstView;

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

        // Categories RecyclerView
        categoryLstView = (RecyclerView) findViewById(R.id.category_lst);
        weekLstView = (RecyclerView) findViewById(R.id.week_lst);
        if(savedInstanceState == null){
            initializeCategories(null);
            initializeWeek(null);
        }

        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnReset = findViewById(R.id.btnReset);

        TextView textViewAvoid = findViewById(R.id.textViewAvoid);
        textViewAvoid.setOnClickListener(c -> {
            et.requestFocus();
            showKeyoard();
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                List<Pair<Boolean, String>> containList = new ArrayList<>();
                for(int i=0; i<categoryLstView.getAdapter().getItemCount(); i++){
                    Pair<Boolean, String> categoryPair = CheckBoxListAdapter.getModelValues(categoryLstView, i);
                    if(categoryPair.first){
                        containList.add(Pair.create(true, categoryPair.second));
                    }
                }
                for(String value : getChipValues()){
                    containList.add(Pair.create(false, value.trim()));
                }
                List<Integer> weekDays = new ArrayList<>();
                for(int i=0; i<weekLstView.getAdapter().getItemCount(); i++){
                    Pair<Boolean, String> categoryPair = CheckBoxListAdapter.getModelValues(weekLstView, i);
                    if(categoryPair.first){
                        weekDays.add(i);
                    }
                }
                String filters = null;
                if(containList != null && !containList.isEmpty()){
                    filters = formatFilters(containList);
                }
                Intent intent = new Intent(ConfigurationActivity.this, HomeActivity.class);
                intent.putExtra(WEEK_DAYS, StringUtils.join(weekDays, ","));
                intent.putExtra(FILTERS, filters);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        vianderViewModel.refreshLoggedUser(token, username);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeCategories(null);
                initializeWeek(null);
                removeChips();
            }
        });

        vianderViewModel.getLoggedUser().observe(ConfigurationActivity.this, new Observer<UserDto>() {
            @Override
            public void onChanged(UserDto userDto) {
                if(savedInstanceState != null){
                    return;
                }
                String filterStr = userDto.getProfile().getFilters();
                if(!StringUtils.isEmpty(filterStr)){
                    removeChips();
                    String[] filters = filterStr.split(",");
                    List<Integer> positiveCategories = new ArrayList<>();
                    for(String filter : filters){
                        if(filter.equals(getString(R.string.celiac))){
                            positiveCategories.add(0);
                        }else if(filter.equals(getString(R.string.diabetic))){
                            positiveCategories.add(1);
                        }else if(filter.equals(getString(R.string.vegan))){
                            positiveCategories.add(2);
                        }else{
                            String word = filter.replace("!", "");
                            addNewChip(word, chipGroup);
                        }
                    }
                    initializeCategories(positiveCategories);
                    manageChipEditText();
                }
                String weekDaysStr = userDto.getProfile().getWeekDays();
                if(!StringUtils.isEmpty(weekDaysStr)){
                    String[] weekDays = weekDaysStr.split(",");
                    List<Integer> positiveDays = new ArrayList<>();
                    for(String weekDay : weekDays){
                        int weekDayInt = Integer.parseInt(weekDay);
                        switch(weekDayInt){ 
                            case SUNDAY:
                                positiveDays.add(SUNDAY);
                                break;
                            case MONDAY:
                                positiveDays.add(MONDAY);
                                break;
                            case TUESDAY:
                                positiveDays.add(TUESDAY);
                                break;
                            case WEDNESDAY:
                                positiveDays.add(WEDNESDAY);
                                break;
                            case THURSDAY:
                                positiveDays.add(THURSDAY);
                                break;
                            case FRIDAY:
                                positiveDays.add(FRIDAY);
                                break;
                            case SATURDAY:
                                positiveDays.add(SATURDAY);
                                break;
                        }
                    }
                    initializeWeek(positiveDays);
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
                    manageChipEditText();
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
                manageChipEditText();
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
                manageChipEditText();
                return false;
            }
        });
        // ------------------
        // End Chip functions
        // ------------------

    }

    private String formatFilters(List<Pair<Boolean, String>> filterList){
        StringBuilder filters = new StringBuilder();
        for(Pair<Boolean, String> pair : filterList){
            filters.append(pair.first ? "" : "!").append(pair.second).append(",");
        }
        return filters.toString();
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // tags
        savedInstanceState.putStringArrayList(TAGS_STATE_NAME, new ArrayList<>(getChipValues()));
        // categories
        boolean[] categories = new boolean[categoryLstView.getAdapter().getItemCount()];
        for (int i = 0; i <categoryLstView.getAdapter().getItemCount() ; i++) {
            CheckBox checkBox = categoryLstView.getChildAt(i).findViewById(R.id.cat_select);
            categories[i] = checkBox.isChecked();
        }
        savedInstanceState.putBooleanArray(CATEGORIES_STATE_NAME, categories);
        // week days weekLstView
        boolean[] weekDays = new boolean[weekLstView.getAdapter().getItemCount()];
        for (int i = 0; i <weekLstView.getAdapter().getItemCount() ; i++) {
            CheckBox checkBox = weekLstView.getChildAt(i).findViewById(R.id.cat_select);
            weekDays[i] = checkBox.isChecked();
        }
        savedInstanceState.putBooleanArray(WEEK_DAYS_STATE_NAME, weekDays);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // tags
        List<String> tags = savedInstanceState.getStringArrayList(TAGS_STATE_NAME);
        for(String tag : tags){
            addNewChip(tag, chipGroup);
        }
        // categories
        boolean[] categoriesValues = savedInstanceState.getBooleanArray(CATEGORIES_STATE_NAME);
        List<Integer> categoriesPositivePositions = getCheckedPositions(categoriesValues);
        initializeCategories(categoriesPositivePositions);
        // week days
        boolean[] weekDaysValues = savedInstanceState.getBooleanArray(WEEK_DAYS_STATE_NAME);
        List<Integer> weekDaysPositivePositions = getCheckedPositions(weekDaysValues);
        initializeWeek(weekDaysPositivePositions);

        super.onRestoreInstanceState(savedInstanceState);
    }

    private List<Integer> getCheckedPositions(boolean[] values){
        List<Integer> positions = new LinkedList<>();
        for(int i=0; i<values.length; i++){
            if(values[i]){
                positions.add(i);
            }
        }
        return positions;
    }

    private void initializeCategories(List<Integer> positivePositions){
        List<CheckboxViewModel> checkboxViewModels = new ArrayList<>();
        checkboxViewModels.add(new CheckboxViewModel(getString(R.string.celiac), false));
        checkboxViewModels.add(new CheckboxViewModel(getString(R.string.diabetic), false));
        checkboxViewModels.add(new CheckboxViewModel(getString(R.string.vegan), false));
        if(!CollectionUtils.isEmpty(positivePositions)){
            for(Integer position : positivePositions) {
                checkboxViewModels.get(position).setChecked(true);
            }
        }
        CheckBoxListAdapter catLstAdapter = new CheckBoxListAdapter(checkboxViewModels, this);
        categoryLstView.setAdapter(catLstAdapter);
        categoryLstView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeWeek(List<Integer> positivePositions){
        List<CheckboxViewModel> checkboxWeekViewModels = new ArrayList<>();
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.monday), false));
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.tuesday), false));
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.wednesday), false));
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.thursday), false));
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.friday), false));
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.saturday), false));
        checkboxWeekViewModels.add(new CheckboxViewModel(getString(R.string.sunday), false));
        if(!CollectionUtils.isEmpty(positivePositions)){
            for(Integer position : positivePositions) {
                checkboxWeekViewModels.get(position).setChecked(true);
            }
        }
        CheckBoxListAdapter weekLstAdapter = new CheckBoxListAdapter(checkboxWeekViewModels, this);
        weekLstView.setAdapter(weekLstAdapter);
        weekLstView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void manageChipEditText(){
        if(chipGroup.getChildCount() > 1){
            et.setHint("");
        }else{
            et.setHint(getString(R.string.enter));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            et.clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showKeyoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getCurrentFocus(), 0);
    }

    private boolean controlChipLimit(FlexboxLayout chipGroup, int chipLimit){
        if(chipGroup.getChildCount() - 1 >= chipLimit){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            et.clearFocus();
            Snackbar.make(findViewById(R.id.recipient_input_ET), getString(R.string.chips_max_count), Snackbar.LENGTH_LONG).show();
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
        chip.setOnCloseIconClickListener(v -> {
            chipGroup.removeView(chip);
            manageChipEditText();
        });
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