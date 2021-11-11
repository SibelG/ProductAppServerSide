package com.example.productappserverside.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.productappserverside.Common.Common;
import com.example.productappserverside.R;
import com.example.productappserverside.adapters.OrderStatusViewHolder;
import com.example.productappserverside.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatusActivity extends AppCompatActivity implements View.OnCreateContextMenuListener{

    FirebaseRecyclerAdapter<Request,OrderStatusViewHolder> adapter;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    DatabaseReference ref;
    MaterialSpinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);


        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Request");

        if(getIntent()==null){
            LoadOrders(Common.CurrentUser.getEmail());
        }else{
            LoadOrders(getIntent().getStringExtra("userEmail"));
        }
    }
    private void LoadOrders(String email){
        adapter=new FirebaseRecyclerAdapter<Request, OrderStatusViewHolder>(Request.class,R.layout.order_status_item_row,OrderStatusViewHolder.class,ref.orderByChild("email").equalTo(email)) {
            @Override
            protected void populateViewHolder(OrderStatusViewHolder orderStatusViewHolder, Request request, int pozition) {
                orderStatusViewHolder.orderEmail.setText(request.getEmail());
                orderStatusViewHolder.orderAddress.setText(request.getAddress());
                orderStatusViewHolder.orderStatus.setText(Common.convertToStatus(request.getStatus()));
                orderStatusViewHolder.orderId.setText(adapter.getRef(pozition).getKey());
                orderStatusViewHolder.setItemClickListener(new OrderStatusViewHolder.itemClickListener() {
                    @Override
                    public void onClick(View view, int pozition, boolean isLongClick) {
                        Intent intent=new Intent(OrderStatusActivity.this,TrackingOrderActivity.class);
                        Common.CurrentRequest=request;
                        startActivity(intent);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    private void deleteOrder(String key){
        ref.child(key).removeValue();
        Toast.makeText(OrderStatusActivity.this,"Order deleted",Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){
            deleteOrder(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }
    private void showUpdateDialog(final String key,final Request item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderStatusActivity.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.update_order_layout,null);
        spinner=add_menu_layout.findViewById(R.id.statusSpinner);
        spinner.setItems("Played","On my way","Shipped");
        alertDialog.setView(add_menu_layout);
        final String localeKey=key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                ref.child(localeKey).setValue(item);
                /*if(newCategory!=null){
                    reference.push().setValue(newCategory);
                    Snackbar.make(drawer,"New Category"+newCategory.getName()+"was added",Snackbar.LENGTH_LONG).show();
                }*/

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
}