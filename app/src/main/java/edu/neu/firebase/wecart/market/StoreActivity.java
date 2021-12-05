package edu.neu.firebase.wecart.market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.neu.firebase.wecart.R;

public class StoreActivity extends AppCompatActivity {

    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        String storeId = getIntent().getStringExtra("storeId");
        System.out.println(storeId);
    }
}