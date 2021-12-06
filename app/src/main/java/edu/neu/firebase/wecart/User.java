package edu.neu.firebase.wecart;

public class User {
    public String username;
    public String user_type;
    public String profile_image;
    public String password;
    public String uid;

    public User() {
        // Default constructor
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String username, int userid) {
        this.username = username;
        this.user_type = user_type;
        this.profile_image = profile_image;
        this.password = password;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

}
