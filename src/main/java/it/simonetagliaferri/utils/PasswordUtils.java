package it.simonetagliaferri.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtils {
    public static String md5(String password) {
        return DigestUtils.md5Hex(password);
    }
}
