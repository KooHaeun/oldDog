package com.example.olddog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class first extends AppCompatActivity {
    ImageButton logo;
    Button title;
    Integer[] iamges = {R.drawable.pic01, R.drawable.pic02, R.drawable.pic03, R.drawable.pic04, R.drawable.pic05, R.drawable.pic06, R.drawable.pic07, R.drawable.pic08, R.drawable.pic09, R.drawable.pic10, R.drawable.pic11, R.drawable.pic12, R.drawable.pic13, R.drawable.pic14, R.drawable.pic15, R.drawable.pic16, R.drawable.pic17, R.drawable.pic18, R.drawable.pic19, R.drawable.pic20};
    int myId;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        Intent i = getIntent();
        myId = i.getIntExtra("myId", 1010);//id를 넘겨받음

        Toolbar bar = (Toolbar)findViewById(R.id.action_bar);
        setSupportActionBar(bar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        //그리드 뷰 생성 후 어댑터를 붙여줌
        GridView grid = findViewById(R.id.grid);
        PictureList adapter = new PictureList(this);
        grid.setAdapter(adapter);

        logo = findViewById(R.id.logo);
        //툴바의 로고 클릭시 첫화면으로 돌아옴
        goMain(logo);

        //앱 타이틀 선택시
        title=findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {//Mainactivity로 돌아감
            @Override
            public void onClick(View v) {
                Intent i = new Intent(first.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
    //BaseAdapter을 상속 받은 PicturList 클래스(어댑터 생성)
    public class PictureList extends BaseAdapter{
        Context c;

        public  PictureList(Context _c){
            c=_c;
        }

        @Override
        public int getCount() {
            return iamges.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img = new ImageView(c);
            img.setLayoutParams(new GridView.LayoutParams(400, 480));
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img.setPadding(5, 10, 5, 10);
            img.setImageResource(iamges[position]);
            //그리드 뷰에 띄울 이미지 뷰 생성&출력

            final int p =position;
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View show = View.inflate(first.this, R.layout.first_show, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(first.this);
                    ImageView show_pic = show.findViewById(R.id.img);
                    show_pic.setImageResource(iamges[p]);
                    dlg.setTitle("강지");
                    dlg.setIcon(R.drawable.logo);
                    dlg.setView(show);
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                }
            });
            return img;
        }
    }

    public void goMain(ImageView logo){//로고버튼 클릭시
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(first.this, first.class);
                startActivity(i);
                finish();
            }
        });
    }

    //옵션 메뉴 생성
    public boolean onCreateOptionsMenu(Menu m){
        super.onCreateOptionsMenu(m);
        MenuInflater min = getMenuInflater();
        min.inflate(R.menu.menu, m);
        return true;
    }

    //옵션메뉴 클릭시
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.schedule:
                Intent i1 = new Intent(first.this, schedule.class);
                i1.putExtra("myId", myId);
                startActivity(i1);
                finish();
                break;
            case R.id.living:
                Intent i2 = new Intent(first.this, living.class);
                i2.putExtra("myId", myId);
                startActivity(i2);
                finish();
                break;
            case R.id.hospital:
                Intent i3 = new Intent(first.this, hospital.class);
                i3.putExtra("myId", myId);
                startActivity(i3);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
