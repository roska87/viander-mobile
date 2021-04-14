package com.bit.viandermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.SessionViewModel;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;
import static  com.bit.viandermobile.constants.Constants.*;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private VianderViewModel vianderViewModel;
    private SessionViewModel sessionViewModel;

    // variable for shared preferences.
    private SharedPreferences sharedpreferences;
    private String email, username, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sessionViewModel = new ViewModelProvider(this, new SessionFactory(getApplication())).get(SessionViewModel.class);

        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        username = sharedpreferences.getString(USERNAME_KEY, null);
        token = sharedpreferences.getString(TOKEN_KEY, null);
        if(token != null){
            Log.i("Token -> ", token);
        }

        vianderViewModel.getPost(token, 59);

        vianderViewModel.getViandsMenu().observe(this, new Observer<List<PostDto>>() {
            @Override
            public void onChanged(List<PostDto> postDtos) {
                if(postDtos != null && !postDtos.isEmpty()){
                    TextView postView = findViewById(R.id.idPost);
                    postView.setText(postDtos.get(0).getTitle());
                }
            }
        });

        Button configurationBtn = findViewById(R.id.idBtnConfiguration);
        configurationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ConfigurationActivity.class);
                startActivity(i);
            }
        });

        Button confirmationBtn = findViewById(R.id.idBtnConfirmation);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ConfirmationActivity.class);
                i.putExtra(TOTAL_AMOUNT, 123);
                startActivity(i);
                finish();
            }
        });


        // initializing our textview and button.
        TextView welcomeTV = findViewById(R.id.idTVWelcome);
        welcomeTV.setText(StringUtils.join(getString(R.string.welcome), " ", email));
        Button logoutBtn = findViewById(R.id.idBtnLogout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionViewModel.delete();

                // calling method to edit values in shared prefs.
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(TOKEN_KEY, null);
                editor.remove(EMAIL_KEY);
                editor.remove(PASSWORD_KEY);

                // below line will clear
                // the data in shared prefs.
                editor.clear();

                // below line will apply empty
                // data to shared prefs.
                editor.apply();

                vianderViewModel.logout();

                // starting mainactivity after
                // clearing values in shared preferences.
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        EditText et = findViewById(R.id.recipient_input_ET);
        FlexboxLayout chipGroup = findViewById(R.id.recipient_group_FL);
        chipGroup.setFlexWrap(FlexWrap.WRAP);
        chipGroup.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
        chipGroup.setAlignItems(AlignItems.CENTER);
        chipGroup.setJustifyContent(JustifyContent.SPACE_AROUND);
        chipGroup.setAlignContent(AlignContent.STRETCH);

        int chipLimit = 2;

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
            if(controlChipLimit(chipGroup, chipLimit)){
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
                Log.i("ChipGroup", ""+chipGroup.getChildCount());
                if(controlChipLimit(chipGroup, chipLimit)){
                    s.clear();
                }
                if (str.length() > 1 && (str.endsWith(" ") || str.endsWith("\n"))) {
                    addNewChip(str, chipGroup);
                    s.clear();
                    controlChipLimit(chipGroup, chipLimit);

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

    private void addNewChip(String person, FlexboxLayout chipGroup) {
        Chip chip = new Chip(HomeActivity.this);
        chip.setText(person);
        chip.setChipIcon(ChipDrawable.createFromResource(HomeActivity.this, R.xml.standalone_chip));
        chip.setCloseIconVisible(true);
        //chip.setClickable(true);
        //chip.setCheckable(false);
        chipGroup.addView(chip, chipGroup.getChildCount() - 1);
        chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
    }

}