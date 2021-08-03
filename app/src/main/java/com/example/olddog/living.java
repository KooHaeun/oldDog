package com.example.olddog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class living extends AppCompatActivity {

    oldDogDB dogDB;
    SQLiteDatabase db;
    ImageButton logo, eatp, peo, medip;
    TextView eatM, drinkM, peeM, pooM, E_mediM, C_mediM, num;
    View eat, poo, medi;
    Button plus, minus, more, recheck;
    RadioGroup eat_drink, eat_cover, pee_poop;
    RadioButton food,drink,pee,poop, eat_medi, cover_medi;
    Integer many, Teat, Tdrink, Tpoo, Tpee, Te_medi, Tc_medi, Neat, Ndrink, Npee, Npoo, Ne_medi, Nc_medi;
    int myId;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.living);

        setFirst();
        logo = findViewById(R.id.logo);
        goMain(logo);

        eatp = findViewById(R.id.eatP);
        peo = findViewById(R.id.pooP);
        medip = findViewById(R.id.mediP);
        eatM = findViewById(R.id.eatM);
        drinkM = findViewById(R.id.drinkM);
        peeM = findViewById(R.id.peeM);
        pooM=findViewById(R.id.pooM);
        E_mediM = findViewById(R.id.E_mediM);
        C_mediM = findViewById(R.id.C_mediM);

        final Cursor cu = db.rawQuery("SELECT * FROM DOG WHERE dNum=='"+myId+"';", null);

        while(cu.moveToNext()){//하루 식사량, 배변량, 약 값 받아오기
            Teat = cu.getInt(1);
            Tdrink = cu.getInt(2);
            Tpoo = cu.getInt(3);
            Tpee = cu.getInt(4);
            Te_medi = cu.getInt(5);
            Tc_medi = cu.getInt(6);
        }
        cu.close();
        setData();//오늘의 식사량, 배변량, 약 값 화면에 출력

        //식사 + 버튼 클릭시
        eatp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(living.this);
                dlg.setTitle("식사량");
                dlg.setIcon(R.drawable.logo);
                eat = View.inflate(getApplication(), R.layout.eat_dialog, null);
                eat_drink = eat.findViewById(R.id.eat_drink);
                food = eat.findViewById(R.id.food);
                drink = eat.findViewById(R.id.drink);
                minus = eat.findViewById(R.id.minus);
                plus = eat.findViewById(R.id.plus);
                num = eat.findViewById(R.id.num);
                many=Integer.parseInt(num.getText().toString());
                dlg.setView(eat);

                //식사 다이얼로그에서 양 증가 감소
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num.setText((many+=10).toString());
                    }
                });
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        num.setText((many-=10).toString());
                    }
                });

                //식사 추가 버튼 클릭시
                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor c = db.rawQuery("SELECT * FROM DAY WHERE dNum=="+myId+";",null);
                        while(c.moveToNext()){
                            if(food.isChecked()){//식사량 추가
                                int a = c.getInt(2);
                                db.execSQL("UPDATE DAY SET eat = "+(a+many)+" WHERE eat == "+a+";");

                            }
                        else if(drink.isChecked()){//물 양 추가
                                int a = c.getInt(3);
                                db.execSQL("UPDATE DAY SET drink = "+(a+many)+" WHERE drink == "+a+";");
                            }
                        }
                        c.close();
                        setData();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        //배변 + 버튼 클릭시
         peo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(living.this);
                dlg.setTitle("배변량");
                dlg.setIcon(R.drawable.logo);
                poo = View.inflate(getApplication(), R.layout.poo_dialog, null);
                pee_poop = poo.findViewById(R.id.pee_poop);
                poop = poo.findViewById(R.id.poop);
                pee = poo.findViewById(R.id.pee);
                dlg.setView(poo);

                //다이얼로그의 추가 버튼 클릭시
               dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                   Cursor c = db.rawQuery("SELECT * FROM DAY WHERE dNum=="+myId+";",null);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        while(c.moveToNext()){
                            if(pee.isChecked()){//대변횟수 추가
                                int a = c.getInt(5);
                                db.execSQL("UPDATE DAY SET pee = "+(a+1)+" WHERE pee == "+a+";");

                            }
                            else if(poop.isChecked()){//소변횟수 추가
                                int a = c.getInt(4);
                                db.execSQL("UPDATE DAY SET poop = "+(a+1)+" WHERE poop == "+a+";");
                            }
                        }
                            setData();
                            c.close();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

         //약 + 버튼 클릭시
        medip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(living.this);
                dlg.setTitle("약");
                dlg.setIcon(R.drawable.logo);
                medi = View.inflate(getApplication(), R.layout.medi_dialog, null);
                eat_cover = medi.findViewById(R.id.eat_cover);
                eat_medi = medi.findViewById(R.id.eat_medi);
                cover_medi = medi.findViewById(R.id.cover_medi);
                more = medi.findViewById(R.id.more);
                dlg.setView(medi);

                //상세 보기 버튼 클릭시
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//약 상세 정보 화면으로 넘어감
                        Intent i = new Intent(living.this, medi.class);
                        i.putExtra("myId", myId);
                        i.putExtra("E_cnt", Te_medi);
                        i.putExtra("C_cnt", Tc_medi);
                        startActivity(i);
                    }
                });

                //다이얼로그의 추가 버튼 클릭시
                dlg.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    Cursor c = db.rawQuery("SELECT * FROM DAY WHERE dNum=="+myId+";",null);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        while(c.moveToNext()){
                            if(eat_medi.isChecked()){//먹는 약 횟수 추가
                                int a = c.getInt(6);
                                db.execSQL("UPDATE DAY SET E_medi="+(a+1)+" WHERE E_medi="+a+";");
                            }
                            else if(cover_medi.isChecked()){//바르는 약 횟수 추가
                                int a = c.getInt(7);
                                db.execSQL("UPDATE DAY SET C_medi ="+(a+1)+" WHERE C_medi ="+a+";");}
                        }
                            setData();
                            c.close();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });


    }

    public void setFirst(){//툴바, 전달받은 값, 데이터베이스 셋팅
        Toolbar bar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(bar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        Intent ii = getIntent();
        myId = ii.getIntExtra("myId", 1010);

        dogDB = new oldDogDB(living.this);
        db = dogDB.getWritableDatabase();
    }

    public void goMain(ImageView logo){//로고버튼 클릭시
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(living.this, first.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void setData(){//화면에 오늘의 식사량, 배변량, 약 출력

        Cursor c = db.rawQuery("SELECT * FROM DAY WHERE dNum=='"+myId+"';",null);

        while(c.moveToNext()){
            Neat = c.getInt(2);
            Ndrink = c.getInt(3);
            Npoo = c.getInt(4);
            Npee = c.getInt(5);
            Ne_medi = c.getInt(6);
            Nc_medi = c.getInt(7);
        }
        eatM.setText("밥: "+Neat+"/"+Teat);
        drinkM.setText("물: "+Ndrink+"/"+Tdrink);
        pooM.setText("대변: "+Npoo+"/"+Tpoo);
        peeM.setText("소변: "+Npee+"/"+Tpee);
        E_mediM.setText("먹는 약: "+Ne_medi+"/"+Te_medi);
        C_mediM.setText("바르는 약: "+Nc_medi+"/"+Tc_medi);
        c.close();
    }

    public boolean onCreateOptionsMenu(Menu m){//옵션메뉴 생성
        super.onCreateOptionsMenu(m);
        MenuInflater min = getMenuInflater();
        min.inflate(R.menu.menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//옵션메뉴 아이템 생성시
        switch (item.getItemId()){
            case R.id.schedule:
                Intent i1 = new Intent(living.this, schedule.class);
                i1.putExtra("myId", myId);
                startActivity(i1);
                finish();
                break;
            case R.id.living:
                Intent i2 = new Intent(living.this, living.class);
                i2.putExtra("myId", myId);
                startActivity(i2);
                finish();
                break;
            case R.id.hospital:
                Intent i3 = new Intent(living.this, hospital.class);
                i3.putExtra("myId", myId);
                startActivity(i3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
