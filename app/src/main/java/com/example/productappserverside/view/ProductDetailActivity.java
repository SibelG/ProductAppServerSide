package com.example.productappserverside.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productappserverside.Common.Common;
import com.example.productappserverside.R;
import com.example.productappserverside.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    TextView productName,productDesc,productPrice;
    Button Order;
    ImageView productImage;
    FirebaseDatabase database;
    DatabaseReference productDetailRef;
    ImageButton orderButtonDesc;
    Button plus,minus;
    Toolbar toolbar;


    String productId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        database=FirebaseDatabase.getInstance();
        productDetailRef=database.getReference("Product");
        productName=findViewById(R.id.productNameDetail);
        productDesc=findViewById(R.id.productDetail);
        productPrice=findViewById(R.id.priceDesc);
        productImage=findViewById(R.id.imageUrl);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);

        toolbar=(Toolbar) findViewById(R.id.toolbarName);
        setSupportActionBar(toolbar);


        if(getIntent()!=null){
            productId=getIntent().getStringExtra("productId");

            if(!productId.isEmpty()){
                if(Common.connectivityInternet(this))
                    getDetailProduct(productId);
                else
                    Toast.makeText(ProductDetailActivity.this,"Please check your internet connectivity",Toast.LENGTH_LONG).show();
            }
        }


        orderButtonDesc=findViewById(R.id.orderButtonDesc);
        orderButtonDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(ProductDetailActivity.this, "Add to cart", Toast.LENGTH_SHORT).show();*/

            }
        }

        );

    }

    private void getDetailProduct(String productId) {
        productDetailRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product=snapshot.getValue(Product.class);
                Picasso.with(getBaseContext()).load(product.getImage()).into(productImage);
                productName.setText(product.getName());
                toolbar.setTitle(product.getName());
                productPrice.setText(product.getPrice());
                productDesc.setText(product.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}