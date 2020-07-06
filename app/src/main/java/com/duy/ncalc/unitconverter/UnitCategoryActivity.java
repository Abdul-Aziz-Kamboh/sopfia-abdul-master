/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.ncalc.unitconverter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.inventioncore.kabir.sopfia.R;
import com.duy.calculator.activities.base.NavDrawerActivity;
import com.duy.ncalc.unitconverter.adapters.CategoryAdapter;

import java.util.ArrayList;


public class UnitCategoryActivity extends NavDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected static final String TAG = UnitCategoryActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unit_converter_acitvity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Unit Converter");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.  string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.ic_temp);
        arrayList.add(R.drawable.ic_weight);
        arrayList.add(R.drawable.ic_length);
        arrayList.add(R.drawable.ic_power);
        arrayList.add(R.drawable.ic_power);
        arrayList.add(R.drawable.ic_speed);
        arrayList.add(R.drawable.ic_area);
        arrayList.add(R.drawable.ic_cubic);
        arrayList.add(R.drawable.ic_bitrate);
        arrayList.add(R.drawable.ic_time);

        RecyclerView recyclerView = findViewById(R.id.rcview);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        CategoryAdapter adapter = new CategoryAdapter(arrayList, UnitCategoryActivity.this);

        adapter.setListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, String text) {
                startActivity(pos, text);
            }

            @Override
            public void onItemLongClick() {

            }
        });
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
//            createInformationDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        super.onNavigationItemSelected(item);
        return true;
    }


    void startActivity(int position, String text) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.key_pos), position);
        bundle.putString(getString(R.string.key_name), text);
        Intent intent = new Intent(UnitCategoryActivity.this, ConverterActivity.class);
        intent.putExtra(getString(R.string.key_data), bundle);
        startActivity(intent);
    }


}