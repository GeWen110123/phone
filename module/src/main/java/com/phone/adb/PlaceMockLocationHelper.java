package com.phone.adb;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlaceMockLocationHelper {

    private static final Logger logger = Logger.getLogger(PlaceMockLocationHelper.class.getName());

    public static final String PACKAGE_NAME = "com.ikun.placemock";
    public static final String MAIN_ACTIVITY = "com.ikun.placemock.MainActivity";
    public static final String APK_PATH = "D:\\IdeaProjects\\phone0521\\admin\\src\\main\\resources\\PlaceMockLocation-debug.apk";

    private static final String WINDOWS_ADB_PATH = "C:\\Android\\sdk\\platform-tools\\adb.exe";

    private PlaceMockLocationHelper() {
    }

    public static void ensureInstalledOnConnectedDevices() {
        List<String> devices = listConnectedDevices();
        if (devices.isEmpty()) {
            logger.warning("未检测到已连接的 Android 设备，跳过全国模拟定位安装检查");
            return;
        }

        for (String devId : devices) {
            ensureInstalled(devId);
        }
    }

    public static boolean ensureInstalled(String devId) {
        if (isInstalled(devId)) {
            logger.info("设备 " + devId + " 已安装全国模拟定位");
            return true;
        }

        File apk = new File(APK_PATH);
        if (!apk.exists()) {
            logger.severe("全国模拟定位 APK 不存在: " + APK_PATH);
            return false;
        }

        logger.info("设备 " + devId + " 未安装全国模拟定位，开始安装: " + APK_PATH);
        CommandResult result = runCommand(120, adb(), "-s", devId, "install", "-r", "-g", APK_PATH);
        if (result.success() && isInstalled(devId)) {
            logger.info("设备 " + devId + " 全国模拟定位安装成功");
            return true;
        }

        logger.severe("设备 " + devId + " 全国模拟定位安装失败: " + result.output);
        return false;
    }

    public static boolean configureMockLocationApp(String devId) {
        if (!ensureInstalled(devId)) {
            return false;
        }

        runCommand(20, adb(), "-s", devId, "shell", "settings", "put", "global", "development_settings_enabled", "1");
        runCommand(20, adb(), "-s", devId, "shell", "settings", "put", "secure", "mock_location", "1");
        runCommand(20, adb(), "-s", devId, "shell", "appops", "set", PACKAGE_NAME, "android:mock_location", "allow");
        CommandResult putResult = runCommand(20, adb(), "-s", devId, "shell", "settings", "put", "secure", "mock_location_app", PACKAGE_NAME);
        CommandResult getResult = runCommand(20, adb(), "-s", devId, "shell", "settings", "get", "secure", "mock_location_app");

        if (getResult.output != null && getResult.output.trim().contains(PACKAGE_NAME)) {
            logger.info("设备 " + devId + " 已选择全国模拟定位为模拟位置信息应用");
            return true;
        }

        logger.warning("设备 " + devId + " 设置模拟位置信息应用可能失败: " + putResult.output + " " + getResult.output);
        return putResult.success();
    }

    public static boolean isInstalled(String devId) {
        CommandResult result = runCommand(20, adb(), "-s", devId, "shell", "pm", "path", PACKAGE_NAME);
        return result.success() && result.output != null && result.output.contains(PACKAGE_NAME);
    }

    public static List<String> listConnectedDevices() {
        CommandResult result = runCommand(20, adb(), "devices");
        List<String> devices = new ArrayList<>();
        if (!result.success() || result.output == null) {
            logger.warning("读取 adb devices 失败: " + result.output);
            return devices;
        }

        String[] lines = result.output.split("\\r?\\n");
        for (String line : lines) {
            String value = line == null ? "" : line.trim();
            if (value.endsWith("\tdevice")) {
                devices.add(value.substring(0, value.indexOf('\t')).trim());
            }
        }
        return devices;
    }

    private static String adb() {
        File adb = new File(WINDOWS_ADB_PATH);
        return adb.exists() ? WINDOWS_ADB_PATH : "adb";
    }

    private static CommandResult runCommand(long timeoutSeconds, String... command) {
        Process process = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            process = builder.start();

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new CommandResult(-1, "command timeout: " + Arrays.toString(command));
            }

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            return new CommandResult(process.exitValue(), output.toString());
        } catch (Exception e) {
            return new CommandResult(-1, e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private static class CommandResult {
        private final int exitCode;
        private final String output;

        private CommandResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        private boolean success() {
            return exitCode == 0;
        }
    }
}
