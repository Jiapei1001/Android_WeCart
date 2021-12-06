package edu.neu.firebase.wecart.market;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import edu.neu.firebase.wecart.R;

public class Market01Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.store1:
                String storeId = idToString(R.id.store1);
                openClickyActivity(storeId);
                break;
        }
    }

    public void openClickyActivity(String storeId) {
        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("storeId", storeId);
        intent.putExtra("marketId", "market01");
        startActivity(intent);
    }

    private String idToString(int id) {
        String storeName = getResources().getResourceName(id);

        String[] storeSplit = storeName.split("id/");
        String storeId = storeSplit[1].replace("store", "");

        return storeId;
    }
}