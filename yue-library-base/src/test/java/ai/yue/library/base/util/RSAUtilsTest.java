package ai.yue.library.base.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author  孙金川
 * @version 创建时间：2018年6月22日
 */
public class RSAUtilsTest {

	/**
	 * RSA私钥
	 */
	static String RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIA/gEwXH1DURHCMeCw7wOwzyUYxnFRx0Vog7NhVFbhQYgvKYZJkBMSxCZRYeTeAXfFvWL449929qHRcno00TnZADH4eUO9w3sKFYGy15issrWbT8SQJvVQkoxDmaoBwjdrmbb/RtTqeFuFC+ih/Sn8C+ZvWzOJx3LSBWkUyCBjhAgMBAAECgYAGXyFIpn1vzpv324K879sN4STG0AAsUdtM1wXtWYzSs2urmkpus3lpLEKFlD+xejcwn4+RI9NsuNd9sHxQF6EuPFkz2TsL7jO+IgcmILNT7PGzm027Wqd2Gekbfly0mFJCxwE6WHLBLLvDDW5jMz0mhRYbw+J5pAwLNwBh3e+WgQJBAOMa7Tk3DDBOAD9y+REtzXBZZrqZC3VxVYMlO7XglhQDobIvg1CqS81gX6XWHZ/ACQ1ZiPAGdg7eEq9EBr84VQ0CQQCQkK+GNcldhlqqJq2FyPw4bR3lqVdyvSdoUL/YdtAtdJgVG4kcAL3LSvckQtA/v8lwi0AIQAN2C+YkM70CG4YlAkEAw4sSyRnhz9HJmtg3JpiGdH812eMfH69HDtXHVPIcpvz9g/wvGAyZmiuqD5OODFgAM1NteiZGDsUd13U+TfWGHQJAB67Y1T4ojILmcKKLmE+dL/aEnz8Hub5ZyM5506xE1hWbO4vyFn3nmFVMmy292ZV8xDFyXr3gMTbirUveoK0LzQJAQ1RRdzyUkvnY/Ls1qusmUajjwIYiZkyxptcvmmyIUzQUwFHXwvZk8nfx7F7gPBZFJghscZYKbW/GNrNS+qKQ4g==";
	/**
	 * RSA公钥
	 */
	static String RSA_PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAP4BMFx9Q1ERwjHgsO8DsM8lGMZxUcdFaIOzYVRW4UGILymGSZATEsQmUWHk3gF3xb1i+OPfdvah0XJ6NNE52QAx+HlDvcN7ChWBsteYrLK1m0/EkCb1UJKMQ5mqAcI3a5m2/0bU6nhbhQvoof0p/Avmb1szicdy0gVpFMggY4QIDAQAB";
	
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("url", "http://183.230.30.245:50003/push_campus/service/api/common/client/downpage");
		json.put("adminKey", "he-campus");
		json.put("adminKeyt", "E97FD2C326FC47129D6FB5D8C1C33C8D");
//		System.out.println(json.toJSONString());
//		System.out.println(encrypt(json.toJSONString(), RSA_PUBLICKEY));
		String str = "epKB2+dhJ0EMTOKEe6eavBdyr1K5Wlw2ZR5AnmbuwqtyUXl/E4w+8o3ZN+CR/bHMNZ8EWLk9zpVlzPN2JtMe2tgSHvUo0XhMbbIMa9lA4qL4D2Xo39d5LpO5K0GlgaBsB+V3L5IjcOLSFhF+AlKH6Uaa9xl2UlSLZfEgB7H80tE=";
		System.out.println(RSAUtils.decrypt(str, RSA_PRIVATE_KEY));
	}
	
}
