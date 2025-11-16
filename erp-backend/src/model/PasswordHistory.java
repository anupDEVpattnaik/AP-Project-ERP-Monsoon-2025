package model;

public class PasswordHistory {
    private int log_id;
    private int user_id;
    private String password_hash;
    private LocalDateTime changed_at;

    public int getLog_id() {return log_id;}
    public void setLog_id(int log_id) {this.log_id = log_id;}

    public int getUser_id() {return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    public String getPassword_hash() {return password_hash;}
    public void setPassword_hash(String password_hash) {this.password_hash = password_hash;}

    public LocalDateTime getChanged_at() {return changed_at;}
    public void setChanged_at(LocalDateTime changed_at) {this.changed_at = changed_at;}
}
