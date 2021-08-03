package com.example.olddog;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;


public class hospital extends AppCompatActivity {
    oldDogDB dogDB;
    SQLiteDatabase db;
    ImageButton logo;
    Button plus_hos;
    ListView hos;

    EditText name_in, address_in, call_in;
    String name, mcall, address;
    View hos_plus;
    int myId;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital);

        setFirst();//초기 세팅

        logo = findViewById(R.id.logo);

        goMain(logo);//메인화면으로 돌아가기

        plus_hos = findViewById(R.id.plus_hos);


        //리스트 뷰에 동물병원 목록 출력
        hos = findViewById(R.id.hos);
        myAdapter adapter = new myAdapter();
        hos.setAdapter(adapter);
        setList(adapter);

        //동물병원 등록 버튼 클릭시
        plus_hos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(hospital.this);
                dlg.setTitle("병원 등록");
                dlg.setIcon(R.drawable.logo);
                hos_plus = View.inflate(getApplication(), R.layout.hos_dialog, null);
                dlg.setView(hos_plus);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        call_in = hos_plus.findViewById(R.id.call_in);
                        mcall = call_in.getText().toString();
                        address_in = hos_plus.findViewById(R.id.address_in);
                        address = address_in.getText().toString();
                        name_in = hos_plus.findViewById(R.id.name_in);
                        name = name_in.getText().toString();
                        if (name.equals("") || address.equals("") || mcall.equals("")) {//빈 항목이 있을 시
                            AlertDialog.Builder dlg = new AlertDialog.Builder(hospital.this);
                            dlg.setTitle("알림");
                            dlg.setMessage("모든 항목을 작성해주세요");
                            dlg.setIcon(R.drawable.logo);
                            dlg.setPositiveButton("확인", null);
                            dlg.show();
                        } else {//동물병원 테이블에 등록
                            Cursor ifN = db.rawQuery("SELECT * FROM HOS WHERE call=='"+mcall+"';", null);
                            if (ifN.getCount() == 0) {
                                db.execSQL("INSERT INTO HOS VALUES('" + mcall + "','" + name + "','" + address + "');");
                                Toast.makeText(getApplicationContext(), "등록완료!", Toast.LENGTH_SHORT).show();
                                myAdapter newAdapter = new myAdapter();
                                hos.setAdapter(newAdapter);
                                setList(newAdapter);
                            } else {//동물병원 전화번호가 중복일시
                                AlertDialog.Builder dlg = new AlertDialog.Builder(hospital.this);
                                dlg.setTitle("알림");
                                dlg.setMessage("전화번호는 중복일 수 없습니다. 새로운 전화번호를 입력해주세요");
                                dlg.setIcon(R.drawable.logo);
                                dlg.setPositiveButton("확인", null);
                                dlg.show();
                            }
                            ifN.close();
                        }
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

        dogDB = new oldDogDB(hospital.this);
        db = dogDB.getWritableDatabase();
    }

    public void goMain(ImageView logo){//로고버튼 클릭시
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(hospital.this, first.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void setList(myAdapter adapter){//데이터베이스에 있는 동물병원 정보를 어댑터에 추가
        Cursor c = db.rawQuery("SELECT * FROM HOS", null);

        for (int i = 0; i < c.getCount(); i++) {
            while (c.moveToNext()) {
                adapter.addItem(c.getString(0), c.getString(1), c.getString(2));
            }
        }
        c.close();
    }

    //BaseAdapter를 상속받은 어댑터 생성 클래스
    public class myAdapter extends BaseAdapter {

        public ArrayList<HosListItem> list = new ArrayList<HosListItem>();

        //초기화
        public myAdapter() {}

        //리스트 뷰 아이템 개수 알려줌
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //현재 아이템 알려줌
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        //리스트뷰에서 아이템과 xml을 연결하여 화면에 출력
        @NonNull
        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

            final Context c = parent.getContext();
            if (view == null) {//xml로 된 layout파일을 view 객체로 변환
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.hos_list, parent, false);
            }

            TextView Tname = view.findViewById(R.id.txt_name);
            TextView Taddress = view.findViewById(R.id.txt_address);
            Button calling = view.findViewById(R.id.calling);
            final HosListItem myList = list.get(position);
            Tname.setText(myList.gethName());
            Taddress.setText(myList.gethAddress());
            calling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//전화 버튼 클릭시
                    Uri uri = Uri.parse("tel:" + myList.gethCall());
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                }
            });

            return view;
        }

        //HosListItem 클래스에 정의한 set 메소드에 데이터를 담아서 리턴함
        public void addItem(String call, String name, String address) {
            HosListItem item = new HosListItem();

            item.sethCall(call);
            item.sethName(name);
            item.sethAddress(address);

            list.add(item);
        }
    }


    public boolean onCreateOptionsMenu(Menu m){//옵션메뉴 생성
        super.onCreateOptionsMenu(m);
        MenuInflater min = getMenuInflater();
        min.inflate(R.menu.menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//옵션 메뉴의 아이템 선택시

        switch (item.getItemId()){
            case R.id.schedule:
                Intent i1 = new Intent(hospital.this, schedule.class);
                i1.putExtra("myId", myId);
                startActivity(i1);
                finish();
                break;
            case R.id.living:
                Intent i2 = new Intent(hospital.this, living.class);
                i2.putExtra("myId", myId);
                startActivity(i2);
                finish();
                break;
            case R.id.hospital:
                Intent i3 = new Intent(hospital.this, hospital.class);
                i3.putExtra("myId", myId);
                startActivity(i3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    }

