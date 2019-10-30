package com.zjrb.core.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.zjrb.core.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;


public class SignatureChecker {
    private static final String MD5 = UIUtils.getString(R.string.app_md5);
    private static final String SHA1_FINGER_PRINT = UIUtils.getString(R.string.app_sha1);

    public static SignatureChecker getInstance() {
        return new SignatureChecker();
    }

    private SignatureChecker() {

    }

    /**
     * 校验MD5
     *
     * @param context context
     * @return true / false
     */
    public boolean checkMD5(Context context) {
        return TextUtils.equals(MD5, getMd5(context));
    }

    /**
     * 校验sha1指纹
     *
     * @param context context
     * @return true / false
     */
    public boolean checkFingerPrint(Context context) {
        return TextUtils.equals(SHA1_FINGER_PRINT, getSha1Fingerprint(context));
    }

    /**
     * 获取MD5
     *
     * @param context context
     * @return md5
     */
    public static String getMd5(Context context) {
        List<PackageInfo> apps = context.getPackageManager().getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (PackageInfo packageinfo : apps) {
            if (packageinfo.packageName.equals(context.getPackageName())) {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    messageDigest.update(packageinfo.signatures[0].toByteArray());
                    return byte2HexFormatted(messageDigest.digest());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取sha1指纹证书
     *
     * @param context context
     * @return sha1fingerprint
     */
    public static String getSha1Fingerprint(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();

        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = context.getPackageName();

        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;

        PackageInfo packageInfo = null;

        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();

        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);

        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;

        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //X509 证书，X.509 是一种非常通用的证书格式
        X509Certificate c = null;

        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String hexString = null;

        try {
            //加密算法的类，这里的参数可以使 MD4,MD5 等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");

            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());

            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    /**
     * 字节数组转换十六进制
     *
     * @param arr byte array
     * @return hex string
     */
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }
}
