package com.disney.teams.utils;

import com.disney.teams.utils.type.StringUtils;

/**
 * @author arron.zhou
 * @version 1.0.0
 * @date 2022/12/17
 * Description:
 * Modification  History:
 * Date         Author        Version        Description
 * ------------------------------------------------------
 * 2022/12/17       arron.zhou      1.0.0          create
 */
public class TerminalUtils {

    public enum TerminalType {
        WEB(0, "WEB", null),
        IOS(1, "IOS", "iOS"),
        ANDROID(2, "ANDROID", "Android"),
        PLUGIN(3, "PLUGIN", "plugin"),
        WAP(4, "WAP", "Mobile"),
        WX(5, "WX", "WXBrowser"),
        WX_APPLET(6, "WX_APPLET", "WXApplet");

        TerminalType(int type, String descr, String... keywords) {
            this.type = type;
            this.descr = descr;
            this.keywords = (keywords == null ? new String[0] : keywords);
        }

        private final int type;
        private final String descr;
        private final String[] keywords;

        public boolean isApp() {
            return type == IOS.type || type == ANDROID.type;
        }

        public boolean isWap() {
            return type == WAP.type || type == WX.type;
        }

        public String getDescr() {
            return descr;
        }

        public String descr() {
            return descr;
        }

        public int getType() {
            return type;
        }

        public int type() {
            return type;
        }

        public static TerminalType fromType(Integer type) {
            return type == null ? TerminalType.WEB : fromType(type.intValue());
        }

        public static TerminalType fromType(int type) {
            for (TerminalType t : values()) {
                if (t.type == type) {
                    return t;
                }
            }
            return TerminalType.WEB;
        }

        public static TerminalType fromType(String type) {
            if (!StringUtils.isNumber(type)) {
                return TerminalType.WEB;
            }
            return fromType(Integer.parseInt(type));
        }

        /**
         * @param type
         * @return
         * @deprecated {@link #fromType(int)}
         */
        @Deprecated
        public static TerminalType formType(int type) {
            return fromType(type);
        }

        public static TerminalType parseByKeyword(String text) {
            if (StringUtils.isBlank(text)) {
                return WEB;
            }
            for (TerminalType type : TerminalType.values()) {
                for (String keyword : type.keywords) {
                    if (text.contains(keyword)) {
                        return type;
                    }
                }
            }
            return WEB;
        }
    }

    public static class Terminal {
        /**
         * 终端类型 Plugin、Web、APP
         */
        private TerminalType terminal;
        /**
         * 终端版本
         */
        private String version;
        /**
         * 系统版本 仅Android IOS 有效
         */
        private String osVersion;
        /**
         * 应用市场或者捆绑渠道号
         */
        private String channel;
        /**
         * app 终端类型
         */
        private String appType;

        private Terminal() {
            terminal = TerminalType.WEB;
        }

        public TerminalType getTerminal() {
            return terminal;
        }

        public void setTerminal(TerminalType terminal) {
            this.terminal = terminal;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public AppType appType() {
            return AppType.fromType(appType);
        }

        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        /**
         * 当前用户是否为移动终端
         *
         * @return
         */
        public boolean isApp() {
            return terminal == TerminalType.ANDROID
                    || terminal == TerminalType.IOS;
        }

        /**
         * 当前用户是否为WAP终端
         *
         * @return
         */
        public boolean isWap() {
            return terminal == TerminalType.WAP;
        }


        /**
         * 当前用户是否为微信浏览器
         *
         * @return
         */
        public boolean isWx() {
            return terminal == TerminalType.WX;
        }

        public boolean isEqual(TerminalType type) {
            return type == null ? terminal == null : type.equals(terminal);
        }
    }

    /**
     * app类型值
     */
    public enum AppType {
        DISNEY_APP(1, "com.disney", "disney://", "迪士尼");

        private final int value;
        private final String schema;
        private final String type;
        private final String descr;

        AppType(int value, String type, String schema, String descr) {
            this.value = value;
            this.type = type;
            this.schema = schema;
            this.descr = descr;
        }

        public static AppType fromType(String type) {
            if (StringUtils.isBlank(type)) {
                return null;
            }
            AppType[] values = AppType.values();
            for (AppType appType : values) {
                if (type.equals(appType.type)) {
                    return appType;
                }
            }
            //默认海淘1号
            return DISNEY_APP;
        }

        public int value() {
            return value;
        }

        public String schema() {
            return schema;
        }

        public String type() {
            return type;
        }

        public String descr() {
            return descr;
        }
    }

    public static Terminal parse(String userAgent, String referer) {
        Terminal terminal = new Terminal();
        if (StringUtils.isBlank(userAgent)) {
            return terminal;
        }
        userAgent = userAgent.toLowerCase();
//        terminal.channel = RegexUtils.findMatch(userAgent, "appsource:([^\\s;]+).*");
        if (StringUtils.isNotBlank(referer) && referer.startsWith("https://servicewechat.com/")) {
            terminal.terminal = TerminalType.WX_APPLET;
        } else if (userAgent.contains("micromessenger")) {
            if (userAgent.lastIndexOf("miniprogram") > -1) {
                terminal.terminal = TerminalType.WX_APPLET;
            } else {
                terminal.terminal = TerminalType.WX;
            }
        } else {
            //check if is app source by useragent
        }
        if (terminal.osVersion == null) {
            terminal.osVersion = RegexUtils.findMatch(userAgent, "(?:android|ios)[:\\s]*([^\\s;]+)");
        }
        return terminal;
    }

    public static Terminal parse(String userAgent) {
        return parse(userAgent, null);
    }
}

