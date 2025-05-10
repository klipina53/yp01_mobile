package com.example.yp01;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class oneItemScreen extends AppCompatActivity {

    ImageView iconOneItemScreen;
    TextView nameDishOneItemScreen;
    TextView priceOneItemScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneitemscreen);

        iconOneItemScreen = findViewById(R.id.iconOneItemScreen);
        nameDishOneItemScreen = findViewById(R.id.nameDishOneItemScreen);
        priceOneItemScreen = findViewById(R.id.priceOneItemScreen);

        String nameDish = getIntent().getStringExtra("NameDish");
        String price = getIntent().getStringExtra("Price");
        String icon = getIntent().getStringExtra("Icon");

        nameDishOneItemScreen.setText(nameDish);
        priceOneItemScreen.setText(price + ".00₽");
        Glide.with(this).load(icon).into(iconOneItemScreen);
    }

    public void onBackMainScreen(View view) {
        finish();
    }
}
