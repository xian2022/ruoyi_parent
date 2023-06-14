package com.xian.eduucenter.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES256 加解密工具类
 *
 * @author gxy
 */
public class Aes256Utils {

    private static final Logger log = LoggerFactory.getLogger(Aes256Utils.class);
    /**
     * 密钥, 256位32个字节
     */
    public static final String DEFAULT_SECRET_KEY = "BKNG978QLLB80K8CHB7BRKG5203JDEVF";
    /**
     * AES256 加密、解密秘钥长度需为32
     */
    private static final int SECURITY_KEY_LEN = 32;
    private static final String AES = "AES";

    /**
     * 初始向量IV, 初始向量IV的长度规定为128位16个字节, 初始向量的来源为随机生成.
     */
    private static final byte[] KEY_VI = "F028%$#1702ES128".getBytes(StandardCharsets.UTF_8);

    /**
     * 加密解密算法/加密模式/填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    /**
     * 加密Encoder
     */
    private static final BASE64Encoder base64Encoder = new BASE64Encoder();
    /**
     * 解密Encoder
     */
    private static final BASE64Decoder base64Decoder = new BASE64Decoder();

    static {
        /**
         * 在 Java 8 之前，必须在 JDK 中下载并安装 JCE 才能使用它。在 OpenJDK 11 中，默认安装了无限制的加密策略。如果您想（或必须）从无限制切换到有限加密策略，只需一行代码即可完成。
         * Security.setProperty("crypto.policy", "limited");
         * 从 Java 1.8.0_151 和 1.8.0_152 开始，有一种新的更简单的方法可以为 JVM 启用无限强度管辖策略。例如，如果不启用此功能，您将无法使用 AES-256 加密。
         * 要使用它，我们需要先下载 JRE。我喜欢将 server-jre 用于服务器。提取 server-jre 时，在 jre/lib/security 文件夹中查找文件 java.security。例如，对于 Java 1.8.0_152，文件结构如下所示：
         * /jdk1.8.0_152 |- /jre |- /lib |- /security |- java.security
         * 现在，使用文本编辑器打开 java.security 并查找定义 java 安全属性 crypto.policy 的行。它可以有两个值是有限的或无限的——默认是有限的。
         * 默认情况下，您应该找到一个注释掉的行：
         * #crypto.policy=unlimited
         * 您可以通过取消注释该行来启用无限制，删除 #:
         * crypto.policy=unlimited
         * 现在重新启动指向 JVM 的 Java 应用程序，您应该已准备就绪。
         */
        java.security.Security.setProperty("crypto.policy", "unlimited");
    }

    /**
     * 加密
     *
     * @param content 需要加密的字符串
     * @param key     密钥
     * @return
     * @throws Exception
     */
    public static String encode(String content, String key) {
        if (StringUtils.isAllBlank(content, key) || key.length() != SECURITY_KEY_LEN) {
            log.error("待加密字符串[{}]串或秘钥[{}]错误！", content, key);
            return null;
        }
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(KEY_VI));
            // 根据密码器的初始化方式加密
            byte[] byteAES = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            // 将加密后的数据转换为字符串
            return base64Encoder.encode(byteAES);
        } catch (Exception e) {
            log.error("待加密字符串[{}]进行AES256加密失败！", content, e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content 需要解密的字符串
     * @param key     密钥
     * @return
     */
    public static String decode(String content, String key) {
        if (StringUtils.isAllBlank(content, key) || key.length() != SECURITY_KEY_LEN) {
            log.error("待解密字符串[{}]或秘钥[{}]错误！", content, key);
            return null;
        }
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(KEY_VI));
            // 将加密并编码后的内容解码成字节数组 解密

            byte[] byteDecode = cipher.doFinal(base64Decoder.decodeBuffer(content));
            return new String(byteDecode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("待解密字符串[{}]进行AES256解密失败！", content, e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param str 需要加密的字符串
     * @return
     * @throws Exception
     */
    public static String encode(String str) {
        return encode(str, DEFAULT_SECRET_KEY);
    }

    /**
     * 解密
     *
     * @param str 需要解密的字符串
     * @return
     */
    public static String decode(String str) {
        return decode(str, DEFAULT_SECRET_KEY);
    }

//    public static void main(String[] args) {
//        JSONObject js = new JSONObject();
//        js.put("name", "巨魔战将");
//        js.put("skill", "狂热");
//        String content = js.toJSONString();
//        log.info("待加密：{}", content);
//        String encodeStr = encode(content);
//        log.info("加密后:{}", encodeStr);
//        log.info("解密后：{}", decode(encodeStr));
//    }
}

