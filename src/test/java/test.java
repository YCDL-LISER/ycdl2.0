import com.liser.socket.util.ToHexTool;
import sun.nio.cs.ext.GBK;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class test {
    public static void main(String[] args) {
        /*byte[] b = " ".getBytes();
        System.out.println(b);*/

        /*byte[] ss = "H".getBytes();

        //int x = ss & 0xff;


        System.out.println(ss);

        String sss = Integer.toBinaryString(2);
        System.out.println(sss);*/

        /*String ss = strTo16("20180426");
        System.out.println(ss);*/
       String aa = "484C543430305F56323033333332332C3132302E39322E32302E3230392C32312C6674705F686F6C6C6F6F2C6674705F686F6C6C6F6F5F2A54505F535A2C484C543430305F563230333333322E42494E2C2E2F2C";
       byte[] ss = ToHexTool.hexString2Bytes(aa);
        try {
            String dd = new String(ss, "GBK");
            System.out.println(dd);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

       /*// byte[] b = ToHexTool.short2Bytes(a);
        String c = ToHexTool.bytes2HexString(b);

        System.out.println("hex：" + c);*/

       /* byte[] sss = ToHexTool.hexString2Bytes("860000065412345678901200000001A00100000000");
        byte check_byte_xor = Protocol.checkCode(sss); // 不包含检验位数组
        System.out.println(check_byte_xor);
        String hex = ToHexTool.bytes2HexString(new byte[]{check_byte_xor});
        System.out.println("hex："+hex);*/

        /*int message_type_1 = 0x03ff & 0x06;
        System.out.println(message_type_1);

        String time = "20101125102503";
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-HH-dd HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyHHddHHmmss");
        try {
            time = formatter1.format(formatter2.parse(time));
        } catch (Exception e){

        }
        System.out.println(time);*/


    }
}
