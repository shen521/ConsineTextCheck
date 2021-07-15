package com.shen.CosineUtils;
import java.util.Collection;

public class StringUtil {

    public StringUtil() {
    }

    /**
     * 函数移除字符串两侧的空白字符
     * @param string
     * @return
     */
    public static boolean isBlank(String string) {
        return string == null || string.trim().equals("");
    }

    /**
     * 判断数组array是否空白
     * @param array
     * @return
     */
    public static boolean isBlank(Collection<? extends Object> array) {
        return array == null || array.size() == 0;
    }

}

