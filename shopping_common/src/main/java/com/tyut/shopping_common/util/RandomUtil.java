package com.tyut.shopping_common.util;

import java.util.Random;

/**
 * @version v1.0
 * @author OldGj 2024/6/20
 * @apiNote 随机数生成工具类
 */
public class RandomUtil {


    /**
     * 生成随机数
     * @param bits 随机数位数
     * @return
     */
    public static String buildCheckCode(Integer bits) {
        String template = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bits; i++) {
            Random random = new Random();
            char ch = template.charAt(random.nextInt(template.length()));
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }

}
