package com.phone.adb;

public class TextCleaner {

    public static String extractName(String text) {
        if (text.contains("真实姓名：")) return text.split("真实姓名：")[1].trim();
        if (text.contains("真实姓名:")) return text.split("真实姓名:")[1].trim();
        if (text.contains("姓名：")) return text.split("姓名：")[1].trim();
        if (text.contains("姓名:")) return text.split("姓名:")[1].trim();
        if (text.contains("：")) return text.split("：")[1].trim();
        if (text.contains(":")) return text.split(":", 2)[1].trim();
        return text.trim();
    }

    public static String extractDigits(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c) || c == '万' || c == '亿') sb.append(c);
        }
        return sb.toString();
    }

    public static String cleanDouyinId(String text) {
        if (text.contains("抖音号:")) return text.split("抖音号:")[1].trim();
        if (text.contains("抖音号：")) return text.split("抖音号：")[1].trim();
        if (text.contains("抖音ID:")) return text.split("抖音ID:")[1].trim();
        if (text.contains("抖音ID：")) return text.split("抖音ID：")[1].trim();
        if (text.contains("ID:") && !text.contains("身份证")) return text.split("ID:")[1].trim();
        if (text.contains("ID：") && !text.contains("身份证")) return text.split("ID：")[1].trim();
        if (text.contains(":")) return text.split(":", 2)[1].trim();
        if (text.contains("：")) return text.split("：", 2)[1].trim();
        return text.trim();
    }

}
