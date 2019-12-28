package com.restaurant.fatih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.fatih.adapter.RestaurantAdapter;
import com.restaurant.fatih.data.Constans;
import com.restaurant.fatih.data.Session;
import com.restaurant.fatih.model.ListRestaurantResponse;

import static com.restaurant.fatih.data.Constans.GET_LIST_RESTAURANT;
import static com.restaurant.fatih.data.Constans.GET_SEARCH_RESTAURANT;
import static com.restaurant.fatih.utils.DialogUtils.progressDialog;

public class DashboardActivity extends AppCompatActivity {

    RestaurantAdapter adapter;
    RecyclerView rv;
    ProgressDialog progressDialog;
    Session session;
    SearchView svRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        initView();
        initRecyclerView();
        initSearch();
    }


    private void initSearch(){
        svRestaurant.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadItem(GET_SEARCH_RESTAURANT + query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                loadItem(GET_SEARCH_RESTAURANT + newText);
                return true;
            }
        });
        svRestaurant.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadItem(GET_LIST_RESTAURANT);
                return false;
            }
        });
    }

    private void initView(){
        svRestaurant = findViewById(R.id.sv_restaurant);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                session.logoutUser();
                break;
            case R.id.menu_account:
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                break;
            case R.id.menu_createRestaurant:
                startActivity(new Intent(DashboardActivity.this, CreateRestaurantActivity.class));
                break;
            case R.id.menu_deleteRestaurant:
                AndroidNetworking.post(Constans.DELETE_RESTAURANT+"/"+session.getUserId())
                        .build()
                        .getAsObject(ListRestaurantResponse.class, new ParsedRequestListener() {
                            @Override
                            public void onResponse(Object response) {
                                if (response instanceof ListRestaurantResponse) {
                                    //disable progress dialog
                                    progressDialog.dismiss();
                                    //null data check
                                    if (((ListRestaurantResponse) response).getData() !=
                                            null && ((ListRestaurantResponse) response).getData().size() > 0) {
                                        adapter.swap(((ListRestaurantResponse) response).getData());
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Toast.makeText(DashboardActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        });
        }
        return true;
    }

    public void initRecyclerView(){
        adapter = new RestaurantAdapter(this);
        loadItem(GET_LIST_RESTAURANT);
        rv = findViewById(R.id.rv_restaurant);
        rv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setNestedScrollingEnabled(false);
        rv.hasFixedSize();
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(DashboardActivity.this, "Clicked Item - " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadItem(String url){

        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        AndroidNetworking.get(url)
                .build()
                .getAsObject(ListRestaurantResponse.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if(response instanceof ListRestaurantResponse){
                            progressDialog.dismiss();
                            if (((ListRestaurantResponse) response).getData() !=
                                    null && ((ListRestaurantResponse) response).getData().size() > 0){
                                adapter.swap(((ListRestaurantResponse)
                                        response).getData());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}