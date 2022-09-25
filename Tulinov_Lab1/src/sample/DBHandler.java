package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DBHandler extends Config {
    Connection connection;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String patern = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(patern, dbUser, dbPass);
        return connection;
    }

    public void signUpUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USERS_LOGIN + "," + Const.USERS_PASSWORD + ")" +  "VALUES(?,?)";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insert);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void changePassword(User user) {
        String insert = "UPDATE " + Const.USER_TABLE + " SET " + Const.USERS_PASSWORD + "=?  WHERE "
                + Const.USERS_LOGIN + "=? ";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insert);
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public User getUser(String login) {
        ResultSet resultSet;
        User user = new User();
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_LOGIN + "=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(select);
            preparedStatement.setString(1, login);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setIsblocked(resultSet.getShort("isbloked"));
                user.setIsLimited(resultSet.getShort("passlimit"));
                user.setIsfirstlogin(resultSet.getShort("isfirstlogin"));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public ResultSet getLogin(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_LOGIN + "=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public String getPassword(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_LOGIN + "=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user1 = new User();
                user1.setPassword(resultSet.getString(3));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return user.getPassword();
    }

    public ArrayList<User> getListUsers() {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE;
        ArrayList<User> arrayList = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(select);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setIsblocked(resultSet.getShort("isbloked"));
                user.setIsLimited(resultSet.getShort("passlimit"));
                arrayList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public Set<String> getLoginUsers() {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE;
        Set<String> set = new HashSet<>();

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(select);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                set.add(resultSet.getString("login"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public void updateUser(User user) {
        String insert = "UPDATE " + Const.USER_TABLE + " SET " + Const.USERS_ISBLOKED +"=? " +"," +
                Const.USERS_PASSLIMIT + "=?  WHERE " + Const.USERS_LOGIN + "=? ";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insert);
            preparedStatement.setShort(1, user.getIsblocked());
            preparedStatement.setShort(2, user.getPasslimit());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateUserLog(User user) {
        String insert = "UPDATE " + Const.USER_TABLE + " SET " + Const.USERS_ISFIRSTLOGIN +"=? "
                + " WHERE " + Const.USERS_LOGIN + "=? ";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insert);
            preparedStatement.setShort(1, user.getIsfirstlogin());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public User getIsfirstloginFromDB(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " + Const.USERS_LOGIN + "=?";
        ArrayList<User> arrayList = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user2 = new User();
                user2.setLogin(resultSet.getString("login"));
                user2.setPassword(resultSet.getString("password"));
                user2.setIsblocked(resultSet.getShort("isbloked"));
                user2.setIsfirstlogin(resultSet.getShort("isfirstlogin"));
                arrayList.add(user2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList.get(0);
    }
}
