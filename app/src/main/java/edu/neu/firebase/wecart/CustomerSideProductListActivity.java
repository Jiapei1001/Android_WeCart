package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerSideProductListActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;
    String StoreId = "Super QQ Fruit Store";
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mProducts;
    private final ArrayList<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_side_product_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProducts = mDatabase.child("products");

        recyclerView = findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Todo: change store id - parse from login seller
        if (getIntent() != null) {
            //int curProductId = Integer.parseInt(productIdTxt.getText().toString());
//            String curProductId = getIntent().getStringExtra("StoreId");
//            System.out.println("----------");
//            System.out.println(curProductId);
//            System.out.println("----------");
            if (!StoreId.isEmpty()) {
                loadProduct(StoreId);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(CustomerSideProductListActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }

        });

    }

    private void loadProduct(String StoreId) {
        // Check if the product exists in firebase database by the product id
        //Query query = mProducts.orderByChild("productId").equalTo(curProductId);

        Query query = FirebaseDatabase.getInstance().getReference().child("products").orderByChild("productStore").equalTo(StoreId);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(query, Product.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {

            @Override
            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_recycler_row, parent, false);

                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ProductViewHolder holder, int position, Product p) {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference mStorageRef = storage.getReference();

                holder.productIdTxt.setText(String.valueOf(p.getProductId()));
                holder.txtProductName.setText(p.getProductName());
                holder.txtProductDescription.setText(p.getProductBrand());
                holder.txtProductPrice.setText(String.valueOf(p.getPrice()));

                // Show Picture that retrieved from Firebase Storage using Glide
                StorageReference imagesStorageRef = mStorageRef.child(String.valueOf(p.getProductImageId()));
                Glide.with(getApplicationContext()).load(imagesStorageRef).into(holder.imgProduct);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intentCustomerSideProductDetailsActivity = new Intent(CustomerSideProductListActivity.this, CustomerSideProductDetailsActivity.class);

                        System.out.println(p.getProductId());
                        intentCustomerSideProductDetailsActivity.putExtra("productId", p.getProductId());
                        startActivity(intentCustomerSideProductDetailsActivity);
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}