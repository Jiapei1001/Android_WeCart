package edu.neu.firebase.wecart;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import edu.neu.firebase.wecart.fragments.SellerHomeFragment;
import edu.neu.firebase.wecart.fragments.SellerOrdersFragment;
import edu.neu.firebase.wecart.market.Store;

public class CreatingSellerStoreActivity extends AppCompatActivity {
    private Button selectStoreImageButton;
    private Button selectOwnerImageButton;
    private Button submitButton;

    private ImageView storeDetailBtnImageView;
    private ImageView storeImageView;
    private ImageView storeOwnerImageView;

    private Uri storeDetailBtnImageUri, storeImageUri, storeOwnerImageUri;

    private EditText storeNameTxt;
    private EditText storeDescTxt;
    private EditText storeOwnerTxt;
    private EditText storeAddressTxt;
    private EditText storePhoneTxt;
    private EditText storeEmailTxt;
    private EditText pickupAddressTxt;

    private User curLoginUser;
    private int storeId;

    private FirebaseStorage storage;
    private DatabaseReference mDatabase; //for insert database object value
    private DatabaseReference mStores;
    private StorageReference mStorageRef;

    private Store newStore;

    // image id (path) saved in firebase
    private String storeBtnImage;
    private String storeImage;
    private String storeOwnerImage;

    private Double longtitude;
    private Double latitude;  // pickup position for delivery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_seller_store);

        curLoginUser = Common.currentUser;

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStores = mDatabase.child("stores");

        Button selectStoreBtnButton = findViewById(R.id.btnSelectStoreButton);
        selectStoreImageButton = findViewById(R.id.btnSelectStoreImage);
        selectOwnerImageButton = findViewById(R.id.btnSelectOwnerImage);
        submitButton = findViewById(R.id.btnSubmit);

        storeNameTxt = findViewById(R.id.storeName);
        storeDescTxt = findViewById(R.id.storeDesc);
        storeOwnerTxt = findViewById(R.id.storeOwner);
        storeAddressTxt = findViewById(R.id.storeAddress);
        storePhoneTxt = findViewById(R.id.storePhone);
        storeEmailTxt = findViewById(R.id.storeEmail);
        pickupAddressTxt = findViewById(R.id.pickupAddress);

        storeDetailBtnImageView = findViewById(R.id.storeDetailBtn);
        storeImageView = findViewById(R.id.storeImage);
        storeOwnerImageView = findViewById(R.id.storeOwnerImage);

        newStore = new Store();

        // Set new store's id
        Query query = mStores.orderByChild("storeId").limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {
                    storeId = myDataSnapshot.getValue(Store.class).getStoreId() + 1;
                    newStore.setStoreId(storeId);
                }
            }

            public void onCancelled(DatabaseError firebaseError) {

            }

        });

        // select an image from gallery in Android Studio (notice that startActivityForResult deprecated)
        selectStoreBtnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContentStoreBtn.launch("image/*");
                // this store button image is selected successfully from the gallery
            }
        });

        selectOwnerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContentOwnerImage.launch("image/*");
                // this owner image is selected successfully from the gallery
            }
        });

        selectStoreImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContentStoreImage.launch("image/*");
                // this store image is selected successfully from the gallery
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload image on button click
                storeInfoUploader();
            }
        });
    }

    // Start an activity for result (new method)
    ActivityResultLauncher<String> mGetContentStoreBtn = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        // this result if the result of uri
                        storeDetailBtnImageView.setImageURI(result);
                        // result will be set in imageUri
                        storeDetailBtnImageUri = result;
                    }
                }
            });

    // Start an activity for result (new method)
    ActivityResultLauncher<String> mGetContentOwnerImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        // this result if the result of uri
                        storeOwnerImageView.setImageURI(result);
                        // result will be set in imageUri
                        storeOwnerImageUri = result;
                    }
                }
            });


    // Start an activity for result (new method)
    ActivityResultLauncher<String> mGetContentStoreImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        // this result if the result of uri
                        storeImageView.setImageURI(result);
                        // result will be set in imageUri
                        storeImageUri = result;
                    }
                }
            });

    private void storeInfoUploader() {

        // If an image is uploaded
        if (storeDetailBtnImageView.getDrawable() != null) {
            storeBtnImage = "stores/storeBtns/" + UUID.randomUUID().toString() + "." + getExtension(storeDetailBtnImageUri);
            // Create a reference to store the image in firebase storage
            // it will be stored inside images folder in firebase storage
            StorageReference reference = mStorageRef.child(storeBtnImage);

            // Store the file
            reference.putFile(storeDetailBtnImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Upload image successfully
                        Toast.makeText(CreatingSellerStoreActivity.this, "Store Button Image was Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Fail to upload
                        Toast.makeText(CreatingSellerStoreActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (storeImageView.getDrawable() != null) {
            storeImage = "stores/storeImages/" + UUID.randomUUID().toString() + "." + getExtension(storeImageUri);
            // Create a reference to store the image in firebase storage
            // it will be stored inside images folder in firebase storage
            StorageReference reference = mStorageRef.child(storeImage);

            // Store the file
            reference.putFile(storeImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Upload image successfully
                        Toast.makeText(CreatingSellerStoreActivity.this, "Store Image was Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Fail to upload
                        Toast.makeText(CreatingSellerStoreActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (storeOwnerImageView.getDrawable() != null) {
            storeOwnerImage = "stores/storeOwners/" + UUID.randomUUID().toString() + "." + getExtension(storeOwnerImageUri);
            // Create a reference to store the image in firebase storage
            // it will be stored inside images folder in firebase storage
            StorageReference reference = mStorageRef.child(storeOwnerImage);

            // Store the file
            reference.putFile(storeOwnerImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Upload image successfully
                        Toast.makeText(CreatingSellerStoreActivity.this, "Store Owner Image was Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Fail to upload
                        Toast.makeText(CreatingSellerStoreActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        newStore.setStoreBtn(storeBtnImage);
        newStore.setStoreImage(storeImage);
        newStore.setOwnerImage(storeOwnerImage);

        newStore.setStoreName(storeNameTxt.getText().toString().trim());
        newStore.setStoreDes(storeDescTxt.getText().toString().trim());
        newStore.setOwnerName(storeOwnerTxt.getText().toString().trim());
        newStore.setStoreAddress(storeAddressTxt.getText().toString().trim());
        newStore.setPhone(storePhoneTxt.getText().toString().trim());
        newStore.setEmail(storeEmailTxt.getText().toString().trim());

        String pickupAddress = pickupAddressTxt.getText().toString().trim();

        newStore.setAddress(pickupAddress);
        System.out.println("####" + getLocationURLFromAddress(CreatingSellerStoreActivity.this, pickupAddress));
        newStore.setLatitude(latitude);
        newStore.setLongitude(longtitude);

        curLoginUser.setStoreId(storeId);
        curLoginUser.setStoreName(storeNameTxt.getText().toString().trim());

        mStores.push().setValue(newStore);
        Toast.makeText(CreatingSellerStoreActivity.this, "Create Store Successfully", Toast.LENGTH_SHORT).show();
    }

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

    public String getLocationURLFromAddress(Context context,
                                                   String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latitude = location.getLatitude();
            longtitude = location.getLongitude();

            return "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
                    + location.getLatitude()
                    + ","
                    + location.getLongitude()
                    + "&sensor=false";

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return strAddress;
    }
}
