package omkar.com.models;

import android.util.Log;

import java.util.Map;

public class User {
    public String ID;
    public String name;
    public String email;
    public String number;
    public String address;


    public User() {
    }

    public User(String name, String email, String number, String address) {

        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
    }

    public static User setDataFromDocument(Map<String, Object> mapData) {
        if (mapData.isEmpty()) {
            return null;
        }
        Log.d("USER>JAVA", mapData.toString());
        return new User(mapData.get("name").toString(), mapData.get("email").toString(),
                mapData.get("number").toString(), mapData.get("address").toString());
    }

    public String getUserID() {
        return ID;
    }

    public void setUserID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
