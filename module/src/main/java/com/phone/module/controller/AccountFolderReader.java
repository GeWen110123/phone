package com.phone.module.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phone.module.domain.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/module/accountReader")
public class AccountFolderReader {

    /**
     * 从文件夹中读取所有 .json 文件并解析为 Account 对象列表（去重 + 自动删除）
     */
    public static List<Account> readAccountsFromFolder(String folderPath) throws Exception {
        Map<String, Account> accountMap = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("文件夹不存在或不是目录: " + folderPath);
        }

        File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("目录下没有找到.json 文件");
            return new ArrayList<>();
        }

        // 按文件名排序（可改为 lastModified）
        Arrays.sort(jsonFiles, Comparator.comparing(File::getName));

        for (File file : jsonFiles) {
            System.out.println("读取文件: " + file.getName());
            boolean parsedSuccessfully = false;

            try {
                ArrayNode array = (ArrayNode) mapper.readTree(file);

                for (JsonNode node : array) {
                    if (!node.has("id")) continue;

                    String douyinId = node.get("id").asText();
                    ((ObjectNode) node).remove("id");

                    Account account = new Account();
                    account.setDouyinId(douyinId);
                    account.setJsonString(mapper.writeValueAsString(node));

                    // 后解析的覆盖前面同 id 数据
                    accountMap.put(douyinId, account);
                }

                parsedSuccessfully = true; // 解析成功

            } catch (Exception e) {
                System.err.println("文件解析失败: " + file.getName() + "：" + e.getMessage());
            }

            // 删除文件（仅在解析成功后）
            if (parsedSuccessfully) {
                if (file.delete()) {
                    System.out.println("删除文件: " + file.getName());
                } else {
                    System.err.println("删除失败: " + file.getName());
                }
            }
        }

        System.out.println("解析成功: " + accountMap.size() + "条数据");
        return new ArrayList<>(accountMap.values());
    }

}
