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

package com.duy.ncalc.geom2d;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import 	androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.inventioncore.kabir.sopfia.R;
import com.duy.calculator.activities.base.NavDrawerActivity;

public class GeometryDescartesActivity extends NavDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int mCount = 3;
    private ViewPager mPager;
    private TabLayout mTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geometry_descartes);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


       // getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("2D Geomatery");

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
      //  navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(mCount);
        PagerApdater mAdapter = new PagerApdater(getSupportFragmentManager(), this);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(this);

        mTab = (TabLayout) findViewById(R.id.tab);
        mTab.setupWithViewPager(mPager, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        super.onNavigationItemSelected(item);
        return true;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    static class PagerApdater extends FragmentPagerAdapter {
        private final Context context;

        public PagerApdater(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new com.duy.ncalc.geom2d.FragmentVector();
                case 1:
                    return new FragmentLine();
                case 3:
                    return new FragmentPolygon();
                case 2:
                    return new FragmentCircle();
                case 4:
                    return new FragmentEllipse();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Resources resources = context.getResources();
            switch (position) {

                case 0:
                    return resources.getString(R.string.vector);
                case 1:
                    return resources.getString(R.string.line);
                case 2:
                    return resources.getString(R.string.circle);
                case 3:
                    return resources.getString(R.string.polygon);
                case 4:
                    return resources.getString(R.string.ellipse);
                default:
                    return null;
            }

        }

    }

}
