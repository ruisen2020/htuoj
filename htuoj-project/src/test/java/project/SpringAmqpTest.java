//package project;
//
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//
//import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
//import org.apache.commons.io.IOUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.example.htuoj.project.HtuOJApplication;
//
//import org.example.htuoj.common.dao.Problem;
//import org.example.htuoj.common.dao.ProblemSourceRelations;
//import org.example.htuoj.common.dao.Sample;
//import org.example.htuoj.common.dao.Source;
//import org.example.htuoj.common.mapper.ProblemMapper;
//import org.example.htuoj.common.mapper.ProblemSourceRelationsMapper;
//import org.example.htuoj.common.mapper.SampleMapper;
//import org.example.htuoj.common.mapper.SourceMapper;
//import org.example.htuoj.project.utils.MailUtils;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.zip.ZipEntry;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//@SpringBootTest(classes = HtuOJApplication.class)
//public class SpringAmqpTest {
//
//    @Autowired
//    private ProblemMapper problemMapper;
//    @Autowired
//    private SampleMapper sampleMapper;
//    @Autowired
//    private SourceMapper sourceMapper;
//
//    @Autowired
//    private ProblemSourceRelationsMapper problemSourceRelationsMapper;
//
//    @Test
//    public void test() throws IOException {
//        for (int i = 121; i <= 150; i++) {
//            String taskUrl = "https://atcoder.jp/contests/abc" + i + "/tasks";
//            URL url1 = new URL(taskUrl);
//            // 打开连接
//            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
//            conn1.setRequestMethod("GET");
//            conn1.setRequestProperty("priority", "u=0, i");
//            conn1.setRequestProperty("sec-fetch-user", "?1");
//            conn1.setRequestProperty("upgrade-insecure-requests", "1");
//            conn1.setRequestProperty("Cookie", "_ga=GA1.1.1314208303.1729154502; REVEL_FLASH=; timeDelta=51; OJB_Session_ojb_updateL10nWebsiteJson_zh=true; REVEL_SESSION=60fda2db086b3ac22d31e22a80627c2e508f9c17-%00a%3Afalse%00%00w%3Afalse%00%00_TS%3A1748226149%00%00csrf_token%3A%2BgvYLRXnVxcX8i2Da%2BKqiJajXaLE1CdGttvZvQ46M3M%3D%00%00SessionKey%3A09d1a2d43fe5d4e7e941fcd6f999f6d492f0dde800b03a1a90ea527962ac7a99c376a88cea3424-c6e79a308e46a57c7617c99cda886915268c3af806c07ff11a5756f856f117c9%00%00UserScreenName%3Axiang18%00%00UserName%3Axiang18%00; _ga_RC512FD18N=GS1.1.1732667989.17.1.1732674148.0.0.0");
//            conn1.setRequestProperty("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
//            conn1.setRequestProperty("Accept", "*/*");
//            conn1.setRequestProperty("Host", "atcoder.jp");
//            conn1.setRequestProperty("Connection", "keep-alive");
//
//            // 获取响应码
//            int responseCode1 = conn1.getResponseCode();
//            List<String> list = new ArrayList<>();
//            if (responseCode1 == HttpURLConnection.HTTP_OK) {
//                InputStream inputStream = conn1.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }
//                reader.close();
//                inputStream.close();
//                String html = stringBuilder.toString().replace("\\n", "\n");
//                System.out.println(html);
//                Pattern Pattern1 = Pattern.compile("<td class=\"text-center no-break\"><a href=\"(.*?)\">", Pattern.DOTALL);
//                Matcher Matcher1 = Pattern1.matcher(html);
//                while (Matcher1.find()) {
//                    String group = Matcher1.group(1);
//                    System.out.println(group);
//                    list.add(group);
//                }
//            } else {
//                System.out.println("ABCDE");
//                continue;
//            }
//            for (int j = 0; j < list.size(); j++) {
//                String index = null;
//                if (j == 0)
//                    index = "a";
//                else if (j == 1)
//                    index = "b";
//                else if (j == 2)
//                    index = "c";
//                else if (j == 3)
//                    index = "d";
//                else
//                    index = "e";
//                try {
//                    // 创建URL对象
//                    URL url = new URL("https://atcoder.jp" + list.get(j));
//                    // 打开连接
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("priority", "u=0, i");
//                    conn.setRequestProperty("sec-fetch-user", "?1");
//                    conn.setRequestProperty("upgrade-insecure-requests", "1");
//                    conn.setRequestProperty("Cookie", "_ga=GA1.1.1314208303.1729154502; REVEL_FLASH=; timeDelta=51; OJB_Session_ojb_updateL10nWebsiteJson_zh=true; REVEL_SESSION=60fda2db086b3ac22d31e22a80627c2e508f9c17-%00a%3Afalse%00%00w%3Afalse%00%00_TS%3A1748226149%00%00csrf_token%3A%2BgvYLRXnVxcX8i2Da%2BKqiJajXaLE1CdGttvZvQ46M3M%3D%00%00SessionKey%3A09d1a2d43fe5d4e7e941fcd6f999f6d492f0dde800b03a1a90ea527962ac7a99c376a88cea3424-c6e79a308e46a57c7617c99cda886915268c3af806c07ff11a5756f856f117c9%00%00UserScreenName%3Axiang18%00%00UserName%3Axiang18%00; _ga_RC512FD18N=GS1.1.1732667989.17.1.1732674148.0.0.0");
//                    conn.setRequestProperty("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
//                    conn.setRequestProperty("Accept", "*/*");
//                    conn.setRequestProperty("Host", "atcoder.jp");
//                    conn.setRequestProperty("Connection", "keep-alive");
//
//                    // 获取响应码
//                    int responseCode = conn.getResponseCode();
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                        // 获取输入流并读取数据
//                        InputStream inputStream = conn.getInputStream();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//                        StringBuilder stringBuilder = new StringBuilder();
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            stringBuilder.append(line).append("\n");
//                        }
//                        reader.close();
//                        inputStream.close();
//                        Problem problem = new Problem();
//                        if (index.equals("a")) {
//                            problem.setDifficultyLevel(0);
//                        } else if (index.equals("b") || index.equals("c")) {
//                            problem.setDifficultyLevel(1);
//                        } else if (index.equals("d") || index.equals("e")) {
//                            problem.setDifficultyLevel(2);
//                        }
//                        // 将读取到的数据转换为字符串
//                        String html = stringBuilder.toString().replace("\\n", "\n");
//                        System.out.println(html);
//                        // 题目描述
//                        Pattern problemTitlePattern = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
//                        Matcher problemTitleMatcher = problemTitlePattern.matcher(html);
//                        if (problemTitleMatcher.find()) {
//                            String group = problemTitleMatcher.group(1);
//                            String[] split = group.split(" - ");
//                            String title = "[ABC" + i + split[0] + "] " + split[1];
//                            problem.setTitle(title);
//                        }
//                        // 题目描述
//                        Pattern problemStatementPattern = Pattern.compile("<h3>Problem Statement</h3>(.*?)</section>", Pattern.DOTALL);
//                        Matcher problemStatementMatcher = problemStatementPattern.matcher(html);
//                        if (problemStatementMatcher.find()) {
//                            String content = problemStatementMatcher.group(1).replace("<var>", "$").replace("</var>", "$");
//                            problem.setContent(content);
//                        }
//                        // 时间
//                        Pattern timePattern = Pattern.compile("Time Limit: (.*?) sec", Pattern.DOTALL);
//                        Matcher timeMatcher = timePattern.matcher(html);
//                        if (timeMatcher.find()) {
//                            String time = timeMatcher.group(1);
//                            problem.setTimeLimit(Integer.parseInt(time) * 1000);
//                        }
//                        // 空间
//                        Pattern memoryPattern = Pattern.compile("Memory Limit: (.*?) MB", Pattern.DOTALL);
//                        Matcher memoryMatcher = memoryPattern.matcher(html);
//                        if (memoryMatcher.find()) {
//                            String memory = memoryMatcher.group(1);
//                            problem.setMemoryLimit(Integer.parseInt(memory));
//                        }
//                        // 输入描述
//                        Pattern inputPattern = Pattern.compile("<h3>Input</h3>(.*?)</section>", Pattern.DOTALL);
//                        Matcher inputMatcher = inputPattern.matcher(html);
//                        if (inputMatcher.find()) {
//                            String input = inputMatcher.group(1).replace("<var>", "$").replace("</var>", "$");
//                            problem.setInputDescription(input);
//                        }
//
//                        // 限制因素
//                        Pattern constraintsPattern = Pattern.compile("<h3>Constraints</h3>(.*?)</section>", Pattern.DOTALL);
//                        Matcher constraintsMatcher = constraintsPattern.matcher(html);
//                        if (constraintsMatcher.find()) {
//                            String constraints = constraintsMatcher.group(1).replace("<var>", "$").replace("</var>", "$");
//                            problem.setInputDescription(problem.getInputDescription() + "\n" + constraints);
//                        }
//
//                        // 输出描述
//                        Pattern outputPattern = Pattern.compile("<h3>Output</h3>(.*?)</section>", Pattern.DOTALL);
//                        Matcher outputMatcher = outputPattern.matcher(html);
//                        if (outputMatcher.find()) {
//                            String output = outputMatcher.group(1).replace("<var>", "$").replace("</var>", "$").replace("<br>", "\n");
//                            problem.setOutputDescription(output);
//                        }
//                        problemMapper.insert(problem);
//                        getTestData(i, j, problem.getId());
//                        System.out.println(problem);
//                        ProblemSourceRelations problemSourceRelations = new ProblemSourceRelations();
//                        problemSourceRelations.setProblemId(problem.getId());
//                        problemSourceRelations.setSourceId(3L);
//                        problemSourceRelationsMapper.insert(problemSourceRelations);
//                        // 循环处理示例输入和输出
//                        for (int k = 1; k < 100; k++) {
//                            Sample sample = new Sample();
//                            sample.setProblemId(problem.getId());
//                            String inputRegex = "<h3>Sample Input " + k + "</h3><pre>(.*?)</pre>";
//                            Pattern inputSamplePattern = Pattern.compile(inputRegex, Pattern.DOTALL);
//                            Matcher inputSampleMatcher = inputSamplePattern.matcher(html);
//                            if (!inputSampleMatcher.find()) {
//                                break;
//                            }
//                            String input = inputSampleMatcher.group(1);
//                            sample.setSampleInput(input);
//                            System.out.println("AAA");
//                            System.out.println(input);
//                            String outputRegex = "<h3>Sample Output " + k + "</h3><pre>(.*?)</pre>";
//                            Pattern outputSamplePattern = Pattern.compile(outputRegex, Pattern.DOTALL);
//                            Matcher outputSampleMatcher = outputSamplePattern.matcher(html);
//                            if (outputSampleMatcher.find()) {
//                                String output = outputSampleMatcher.group(1);
//                                sample.setSampleOutput(output);
//                            }
//                            sampleMapper.insert(sample);
//                            System.out.println(sample);
//                        }
//                    } else {
//                        System.out.println("请求失败，响应码: " + responseCode);
//                    }
//                    // 关闭连接
//                    conn.disconnect();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    private void getTestData(int i, int j, Long id) {
//        String index = null;
//        if (j == 0)
//            index = "A";
//        else if (j == 1)
//            index = "B";
//        else if (j == 2)
//            index = "C";
//        else if (j == 3)
//            index = "D";
//        else
//            index = "E";
//        String sourceFolderPath = "D:\\test\\" + i + "\\" + index;
//        String destinationFolderPath = "D:\\test\\case\\" + id;
//
//        copyFolder(sourceFolderPath, destinationFolderPath);
//    }
//
//    private static void downZip(int i) {
//        String downloadUrl = "http://123.158.55.213:8090/atcoder/ABC" + i + ".zip"; // 替换为实际的下载链接
//        String savePath = "D:\\htuoj\\htuoj-project\\src\\test\\java\\project\\test\\" + i + ".zip"; // 替换为本地保存路径
//
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet httpGet = new HttpGet(downloadUrl);
//
//        try {
//            HttpResponse response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//
//            if (entity != null) {
//                InputStream inputStream = entity.getContent();
//                FileOutputStream outputStream = new FileOutputStream(savePath);
//
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//
//                outputStream.close();
//                inputStream.close();
//
//                System.out.println("文件下载成功！");
//            } else {
//                System.out.println("文件下载失败，响应中无实体内容");
//            }
//
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("文件下载失败：" + e.getMessage());
//        }
//
//    }
//
//    public static void copyFolder(String sourceFolderPath, String destinationFolderPath) {
//        File sourceFolder = new File(sourceFolderPath);
//        File destinationFolder = new File(destinationFolderPath);
//
//        // 如果目标文件夹不存在，则创建它
//        if (!destinationFolder.exists()) {
//            destinationFolder.mkdirs();
//        }
//
//        // 遍历源文件夹中的所有文件和子文件夹
//        for (File file : sourceFolder.listFiles()) {
//            if (file.getName().equals("in")) {
//                for (File file1 : file.listFiles()) {
//                    try {
//                        copyFile(file1, new File(destinationFolder, file1.getName() + ".in"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else {
//                for (File file1 : file.listFiles()) {
//                    try {
//                        copyFile(file1, new File(destinationFolder, file1.getName() + ".out"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
////            if (file.isFile()) {
////                try {
////                    // 复制文件
////                    copyFile(file, new File(destinationFolder, file.getName()+""));
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            } else if (file.isDirectory()) {
////                // 如果是子文件夹，递归调用复制文件夹的方法
////                String newSourceFolderPath = file.getPath();
////                String newDestinationFolderPath = destinationFolder.getPath();
////                copyFolder(newSourceFolderPath, newDestinationFolderPath);
////            }
//        }
//    }
//
//    private static String readFileContent(File file) throws IOException {
//        try (FileInputStream fis = new FileInputStream(file)) {
//            // 读取文件内容
//            return IOUtils.toString(fis, StandardCharsets.UTF_8);
//        }
//    }
//
//    public static Map<String, String> readFilesInDirectory(String directoryPath) {
//        // 创建 File 对象
//        File directory = new File(directoryPath);
//        // 检查文件夹是否存在
//        if (directory.exists() && directory.isDirectory()) {
//            // 列出文件夹中的所有文件
//            File[] files = directory.listFiles();
//            if (files != null) {
//                Map<String, String> fileContents = new HashMap<>();
//
//                for (File file : files) {
//                    if (file.isFile()) {
//                        try {
//                            // 读取文件内容
//                            String content = readFileContent(file);
//                            // 将文件名和内容存储到 Map 中
//                            fileContents.put(file.getName(), content);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                return fileContents;
//            } else {
//                System.out.println("文件夹为空");
//            }
//        } else {
//            System.out.println("文件夹不存在");
//        }
//
//        return new HashMap<>();
//    }
//
//    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
//        FileInputStream fis = new FileInputStream(sourceFile);
//        FileOutputStream fos = new FileOutputStream(destinationFile);
//
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = fis.read(buffer)) > 0) {
//            fos.write(buffer, 0, length);
//        }
//
//        fis.close();
//        fos.close();
//
//        System.out.println("已复制文件: " + sourceFile.getName());
//    }
//
//    public static void main(String[] args) {
//        String sourceFolderPath = "D:\\test\\100\\A";
//        String destinationFolderPath = "D:\\test\\case\\1";
//
//        copyFolder(sourceFolderPath, destinationFolderPath);
////        Map<String, String> stringStringMap = readFilesInDirectory("D:\\test\\case\\A");
////        for (String key : stringStringMap.keySet())
////        {
////            System.out.println(key);
////        }
//    }
//
//    private static void unZip(String savePath) {
//        String zipFilePath = savePath; // 替换为实际的ZIP文件路径
//        String destDirectory = "D:\\htuoj\\htuoj-project\\src\\test\\java\\project"; // 替换为解压后的目标目录路径
//        try {
//            // 创建目标目录（如果不存在）
//            File destDir = new File(destDirectory);
//            if (!destDir.exists()) {
//                destDir.mkdirs();
//            }
//
//            // 创建ZipInputStream对象，用于读取ZIP文件内容
//            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
//
//            // 读取ZIP文件中的每个条目
//            ZipEntry entry;
//            while ((entry = zipIn.getNextEntry()) != null) {
//                String entryName = entry.getName();
//                File outputFile = new File(destDir, entryName);
//
//                // 如果条目是一个目录，则创建该目录
//                if (entry.isDirectory()) {
//                    outputFile.mkdirs();
//                } else {
//                    // 如果条目是一个文件，则创建文件并写入数据
//                    FileOutputStream outputStream = new FileOutputStream(outputFile);
//                    byte[] buffer = new byte[1024];
//                    int length;
//                    while ((length = zipIn.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, length);
//                    }
//                    outputStream.close();
//                }
//
//                zipIn.closeEntry();
//            }
//
//            zipIn.close();
//
//            System.out.println("ZIP文件解压成功！");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("ZIP文件解压失败：" + e.getMessage());
//        }
//
//    }
//
////    public static void main(String[] args) {
////        update();
//////        for(int i=100;i<=197;i++)
//////        downZip(i);
//////        unZip("D:\\htuoj\\htuoj-project\\src\\test\\java\\project\\test\\100.zip");
////    }
//
//    private void download(Long id, int i, String index) {
//        String downloadUrl = "http://123.158.55.213:8090/atcoder/ABC" + i + ".zip"; // 替换为实际的下载链接
//        String savePath = "D:\\htuoj\\htuoj-project\\src\\main\\resources\\case"; // 替换为本地保存路径
//
//        try {
//            URL url = new URL(downloadUrl);
//            InputStream inputStream = url.openStream();
//            FileOutputStream outputStream = new FileOutputStream(savePath);
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            outputStream.close();
//            inputStream.close();
//
//            System.out.println("文件下载成功！");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("文件下载失败：" + e.getMessage());
//        }
//    }
//
////    @Test
////    public void test1() {
////        // 文件夹路径
////        String directoryPath = "D:\\htuoj\\htuoj-project\\src\\main\\resources\\python\\输入输出数据";
////
////        // 创建 File 对象
////        File directory = new File(directoryPath);
////        int count = 2;
////        // 检查文件夹是否存在
////        if (directory.exists() && directory.isDirectory()) {
////            // 列出文件夹中的所有文件
////            File[] files = directory.listFiles(File::isDirectory);
////
////            if (files != null) {
////                for (File subdirectory : files) {
////                    // 获取当前子文件夹的名称
////                    String currentName = subdirectory.getName();
////                    // 构建新的文件夹名称
////                    String newName = String.valueOf(count);
////                    // 创建新的文件夹路径
////                    Path newDirectoryPath = Paths.get(subdirectory.getParent(), newName);
////                    try {
////                        // 重命名文件夹
////                        Files.move(subdirectory.toPath(), newDirectoryPath);
////                        System.out.println("文件夹 " + currentName + " 已成功重命名为 " + newName);
////                        count++;
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                        System.out.println("文件夹 " + currentName + " 重命名失败: " + e.getMessage());
////                    }
////                }
////            } else {
////                System.out.println("文件夹为空");
////            }
////        } else {
////            System.out.println("文件夹不存在");
////        }
////
////    }
////
////    @Test
////    public void testSimpleQueue() throws Exception {
////        // 文件夹路径
////        String directoryPath = "D:\\htuoj\\htuoj-project\\src\\main\\resources\\python\\题面";
////
////        // 创建 File 对象
////        File directory = new File(directoryPath);
////
////        // 检查文件夹是否存在
////        if (directory.exists() && directory.isDirectory()) {
////            // 列出文件夹中的所有文件
////            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
////
////            if (files != null) {
////                for (File file : files) {
//////                    System.out.println(file.getName());
////                    Long problemId = fun(file);
//////                    System.out.println(problemId);
//////                    String string = problemId.toString();
//////                    System.out.println(file.toPath());
//////                    System.out.println(file.toPath().getParent());
//////                    Files.move(Paths.get(file.toPath().toUri()), Paths.get(file.toPath().getParent() + "\\" + string + ".json"));
//////                    break;
////                }
////            } else {
////                System.out.println("文件夹为空");
////            }
////        } else {
////            System.out.println("文件夹不存在");
////        }
////
////    }
//
////    private Long fun(File file) {
////
////        try (FileReader reader = new FileReader(file)) {
////            // 读取文件内容到字符串
////            String content = new java.util.Scanner(reader).useDelimiter("\\A").next();
////            // 将字符串转换为JSONObject
////            JSONObject jsonObject = new JSONObject(content);
////            JSONArray tags = (JSONArray) jsonObject.get("tags");
////            // 获取数据
////            JSONObject problem = (JSONObject) jsonObject.get("problem");
////            String title = (String) problem.get("title");
////            String description = (String) problem.get("description");
////            description = description.replace("\\n", "\n");
////            Integer timeLimit = (Integer) problem.get("timeLimit");
////            Integer memoryLimit = (Integer) problem.get("memoryLimit");
////            Integer difficulty = (Integer) problem.get("difficulty");
////            String inputDescription = (String) problem.get("input");
////            inputDescription = inputDescription.replace("\\n", "\n");
////            String outputDescription = (String) problem.get("output");
////            outputDescription = outputDescription.replace("\\n", "\n");
////            String examples = (String) problem.get("examples");
////            String inputString = examples;
////
////            // 提取<input>标签内的内容
////            int inputStart = inputString.indexOf("<input>") + "<input>".length();
////            int inputEnd = inputString.indexOf("</input>");
////            String inputContent = inputString.substring(inputStart, inputEnd);
////            inputContent = inputContent.replace("\\n", "\n");
////            // 提取<output>标签内的内容
////            int outputStart = inputString.indexOf("<output>") + "<output>".length();
////            int outputEnd = inputString.indexOf("</output>");
////            String outputContent = inputString.substring(outputStart, outputEnd);
////            outputContent = outputContent.replace("\\n", "\n");
//////            System.out.println("Output Content: " + outputContent);
////            Problem problem1 = new Problem();
////            problem1.setTitle(title);
////            problem1.setContent(description);
////            problem1.setTimeLimit(timeLimit);
////            problem1.setMemoryLimit(memoryLimit);
////            problem1.setDifficultyLevel(difficulty);
////            problem1.setInputDescription(inputDescription);
////            problem1.setOutputDescription(outputDescription);
////            problemMapper.insert(problem1);
////            Long id = problem1.getId();
////            // 添加样例
////            Sample sample = new Sample();
////            sample.setSampleInput(inputContent);
////            sample.setSampleOutput(outputContent);
////            sample.setProblemId(id);
////            sampleMapper.insert(sample);
////            // 添加来源
////            tags.forEach(tag -> {
////                LambdaQueryWrapper<Source> sourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
////                sourceLambdaQueryWrapper.eq(Source::getSourceName, tag);
////                Source source = sourceMapper.selectOne(sourceLambdaQueryWrapper);
////                if (source == null) {
////                    source = new Source();
////                    source.setSourceName(tag.toString());
////                    sourceMapper.insert(source);
////                }
////                ProblemSourceRelations problemSourceRelations = new ProblemSourceRelations();
////                problemSourceRelations.setProblemId(id);
////                problemSourceRelations.setSourceId(source.getId());
////                problemSourceRelationsMapper.insert(problemSourceRelations);
////            });
////            return id;
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//}