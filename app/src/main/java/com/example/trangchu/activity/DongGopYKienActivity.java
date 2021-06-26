package com.example.trangchu.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.trangchu.R;

public class DongGopYKienActivity extends AppCompatActivity {
    private Toolbar gopy_toolbar;
    private Button btn_gui;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donggopykien);
        gopy_toolbar=findViewById(R.id.gopy_toolbar);

        setGopy_Toolbar();

        btn_gui=findViewById(R.id.gopy_btn_gui);
        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DongGopYKienActivity.this,"Cảm ơn góp ý của bạn",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

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
    public void setGopy_Toolbar(){
        setSupportActionBar(gopy_toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
