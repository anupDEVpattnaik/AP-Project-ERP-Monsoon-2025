package model;

import java.time.LocalDateTime;

public class User {
    private int user_id;
    private String user_name;
    private String role;
    private String password_hash;
    private String status;
    private LocalDateTime last_login;

    public int getUser_id() {return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    public String getUser_name() {return user_name;}
    public void setUser_name(String user_name) {this.user_name = user_name;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    public String getPassword_hash() {return password_hash;}
    public void setPassword_hash(String password_hash) {this.password_hash = password_hash;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public LocalDateTime getLast_login() {return last_login;}
    public void setLast_login(LocalDateTime last_login) {this.last_login = last_login;}
}
