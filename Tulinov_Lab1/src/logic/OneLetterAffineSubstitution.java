package logic;

public class OneLetterAffineSubstitution {
    public static String encryptMessage(char[] msg, String key) {
        if (key.equals(""))
            return "";
        StringBuilder cipher = new StringBuilder();
        int[] params = getParams(key);
        int a = params[0]; int b = params[1];
        for (char c : msg) {
            if (c != ' ') {
                cipher.append((char) ((((a * c) + b) % 128)));
            } else {
                cipher.append(c);
            }
        }
        return cipher.toString();
    }

    private static int[] getParams(String pass) {
        int[] params = new int[2];
        params[0] = Character.codePointAt(pass, 0);
        params[1] = Character.codePointAt(pass, 1);
        return params;
    }
}