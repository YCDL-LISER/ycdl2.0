package com.liser.socket.util;

/**
 * 处理字符数据
 */
public abstract class Protocol {

    /**
     * 校验数据
     * 去除数据 头尾标志和校验位  将数据全部异或
     *
     * @param input_data
     * @return bool
     */
    public static boolean verify(String input_data) {
        String ver_code = input_data.substring(input_data.length() - 4, input_data.length() - 2);
        input_data = input_data.substring(2, input_data.length() - 2);
        input_data = input_data.substring(0, input_data.length() - 2);
        if (ver_code.equals(_checkCode(_inputDataEscape(input_data)))) {
            return true;
        }
        return false;
    }

    /**
     * 发送数据时特殊字符转义
     *
     * @param out_data 原始数据
     * @return 转义待发送的数据
     */
    public static String _outDataEscape(String out_data) {
        out_data = out_data.replace("E6", "E601"); // 修改后 2018-04-22
        out_data = out_data.replace("E7", "E602");
        //out_data = out_data.replace("E6", "E601"); // 修改前 2018-04-22
        return out_data;
    }

    /**
     * 接受数据时特殊字符转义
     *
     * @param input_data 原始数据
     * @return 转义后的结果
     */
    public static String _inputDataEscape(String input_data) {
        input_data = input_data.replace("E602", "E7");
        input_data = input_data.replace("E601", "E6");
        return input_data;
    }

    /**
     * 添加校验位
     *
     * @param input_data
     * @return 等待发送的数据
     */
    public static String addVerify(String input_data) {
        String ver_code = _checkCode(input_data);
        return "E7" + _outDataEscape(input_data + ver_code) + "E7";
    }

    /**
     * 获取数据校验位
     *
     * @param para 需要校验的数据
     * @return String
     */
    private static String _checkCode(String para) {
        byte[] dateArr = ToHexTool.hexString2Bytes(para);
        int code = dateArr[0];
        for (int i = 1; i < dateArr.length; i++) {
            code ^= dateArr[i];
        }
        if (code < 0) {
            code = 256 + code;
        }
        return Integer.toHexString(code).toUpperCase();
    }

    /**
     * 获取数据校验位
     *
     * @param dateArr 需要校验的数据
     * @return byte
     */
    public static byte checkCode(byte[] dateArr) {
        byte code = dateArr[0];
        for (int i = 1; i < dateArr.length; i++) {
            code ^= dateArr[i];
        }
        return code;
    }
}
