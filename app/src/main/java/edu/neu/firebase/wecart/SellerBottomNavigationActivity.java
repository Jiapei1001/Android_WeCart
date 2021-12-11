package edu.neu.firebase.wecart.seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.neu.firebase.wecart.R;

public class SellerBottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_bottom_navigation);

        // Initialize Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.sellerBottomNav);

        // Pass the ID's of different destination
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_seller_home,
                R.id.navigation_seller_orders, R.id.navigation_seller_chat, R.id.navigation_seller_profile).build();

        // Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}