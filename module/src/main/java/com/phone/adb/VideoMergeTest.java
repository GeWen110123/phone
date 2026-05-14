package com.phone.adb;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 将录制视频合并为一个视频文件
 * */
public class VideoMergeTest {

    // ffmpeg.exe 的绝对路径
    private static final String FFMPEG_PATH = "F:/douyin_output/ffmpeg/bin/ffmpeg.exe";

    // 视频输出文件夹
    private static final String VIDEO_OUTPUT_FOLDER = "F:/douyin_output/video";

    public static void mergeVideoSegments(List<String> segmentFiles, String finalFilename) throws Exception {
        // 临时列表文件
        String listFile = VIDEO_OUTPUT_FOLDER + "/merge_list.txt";

        StringBuilder sb = new StringBuilder();
        for (String seg : segmentFiles) {
            sb.append("file '").append(seg.replace("\\", "/")).append("'\n");
        }
        Files.write(Paths.get(listFile), sb.toString().getBytes(StandardCharsets.UTF_8));

        // 调用 ffmpeg 合并
        Process ffmpeg = new ProcessBuilder(
                FFMPEG_PATH,
                "-f", "concat",
                "-safe", "0",
                "-i", listFile,
                "-c", "copy",
                finalFilename
        ).inheritIO() // 输出 ffmpeg 日志到控制台
                .start();

        ffmpeg.waitFor();
        // 删除所有 part 文件
        for (String seg : segmentFiles) {
            File partFile = new File(seg);
            if (partFile.exists()) {
                if (partFile.delete()) {
                    System.out.println("删除分段文件 → " + seg);
                } else {
                    System.err.println("删除分段文件失败 → " + seg);
                }
            }
        }


        // 删除临时列表文件
        new File(listFile).delete();

    }
}
