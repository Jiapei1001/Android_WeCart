package edu.neu.firebase.wecart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    RadioGroup get_user_type;
    String user_type;
    EditText name,password;
    ImageView profile;
    Button btn;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        get_user_type = (RadioGroup) findViewById(R.id.get_type);
        password = findViewById(R.id.editPassword);
        name = findViewById(R.id.editUsername);
        profile = findViewById(R.id.img_profile);
        btn = findViewById(R.id.button_register_user);

        get_user_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.get_shop){
                    user_type = "Seller";
                }else if (checkedId == R.id.get_user){
                    user_type = "User";
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Please enter user Name!  :)", Toast.LENGTH_SHORT).show();
                }else if (password.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Please enter Password!  :)", Toast.LENGTH_SHORT).show();
                }else if (user_type.length() == 0){
                    Toast.makeText(RegisterActivity.this, "Please Choose User type!  :)", Toast.LENGTH_SHORT).show();
                }else if (profile == null){
                    Toast.makeText(RegisterActivity.this, "Please upload user picture!  :)", Toast.LENGTH_SHORT).show();
                }else{
                        signup();
                    }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }

    private void upload() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 10);
    }

    private void signup() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(name.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("users").child(uid);

                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String profile_image = task.toString();
                                        User user = new User();
                                        user.user_type = user_type;
                                        user.username = name.getText().toString();
                                        user.password = password.getText().toString();
                                        user.profile_image = profile_image;
                                        user.uid = uid;
                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);
                                        Toast.makeText(RegisterActivity.this,"Success Create",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                    }
                                });
                            }else{

                                Toast.makeText(RegisterActivity.this,"Error on upload Data",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{

                    Toast.makeText(RegisterActivity.this,"Error on email name!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }
}