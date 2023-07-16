package com.example.chat_bed.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author Lehr
 * @create: 2020-02-04
 */
@Component
public class Jwt {

    private static final String ALGORITHM_NAME = "AES";
    private static final String DEFAULT_ENCRYPT_RULE = "AES/CBC/PKCS5Padding";
    private static final String RANDOM_KEY_ALGORITHM = "SHA1PRNG";
    private static final String RANDOM_KEY_ALGORITHM_PROVIDER = "SUN";

    /**
     签发对象：这个用户的id
     签发时间：现在
     有效时间：30分钟
     载荷内容：暂时设计为：这个人的名字，这个人的昵称
     加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(String userId,String key) throws Exception {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MONTH,1);
        Date expiresDate = nowTime.getTime();
        return encrypt((JWT.create().withAudience(userId)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("userid", userId)
                .withClaim("key",key)
                //载荷，随便写几个都可以
                .sign(Algorithm.HMAC256(userId+"HelloLehr")))).toString();   //加密
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     * @param token
     * @throws
     */
    public static Boolean verifyToken(String token, String userid){
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(userid+"HelloLehr")).build();
            jwt = verifier.verify(decrypt(token));
            return true;
        } catch (Exception e) {
            //效验失败
            //这里抛出的异常是我自定义的一个异常，你也可以写成别的
            return false;
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token){
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
        } catch (Exception e) {
            e.printStackTrace();
        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static String getClaimByName(String token, String name){
        try {
            String temp = decrypt(token);
            if(temp==null) {
                return "error";
            }
            return JWT.decode(temp).getClaim(name).asString();
        }catch (Exception e){
            return "error";
        }
    }

    /**
     * AES加密
     * @param content 待加密的内容，为空时为回空
     * @return 加密后的base64格式的结果，出现异常时返回null
     */
    public static String encrypt(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_NAME);
            SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_KEY_ALGORITHM, RANDOM_KEY_ALGORITHM_PROVIDER);
            secureRandom.setSeed(DEFAULT_ENCRYPT_RULE.getBytes());
            keyGenerator.init(128, secureRandom);
            SecretKey originalKey = keyGenerator.generateKey();
            SecretKey secretKey = new SecretKeySpec(originalKey.getEncoded(), ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            String result = new String(Base64.getEncoder().encodeToString(encrypted));
            return  result;
        } catch (Exception e) {

            return null;
        }
    }


    /**
     * 解密
     * @param encrypted 加密后的base64格式的密文
     * @return 解密后的原文，出现异常时返回null
     */
    public static String decrypt(String encrypted) {
        if (StringUtils.isEmpty(encrypted)) {
            return null;
        }
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_NAME);
            SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_KEY_ALGORITHM, RANDOM_KEY_ALGORITHM_PROVIDER);
            secureRandom.setSeed(DEFAULT_ENCRYPT_RULE.getBytes());
            keyGenerator.init(128, secureRandom);
            SecretKey originalKey = keyGenerator.generateKey();
            SecretKey secretKey = new SecretKeySpec(originalKey.getEncoded(), ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(decrypted, "utf-8");
        } catch (Exception e) {

            return null;
        }
    }


}