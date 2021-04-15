package com.bit.viandermobile;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.bit.viandermobile.constants.Constants.TOTAL_AMOUNT;

public class ViandasActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String s1[], s2[];
    //Añadir imagenes
    //imagenes int images[] = {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viandas);

        recyclerView = findViewById(R.id.recyclerView);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, "Prueba", "200", 2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button confirmationBtn = findViewById(R.id.confirm_button);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViandasActivity.this, ConfirmationActivity.class);
                i.putExtra(TOTAL_AMOUNT, 123);
                startActivity(i);
                finish();
            }
        });
    }

    //images[] Se consigue la imagen


}
