package edu.neu.firebase.wecart;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.firebase.database.DatabaseReference;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Upload image to firebase storage (ref: https://www.youtube.com/watch?v=7p4MBsz__ao)
 *
 */
public class AddingProductActivity extends AppCompatActivity {
    Button selectProductButton, submitButton;
    ImageView productImage;

    EditText productNameTxt;
    EditText productBrandTxt;
    EditText priceTxt;
    EditText quantityTxt;
    TextView productStoreTxt;

    String productStore;
    Product product;

    FirebaseStorage storage;
    private DatabaseReference mDatabase; //for insert database object value
    private DatabaseReference mProducts;

    Uri productImageUri;

    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_product);

        // TODO: productStore should be passed from the previous screen
        String productStore = "Super QQ Fruit Store";

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProducts = mDatabase.child("products");

        selectProductButton = findViewById(R.id.btnSelectProduct);
        submitButton = findViewById(R.id.btnSubmit);
        productImage = findViewById(R.id.imgViewProduct);

        productNameTxt = findViewById(R.id.editTextProductName);
        productBrandTxt = findViewById(R.id.editTextBrand);
        priceTxt = findViewById(R.id.editTextPrice);
        quantityTxt = findViewById(R.id.editTextQuantity);
        productStoreTxt = findViewById(R.id.textViewStore);
        productStoreTxt.setText(productStore);

        product = new Product();

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

    private void productInfoUploader() {

        String productImageId;
        productImageId = "images/" + UUID.randomUUID().toString() + "." + getExtension(productImageUri);
        System.out.println("####" + productImageId);

        if (productImageUri != null) {
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

        product.setProductName(productNameTxt.getText().toString().trim());
        product.setProductBrand(productBrandTxt.getText().toString().trim());

        long productPrice = (long)Double.parseDouble(priceTxt.getText().toString().trim());
        product.setPrice(productPrice);

        int productQuantity = Integer.parseInt(quantityTxt.getText().toString().trim());
        product.setQuantity(productQuantity);

        product.setProductStore(productStore);

        mProducts.push().setValue(product);
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