package omkar.com.other;

import android.app.Application;

import omkar.com.models.User;

public class MyProperties extends Application {

    public int someValueIWantToKeep;
    public User user;


    public int getSomeValueIWantToKeep() {
        return someValueIWantToKeep;
    }

    public void setSomeValueIWantToKeep(int someValueIWantToKeep) {
        this.someValueIWantToKeep = someValueIWantToKeep;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
