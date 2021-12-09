package edu.neu.firebase.wecart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class SellerCheckOrderDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mRequest;

    int storeId;
    String orderKey;
    Request curOrder;
    String orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_check_order_detail);

        storeId = Common.currentUser.getStoreId();

        if (getIntent() != null) {
            orderKey = getIntent().getStringExtra("ORDERKEY");
            orderStatus = getIntent().getStringExtra("ORDERSTATUS");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRequest = mDatabase.child("request");

        // Change the order status to "pending" (preparing the order)
        changeOrderStatusToPending();

        // Show the product in the "request"
        showOrder();
    }

    private void changeOrderStatusToPending() {
        if (orderStatus.equals("ordered")) {
            mRequest.child(orderKey).child("status").setValue("pending");
            Toast.makeText(SellerCheckOrderDetailActivity.this, "The order is pending.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showOrder() {
        // Retrieve and change data: ref: https://firebase.google.com/docs/database/admin/retrieve-data
        Query query = mRequest.orderByChild("storeId").equalTo(storeId);
        System.out.println("orderKey ##" + orderKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        if (Objects.equals(orderSnapshot.getKey(), orderKey))
                            curOrder = orderSnapshot.getValue(Request.class);
                    }
                }
                StringBuilder orderDetails = null;
                if (curOrder != null) {
                    List<Order> productOrders = curOrder.getProducts();
                    orderDetails = new StringBuilder();
                    for (Order productOrder : productOrders) {
                        orderDetails.append(productOrder.getProductName()).append(" x ").append(productOrder.getQuantity()).append("\n");
                    }
                    System.out.println("##" + orderDetails);
                    TextView productsTxtView = findViewById(R.id.textViewProducts);
                    productsTxtView.setText(orderDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}