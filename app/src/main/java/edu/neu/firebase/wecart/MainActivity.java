package edu.neu.firebase.wecart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edu.neu.firebase.wecart.market.Market01Activity;
import edu.neu.firebase.wecart.market.StoreActivity;

public class MainActivity extends AppCompatActivity {
    EditText name;  // Create Username
    EditText passwd;  // Create Password

    EditText UID;

    FirebaseAuth userAuth;

    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        table_user = database.getReference().child("users");

        userAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.editUsername);   //Get the username
        passwd = findViewById(R.id.editPassword);     //get the Password

        Button button1 = findViewById(R.id.button_register);
        Button button2 = findViewById(R.id.button_login);

        button1.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent1);
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // loginEvent();
                buttonEffect(view);
                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Loading...");
                mDialog.show();

                Query query = table_user.orderByChild("username").equalTo(name.getText().toString());
                //        Query query = mProducts.orderByChild("productId").equalTo(curProductId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mDialog.dismiss();
                        for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                            if (userSnapShot.exists()) {
                                User loginUser = userSnapShot.getValue(User.class);
                                if (loginUser.getPassword().equals(passwd.getText().toString())) {
                                    // If login user is a seller
                                    // jump to "SellerBottomNavigationActivity"
                                    if (loginUser.getUser_type().equals("Seller")) {
                                        Intent sellerHomeIntent = new Intent(MainActivity.this,
                                                SellerBottomNavigationActivity.class);
                                        Common.currentUser = loginUser;
                                        startActivity(sellerHomeIntent);
                                        finish();
                                    } else {
                                        Intent homeIntent = new Intent(MainActivity.this, Market01Activity.class);
                                        Common.currentUser = loginUser;
                                        startActivity(homeIntent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Sign-in failed: Your password is wrong.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "The user does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void buttonEffect(View view) {

        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}

