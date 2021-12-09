package edu.neu.firebase.wecart;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class SellerOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus;
    public Button checkOrderButton, deliverToPickupButton;

    private SellerItemClickListener sellerItemClickListener;

    public SellerOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        checkOrderButton = itemView.findViewById(R.id.btnCheckOrder);
        deliverToPickupButton = itemView.findViewById(R.id.btnDeliverToPickUp);

        checkOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sellerItemClickListener != null) {
                    // Returns the position of the ViewHolder in terms of the latest layout pass
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        sellerItemClickListener.onCheckOrderClick(position);
                    }
                }
            }
        });

        deliverToPickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sellerItemClickListener != null) {
                    // Returns the position of the ViewHolder in terms of the latest layout pass
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        sellerItemClickListener.onDeliverToPickupClick(position);
                    }
                }
            }
        });
        itemView.setOnClickListener(this);

    }

    public void setSellerItemClickListener(SellerItemClickListener sellerItemClickListener) {
        this.sellerItemClickListener = sellerItemClickListener;
    }

    @Override
    public void onClick(View view) {
        sellerItemClickListener.onClick(view, getAdapterPosition(), false);

    }
}