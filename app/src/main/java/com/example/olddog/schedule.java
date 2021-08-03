package com.example.olddog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class schedule extends AppCompatActivity {
    oldDogDB dogDB;
    SQLiteDatabase db;
    int y, mon, day;
    Button cal;
    Spinner spin1, spin2, spin_sort;
    EditText what_in;
    String where, what, sort, when;
    int num, myId;
    ImageButton logo;
    Button plus_sche;
    View sche_plus;
    ListView sche;
    ArrayAdapter<String> Myadapter, hosAdapter;
    ArrayList<String> MyList;
    Boolean check=false;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        setFirst();//초기 셋팅

        logo = findViewById(R.id.logo);
        plus_sche = findViewById(R.id.plus_sche);
        spin_sort=findViewById(R.id.spin_sort);
        sche = findViewById(R.id.sche);
        MyList = new ArrayList<String>();

        goMain(logo);//로고 클릭시

        dogDB = new oldDogDB(schedule.this);
        db = dogDB.getWritableDatabase();

        //일정 화면 리스트 뷰 셋팅
        Myadapter = new ArrayAdapter<String>(schedule.this, android.R.layout.simple_list_item_1, MyList);
        sche.setAdapter(Myadapter);
        Cursor s = db.rawQuery("SELECT * FROM SCHE where dNum =='"+myId+"'", null);
        setList(s);


        //동물병원 스피너에 담을 어댑터 생성&초기화
        hosAdapter = new ArrayAdapter<>(schedule.this, android.R.layout.simple_spinner_item);
        Cursor c = db.rawQuery("SELECT * FROM HOS", null);
        num = c.getCount();
        hosAdapter.add("전체");
        for (int i = 0; i < num; i++) {
            while (c.moveToNext()) {
                hosAdapter.add(c.getString(1));
            }
        }//디비에 있는 동물병원 이름 배열에 저장
        c.close();

        //화면의 스피너에서 값 선택시
        spin_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String mySort = String.valueOf(parent.getItemAtPosition(position));
                        if(mySort.equals("전체")){
                            final Cursor s = db.rawQuery("SELECT * FROM SCHE where dNum =='"+myId+"';", null);
                            setList(s);
                        }else{
                            Cursor s = db.rawQuery("SELECT * FROM SCHE where dNum =='"+myId+"' AND sort=='"+mySort+"'", null);
                            setList(s);}
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //리스트뷰의 항목 선택시
        sche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String deDate = String.valueOf(MyList.get(position)).substring(0,10);
                AlertDialog.Builder dlg = new AlertDialog.Builder(schedule.this);
                dlg.setTitle("일정 삭제");
                dlg.setMessage("삭제하시겠습니까?");
                dlg.setIcon(R.drawable.logo);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {//선택 항목을 리스트뷰와 데이터베이스에서 삭제
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyList.remove(position);
                        db.execSQL("DELETE FROM SCHE WHERE dNum=='"+myId+"' AND date =='"+deDate+"'; ");
                        Cursor s = db.rawQuery("SELECT * FROM SCHE WHERE dNum=='"+myId+"';", null);
                        setList(s);
                    }
                });//데이터베이스에서 삭제
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        //일정 추가 버튼 눌렀을 시
        plus_sche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder d = new AlertDialog.Builder(schedule.this);
                d.setTitle("일정 등록");
                d.setIcon(R.drawable.logo);

                sche_plus = View.inflate(getApplication(), R.layout.sche_dialog, null);
                cal = sche_plus.findViewById(R.id.cal);
                spin1 = sche_plus.findViewById(R.id.spin1);
                spin2 = sche_plus.findViewById(R.id.spin2);
                what_in = sche_plus.findViewById(R.id.what_in);


                spin2.setAdapter(hosAdapter);

                //날짜 추가 버튼 클릭시
                cal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePick(v);
                    }
                });

                //일정 종류 스피너 선택시
                spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sort = String.valueOf(parent.getItemAtPosition(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //동물병원 스피너 선택시
                spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       where = String.valueOf(spin2.getItemAtPosition(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                //다이얼로그의 등록 버튼 클릭시
                d.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Cursor cursor = db.rawQuery("SELECT * FROM SCHE WHERE dNum=='"+myId+"' AND date=='"+when+"' ;",null);
                        what = what_in.getText().toString();
                        if(check==false){//날짜 선택 안됐을 시
                            Toast.makeText(getApplicationContext(),"날짜를 선택해주세요",Toast.LENGTH_SHORT).show();
                        }
                        else if(cursor.getCount()!=0){//일정 있는 날 선택 시
                            Toast.makeText(getApplicationContext(),"일정 있는 날은 선택이 불가합니다.",Toast.LENGTH_SHORT).show();
                        }
                        else if(what.equals("")){//일정 이름 입력 안했을 시
                            Toast.makeText(getApplicationContext(),"일정 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                        } else{//데이터베이스에 일정 저장
                            db.execSQL("INSERT INTO SCHE VALUES('" + myId + "','" + when + "','" + what + "','" + where + "','" + sort + "')");
                            Toast.makeText(getApplicationContext(), "등록완료", Toast.LENGTH_SHORT).show();
                            Cursor s= db.rawQuery("SELECT * FROM SCHE where dNum =='"+myId+"';", null);
                            setList(s);}
                        cursor.close();
                    }
                });
                d.setNegativeButton("취소", null);
                d.setView(sche_plus);
                d.show();

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

        dogDB = new oldDogDB(schedule.this);
        db = dogDB.getWritableDatabase();
    }

    public void goMain(ImageView logo){//로고버튼 클릭시
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(schedule.this, first.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void datePick (View v){//일정추가 다이얼로그에서 날짜선택 버튼 클릭시 DatePicker 다이얼로그 생성&데이터 처리
        DatePickerDialog d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                mon = month + 1;
                day = dayOfMonth;
                if(mon<10&&day<10)//달이 10보다 작고 일이 10보다 작을때
                    when = Integer.toString(y) + "-0" + Integer.toString(mon) + "-0" + Integer.toString(day);
                else if(mon<10)//달이 10보다 작을때
                    when = Integer.toString(y) + "-0" + Integer.toString(mon) + "-" + Integer.toString(day);
                else if(day<10)//일이 10보다 작을때
                    when = Integer.toString(y) + "-" + Integer.toString(mon) + "-0" + Integer.toString(day);
                else
                    when = Integer.toString(y) + "-" + Integer.toString(mon) + "-" + Integer.toString(day);

                if (when != null) {
                    check = true;//날짜 선택시
                }
            }
        }, 2020, 6, 5);
        d.show();
    }

    public void setList(Cursor s){//리스트 뷰에 목록 출력
        Myadapter.clear();//어댑터 비우기
        if(s.getCount()==0){//커서가 참조한 행이 없을 때
            MyList.add("일정 없음");
        }
        else {
            for (int i = 0; i < s.getCount(); i++) {
                while (s.moveToNext()) {
                    if (s.getInt(0) == myId) {
                        MyList.add(s.getString(1) + "\n" + s.getString(2) + "(" + s.getString(3) + ")");
                        //리스트뷰 구성하는 arraylist에 출력할 데이터 추가
                    }
                }
            }
        }
        s.close();
    }

    public boolean onCreateOptionsMenu(Menu m){//옵션메뉴 생성
        super.onCreateOptionsMenu(m);
        MenuInflater min = getMenuInflater();
        min.inflate(R.menu.menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//옵션메뉴의 아이템 선택시
        switch (item.getItemId()){
            case R.id.schedule:
                Intent i1 = new Intent(schedule.this, schedule.class);
                i1.putExtra("myId", myId);
                startActivity(i1);
                finish();
                break;
            case R.id.living:
                Intent i2 = new Intent(schedule.this, living.class);
                i2.putExtra("myId", myId);
                startActivity(i2);
                finish();
                break;
            case R.id.hospital:
                Intent i3 = new Intent(schedule.this, hospital.class);
                i3.putExtra("myId", myId);
                startActivity(i3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}







