package com.example.olddog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class oldDogDB extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_DOG = "CREATE TABLE DOG(dNum INTEGER PRIMARY KEY NOT NULL, eat INTEGER, drink INTEGER, poop INTEGER, pee INTEGER, E_medi INTEGER, C_medi INTEGER)";// 반려견 정보 테이블 생성문
    private static final String CREATE_TABLE_DAY="CREATE TABLE DAY(dNum INTEGER NOT NULL, day TEXT DEFAULT (date('now')) NOT NULL, eat INTEGER DEFAULT 0, drink INTEGER DEFAULT 0, poop INTEGER DEFAULT 0, pee INTEGER DEFAULT 0, E_medi INTEGER DEFAULT 0, C_medi INTEGER DEFAULT 0, PRIMARY KEY(dNum, day), FOREIGN KEY(dNum) references DOG(dNum))";// 생활 테이블 생성문
    private static final String CREATE_TABLE_MEDI="CREATE TABLE MEDI(dNum INTEGER PRIMARY KEY NOT NULL, E_medi TEXT, C_medi TEXT, FOREIGN KEY(dNum) references DOG(dNum))";// 약 정보 테이블 생성문
    private static final String CREATE_TABLE_SCHE="CREATE TABLE SCHE(dNum INTEGER NOT NULL, date TEXT NOT NULL, name TEXT, hos TEXT, sort TEXT,  PRIMARY KEY(dNum, date), FOREIGN KEY(hos) references HOS(name))";// 일정 테이블 생성문
    private static final String CREATE_TABLE_HOS = "CREATE TABLE HOS(call TEXT PRIMARY KEY NOT NULL, name TEXT, adress TEXT)";// 동물병원 테이블 생성문

    public oldDogDB(Context c){
        super(c, "oldDogDB", null, 1);
    }//데이터 베이스 생성

    public void onCreate(SQLiteDatabase db){//데이터 베이스 테이블 생성
        db.execSQL(CREATE_TABLE_DOG);
        db.execSQL(CREATE_TABLE_DAY);
        db.execSQL(CREATE_TABLE_MEDI);
        db.execSQL(CREATE_TABLE_SCHE);
        db.execSQL(CREATE_TABLE_HOS);
        db.execSQL("INSERT INTO DOG VALUES(1010, 200, 300, 6, 18, 2, 1)");
        db.execSQL("INSERT INTO DAY(dNum) VALUES(1010)");
        db.execSQL("INSERT INTO MEDI VALUES(1010, '항생제', '소독약')");
        db.execSQL("INSERT INTO HOS VALUES('029997582', '강북우리동물의료센터', '서울 도봉구 도봉로 586')");
        db.execSQL("INSERT INTO HOS VALUES('029968275', '행복한동물병원', '서울 도봉구 도봉로136길 7')");
        db.execSQL("INSERT INTO HOS VALUES('029930075', '둘리동물병원', '서울 도봉구 도봉로 506')");
    }
    public void onUpgrade(SQLiteDatabase db, int o, int n){
        db.execSQL("DROP TABLE IF EXISTS DOG");
        db.execSQL("DROP TABLE IF EXISTS DAY");
        db.execSQL("DROP TABLE IF EXISTS MEDI");
        db.execSQL("DROP TABLE IF EXISTS SCHE");
        db.execSQL("DROP TABLE IF EXISTS HOS");
        onCreate(db);//새로운 테이블 생성

    }
}
