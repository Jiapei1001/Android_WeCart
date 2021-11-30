package edu.neu.firebase.wecart;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.firebase.database.DatabaseReference;

import java.util.Objects;
import java.util.UUID;

/**
 * Upload image to firebase storage (ref: https://www.youtube.com/watch?v=7p4MBsz__ao)
 */
public class AddingProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
    Product product;
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
        setContentView(R.layout.activity_adding_product);

        // TODO: productStore should be passed from the previous screen
        productStore = "Super QQ Fruit Store";

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

        // create a new product
        product = new Product();

        product.setProductStore(productStore);

        // check if product id exists
        checkProductIdButton = findViewById(R.id.btnCheckId);
        checkProductIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if productIdTxt EditText is empty or not
                if (!productIdTxt.getText().toString().matches("")) {
                    getProductData();
                } else {
                    // If there is no input
                    Toast.makeText(AddingProductActivity.this, "Please input the product " +
                            "id to check the stock status.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // select an image from gallery in Android Studio (notice that startActivityForResult deprecated)
        selectProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
                // this product is selected successfully from the gallery
            }
        });

        // Upload the image to firebase storage
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload image on button click
                productInfoUploader();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        productUnit = parent.getItemAtPosition(pos).toString();
        product.setProductUnit(productUnit);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void getProductData() {
        // Set product id
        int curProductId = Integer.parseInt(productIdTxt.getText().toString());
        // Check if the product exists in firebase database by the product id
        Query query = mProducts.orderByChild("productId").equalTo(curProductId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if the firebase query is not empty
                if (snapshot.exists()) {
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

                        inStockTxt.setText(R.string.in_stock_message);
                    }
                } else {
                    existedProduct = null;
                    inStockTxt.setText(R.string.not_in_stock_message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }

        });
    }

    private void updateProductData() {
        // Set product id
        int curProductId = Integer.parseInt(productIdTxt.getText().toString());
        // Check if the product exists in firebase database by the product id
        Query query = mProducts.orderByChild("productId").equalTo(curProductId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Even when there is only a single match for the query, the snapshot is still a
                // list; it just contains a single item. To access the item, you need to loop over
                // the result
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    if (productSnapshot.exists()) {
                        existedProduct = productSnapshot.getValue(Product.class);
                        String key = productSnapshot.getKey();
                        assert key != null;
                        // Update the product info
                        if (!existedProduct.getProductName().equals(product.getProductName())) {
                            mProducts.child(key).child("productName").setValue(product.getProductName());
                        } else if (!existedProduct.getProductBrand().equals(product.getProductBrand())) {
                            mProducts.child(key).child("productBrand").setValue(product.getProductBrand());
                        } else if (existedProduct.getQuantity() != product.getQuantity()) {
                            mProducts.child(key).child("quantity").setValue(product.getQuantity());
                        } else if (existedProduct.getPrice() != product.getPrice()) {
                            mProducts.child(key).child("price").setValue(product.getPrice());
                        } else if (!existedProduct.getProductUnit().equals(product.getProductUnit())) {
                            mProducts.child(key).child("productUnit").setValue(product.getProductUnit());
                            // TODO: update product image (check if the image is the same one)
                        } else if (existedProduct.getProductImageId() == null) {
                            mProducts.child(key).child("productImageId").setValue(product.getProductImageId());
                        }
                    } else {
                        inStockTxt.setText(R.string.not_in_stock_message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing
            }

        });
    }

    private void productInfoUploader() {

        // If an image is uploaded
        if (productImageUri != null) {
            productImageId = "images/" + UUID.randomUUID().toString() + "." + getExtension(productImageUri);
            // Create a reference to store the image in firebase storage
            // it will be stored inside images folder in firebase storage
            StorageReference reference = mStorageRef.child(productImageId);

            // TODO: can use user auth id instead of uuid if the app has firebase auth or use System.currentTimeMillis()

            // Store the file
            reference.putFile(productImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Upload image successfully
                        Toast.makeText(AddingProductActivity.this, "Product Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Fail to upload
                        Toast.makeText(AddingProductActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        product.setProductImageId(productImageId);

        int curProductId = Integer.parseInt(productIdTxt.getText().toString());
        product.setProductId(curProductId);

        // Set product name and brand
        product.setProductName(productNameTxt.getText().toString().trim());
        product.setProductBrand(productBrandTxt.getText().toString().trim());

        // Set product price
        double productPrice = Double.parseDouble(priceTxt.getText().toString().trim());
        product.setPrice(productPrice);

        // TODO: Plus and minus button with EditText
        //  (https://stackoverflow.com/questions/56716633/plus-and-minus-button-with-edittext)
        int productQuantity = Integer.parseInt(quantityTxt.getText().toString().trim());
        product.setQuantity(productQuantity);

        if (!productUnit.isEmpty()) {
            product.setProductUnit(productUnit);
        }

        // If the product exists, just update the product data
        if (existedProduct != null) {
            updateProductData();
        } else {
            mProducts.push().setValue(product);
        }
    }

    // Start an activity for result (new method)
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        // this result if the result of uri
                        productImage.setImageURI(result);
                        // result will be set in imageUri
                        productImageUri = result;
                    }
                }
            });

    /**
     * Get the file extension from uri
     *
     * @param uri
     * @return
     */
    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}