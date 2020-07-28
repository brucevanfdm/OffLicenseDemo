package cn.fdm.offlicensedemo.utils;

import android.content.Context;

import java.util.Random;

public class MyUtil {

    /**
     * 获取app id
     */
    public static String getAppId(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取N位随机密钥
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append((char) result);
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append((char) result);
                    break;
                case 2:
                    sb.append(new Random().nextInt(10));
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }

}
