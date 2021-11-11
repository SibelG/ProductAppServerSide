package com.example.productappserverside.view;


import static com.example.productappserverside.Common.Common.pickImage;
import static com.example.productappserverside.adapters.MenuViewHolder.Delete;
import static com.example.productappserverside.adapters.MenuViewHolder.Update;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productappserverside.R;
import com.example.productappserverside.Service.ListenOrder;
import com.example.productappserverside.adapters.MenuViewHolder;
import com.example.productappserverside.adapters.menuAdapter;
import com.example.productappserverside.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private menuAdapter menuAdapter;
    private RecyclerView.LayoutManager manager;
    TextView textView;
    ImageView imageView;
    RecyclerView recycleView;
    Category newCategory;
    public FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    FloatingActionButton button;
    MaterialEditText editName;
    Button select,upload;
    Uri newUri;
    DrawerLayout drawer;
    ArrayList<Category>categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        categoryList=new ArrayList<>();

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        drawer=findViewById(R.id.DrawerLayout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView view=findViewById(R.id.navView);
        view.setNavigationItemSelectedListener( this);
        View headerView=view.getHeaderView(0);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        textView=headerView.findViewById(R.id.userName);
        imageView=findViewById(R.id.imageView);
        upload=findViewById(R.id.btnUpload);
        recycleView=(RecyclerView) findViewById(R.id.recycle_menu_row);
        recycleView.setHasFixedSize(true);
        manager=new LinearLayoutManager(HomeActivity.this);
        recycleView.setLayoutManager(manager);


        loadMenu();

        Intent service=new Intent(HomeActivity.this, ListenOrder.class);
        startService(service);



        button=findViewById(R.id.fabButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showUpdateDialog();
                //Intent intent=new Intent(HomeActivity.this,OrderCartActivity.class);
                //startActivity(intent);
            }
        });

    }
    private void loadMenu(){


        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.recycler_menu_item,MenuViewHolder.class,reference) {

            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category model, int pozition) {
                menuViewHolder.textCategoryName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImageUrl()).into(menuViewHolder.imageView);
                final Category clickItem=model;

                menuViewHolder.setItemClicklistener(new MenuViewHolder.ItemClickListener() {
                    @Override
                    public void onClick(View view, int pozition, boolean isLongClick) {
                        Intent intent=new Intent(HomeActivity.this, ProductListActivity.class);
                        intent.putExtra("categoryId",adapter.getRef(pozition).getKey());
                        startActivity(intent);
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recycleView.setAdapter(adapter);

    }
    private void changeImage(final Category item) {
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
                            Toast.makeText(HomeActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //newCategory=new Category(editName.getText().toString(),newUri.toString());
                                    item.setImageUrl(uri.toString());
                                }
                            });

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(HomeActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newCategory=new Category(editName.getText().toString(),newUri.toString());
                                    //item.setImageUrl(uri.toString());
                                }
                            });

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void showUpdateDialog(final String key,final Category item) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);
        editName=add_menu_layout.findViewById(R.id.edtName);
        editName.setText(item.getName());
        select=add_menu_layout.findViewById(R.id.btnSelect);
        upload=add_menu_layout.findViewById(R.id.btnUpload);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
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
                item.setName(editName.getText().toString());
                reference.child(key).setValue(item);
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


    }
    private void showUpdateDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Add Category");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater=getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_menu_layout,null);
        editName=add_menu_layout.findViewById(R.id.edtName);
        //editName.setText(item.getName());
        select=add_menu_layout.findViewById(R.id.btnSignIn);
        upload=add_menu_layout.findViewById(R.id.btnUpload);
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

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                /*item.setName(editName.getText().toString());
                reference.child(key).setValue(item);*/
                if(newCategory!=null){
                    reference.push().setValue(newCategory);
                    Snackbar.make(drawer,"New Category"+newCategory.getName()+" was added",Snackbar.LENGTH_LONG).show();
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

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.

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


    public void onBackPressed(){
        DrawerLayout layout=findViewById(R.id.DrawerLayout);
        if(layout.isDrawerOpen(GravityCompat.START)){
            layout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        /*if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);*/
        AdapterView.AdapterContextMenuInfo bilgi=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //TextView secilenoge=(TextView) bilgi.targetView;
        if(item.getItemId()==Delete)
        {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
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
    }
    private void deleteCategory(String key){
        DatabaseReference products=database.getReference("Product");
        Query query=products.orderByChild("categoryId").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post:dataSnapshot.getChildren()){
                    post.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reference.child(key).removeValue();
        Toast.makeText(HomeActivity.this,"Category deleted",Toast.LENGTH_LONG).show();
    }



   @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       int id = item.getItemId();

       if (id == R.id.nav_menu) {
           // Handle the camera action
       } else if (id == R.id.nav_cart) {
           /*Intent cartIntent = new Intent(HomeActivity.this, Cart.class);
           startActivity(cartIntent);*/

       } else if (id == R.id.nav_orders) {
           Intent orderIntent = new Intent(HomeActivity.this, OrderStatusActivity.class);
           startActivity(orderIntent);

       } else if (id == R.id.nav_log_out) {
           //Logout
           Intent signIn = new Intent(HomeActivity.this, SignInActivity.class);
           signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(signIn);
       }

       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
       drawer.closeDrawer(GravityCompat.START);
       return true;
    }
    public boolean onCreateoptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.refresh){
            loadMenu();
        }
        return super.onOptionsItemSelected(item);
    }
}