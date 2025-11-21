package com.phone.adb;


public class XPathRegistry {

    public static final String[] NICKNAME_XPATHS = {
            "//*[@resource-id='com.ss.android.ugc.aweme:id/sga']"
    };

    public static final String[] DOUYIN_ID_XPATHS = {
            "//*[@resource-id='com.ss.android.ugc.aweme:id/4bn']"
    };

    public static final String[] FOLLOW_COUNT_XPATHS = {
            "//*[@resource-id='com.ss.android.ugc.aweme:id/4ml']",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/dwc']//android.widget.TextView[1]",
            "//*[contains(@text, '关注')]/preceding-sibling::android.widget.TextView[1]",
            "//*[contains(@text, '关注')]/following-sibling::android.widget.TextView[1]",
            "//*[contains(@resource-id, 'follow') and not(contains(@resource-id, 'button'))]",
            "//android.widget.TextView[contains(@resource-id, 'follow')][position()=1]",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/f75']"
    };

    public static final String[] FANS_COUNT_XPATHS = {
            "//*[@resource-id='com.ss.android.ugc.aweme:id/4mp']",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/dwc']//android.widget.TextView[3]",
            "//*[contains(@text, '粉丝')]/preceding-sibling::android.widget.TextView[1]",
            "//*[contains(@text, '粉丝')]/following-sibling::android.widget.TextView[1]",
            "//*[contains(@resource-id, 'fans')]",
            "//android.widget.TextView[contains(@resource-id, 'fans')][position()=1]",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/f77']"
    };

    public static final String[] LIKES_COUNT_XPATHS = {
            "//*[@resource-id='com.ss.android.ugc.aweme:id/4mi']",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/dwc']//android.widget.TextView[5]",
            "//*[contains(@text, '获赞')]/preceding-sibling::android.widget.TextView[1]",
            "//*[contains(@text, '获赞')]/following-sibling::android.widget.TextView[1]",
            "//*[contains(@resource-id, 'likes')]",
            "//android.widget.TextView[contains(@resource-id, 'likes')][position()=1]",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/f79']"
    };

    public static final String[] WORKS_COUNT_XPATHS = {
            "//*[contains(@text, '作品')]",
            "//*[contains(@content-desc, '作品')]",
            "//*[contains(@text, '作品') and not(contains(@resource-id, 'tab'))]/preceding-sibling::android.widget.TextView[1]",
            "//*[contains(@text, '作品') and not(contains(@resource-id, 'tab'))]/following-sibling::android.widget.TextView[1]",
            "//*[contains(@resource-id, 'works') and not(contains(@resource-id, 'tab'))]",
            "//*[contains(@resource-id, 'video') and not(contains(@resource-id, 'tab'))]",
            "//*[@resource-id='com.ss.android.ugc.aweme:id/f73']",
            "//*[contains(@resource-id, 'tab_title') and contains(@text, '作品')]/../preceding-sibling::*[1]",
            "//*[@text='作品']/../android.widget.TextView[1]",
            "//*[contains(@text, '作品')]/..//android.widget.TextView[1]",
            "//*[contains(@text, '作品')]/ancestor::*[1]/android.widget.TextView[1]"
    };

    public static final String[] SIGNATURE_XPATHS = {
            "//*[contains(@text, '关注')]/ancestor::*[3]/following-sibling::*[1]//android.widget.TextView",
            "//*[contains(@text, '粉丝')]/ancestor::*[3]/following-sibling::*[1]//android.widget.TextView",
            "//*[contains(@text, '获赞')]/ancestor::*[3]/following-sibling::*[1]//android.widget.TextView",
            "//*[contains(@text, '抖音号')]/following-sibling::android.widget.TextView",
            "//android.widget.TextView[@enabled='true' and @clickable='false']"
    };

    public static final String[] REGION_XPATHS = {
            "//*[contains(@text, '国')]/following-sibling::*",
            "//*[contains(@resource-id, 'region')]",
            "//*[contains(@content-desc, '地区')]",
            "//*[contains(@text, 'IP:')]",
            "//*[contains(@text, 'IP：')]"
    };

    public static final String[] IP_XPATHS = {
            "//*[contains(@text, 'IP：')]",
            "//*[contains(@content-desc, 'IP：')]",
            "//*[contains(@text, 'IP:')]",
            "//*[contains(@content-desc, 'IP:')]",
            "//android.widget.FrameLayout[contains(@content-desc, 'IP')]",
            "//*[contains(@text, 'IP')]",
            "//*[contains(@content-desc, 'IP')]"
    };

    public static final String[] GENDER_XPATHS = {
            "//*[contains(@text, '男') or contains(@text, '女')]",
            "//*[contains(@resource-id, 'gender')]",
            "//*[contains(@content-desc, '性别')]"
    };

    public static final String[] AGE_XPATHS = {
            "//*[contains(@text, '岁')]",
            "//*[contains(@resource-id, 'age')]",
            "//*[contains(@content-desc, '年龄')]",
            "//*[contains(@text, '00后') or contains(@text, '90后') or contains(@text, '80后')]"
    };

    public static final String[] SCHOOL_XPATHS = {
            "//*[contains(@resource-id, 'school')]",
            "//*[contains(@content-desc, '学校')]",
            "//*[contains(@text, '大学') or contains(@text, '学院') or contains(@text, '学校')]"
    };

}
