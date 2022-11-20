package sample;

public class User {
    private String login;
    private String password;
    private short isblocked;
    private short passlimit;
    private short isfirstlogin;

    public User() {

    }

    public String getLogin() {
        return login;
    }

    public void setIsblocked(short isblocked) {
        this.isblocked = isblocked;
    }

    public User(String login, String password, short isblocked, short passlimit) {
        this.login = login;
        this.password = password;
        this.isblocked = isblocked;
        this.passlimit = passlimit;
    }

    public short getIsfirstlogin() {
        return isfirstlogin;
    }

    public short getIsblocked() {
        return isblocked;
    }

    public short getPasslimit() {
        return passlimit;
    }

    public String getPassword() {
        return password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsfirstlogin(short isfirstlogin) {
        this.isfirstlogin = isfirstlogin;
    }

    public void setIsLimited(short passlimit) {
        this.passlimit = passlimit;
    }

    public boolean isAdmin() {
        return login.equals("admin");
    }
}