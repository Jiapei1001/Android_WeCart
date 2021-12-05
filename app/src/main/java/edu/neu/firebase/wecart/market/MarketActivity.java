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

public class MarketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

//        ScrollView scrollView = (ScrollView) findViewById(R.id.market01ScrollView);
//        HorizontalScrollView scrollViewHorizontal = (HorizontalScrollView) findViewById(R.id.market01HorizontalScrollView);
////        scrollView.smoothScrollTo(2,3);
////        scrollView.smoothScrollBy(1, 2);
//
//        ObjectAnimator.ofInt(scrollView, "scrollY",  scrollView.getBottom()).setDuration(6000).start();
//        ObjectAnimator.ofInt(scrollViewHorizontal, "scrollX",  scrollViewHorizontal.getRight()).setDuration(6000).start();
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
        startActivity(intent);
    }

    private String idToString(int id) {
        @SuppressLint("ResourceType") String storeName = getString(id);
        System.out.println(storeName);
        String storeId = storeName.replace("store", "");
        System.out.println(storeId);

        return storeId;
    }
}