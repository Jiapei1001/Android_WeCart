package edu.neu.firebase.wecart.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
            case R.id.store2:
                storeId = idToString(R.id.store2);
                openClickyActivity(storeId);
                break;
            case R.id.store3:
                storeId = idToString(R.id.store3);
                openClickyActivity(storeId);
                break;
            case R.id.store4:
                storeId = idToString(R.id.store4);
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