package com.sumer.safeho.modelclasses;

import java.util.ArrayList;

public class User {
    private String name;
    private String profilepic;
    private int age;
    private boolean isEmergencyNoGiven;
    private boolean isDetailsGiven;
    private String uid;
    private String phoneNumber;

    private ArrayList<String> EmergencyPhoneList;
    //empty constructor
    public User() {}

    public User( String phoneNumber,String uid,boolean isEmergencyNoGiven, boolean isDetailsGiven) {
        this.isEmergencyNoGiven = isEmergencyNoGiven;
        this.isDetailsGiven = isDetailsGiven;
        this.uid = uid;
        this.phoneNumber = phoneNumber;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static boolean isValidPhoneNumber(String ph)
    {
        if(ph.length()<10) return false;
        for(int i=1;i<ph.length();i++)
        {
            if(ph.charAt(i)>'9'||ph.charAt(i)<'0')
                return false;
        }
        if(ph.charAt(0)=='0') return false;
        return true;
    }
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isEmergencyNoGiven() {
        return isEmergencyNoGiven;
    }

    public void setEmergencyNoGiven(boolean emergencyNoGiven) {
        isEmergencyNoGiven = emergencyNoGiven;
    }

    public boolean isDetailsGiven() {
        return isDetailsGiven;
    }

    public void setDetailsGiven(boolean detailsGiven) {
        isDetailsGiven = detailsGiven;
    }

    public ArrayList<String> getEmergencyPhoneList() {
        return EmergencyPhoneList;
    }

    public void setEmergencyPhoneList(ArrayList<String> emergencyPhoneList) {
        EmergencyPhoneList = emergencyPhoneList;
    }
}