package com.restaurant.fatih;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.fatih.adapter.RestaurantAdapter;
import com.restaurant.fatih.auth.ChangePasswordActivity;
import com.restaurant.fatih.data.Constans;
import com.restaurant.fatih.data.Session;
import com.restaurant.fatih.model.ListRestaurantResponse;
import com.restaurant.fatih.model.RegisterResponse;
import com.restaurant.fatih.model.RestaurantItem;
import com.restaurant.fatih.utils.DialogUtils;

public class CreateRestaurantActivity extends AppCompatActivity {

    Session session;
    EditText namaRM;
    EditText kategoriRM;
    EditText alamatRM;
    EditText linkFoto;
    Button addRestaurant;
    ProgressDialog progressDialog;
    String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        userId = getIntent().getStringExtra("userId");
        initBinding();
        initClick();
    }
    private void initBinding() {
        namaRM = findViewById(R.id.nama_rm);
        kategoriRM = findViewById(R.id.kategori_rm);
        alamatRM = findViewById(R.id.alamat_rm);
        linkFoto = findViewById(R.id.link_foto);
        addRestaurant = findViewById(R.id.btn_add_restaurant);
    }
    private void initClick() {
        addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alamatRM.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Alamat Rumah Makan harus diisi", Toast.LENGTH_SHORT).show();
                }else if(namaRM.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Nama Rumah Makan harus diisi", Toast.LENGTH_SHORT).show();
                }else if(kategoriRM.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Kategori harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    createRestaurant();
                }
            }
        });
    }
    public void createRestaurant() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.CREATE_RESTAURANT)
                .addQueryParameter("userid",session.getUserId())
                .addBodyParameter("userid",session.getUserId())
                .addBodyParameter("namarm",namaRM.getText().toString())
                .addBodyParameter("kategori",kategoriRM.getText().toString())
                .addBodyParameter("foto",linkFoto.getText().toString())
                .addBodyParameter("alamat",alamatRM.getText().toString())
                .build()
                .getAsObject(ListRestaurantResponse.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RegisterResponse) {
                            RegisterResponse res = (RegisterResponse) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(CreateRestaurantActivity.this,"Berhasil Menambahkan Restaurant", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateRestaurantActivity.this,"Gagal Menambahkan Restaurant", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateRestaurantActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
