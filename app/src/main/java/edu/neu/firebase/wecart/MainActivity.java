package edu.neu.firebase.wecart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText name;  //Create Username
    EditText passwd;  //Create Password
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editUsername);   //Get the username
        passwd = findViewById(R.id.editPassword);     //get the Password
        Button button1 = (Button) findViewById(R.id.button_register);
        Button button2 = (Button) findViewById(R.id.button_login);
        button1.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent1);
    });

        button2.setOnClickListener(v -> {
            String username = name.getText().toString();
            if (username.length() == 0) {
                Toast.makeText(MainActivity.this, "Please enter user Name!  :)", Toast.LENGTH_SHORT).show();
            }
            else if (username.contains(".")||username.contains("//")) {
                Toast.makeText(MainActivity.this, "Please do not enter . or //!  :)", Toast.LENGTH_SHORT).show();
            }else {
                    Intent intent1 = new Intent(MainActivity.this, Jump.class);
                    startActivity(intent1);
                }
    });
}}