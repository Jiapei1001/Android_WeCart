package edu.neu.firebase.wecart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.neu.firebase.wecart.Database.Database;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    double tempTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Todo: storeName, storeId
        String storeName = "Mary's County Ranch";
        int storeId = 1;

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("request");

        //init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonEffect(view);

                Request request = new Request(

                        Common.currentUser.getUsername(),
                        txtTotalPrice.getText().toString(),
                        cart, storeName, storeId
                );

                if(tempTotal != 0.0) {
                    //submit to firebase
                    requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    //Delete Cart
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(CartActivity.this, "Successfully Ordered!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadListProduct();

    }

    private void buttonEffect(View view) {

        view.setOnTouchListener(new View.OnTouchListener() {

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

    private void loadListProduct() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);


        //Calculate price
        double total = 0.0;
        for(Order order:cart)
            total+=(Double.parseDouble(order.getPrice()))*(Double.parseDouble(order.getQuantity()));
        tempTotal = total;
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }
}