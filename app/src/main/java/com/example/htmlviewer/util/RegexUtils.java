package com.example.htmlviewer.util;

import java.util.regex.Pattern;

/**
 * RegexUtils 
 * <p> 正则校验工具
 * @author yangdu <youngdu29@gmail.com>
 * @createtime 2018/10/14 7:17 PM
 **/
public class RegexUtils {

    private RegexUtils() {

    }

    /**
     * 验证网址
     * （1）验证http,https,ftp开头
     * （2）验证一个":"，验证多个"/"
     * （3）验证网址为 xxx.xxx
     * （4）验证有0个或1个问号
     * （5）验证参数必须为xxx=xxx格式，且xxx=空    格式通过
     * （6）验证参数与符号&连续个数为0个或1个
     * @author yangdu <youngdu29@gmail.com>
     * @createtime 2018/10/14 7:14 PM
     */
    public static boolean checkURL(String url) {
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }

}
