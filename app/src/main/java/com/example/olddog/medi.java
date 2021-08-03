package com.example.olddog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class medi extends AppCompatActivity {

    oldDogDB dogDB;
    SQLiteDatabase db;
    ImageButton logo;
    Integer myId, E_cnt, C_cnt;
    TextView eat_medi, cover_medi;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_medi);

        setFirst();

        logo = findViewById(R.id.logo);
        eat_medi = findViewById(R.id.eat_medi);
        cover_medi = findViewById(R.id.cover_medi);

        goMain(logo);

        final Cursor c = db.rawQuery("SELECT * FROM MEDI WHERE dNum=='"+myId+"';",null);

        //약 정보 받아와서 화면에 출력
        while(c.moveToNext()){
            if (c.getString(1) == null && c.getString(2) == null) {
                eat_medi.setText("없음");
                cover_medi.setText("없음");
            } else if (c.getString(1) != null && c.getString(2) == null) {
                eat_medi.setText(c.getString(1) + "(일: " + E_cnt + "회)");
                cover_medi.setText("없음");
            } else if (c.getString(1) == null && c.getString(2) != null) {
                eat_medi.setText("없음");
                cover_medi.setText(c.getString(2) + "(일: " + C_cnt + "회)");
            } else {
                eat_medi.setText(c.getString(1) + "(일: " + E_cnt + "회)");
                cover_medi.setText(c.getString(2) + "(일: " + C_cnt + "회)");
            }
        }
        c.close();


    }
    public void setFirst(){//툴바, 전달받은 값, 데이터베이스 셋팅
        Intent i = getIntent();
        myId = i.getIntExtra("myId", 1010);
        E_cnt =i.getIntExtra("E_cnt", 0);
        C_cnt = i.getIntExtra("C_cnt", 0);

        Toolbar bar = (Toolbar)findViewById(R.id.action_bar);
        setSupportActionBar(bar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dogDB = new oldDogDB(medi.this);
        db = dogDB.getWritableDatabase();
    }

    public void goMain(ImageView logo){//로고버튼 클릭시
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(medi.this, first.class);
                startActivity(i);
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {//toolbar의 back키 눌렀을 시
        switch (item.getItemId()){
            case android.R.id.home:{//이전 화면으로 돌아감
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
