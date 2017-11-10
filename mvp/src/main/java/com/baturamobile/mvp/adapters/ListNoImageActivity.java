package com.baturamobile.mvp.adapters;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.baturamobile.design.BaturaTextView;
import com.baturamobile.design.adapter.BaseAdapter;
import com.baturamobile.design.adapter.NoImageModel;
import com.baturamobile.mvp.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static com.baturamobile.mvp.adapters.ListSelectableFragment.NO_IMAGE_ADAPTER;


public class ListNoImageActivity<M extends NoImageModel> extends AppCompatActivity {

    public static final int selectCountryRequestCode = 123;


    BaturaTextView title;

    Toolbar toolbar;

    public static final String KEY_MODEL_LIST = "modelList";
    public static final String KEY_MODEL_SELECTED = "modelSelected";
    public static final String KEY_TITLE = "titleKey";

    ArrayList<M> modelList;
    M modelSelected;
    String titleText;


    public void onCreate(Bundle savedInstance ){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_select_list);

        toolbar = findViewById(R.id.tt_toolbar);
        title = findViewById(R.id.tt_title1);

        modelList = (ArrayList<M>) getIntent().getSerializableExtra(KEY_MODEL_LIST);
        modelSelected = (M) getIntent().getSerializableExtra(KEY_MODEL_SELECTED);
        titleText = getIntent().getStringExtra(KEY_TITLE);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAnalytics.getInstance(this).setCurrentScreen(this,titleText,"ListNoImageActivity");
    }

    public void setupViews(){

        ListSelectableFragment<? extends NoImageModel, ? extends BaseAdapter<? extends NoImageModel>> listSelectableFragment =
                ListSelectableFragment.instance(modelList,modelSelected,NO_IMAGE_ADAPTER);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,listSelectableFragment).commit();

        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setBackground(null);
        title.setTextColor(Color.BLACK);
        title.setText(titleText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
