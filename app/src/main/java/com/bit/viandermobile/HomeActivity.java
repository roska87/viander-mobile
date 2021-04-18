package com.bit.viandermobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.SessionViewModel;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;
import static  com.bit.viandermobile.constants.Constants.*;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int LAUNCH_CONFIGURATION_ACTIVITY = 1;

    private VianderViewModel vianderViewModel;
    private SessionViewModel sessionViewModel;

    // variable for shared preferences.
    private SharedPreferences sharedpreferences;
    private String email, username, token;

    // variable for DrawerLayout
    DrawerLayout drawerLayout;

    // variable for ViewPager
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Slider ViewPager
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        drawerLayout = findViewById(R.id.activityHome);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.imagen1));
        sliderItems.add(new SliderItem(R.drawable.imagen2));
        sliderItems.add(new SliderItem(R.drawable.imagen3));
        sliderItems.add(new SliderItem(R.drawable.canelones_home_temporal));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected (int position){
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
        //

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

        /*vianderViewModel.getViandsMenu().observe(this, new Observer<List<PostDto>>() {
            @Override
            public void onChanged(List<PostDto> postDtos) {
                if(postDtos != null && !postDtos.isEmpty()){
                    TextView postView = findViewById(R.id.idPost);
                    postView.setText(postDtos.get(0).getTitle());
                }
            }
        });*/


        // initializing our textview and button.
        TextView welcomeTV = findViewById(R.id.welcome);
        welcomeTV.setText(StringUtils.join(getString(R.string.welcome)));

        TextView user = findViewById(R.id.emailHome);
        user.setText(email);


    }

    //Slider ViewPager
    private Runnable sliderRunnable = new Runnable(){
        @Override
        public void run(){
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    //DrwaerLayout
    public void clickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void clickLogo(View view){
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void clickViandas(View view){
        Intent i = new Intent(HomeActivity.this, ViandasActivity.class);
        startActivity(i);
    }

    public void clickConfig(View view){
        Intent i = new Intent(HomeActivity.this, ConfigurationActivity.class);
        startActivityForResult(i, LAUNCH_CONFIGURATION_ACTIVITY);
    }

    public void clickLogout(View view){
        logout(this);
    }

    private void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(getString(R.string.logout_question));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CONFIGURATION_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Snackbar.make(findViewById(R.id.activityHome), getString(R.string.configuration_updated), Snackbar.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //DrawerLayout
        closeDrawer(drawerLayout);
        
        //Slider ViewPager
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }



}