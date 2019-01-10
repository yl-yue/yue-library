package ai.yue.library.base.util;

/**
 * @author  孙金川
 * @version 创建时间：2018年6月22日
 */
public class AESUtilsTest {

    public static void main(String[] args) {
    	String keyt = "AES/ECB/PKCS5Padding3306";
        String s = "admin";

        String s1 = AESUtils.encrypt(s, keyt);
        System.out.println(s1);
        
        System.out.println(AESUtils.decrypt(s1, keyt));
    }
    
}
