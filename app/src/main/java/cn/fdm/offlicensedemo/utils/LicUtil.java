package cn.fdm.offlicensedemo.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.fdm.offlicensedemo.App;

/**
 * <pre>
 *     @author  : fdm
 *     @time    : 2020/07/28
 *     @desc    : LicenseDemo
 *     @version : 1.0
 * </pre>
 */
public class LicUtil {

    private static String data = "{\n" +
            "\"appId\":\"cn.fdm.offlicensedemo\",\n" +
            "\"issuedTime\":1595951714000,\n" +
            "\"notBefore\":1538671712000,\n" +
            "\"notAfter\": 1640966400000,\n" +
            "\"customerInfo\":\"亚马逊公司\"\n" +
            "}";

    private static String RSA_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmj32yIpR01JehkdziAZ2BUo0HMq/FyeAPsMN1TC2PrJKgt5TZHCoFbj4P8uOfNiXaDfAJccegC8srqulkbKf3FT2rcehCxQkB1AxDlW/37gHxf1FxWrYFvL18YDff+bDx23BBersTUYR+ItwIdEkcEgCbgtE8wCPGcr7kxQBfOQIDAQAB";
    private static String RSA_PRI_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOaPfbIilHTUl6GR3OIBnYFSjQcyr8XJ4A+ww3VMLY+skqC3lNkcKgVuPg/y4582JdoN8Alxx6ALyyuq6WRsp/cVPatx6ELFCQHUDEOVb/fuAfF/UXFatgW8vXxgN9/5sPHbcEF6uxNRhH4i3Ah0SRwSAJuC0TzAI8ZyvuTFAF85AgMBAAECgYAOYbT9f5qzDu/GbErS7jbc8kFJyaedNtJ7KBOJJY9R/vnGfK0UPeWBITdN12DLdZ021Ne8PxSsrw+lUavtT7nKWYBbHNqP5pZVQg+zQilgk1PJtWoPhJ+UUGvTgHb8Iozluqn7q75PhtvN0UFnJsgrq6isDReWDNUrkP+uD950AQJBAPeFNTemhUbr8vANn//oGfD8B4aryXqR3/BzFVCfVEmPTOGQGo1Ace5FqNFS/bh0dSH8D+/L5d/zEw00S1rhuokCQQDudYrxQnHF3TFu9MmKuEpn9ZE5CYrmtd+pNp5mnEPzsuki+U4ldus1cqRoINAbj10NzfTrd12c24JqWQ+sd5MxAkEAmnDnb73bUxFOGDKoLsjnxtG68HCn2m7CyAlGt3Ny6CMd2XBM4O3hKMcWOuYJodooa/gXCYyz9jR5IrgKfL5z4QJAaOVB+oENBUoY/cMmiSQqbvLCMEH3XSeyPIBvS5n/9krEbYMXkjlunDNTCQ8uHIGDIJhx3cbDYkXZZ6jH0UlEcQJBALKpvAiJAHDiP4WnpEKCl/6a3zneuiLc6el6pb6GgGv20eREHxnivLYrR7QssLPdkFBTYsnPlooBHJoKXb6qD2E=";

    public static String getLic(licenseBean bean) {

        String lic = "";
        try {
            String aesKey = MyUtil.getRandomString(16);
            String encData = AesUtil.encrypt(aesKey, bean.toJson());
            String encDataLength = Integer.toHexString(encData.length());
            String sign = RSAUtil.sign(RSA_PRI_KEY, encData);
            lic = aesKey + encDataLength + encData + sign;
            Log.d("bruce", lic);
            return lic;
        } catch (Exception e) {
            e.printStackTrace();
            return lic;
        }
    }

    public static boolean checkLic(String lic) {
        if (TextUtils.isEmpty(lic)) {
            Log.d("bruce", "false");
            return false;
        }
        try {

            String aesKey = lic.substring(0, 16);
            Log.d("bruce", aesKey);
            int encDataLength = Integer.parseInt(lic.substring(16, 18), 16);
            String encData = lic.substring(18, 18 + encDataLength);
            String sign = lic.substring(18 + encDataLength);
            if (!RSAUtil.verifySign(RSA_PUB_KEY, encData, sign)) {
                Log.d("bruce", "false");
                return false;
            }
            String data = AesUtil.decrypt(aesKey, encData);
            String appId = JsonUtil.getString(data, "appId");
            long notBefore = JsonUtil.getLong(data, "notBefore");
            long notAfter = JsonUtil.getLong(data, "notAfter");
            String customerInfo = JsonUtil.getString(data, "customerInfo");
            Log.d("bruce", appId);
            Log.d("bruce", customerInfo);

            if (!MyUtil.getAppId(App.getContext()).equals(appId)) {
                return false;
            }

            //注意下时间戳精度的问题，
            long time = System.currentTimeMillis();
            if (time < notBefore || time > notAfter) {
                Log.d("bruce", "time:" + time);
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static class licenseBean {

        /**
         * appId : cn.fdm.offlicensedemo
         * issuedTime : 1595951714000
         * notBefore : 1538671712000
         * notAfter : 1640966400000
         * customerInfo : 亚马逊公司
         */

        private String appId;
        private long issuedTime;
        private long notBefore;
        private long notAfter;
        private String customerInfo;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public long getIssuedTime() {
            return issuedTime;
        }

        public void setIssuedTime(long issuedTime) {
            this.issuedTime = issuedTime;
        }

        public long getNotBefore() {
            return notBefore;
        }

        public void setNotBefore(long notBefore) {
            this.notBefore = notBefore;
        }

        public long getNotAfter() {
            return notAfter;
        }

        public void setNotAfter(long notAfter) {
            this.notAfter = notAfter;
        }

        public String getCustomerInfo() {
            return customerInfo;
        }

        public void setCustomerInfo(String customerInfo) {
            this.customerInfo = customerInfo;
        }

        private String toJson() {

            JSONObject jo = new JSONObject();
            try {
                jo.put("appId", appId);
                jo.put("issuedTime", issuedTime);
                jo.put("notBefore", notBefore);
                jo.put("notAfter", notAfter);
                jo.put("customerInfo", customerInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jo.toString();
        }
    }

}
