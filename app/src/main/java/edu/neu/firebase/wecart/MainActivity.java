package edu.neu.firebase.wecart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText name;  //Create Username
    EditText passwd;  //Create Password
    FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.editUsername);   //Get the username
        passwd = findViewById(R.id.editPassword);     //get the Password
        Button button1 = findViewById(R.id.button_register);
        Button button2 = findViewById(R.id.button_login);
        button1.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent1);
        });

        button2.setOnClickListener(view -> loginEvent());
    }

        private void loginEvent() {

            userAuth.signInWithEmailAndPassword(name.getText().toString(), passwd.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(MainActivity.this,SellerChatActivity.class));

                            } else {
                                Toast.makeText(MainActivity.this,"Password doesn't match!",Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

            }}