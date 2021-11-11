package com.example.productappserverside.view;

import static com.example.productappserverside.adapters.MenuViewHolder.Delete;
import static com.example.productappserverside.adapters.MenuViewHolder.Update;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productappserverside.R;
import com.example.productappserverside.adapters.productViewHolder;
import com.example.productappserverside.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView Recyclerview;
    FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    DatabaseReference ref,refCategory;
    ImageView image;
    TextView foodName;
    String categoryId="";
    Button select,upload;
    Product newProduct;
    Uri newUri;
    RelativeLayout rootLayout;
    FloatingActionButton fab;
    private final int pickImage=71;

    FirebaseRecyclerAdapter<Product, productViewHolder>adapter;
    FirebaseRecyclerAdapter<Product, productViewHolder>searchAdapter;

    List<String> LastSearchList=new ArrayList<>();
    MaterialSearchBar searchBar;
    MaterialEditText price,discount,name,description;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        database=FirebaseDatabase.getInstance();
        foodName=findViewById(R.id.productName);
        image=findViewById(R.id.imageViewProduct);
        rootLayout=findViewById(R.id.rootLayout);

        ref=database.getReference("Product");
        Recyclerview=findViewById(R.id.recycler_food);
        Recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(ProductListActivity.this);
        Recyclerview.setLayoutManager(manager);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        if(getIntent()!=null){
            categoryId=getIntent().getStringExtra("categoryId");

        }else{
            Toast.makeText(ProductListActivity.this,"Null",Toast.LENGTH_LONG).show();
        }
        if(!categoryId.isEmpty()&&categoryId!=null){
            loadMenu(categoryId);

        }
        //loadSuggest();
        fab=findViewById(R.id.fabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAndProduct();
            }
        });


    }
    private void loadMenu(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, productViewHolder>(Product.class, R.layout.recycler_product_item_row, productViewHolder.class, ref.orderByChild("categoryId").equalTo(categoryId)) {


            @Override
            protected void populateViewHolder(productViewHolder productViewHolder, Product product, int i) {
                productViewHolder.foodName.setText(product.getName());
                Picasso.with(getBaseContext()).load(product.getImage()).into(productViewHolder.foodImage);

                final Product productDesc=product;
                productViewHolder.setItemClickListener(new productViewHolder.ItemClickListener() {
                    @Override
                    public void onClick(View view, int pozition, boolean isLongClick) {

                        /*Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        intent.putExtra("productId", adapter.getRef(pozition).getKey());
                        startActivity(intent);*/
                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
        System.out.println("Tag"+adapter.getItemCount());
        Recyclerview.setAdapter(adapter);
    }
    private void showAndProduct(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProductListActivity.this);
        alertDialog.setTitle("Add new product");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=getLayoutInflater();
        View add_product_layout=inflater.inflate(R.layout.add_new_product_recycler_layout,null);
        name=add_product_layout.findViewById(R.id.edtName);
        description=add_product_layout.findViewById(R.id.edtDescription);
        price=add_product_layout.findViewById(R.id.edtPrice);
        discount=add_product_layout.findViewById(R.id.edtDiscount);
        select=add_product_layout.findViewById(R.id.select);
        upload=add_product_layout.findViewById(R.id.btnUpload);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });

        alertDialog.setView(add_product_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(newProduct!=null){
                    ref.push().setValue(newProduct);
                    Snackbar.make(rootLayout,"New Product "+newProduct.getName()+" was added",Snackbar.LENGTH_LONG).show();
                }

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

    private void uploadImage() {
        if(newUri!=null){
            ProgressDialog dialog=new ProgressDialog(this);
            dialog.setMessage("Image uploading..");
            dialog.show();


            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder=storageReference.child("image/"+imageName);
            imageFolder.putFile(newUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();

                            Toast.makeText(ProductListActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newProduct=new Product(name.getText().toString(),price.getText().toString(),discount.getText().toString(),categoryId,newUri.toString(),description.getText().toString());
                                    //item.setImageUrl(uri.toString());
                                    upload.setText("Uploaded");
                                }
                            });

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(ProductListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded"+progress+"%");
                }
            });



        }else{
            Toast.makeText(ProductListActivity.this,"Uri not selected",Toast.LENGTH_LONG).show();

        }
    }
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,pickImage);
        //startActivityForResult(Intent.createChooser(intent,"Select Picture"),pickImage);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK ){
            switch (requestCode){
                case pickImage:
                    newUri=data.getData();
                    select.setText("Image selected");
                    break;
            }
        }
    }


    private void startSearh(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<Product, productViewHolder>(Product.class,
                R.layout.recycler_product_item_row, productViewHolder.class, ref.orderByChild("Name").equalTo(text.toString())) {

            @Override
            protected void populateViewHolder(productViewHolder productViewHolder, Product product, int i) {
                productViewHolder.foodName.setText(product.getName());
                Picasso.with(getBaseContext()).load(product.getImage()).into(productViewHolder.foodImage);

                final Product productDesc=product;
                productViewHolder.setItemClickListener(new productViewHolder.ItemClickListener(){
                    @Override
                    public void onClick(View view, int pozition, boolean isLongClick) {
                        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                        intent.putExtra("productId", searchAdapter.getRef(pozition).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
    }

    private void loadSuggest() {
        ref.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot post:snapshot.getChildren()){
                    Product item=post.getValue(Product.class);
                    LastSearchList.add(item.getName());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void deleteProduct(String key){
        ref.child(key).removeValue();
        Toast.makeText(ProductListActivity.this,"Product deleted",Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo bilgi=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //TextView secilenoge=(TextView) bilgi.targetView;
        if(item.getItemId()==Delete)
        {
            deleteProduct(adapter.getRef(item.getOrder()).getKey());
            //durumTextview.setText(secilenoge.getText()+" için sil tıklandı");
            return true;

        }
        if(item.getItemId()==Update)
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
            //durumTextview.setText(secilenoge.getText()+" için düzenle tıklandı");
            return true;

        }
        return false;
        /*if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);*/
    }
    private void changeImage(final Product item) {
        if(newUri!=null){
            ProgressDialog dialog=new ProgressDialog(this);
            dialog.setMessage("Image uploading..");
            dialog.show();

            String imageName= UUID.randomUUID().toString();
            StorageReference imageFolder=storageReference.child("image/"+imageName);
            imageFolder.putFile(newUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(ProductListActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //newCategory=new Category(editName.getText().toString(),newUri.toString());
                                    item.setImage(uri.toString());
                                }
                            });

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(ProductListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded"+progress+"%");
                }
            });



        }
    }
    private void showUpdateDialog(final String key,final Product item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProductListActivity.this);
        alertDialog.setTitle("Edit Product");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_product_recycler_layout,null);
        name=add_menu_layout.findViewById(R.id.edtName);
        discount=add_menu_layout.findViewById(R.id.edtDiscount);
        price=add_menu_layout.findViewById(R.id.edtPrice);
        description=add_menu_layout.findViewById(R.id.edtDescription);

        name.setText(item.getName());
        discount.setText(item.getDiscount());
        price.setText(item.getPrice());
        description.setText(item.getDescription());
        select=add_menu_layout.findViewById(R.id.select);
        upload=add_menu_layout.findViewById(R.id.btnUpload);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        upload.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);

            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                item.setName(name.getText().toString());
                item.setDescription(description.getText().toString());
                item.setDiscount(discount.getText().toString());
                item.setPrice(price.getText().toString());
                ref.child(key).setValue(item);
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









































































































































