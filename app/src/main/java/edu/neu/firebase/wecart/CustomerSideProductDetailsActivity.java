package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import edu.neu.firebase.wecart.Database.Database;

public class CustomerSideProductDetailsActivity extends AppCompatActivity {

    TextView productName;
    TextView productPrice;
    TextView productDescription;
    ImageView productImage;
    FloatingActionButton cartBtn;
    ElegantNumberButton quantityBtn;

    int storeId;
    int curProductId;

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
                        String.valueOf(curProductId), p.getProductName(), quantityBtn.getNumber(), String.valueOf(p.getPrice())
                ));
                Snackbar.make(view, "Add to Cart", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        productDescription = findViewById(R.id.product_description);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.img_product);

        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            storeId = extras.getInt("storeId");
            curProductId = extras.getInt("productId");
            //if (!productId.isEmpty()) {
            getDetailProduct();
            //}
        }
    }

//    private void getDetailProduct(int productId) {
//        mProducts.child(String.valueOf(productId)).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                p = snapshot.getValue(Product.class);
//
//                //Picasso.get().load(p.getProductImageId()).into(productImage);
//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                StorageReference mStorageRef = storage.getReference();
//                // Show Picture that retrieved from Firebase Storage using Glide
//                StorageReference imagesStorageRef = mStorageRef.child(String.valueOf(p.getProductImageId()));
//                Glide.with(getApplicationContext()).load(imagesStorageRef).into(productImage);
//
//                productPrice.setText(String.valueOf(p.getPrice()));
//                productName.setText(p.getProductName());
//                productDescription.setText(p.getProductBrand());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getDetailProduct() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReference();

        // Check if the product exists in firebase database by the product id
        String storeIdToProductId = storeId + "_" + curProductId;
        Query query = mProducts.orderByChild("storeIdToProductId").equalTo(storeIdToProductId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Even when there is only a single match for the query, the snapshot is still a
                // list; it just contains a single item. To access the item, you need to loop over
                // the result
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {

                    p = productSnapshot.getValue(Product.class);

//                    Picasso.get().load(p.getProductImageId()).into(productImage);

                    // Show Picture that retrieved from Firebase Storage using Glide
                    assert p != null;
                    StorageReference imagesStorageRef = mStorageRef.child(String.valueOf(p.getProductImageId()));
                    GlideApp.with(getApplicationContext()).load(imagesStorageRef).into(productImage);

                    productPrice.setText(String.valueOf(p.getPrice()));
                    productName.setText(p.getProductName());
                    productDescription.setText(p.getProductBrand());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }

        });
    }
}