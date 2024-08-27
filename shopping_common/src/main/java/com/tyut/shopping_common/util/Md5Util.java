package com.tyut.shopping_common.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Util {
    public final static String md5key = "BAIZHAN"; // 秘钥


    /**
   * 加密
   * @param text 明文
   * @return 密文
   */
    public static String encode(String text){
        return DigestUtils.md5Hex(text + md5key);
    }


    /**
   * 验证
   *
   * @param text 明文
   * @param cipher  密文
   * @return true/false
   */
    public static boolean verify(String text, String cipher){
        // 将明文转为密文进行比对
        String md5Text = encode(text);
        if (md5Text.equalsIgnoreCase(cipher)) {
            return true;
        }
        return false;
    }
}