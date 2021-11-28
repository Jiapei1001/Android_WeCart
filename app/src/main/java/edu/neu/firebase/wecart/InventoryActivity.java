package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * List all product as inventory
 */
public class InventoryActivity extends AppCompatActivity {

    private InventoryAdapter inventoryAdapter;
    private final ArrayList<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // for reading product data
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mProducts = mDatabase.child("products");

        // call method to build the recyclerView
        createRecyclerView();

        // Todo: obtain current store
        String currStore = "Super QQ Fruit Store";

        TextView inventoryTitle = (TextView) this.findViewById(R.id.textViewInventory);
        inventoryTitle.setText(R.string.inventory);

        // Read data
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Product count: ", "" + snapshot.getChildrenCount());

                productList.clear();
                for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                    // Get User object and use the values to update the UI
                    Product product = productSnapShot.getValue(Product.class);

                    // Check if the contactUser is not current login user
                    assert product != null;
                    if (product.getProductStore().equals(currStore)) {
                        productList.add(product);
                    }
                }
                inventoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting product failed, log a message
                Log.e("The read failed: ", error.getMessage());
            }
        };
        mProducts.addValueEventListener(productListener);
    }

    // Call on create option menu
    // layout to inflate our menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get menu inflater
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Get the menu item
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // Get search view of the item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Call setOnQueryTextListener method
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Call a method to filter our recyclerView
                filter(newText);
                return false;
            }
        });

        return true;
    }

    private void filter(String text) {
        // Create a new array list to filter our data
        ArrayList<Product> filteredList = new ArrayList<>();

        // Run a "for loop" to compare elements
        for (Product item : productList) {
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Product Data Found...", Toast.LENGTH_SHORT).show();
        } else {
            inventoryAdapter.filterList(filteredList);
        }
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView inventoryRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewInventory);
        inventoryRecyclerView.setHasFixedSize(true);
        this.inventoryAdapter = new InventoryAdapter(this.productList, this);
        inventoryRecyclerView.setAdapter(this.inventoryAdapter);
        inventoryRecyclerView.setLayoutManager(layoutManager);
    }
}