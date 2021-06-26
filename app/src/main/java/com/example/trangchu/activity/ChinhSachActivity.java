package com.example.trangchu.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trangchu.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class ChinhSachActivity  extends AppCompatActivity {
    private Toolbar chinhsach_toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinhsach);
        chinhsach_toolbar=findViewById(R.id.chinhsach_toolbar);

        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view1);
        expTv1.setText(getString(R.string.quyen1));

        ExpandableTextView expTv2 = (ExpandableTextView) findViewById(R.id.expand_text_view2);
        expTv2.setText(getString(R.string.quyen2));

        ExpandableTextView expTv3 = (ExpandableTextView) findViewById(R.id.expand_text_view3);
        expTv3.setText(getString(R.string.quyen3));

        ExpandableTextView expTv4 = (ExpandableTextView) findViewById(R.id.expand_text_view4);
        expTv4.setText(getString(R.string.quyen4));

        ExpandableTextView expTv5 = (ExpandableTextView) findViewById(R.id.expand_text_view5);
        expTv5.setText(getString(R.string.quyen5));

        ExpandableTextView expTv6 = (ExpandableTextView) findViewById(R.id.expand_text_view6);
        expTv6.setText(getString(R.string.quyen6));

        setChinhsach_Toolbar();
    }
    public void setChinhsach_Toolbar(){
        setSupportActionBar(chinhsach_toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
