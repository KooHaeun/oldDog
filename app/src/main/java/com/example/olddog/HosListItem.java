package com.example.olddog;

public class HosListItem {//동물병원 어댑터 생성시 어댑터에 데이터 추가할 때 쓰이는 클래스
    public String hCall, hName, hAddress;

    public String gethCall() {
        return hCall;
    }

    public void sethCall(String hCall) {
        this.hCall = hCall;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String gethAddress() {
        return hAddress;
    }

    public void sethAddress(String hAddress) {
        this.hAddress = hAddress;
    }
}
