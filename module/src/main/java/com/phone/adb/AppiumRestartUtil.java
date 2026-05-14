package com.phone.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Appium 服务重启工具类（适配抖音爬虫版）
 * 特性：
 * 1. 精准清理 Appium 关联进程（仅杀 Appium Node，保留前端/其他 Node 服务）
 * 2. 适配 Appium 1.22.3 + 抖音爬虫配置（8201端口/绑定设备/跳过检测）
 * 3. 延长启动等待 + 端口重试检测，避免误判
 * 4. 打开全新 CMD 窗口，执行带核心参数的 appium 命令
 * 5. 不影响前端/其他 Node 服务运行
 */
public class AppiumRestartUtil {

    // ========== 适配配置（根据你的环境修改） ==========
    private static final String DEVICE_UDID = "ec86e946"; // 你的设备UDID
    private static final int UIA2_SERVER_PORT = 8201; // 适配爬虫代码的端口
    private static final int APPIUM_MAIN_PORT = 4723; // Appium主端口
    private static final int APPIUM_BOOT_PORT = 4724; // Appium启动端口
    // ========== 基础配置 ==========
    private static final int KILL_WAIT_MS = 3000;
    private static final int START_WAIT_MS = 8000; // 延长到8秒，适配参数加载
    private static final Charset CMD_CHARSET = Charset.forName("GBK");
    private static final int PORT_CHECK_RETRY = 3; // 端口检测重试次数

    /**
     * 重启 Appium 服务（主方法）- 适配抖音爬虫
     */
    public static void restartAppium() throws IOException, InterruptedException {
        System.out.println("===== 开始重启 Appium 服务（适配抖音爬虫） =====");
        System.out.println("📌 绑定设备UDID：" + DEVICE_UDID);
        System.out.println("📌 UIA2服务端口：" + UIA2_SERVER_PORT);

        // 步骤1：精准清理 Appium 关联进程（保留其他 Node 服务）
        closeAllAppiumRelatedProcesses();

        // 步骤2：等待进程完全释放
        System.out.println("等待进程资源释放...");
        Thread.sleep(KILL_WAIT_MS);

        // 步骤3：打开新CMD窗口，执行带核心参数的appium命令（适配1.22.3）
        startAppiumInNewCmdWindow();

        // 步骤4：等待服务加载（延长时间，适配参数初始化）
        System.out.println("等待 Appium 服务加载完成...");
        Thread.sleep(START_WAIT_MS);

        // 步骤5：验证启动状态（检测4723+8201端口）
        checkAppiumPorts();

        System.out.println("===== Appium 服务重启流程完成 =====");
    }

    /**
     * 精准清理所有 Appium 相关进程（核心优化：保留前端 Node 服务）
     */
    private static void closeAllAppiumRelatedProcesses() throws IOException, InterruptedException {
        System.out.println("===== 开始精准清理 Appium 相关进程（保留前端 Node 服务） =====");

        // 1. 关闭 Appium 常用端口占用进程（新增8201端口）
        closeAppiumBySpecificPorts();

        // 2. 仅杀死 Appium 关联的 Node 进程（不杀所有 Node）
        killOnlyAppiumRelatedNodeProcesses();

        // 3. 兜底关闭所有标题含 Appium 的 CMD 窗口
        closeAllAppiumCmdWindows();

        System.out.println("===== Appium 相关进程清理完成 =====");
    }

    /**
     * 仅关闭 Appium 常用端口的进程（新增8201，避免误关其他端口）
     */
    private static void closeAppiumBySpecificPorts() throws IOException, InterruptedException {
        System.out.println("1. 检测并关闭 Appium 常用端口占用进程");
        // 适配爬虫的端口列表（核心：新增8201）
        String[] appiumPorts = {
                String.valueOf(APPIUM_MAIN_PORT),
                String.valueOf(APPIUM_BOOT_PORT),
                String.valueOf(UIA2_SERVER_PORT),
                "8200", "9100"
        };

        for (String port : appiumPorts) {
            String checkPortCmd = String.format("netstat -ano | findstr :%s | findstr LISTENING", port);
            Process portProcess = new ProcessBuilder("cmd.exe", "/c", checkPortCmd)
                    .redirectErrorStream(true)
                    .start();

            StringBuilder portOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(portProcess.getInputStream(), CMD_CHARSET))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    portOutput.append(line).append("\n");
                }
                portProcess.waitFor();
            }

            if (portOutput.toString().isEmpty()) {
                System.out.println("   ℹ️  端口 " + port + " 未被占用，跳过");
                continue;
            }

            // 提取该端口关联的 PID 并杀死
            String[] lines = portOutput.toString().split("\n");
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                String pid = parts[parts.length - 1];
                System.out.println("   找到占用 Appium 端口 " + port + " 的进程 PID：" + pid);
                executeCmdWithLog("taskkill /f /pid " + pid, "杀死端口 " + port + " 进程 PID-" + pid);
            }
        }
    }

    /**
     * 仅杀死 Appium 关联的 Node 进程（保留前端/其他 Node 服务）
     */
    private static void killOnlyAppiumRelatedNodeProcesses() throws IOException, InterruptedException {
        System.out.println("2. 检测并仅杀死 Appium 关联的 Node 进程");
        // 获取所有 Node 进程的命令行和 PID
        String wmicCmd = "wmic process where name='node.exe' get CommandLine,ProcessId /format:list";
        Process wmicProcess = new ProcessBuilder("cmd.exe", "/c", wmicCmd)
                .redirectErrorStream(true)
                .start();

        StringBuilder nodeProcesses = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(wmicProcess.getInputStream(), CMD_CHARSET))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.contains("WMIC.exe")) continue;
                nodeProcesses.append(line).append("\n");
            }
            wmicProcess.waitFor();
        }

        if (nodeProcesses.toString().isEmpty()) {
            System.out.println("   ℹ️  未检测到运行中的 Node 进程");
            return;
        }

        // 解析并筛选 Appium 关联的 Node 进程
        String[] processLines = nodeProcesses.toString().split("\n");
        String currentCmd = "";
        String currentPid = "";

        for (String line : processLines) {
            line = line.trim();
            if (line.startsWith("CommandLine=")) {
                currentCmd = line.substring("CommandLine=".length()).toLowerCase();
            } else if (line.startsWith("ProcessId=")) {
                currentPid = line.substring("ProcessId=".length()).trim();
            }

            // 仅杀死命令行含 appium 的 Node 进程
            if (!currentCmd.isEmpty() && !currentPid.isEmpty()) {
                if (currentCmd.contains("appium") || (currentCmd.contains("main.js") && currentCmd.contains("appium"))) {
                    System.out.println("   找到 Appium 关联 Node 进程 PID：" + currentPid + "，命令行：" + currentCmd);
                    executeCmdWithLog("taskkill /f /pid " + currentPid, "杀死 Appium Node 进程 PID-" + currentPid);
                } else {
                    System.out.println("   非 Appium Node 进程 PID：" + currentPid + "，保留（前端/其他服务）");
                }
                // 重置临时变量
                currentCmd = "";
                currentPid = "";
            }
        }
    }

    /**
     * 关闭所有标题含 Appium 的 CMD 窗口
     */
    private static void closeAllAppiumCmdWindows() throws IOException, InterruptedException {
        System.out.println("3. 关闭所有 Appium 关联 CMD 窗口");
        // 普通窗口
        executeCmdWithLog(
                "taskkill /f /t /fi \"imagename eq cmd.exe\" /fi \"windowtitle eq *Appium*\"",
                "关闭普通 Appium CMD 窗口"
        );
        // 管理员窗口
        executeCmdWithLog(
                "taskkill /f /t /fi \"imagename eq cmd.exe\" /fi \"windowtitle eq 管理员: *Appium*\"",
                "关闭管理员权限 Appium CMD 窗口"
        );
    }

    /**
     * 核心：打开新CMD窗口，执行带爬虫适配参数的appium命令（适配1.22.3）
     */
    private static void startAppiumInNewCmdWindow() throws IOException, InterruptedException {
        // 适配爬虫的核心启动参数（关键：绑定设备+指定端口+跳过检测）
        String appiumArgs = String.format(
                "appium -p %d -bp %d --udid %s --device-name %s --default-capabilities \"{\\\"uiautomator2ServerPort\\\":%d,\\\"skipDeviceInfo\\\":true,\\\"skipGetDevicePixelRatio\\\":true}\" --session-override",
                APPIUM_MAIN_PORT,
                APPIUM_BOOT_PORT,
                DEVICE_UDID,
                DEVICE_UDID,
                UIA2_SERVER_PORT
        );

        // 打开新CMD窗口，标题为 Appium-抖音爬虫，执行命令并保持窗口打开
        String startCmd = String.format("cmd.exe /c start \"Appium-抖音爬虫\" cmd /k \"%s\"", appiumArgs);

        System.out.println("\n📢 执行启动命令：打开新CMD窗口并执行适配参数的appium命令");
        System.out.println("   命令详情：" + startCmd);

        // 启动新窗口（必须管理员权限）
        Process startProcess = new ProcessBuilder("cmd.exe", "/c", startCmd)
                .redirectErrorStream(true)
                .start();

        // 等待新窗口初始化
        Thread.sleep(3000);

        // 检查命令发送状态
        int exitCode;
        try {
            exitCode = startProcess.exitValue();
        } catch (IllegalThreadStateException e) {
            // 进程仍在运行，说明命令发送成功
            exitCode = 0;
        }

        if (exitCode == 0) {
            System.out.println("✅ 新 CMD 窗口已打开，Appium 正在启动...");
            System.out.println("💡 窗口中显示 'Appium REST http interface listener started on 0.0.0.0:4723' 即启动成功");
            System.out.println("💡 关键参数：UDID=" + DEVICE_UDID + "，UIA2端口=" + UIA2_SERVER_PORT);
        } else {
            System.err.println("❌ 新 CMD 窗口启动失败，退出码：" + exitCode);
            System.err.println("⚠️  请确保：1.以管理员身份运行 2.Appium 1.22.3 已安装 3.环境变量配置正常");
        }
    }

    /**
     * 优化：检测4723+8201端口，增加重试，适配爬虫配置
     */
    private static void checkAppiumPorts() throws IOException, InterruptedException {
        System.out.println("\n🔍 验证 Appium 端口状态...");

        // 检测主端口4723
        checkSinglePort(APPIUM_MAIN_PORT, "Appium主端口");
        // 检测UIA2服务端口8201
        checkSinglePort(UIA2_SERVER_PORT, "UIAutomator2服务端口");
    }

    /**
     * 检测单个端口状态（带重试）
     */
    private static void checkSinglePort(int port, String portDesc) throws IOException, InterruptedException {
        System.out.println("\n📌 检测" + portDesc + "(" + port + ")：");
        String checkPortCmd = String.format("netstat -ano | findstr :%d", port);
        boolean portUsed = false;
        StringBuilder portOutput = new StringBuilder();

        for (int i = 1; i <= PORT_CHECK_RETRY; i++) {
            Process portProcess = new ProcessBuilder("cmd.exe", "/c", checkPortCmd)
                    .redirectErrorStream(true)
                    .start();

            portOutput.setLength(0);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(portProcess.getInputStream(), CMD_CHARSET))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    portOutput.append(line).append("\n");
                }
                portProcess.waitFor();
            }

            if (!portOutput.toString().isEmpty()) {
                portUsed = true;
                break;
            }

            // 未检测到端口，重试前等待1秒
            System.out.println("⚠️ 第" + i + "次检测：" + portDesc + "未占用，1秒后重试...");
            Thread.sleep(1000);
        }

        if (portUsed) {
            System.out.println("✅ " + portDesc + "(" + port + ")已被占用（启动成功）：");
            System.out.println(portOutput);
        } else {
            System.err.println("❌ " + portDesc + "(" + port + ")未检测到占用！");
            if (port == UIA2_SERVER_PORT) {
                System.err.println("   ⚠️  " + portDesc + "未占用是正常的（仅在创建会话后绑定），无需担心");
            } else {
                System.err.println("   ⚠️  " + portDesc + "未占用说明Appium启动失败，请检查CMD窗口报错");
            }
        }
    }

    /**
     * 通用 CMD 命令执行方法（带日志）
     */
    private static void executeCmdWithLog(String cmd, String desc) throws IOException, InterruptedException {
        System.out.println("   📝 执行[" + desc + "]：" + cmd);
        Process process = new ProcessBuilder("cmd.exe", "/c", cmd)
                .redirectErrorStream(true)
                .start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), CMD_CHARSET))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("   输出：" + line);
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("   ✅ " + desc + "成功");
            } else {
                System.err.println("   ❌ " + desc + "失败，退出码：" + exitCode);
            }
        }
    }

    // 测试主方法
    public static void main(String[] args) {
        try {
            // ⚠️ 必须以管理员身份运行！
            System.out.println("⚠️  注意：请确保以【管理员身份】运行本程序！");
            Thread.sleep(2000);
            restartAppium();
        } catch (Exception e) {
            System.err.println("重启 Appium 失败：");
            e.printStackTrace();
        }
    }
}