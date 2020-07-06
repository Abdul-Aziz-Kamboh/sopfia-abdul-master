package com.outstarttech.kabir.property.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.codextech.kabir.bills_app.SessionManager;
import com.codextech.kabir.bills_app.utils.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;
import com.inventioncore.kabir.sopfia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dictionaryclass extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;

    private SessionManager sessionManager;

    private NavigationView navigationView1;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionaryclass);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        navigationView1 = (NavigationView) findViewById(R.id.drawerDictionary);
        navigationView1.setNavigationItemSelectedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutDictionary);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_format_align_center_black_24dp);


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
        getSupportActionBar().setTitle("");
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        getSupportActionBar().setTitle("Dictionary");



        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        sessionManager = new SessionManager(this);


        View headerView = navigationView1.getHeaderView(0);
        TextView text = (TextView) headerView.findViewById(R.id.navName);
        TextView text2 = (TextView) headerView.findViewById(R.id.navEmail);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.navImage);
        if(sessionManager.getKeyLoginId().length() > 1)
        {
            text.setText(sessionManager.getLoginUsername());
            text2.setText(sessionManager.getKeyLoginEmail());
        }
        else {
            text.setText("Guest");
            text2.setText("SignIn For Complete Features");
        }
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Dictionaryclass.this, LogInActivity.class));
            }
        });
        //Set toolbar details
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setIcon(R.drawable.coinethmny);
//        actionBar.setTitle( "Open Dictionary");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wordPresent = false;
                String newEntry = editText.getText().toString();
                wordPresent = mDatabaseHelper.checkIfPresent(newEntry);
                if(editText.length()!= 0 && wordPresent == false){
                    queryData(newEntry);
                    editText.setText("");
                }else if( wordPresent == true){
                    toastMessage("Word is already present in Dictionary");
                } else{
                    toastMessage("You must enter something in the text field");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dictionaryclass.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }

    public void queryData(final String data){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://owlbot.info/api/v2/dictionary/" + data +"?format=json";

        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            toastMessage("Enter a valid word");
                        }

                        for( int i=0 ; i<response.length();i++){
                            String meaningOfWord = null;
                            String exampleOfWord = null;
                            String typeOfWord = null;
                            try {
                                JSONObject currentJsonObj = response.getJSONObject(i);
                                meaningOfWord = currentJsonObj.getString("definition"); //Get meaning from REST response
                                exampleOfWord = currentJsonObj.getString("example");    //Get example from REST response
                                typeOfWord = currentJsonObj.getString("type");

                                //Log.e("Checking meaning",meaningOfWord); //Debug
                                //Log.e("Rest Response:", response.toString());//Debug

                                //Database add function call
                                addRow(data, meaningOfWord, exampleOfWord, typeOfWord);
                                Toast.makeText(Dictionaryclass.this, "Word Added, Check in Word List", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toastMessage("Please enter a valid word");
                    }
                }
        );
        requestQueue.add(jsonarrayRequest);
    }

    //Add Data to Database
    public void addRow(String word, String meaning, String example, String type){
        boolean success = mDatabaseHelper.addRow(word, meaning, example, type);

        /*//Condition to check if row was successfully added to database
        if(success) {
            //Log.e("ROW ADDED", "SUCCESSFULLY"); //Debug
        }else {
            //Log.e("ROW ADDED", "FAILED"); //Debug
        }*/
    }

    //Create Menu items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.information_topright,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Select Menu Items on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
//        createInformationDialog();
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Create App Info Dialogue
    private void createInformationDialog(){
        ((TextView) new AlertDialog.Builder(this)
                .setTitle("Info")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(Html.fromHtml("This application is mainly designed to be an on-the-go dictionary with simplistic design and an open source mindset with contribution and documentation made by the public." +
                        "<p>Just type in the required word in the provided box and click add word. This will add the word in your Simple Dictionary</p> " +
                        "<p>Click view list to see the meanings of all the words you have saved in your own dictionary. </p><br>" +
                        "<br><br><br><br>" +
                        "<p>Application repo at: <a href=\"http://www.github.com/shubhankar30/SimpleDictionary\">Github Repository link</a><br>" +
                        ""))
                .show()
                // Need to be called after show(), in order to generate hyperlinks
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Close database connection
    @Override
    public void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }

    //customizable toast message
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.


        switch (item.getItemId()) {

            case R.id.home: {
                finish();
                startActivity(new Intent(Dictionaryclass.this, Main2Activity.class));
                //do somthing
                break;
            }
            case R.id.aboutus:
            {
                Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_SHORT).show();
                //do somthing
                break;
            }
            case R.id.roadmap:
            {
                Toast.makeText(getApplicationContext(), "Road Map", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.help:
            {
                finish();
                startActivity(new Intent(Dictionaryclass.this, UpdateDetails.class));
                //do somthing
                break;
            }
            case R.id.nav_logout:
            {
                finish();
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.logoutUser();
                startActivity(new Intent(Dictionaryclass.this, LogInActivity.class));
                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();

                break;
            }

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

