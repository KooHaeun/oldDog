package com.example.olddog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class join extends AppCompatActivity {

    oldDogDB dogDB;
    SQLiteDatabase db;
    EditText id_in, eat_in, drink_in, poop_in, pee_in, name_in, count_in;
    Button join, check, medi;
    RadioGroup medi_sort;
    RadioButton e_medi, c_medi;
    String id, eat, drink, poop, pee, name, count;
    View medi_plus;
    Boolean push=false;
    int pill=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        //가입화면
        id_in = findViewById(R.id.id_in);
        eat_in = findViewById(R.id.eat_in);
        drink_in = findViewById(R.id.drink_in);
        poop_in = findViewById(R.id.poop_in);
        pee_in = findViewById(R.id.pee_in);
        join = findViewById(R.id.join);
        check = findViewById(R.id.check);
        medi = findViewById(R.id.medicine);

        dogDB = new oldDogDB(join.this);
        db = dogDB.getWritableDatabase();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = id_in.getText().toString();
                int mId = Integer.parseInt(id);
                Cursor cu;
                cu = db.rawQuery("SELECT * FROM DOG WHERE dNum =='"+mId+"';", null);
                if (id.equals("")) {//등록번호를 아직 입력안했을 시
                    Toast.makeText(join.this, "등록번호를 입력해주세요", Toast.LENGTH_LONG).show();
                } else {
                    if (cu.getCount() == 0) {//등록번호가 중복이 아닐 시
                        AlertDialog.Builder dlg = new AlertDialog.Builder(join.this);
                        dlg.setTitle("중복확인");
                        dlg.setMessage("사용 가능한 등록번호입니다.");
                        dlg.setIcon(R.drawable.logo);
                        dlg.setNegativeButton("확인", null);
                        dlg.show();
                        push=true;
                    }
                       else{//등록번호가 중복일 때
                            AlertDialog.Builder dlg = new AlertDialog.Builder(join.this);
                            dlg.setTitle("중복확인");
                            dlg.setMessage("중복입니다. 새로운 등록번호를 입력해주세요");
                            dlg.setIcon(R.drawable.logo);
                            dlg.setPositiveButton("확인", null);
                            dlg.show();
                             }
                        }

                cu.close();
                }
             });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eat = eat_in.getText().toString();
                drink = drink_in.getText().toString();
                poop = poop_in.getText().toString();
                pee = pee_in.getText().toString();
                db = dogDB.getWritableDatabase();

                Cursor c;
                c = db.rawQuery("SELECT * FROM DOG;", null);
                if(push==false){//등록번호 중복체크 안했을 시
                    AlertDialog.Builder dlg = new AlertDialog.Builder(join.this);
                    dlg.setTitle("알림");
                    dlg.setMessage("등록번호 중복체크를 해주세요");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                }
                else if(eat.equals("")||drink.equals("")||poop.equals("")||pee.equals("")){//입력안한 부분 있을 시
                    Toast.makeText(join.this, "모든 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                }
                else{
                    if(pill==1) {//먹는 약 추가 했을 시
                        db.execSQL("INSERT INTO DOG(dNum, eat, drink, poop, pee, E_medi) VALUES('" + Integer.parseInt(id) + "', '" + Integer.parseInt(eat) + "', '" + Integer.parseInt(drink) + "', '" + Integer.parseInt(poop) + "', '" + Integer.parseInt(pee) + "','" + Integer.parseInt(count) + "');");
                        db.execSQL("INSERT INTO MEDI(dNum, E_medi)VALUES('" + Integer.parseInt(id) + "', '" + name + "');");
                    }else if(pill==2){//바르는 약 추가 했을 시
                        db.execSQL("INSERT INTO DOG(dNum, eat, drink, poop, pee, C_medi) VALUES('" + Integer.parseInt(id) + "', '" + Integer.parseInt(eat) + "', '" + Integer.parseInt(drink) + "', '" + Integer.parseInt(poop) + "', '" + Integer.parseInt(pee) + "','" + Integer.parseInt(count) + "');");
                        db.execSQL("INSERT INTO MEDI(dNum, C_medi)VALUES('" + Integer.parseInt(id) + "','" + name + "');");
                    }
                    else{
                        db.execSQL("INSERT INTO DOG(dNum, eat, drink, poop, pee) VALUES('" + Integer.parseInt(id) + "', '" + Integer.parseInt(eat) + "', '" + Integer.parseInt(drink) + "', '" + Integer.parseInt(poop) + "', '" + Integer.parseInt(pee) + "');");
                    }
                    db.execSQL("INSERT INTO DAY(dNum) values ('"+Integer.parseInt(id)+"');");
                    c.close();
                    db.close();


                    Toast.makeText(getApplicationContext(),"등록되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(join.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }

        });//회원가입 완료 후 메인화면으로 넘어감

        //약 추가 버튼 눌렀을 시
        medi.setOnClickListener(new View.OnClickListener() {//약 추가 다이얼로그
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(join.this);
                dlg.setTitle("약 추가");
                dlg.setIcon(R.drawable.logo);
                medi_plus = View.inflate(getApplication(), R.layout.medi_plus_dialog, null);
                dlg.setView(medi_plus);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        medi_sort = medi_plus.findViewById(R.id.medi_sort);
                        e_medi = medi_plus.findViewById(R.id.eat_medi);
                        c_medi = medi_plus.findViewById(R.id.cover_medi);
                        count_in = medi_plus.findViewById(R.id.count_in);
                        name_in = medi_plus.findViewById(R.id.name_in);
                        name = name_in.getText().toString();
                        count = count_in.getText().toString();
                        if(e_medi.isChecked())
                            pill=1;
                        else if(c_medi.isChecked())
                            pill=2;
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });//약 추가


    }
}
