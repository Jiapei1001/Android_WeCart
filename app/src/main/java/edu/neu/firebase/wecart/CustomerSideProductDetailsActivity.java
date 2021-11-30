package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import edu.neu.firebase.wecart.Database.Database;

public class CustomerSideProductDetailsActivity extends AppCompatActivity {

    TextView productName;
    TextView productPrice;
    TextView productDescription;
    ImageView productImage;
    FloatingActionButton cartBtn;
    ElegantNumberButton quantityBtn;

    String productId;

    private DatabaseReference mDatabase;
    private DatabaseReference mProducts;

    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_side_product_details);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProducts = mDatabase.child("products");

        //init view
        quantityBtn = findViewById(R.id.quantity_button);
        cartBtn = findViewById(R.id.cart_button);

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        String.valueOf(productId), p.getProductName(), quantityBtn.getNumber(), String.valueOf(p.getPrice())
                ));
                Snackbar.make(view, "Add to Cart", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        productDescription = findViewById(R.id.product_description);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.product_image);

        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId");
            if (!productId.isEmpty()) {
                getDetailProduct(productId);
            }
        }
    }

    private void getDetailProduct(String productId) {
        mProducts.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p = snapshot.getValue(Product.class);

                Picasso.get().load(p.getProductImageId()).into(productImage);

                productPrice.setText(String.valueOf(p.getPrice()));
                productName.setText(p.getProductName());
                productDescription.setText(p.getProductBrand());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}