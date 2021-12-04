package edu.neu.firebase.wecart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Jump extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);

        Button button1 = (Button)findViewById(R.id.button_jump);
        button1.setOnClickListener(v -> {
            Intent intent1 = new Intent(Jump.this, CustomerSideProductListActivity.class);
            startActivity(intent1);
        });
    }
}