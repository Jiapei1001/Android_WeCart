//package edu.neu.firebase.wecart;
//
//import android.Manifest;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.util.Objects;
//import java.util.UUID;
//
///**
// * Import excel sheet product data to Firebase Realtime Database in Android.
// * <p>
// * Ref: https://www.geeksforgeeks.org/how-to-upload-excel-sheet-data-to-firebase-realtime-database-in-android/
// */
//public class ImportingProductActivity extends AppCompatActivity {
//
//    // Initialize the cell count as 2
//    public static final int cellCount = 2;
//    Button uploadExcelButton;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_importing_product);
//
//        uploadExcelButton = findViewById(R.id.btnUploadExcel);
//
//        uploadExcelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check whether you have a permission
//                // PackageManager.PERMISSION_GRANTED if you have the permission, or
//                // PackageManager.PERMISSION_DENIED if not.
//                if (ActivityCompat.checkSelfPermission(ImportingProductActivity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    selectFile();
//                } else {
//                    // Request permissions in your manifest
//                    ActivityCompat.requestPermissions(ImportingProductActivity.this,
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
//                }
//            }
//        });
//    }
//
//    // Request for storage permission if not given
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 101) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                selectFile();
//            } else {
//                Toast.makeText(ImportingProductActivity.this, "Permission Not Granted",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void selectFile() {
//        // Launch an activity for result
//        mGetContent.launch("*/*");
//    }
//
//    // Start an activity for result (new method)
//    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
//            new ActivityResultContracts.GetContent(),
//            new ActivityResultCallback<Uri>() {
//                @Override
//                public void onActivityResult(Uri result) {
//                    if (result != null) {
//                        if (result.toString().endsWith(".xlsx") || result.toString().endsWith(".xls")) {
//                            readFile()
//                        } else {
//                            Toast.makeText(ImportingProductActivity.this,
//                                    "Please Select an Excel file to upload", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//            });
//}