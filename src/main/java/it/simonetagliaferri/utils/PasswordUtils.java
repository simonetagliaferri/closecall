package it.simonetagliaferri.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtils {

    private PasswordUtils() {}

    public static String sha256Hex(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
