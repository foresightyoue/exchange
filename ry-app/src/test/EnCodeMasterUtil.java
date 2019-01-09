package test;

import java.util.Base64;

public class EnCodeMasterUtil {
    private final static String BKEY = "2kaqxLDxrQauvcmbeCyytU8aKM"; //1300451317 % 26 = 9   aJou^y@7w0wT1   sEWJmf1J0aJou^y@7qcRC1ZX3v9VB7Fn1c = c0VXSm1mMUowYUpvdV55QDdxY1JDMVpYM3Y5VkI3Rm4xYw

    private final static String EKEY = "ga8ni9YtDPKGrDb6HmZGpf2TTu";//1300451095 % 26 = 21   DwVE_F0BgowANt   ix3guYG5ZzTMDH0EN36mxDwVE_F0B2AETP = aXgzZ3VZRzVaelRNREgwRU4zNm14RHdWRV9GMEIyQUVUUA

    private final static String UKEY = "rtvHfXbQaE8WzhGMjvFWy1fAW1";//1300451624 % 26 = 4    n0rG1+5Z    FuaEn0rG1+5ZW5d6viwt0h2wdCvc3q9TPp = RnVhRW4wckcxKzVaVzVkNnZpd3QwaDJ3ZEN2YzNxOVRQcA

    private final static String BUSER = "7zJCR2ZCaDxBRo0Tby7YiPLdsq";//-1664333297 % 26 = -3  kKozWHUu   ozktMy99A7nkHJZmgdZiK1kKozWHUuoFTE = b3prdE15OTlBN25rSEpabWdkWmlLMWtLb3pXSFV1b0ZURQ

    private final static String USDTUSER = "hEci0e82h2VswERJHyWBgHyrxq";//1907834803 % 26 = 17

    private final static String EMAINADDRESS = "0vZhHYkjKZYghFJ26jZZXRg2W7";//-1211755100 % 26 = -10    0xd7f6cd84277f275691d3de0278c4f50e7c56eb6b = PDQTXT9PgjbqFnxQd7f6cd84277f275691d3de0278c4f50e7c56eb6bzomwf9zZCr

    private final static String BMAINADDRESS = "i9k8TbaJiQtJgBGF1s5VvQZqha";//-940466425 % 26 = -15     3KLyxZiSV2xYDtH8v2cCsEdNr8vJksFT2f = Rvqn2qCdDm03KLyxZiSV2xYDtH8v2cCsEdNr8vJksFT2fPH3jeMmhFzemj3u

    private final static String USDTMAINADDRESS = "0DT21adquYArhF3EkurbjbD3Nj";//-830532563 % 26 = -3   19J9fFjKeKM3Bu9Ro2SkDDrpww579z7Taj = er5Jm194dayG8dDPJDCD9yG19J9fFjKeKM3Bu9Ro2SkDDrpww579z7TajfDC

    public static void enCode() {
        String ekey = new String(Base64.getEncoder().encode((EKEY.substring(1, 21) + "wC0prRb8@u!ye3" + EKEY.substring(22, 26)).getBytes()));
        String eAddress = EMAINADDRESS.substring(1, 26 - 10) + "2b4b073853a19a151ea31cbdeaa42ac472a8c69d" + EMAINADDRESS.substring(26 - 10 + 1, 26);
        String bUser = new String(Base64.getEncoder().encode((BUSER.substring(1, 26 - 3) + "gLem71YdncX2Aa" + BUSER.substring(26 - 3 + 1, 26)).getBytes()));
        String bKey = new String(Base64.getEncoder().encode((BKEY.substring(1, 9) + "5omtyhyitVaBf9h9" + BKEY.substring(10, 26)).getBytes()));
        String usdtUser = new String(Base64.getEncoder().encode((USDTUSER.substring(1, 17) + "a9ym2MPVgJcyUK" + USDTUSER.substring(18, 26)).getBytes()));
        String usdtKey = new String(Base64.getEncoder().encode((UKEY.substring(1, 4) + "uKbQ94Be6UHnpQPg" + UKEY.substring(5, 26)).getBytes()));
        String bAdddress = BMAINADDRESS.substring(1, 26 - 15) + "3LJSmXrixUrVKyKBxGUwGDHdzDHk6YrMzS" + EMAINADDRESS.substring(26 - 15 + 1, 26);
        String uAddress = USDTMAINADDRESS.substring(1, 26 - 3) + "1GH2tsTwdnweHmVUxdiKiVDJqeewVVCuFX" + EMAINADDRESS.substring(26 - 3 + 1, 26);
        System.out.println("ekey : " + ekey + "\n eAddress : " + eAddress
                + "\n bUser : " + bUser + "\n bKey : " + bKey + "\n bAddress : " + bAdddress
                + "\n usdtUser : " + usdtUser + "\n usdtKey : " + usdtKey + "\n uAddress : " + uAddress);

    }

    public static void main(String... args) {
        enCode();
    }
}
