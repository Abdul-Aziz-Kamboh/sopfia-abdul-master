package com.outstarttech.kabir.property.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.codextech.kabir.bills_app.utils.CustomAdapter;
import com.codextech.kabir.bills_app.utils.DatabaseHelper;
import com.codextech.kabir.bills_app.utils.WordList;
import com.inventioncore.kabir.sopfia.R;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    ArrayList<WordList> wordList;
    WordList words;
    private boolean isFirstRun = true;
    private SharedPreferences prefs;
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        //Add back button to toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
        getSupportActionBar().setTitle("Dictionary Word List");

        //Create AlertDialog only once
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = prefs.getBoolean("isFirstRun", true);
        if(isFirstRun){
            createInformationDialog();
        }
        isFirstRun = false;
        prefs.edit().putBoolean("isFirstRun", isFirstRun).commit();

        //Main Function to Add items to ListView
        populateListView();
    }

    //To refresh the ListDataActivity page when user backtracks to Activity
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_list_data);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        populateListView();
    }

    //Show information icon on top right of toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.information_topright,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Choose between back button and information button on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
//            createInformationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    //Fill the ListView with items
    private void populateListView() {
        wordList = new ArrayList<>();
        Cursor data = mDatabaseHelper.getData();
        int numRows = data.getCount();
        if (numRows == 0) {
            toastMessage("Nothing in database");
        } else {
            while (data.moveToNext()) { //Move to next Row
                //COLUMN 1 contains words, COLUMN 2 contains meanings, COLUMN 4 contains examples
                words = new WordList(data.getString(1), data.getString(2), data.getString(4));
                wordList.add(words);
            }
            CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_list_adapter, wordList);
            mListView.setAdapter(adapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String meaning = ((TextView) view.findViewById(R.id.meaningId)).getText().toString();

                //Single Quotes Error Fix ( SQL Injection )
                if(meaning.contains("'")){
                    meaning = meaning.replaceAll("'", "''");
                }

                //Log.d(TAG, "onItemClick: You clicked" + meaning); //Debug

                Cursor data = mDatabaseHelper.getItemId(meaning);
                int itemId = -1;
                while(data.moveToNext()){
                    itemId = data.getInt(0);
                }
                if(itemId > -1){
                    //Log.d(TAG, "onItemClick: ID is : " + itemId);//Debug
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("meaning", meaning); //To send meaning of row to EditDataActivity
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with " + meaning);
                }
            }
        });
    }

    //App Info Dialogue
    private void createInformationDialog(){
        ((TextView) new AlertDialog.Builder(this)
                .setTitle("Info")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })                .setIcon(android.R.drawable.ic_menu_info_details)
                .setMessage(Html.fromHtml("" +
                        "<p>Scroll through your list to see any word you have added to your dictionary</p>" +
                        "<p>You can click on any word to have the option to delete it and see its example with the related meaning.</p><br>" +
                        "<br><br><br><br>" +
                        ""))
                .show()
                // Need to be called after show(), in order to generate hyperlinks
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Customizable toast
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
