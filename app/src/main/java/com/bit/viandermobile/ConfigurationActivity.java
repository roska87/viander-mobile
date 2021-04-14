package com.bit.viandermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Pair;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.ChipInfo;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

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

    private SharedPreferences sharedPreferences;
    private String username, token;

    private CheckBox chCeliac, chDiabetic, chVegan, chSunday, chMonday, chTuesday, chWednesday, chThursday, chFriday, chSaturday;

    private VianderViewModel vianderViewModel;

    private NachoTextView vChip;
    private TextInputEditText editText;

    private int spannedLength = 0, chipLength = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        final ChipGroup entryChipGroup = findViewById(R.id.entry_chip_group);

        editText = findViewById(R.id.editText);

        /*
        ChipDrawable chipDrawable = ChipDrawable.createFromResource(ConfigurationActivity.this, R.xml.standalone_chip);
        chipDrawable.setBounds(0, 0, chipDrawable.getIntrinsicWidth(), chipDrawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(chipDrawable);
        Editable text = editText.getText();
        text.append("hola");
        text.setSpan(span, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         */

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable){
                String text = editable.toString();
                if(text.endsWith(" ")){
                    text = text.replace(" ", "");
                    Chip entryChip2 = getChip(entryChipGroup, text);
                    entryChipGroup.addView(entryChip2);
                    editable.clear();
                }
                if(text.endsWith("\n")){
                    text = text.replace("\n", "");
                    Chip entryChip2 = getChip(entryChipGroup, text);
                    entryChipGroup.addView(entryChip2);
                    editable.clear();
                }

                text = editable.toString();
                if(text.endsWith(" ")){
                    String[] parts = editable.toString().split(" ");
                    int lastpos = 0;
                    for(String part : parts){
                        ChipDrawable chip = ChipDrawable.createFromResource(ConfigurationActivity.this, R.xml.standalone_chip);
                        chip.setText(part);
                        chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
                        //ImageSpan span = new ImageSpan(chip);
                        editable.setSpan(chip, lastpos, lastpos + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        lastpos += part.length() + 1;
                    }
                }
            }
        });

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setAlignItems(AlignItems.STRETCH);

        //RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //recyclerView.setLayoutManager(flexboxLayoutManager);
        //recyclerView.setAdapter(new ChipAdapter());

        LinearLayout linearLayout = findViewById(R.id.linearChips);
        //GridLayout linearLayout = findViewById(R.id.linearChips);
        //TextInputLayout linearLayout = findViewById(R.id.textInputLayout);
        //FlexboxLayout linearLayout = findViewById(R.id.linearChips);
        TextInputEditText tiet = findViewById(R.id.textInputEditText);
        //ChipGroup chipGroup = findViewById(R.id.chipGroup);
        tiet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                //if (trimmed.length() > 1 && trimmed.endsWith(",")) {
                if (str.length() > 1 && (str.endsWith(" ") || str.endsWith("\n"))) {
                    if(TextUtils.isEmpty(str.replace(" ", ""))){
                        s.clear();
                        return;
                    }
                    Chip chip = new Chip(ConfigurationActivity.this);
                    String trimmed = str.trim();
                    chip.setChipDrawable(ChipDrawable.createFromResource(ConfigurationActivity.this, R.xml.standalone_chip));
                    chip.setText(trimmed.substring(0, trimmed.length()));
                    chip.setCloseIconVisible(true);
                    chip.setChipEndPadding(50);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //chipGroup.removeView(chip);
                            linearLayout.removeView(chip);
                        }
                    });
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Chip c = (Chip) v;
                            s.clear();
                            s.append(c.getText());
                            linearLayout.removeView(c);
                            //chipGroup.removeView(c);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    });
                    //chipGroup.addView(chip, chipGroup.getChildCount()-1);
                    linearLayout.addView(chip, linearLayout.getChildCount()-1);
                    s.clear();
                }
            }
        });

        tiet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /*
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    if (tiet.length() == 0 && chipGroup.getChildCount() > 0) {
                        Chip chip = (Chip) chipGroup.getChildAt(chipGroup.getChildCount() - 1);
                        chipGroup.removeView(chip);
                    }
                }*/

                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    if (tiet.length() == 0 && linearLayout.getChildCount() > 0) {
                        Chip chip = (Chip) linearLayout.getChildAt(linearLayout.getChildCount() - 2);
                        linearLayout.removeView(chip);
                    }
                }
                return false;
            }
        });

        vChip = findViewById(R.id.vChip);
        vChip.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        boolean moveChipToEnd = false;
        boolean chipifyUnterminatedTokens = true;
        vChip.enableEditChipOnTouch(moveChipToEnd, chipifyUnterminatedTokens);

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
                for(String value : vChip.getChipValues()){
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
                vChip.setText("");
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
                vChip.setTextWithChips(new ArrayList<>());
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
                            ChipInfo ci = new ChipInfo(word, word);
                            chipList.add(ci);
                        }
                    }
                    vChip.setTextWithChips(chipList);
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

        /*
        // load autocomplete
        FlexboxLayout tagsChipGroup = findViewById(R.id.tagsChipGroup);
        AutoCompleteTextView editTextDrop = findViewById(R.id.tagsAutoCompleteTextView);
        String[] allTags = {"Love", "Passion", "Peace", "Hello", "Test"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allTags);
        editTextDrop.setAdapter(adapter);

        String[] tags = {"Hello", "This Is A Big World", "Test Multiple Row"};

        for (String name : tags) {
            LayoutInflater inflater = LayoutInflater.from(this);
            Chip chip = (Chip) inflater.inflate(R.layout.view_chip,  null);
            chip.setText(name);
            chip.setCloseIconVisible(true);
            chip.setClickable(true);
            chip.setCheckable(false);
            tagsChipGroup.addView(chip);
        }

         */

    }

    private Chip getChip(final ChipGroup entryChipGroup, String text) {
        final Chip chip = new Chip(this);
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.standalone_chip));
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryChipGroup.removeView(chip);
            }
        });
        return chip;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            //View view = getCurrentFocus().getTouchables().get(0);
            //if(!(view instanceof Chip)){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            //}
        }
        return super.dispatchTouchEvent(ev);
    }

}