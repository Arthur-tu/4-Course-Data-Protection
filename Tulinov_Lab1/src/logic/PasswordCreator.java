package logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordCreator {
    private char[] password;

    public boolean checkPassword(String password) {

        String regx = "^([^A-z]*[0-9]+[^A-z]*[A-z]+[^0-9]*[0-9]+.*)$";

        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(password);
        if (matcher.find()) {
            System.out.println("Pass is good");
            return true;
        }
        System.out.println("Pass is bad");
        return false;
    }
}