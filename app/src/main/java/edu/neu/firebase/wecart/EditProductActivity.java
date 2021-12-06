package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EditProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int curProductId;

    Button selectProductButton, submitButton, checkProductIdButton;
    ImageView productImage;

    EditText productIdTxt;
    EditText productNameTxt;
    EditText productBrandTxt;
    EditText priceTxt;
    EditText quantityTxt;
    TextView productStoreTxt;
    TextView inStockTxt;

    Spinner UnitChoiceSpinner;
    String productUnit;

    String productStore;
    Product existedProduct;

    FirebaseStorage storage;
    private DatabaseReference mDatabase; //for insert database object value
    private DatabaseReference mProducts;

    Uri productImageUri;

    String productImageId;

    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Todo: change store
        productStore = "Super QQ Fruit Store";

        // Retrieve curProductId from previous activity ImportingProductActivity
        curProductId = getIntent().getIntExtra("curProductId", 0);

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProducts = mDatabase.child("products");

        selectProductButton = findViewById(R.id.btnSelectProduct);
        submitButton = findViewById(R.id.btnSubmit);
        productImage = findViewById(R.id.imgViewProduct);

        productIdTxt = findViewById(R.id.editTextProductId);
        productNameTxt = findViewById(R.id.editTextProductName);
        productBrandTxt = findViewById(R.id.editTextBrand);
        priceTxt = findViewById(R.id.editTextPrice);
        quantityTxt = findViewById(R.id.editTextQuantity);
        inStockTxt = findViewById(R.id.textViewInStock);

        productStoreTxt = findViewById(R.id.textViewStore);
        productStoreTxt.setText(productStore);

        // Using spinners to do the product unit Choice
        // Take the instance of Spinner and apply OnItemSelectedListener on it which tells which item
        // of spinner is clicked.
        UnitChoiceSpinner = (Spinner) findViewById(R.id.spinnerUnitChoice);
        UnitChoiceSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        UnitChoiceSpinner.setAdapter(arrayAdapter);

        // Retrieve the data from firebase
        getProductData();
//        updateProductData();
    }

    private void getProductData() {

        // Check if the product exists in firebase database by the product id
        Query query = mProducts.orderByChild("productId").equalTo(curProductId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Even when there is only a single match for the query, the snapshot is still a
                // list; it just contains a single item. To access the item, you need to loop over
                // the result
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {

                    existedProduct = productSnapshot.getValue(Product.class);

                    // When the product exists, just display the info in EditText
                    productIdTxt.setText(String.valueOf(Objects.requireNonNull(existedProduct).getProductId()));
                    productNameTxt.setText(existedProduct.getProductName());
                    productBrandTxt.setText(existedProduct.getProductBrand());
                    priceTxt.setText(String.valueOf(existedProduct.getPrice()));
                    quantityTxt.setText(String.valueOf(existedProduct.getQuantity()));

                    // Todo: Assign the specific unit to spinners

                    // Show Picture that retrieved from Firebase Storage using Glide
                    StorageReference imagesStorageRef = mStorageRef.child(String.valueOf(existedProduct.getProductImageId()));
                    Glide.with(getApplicationContext()).load(imagesStorageRef).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }

        });
    }

//    private void updateProductData() {
//        existedProduct。
//        Query query = mProducts.orderByChild("productId").equalTo(curProductId);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Even when there is only a single match for the query, the snapshot is still a
//                // list; it just contains a single item. To access the item, you need to loop over
//                // the result
//                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
//                    existedProduct = productSnapshot.getValue(Product.class);
//                    String key = productSnapshot.getKey();
//                    assert key != null;
//                    // Update the product info
//                    if (!existedProduct.getProductName().equals(product.getProductName())) {
//                        mProducts.child(key).child("productName").setValue(product.getProductName());
//                    } else if (!existedProduct.getProductBrand().equals(product.getProductBrand())) {
//                        mProducts.child(key).child("productBrand").setValue(product.getProductBrand());
//                    } else if (existedProduct.getQuantity() != product.getQuantity()) {
//                        mProducts.child(key).child("quantity").setValue(product.getQuantity());
//                    } else if (existedProduct.getPrice() != product.getPrice()) {
//                        mProducts.child(key).child("price").setValue(product.getPrice());
//                    } else if (!existedProduct.getProductUnit().equals(product.getProductUnit())) {
//                        mProducts.child(key).child("productUnit").setValue(product.getProductUnit());
//                        // TODO: update product image (check if the image is the same one)
//                    } else if (existedProduct.getProductImageId() == null) {
//                        mProducts.child(key).child("productImageId").setValue(product.getProductImageId());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        productUnit = parent.getItemAtPosition(position).toString();
        existedProduct.setProductUnit(productUnit);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}