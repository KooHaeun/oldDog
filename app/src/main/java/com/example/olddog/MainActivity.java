package com.example.olddog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    oldDogDB dogDB;
    SQLiteDatabase db;
    Button btnJoin;
    ImageButton myDog;
    View login;
    EditText id;
    int myId;
    Boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDog = findViewById(R.id.myDog);
        btnJoin = findViewById(R.id.btnJoin);


        //강아지 이미지 버튼 클릭시
        myDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogDB = new oldDogDB(MainActivity.this);
                db = dogDB.getReadableDatabase();
                final Cursor c = db.rawQuery("SELECT dNum FROM DOG;", null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("로그인");
                dlg.setIcon(R.drawable.logo);
                login = View.inflate(getApplicationContext(), R.layout.login, null);
                id = login.findViewById(R.id.login_in);

                dlg.setView(login);
                dlg.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(id.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "등록번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            myId = Integer.parseInt(id.getText().toString());
                            while(c.moveToNext()) {
                                if(c.getInt(0)==myId)
                                    check=true;//입력 값과 같은 값을 찾을 시
                            }
                            c.close();
                            db.close();
                            if(check==true) {//로그인 성공 후 첫 화면 넘어감
                                Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                                Intent first = new Intent(MainActivity.this, first.class);
                                first.putExtra("myId", myId);//자신의 id를 계속 다른 액티비티에 넘겨줌
                                startActivity(first);

                            }
                            else{//로그인 실패시
                                AlertDialog.Builder adlg = new AlertDialog.Builder(MainActivity.this);
                                adlg.setTitle("알림");
                                adlg.setIcon(R.drawable.logo);
                                adlg.setMessage("로그인에 실패헸습니다. 정확한 등록번호를 입력해주세요.");
                                adlg.setPositiveButton("확인", null);
                                adlg.show();
                            }
                        }
                    }
                });
                dlg.show();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//등록 버튼 누를시
                Intent join = new Intent(MainActivity.this, join.class);
                startActivity(join);
            }
        });


    }
}
