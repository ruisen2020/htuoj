package org.example.htuoj.judge.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.example.htuoj.common.dao.JudgeResult;
import org.example.htuoj.common.dao.Problem;
import org.example.htuoj.common.dao.Submission;
import org.example.htuoj.common.dto.resp.JudgeTestRespDTO;
import org.example.htuoj.common.dto.resp.SampleGetListByProblemIdRespDTO;
import org.example.htuoj.common.utils.ThreadPoolUtils;
import org.python.antlr.op.Sub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

@Component
public class JudgeUtils {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${htuoj.ip}")
    private String ip;

    @Value("${htuoj.path}")
    private String path;


    public static Map<String, String> readFilesInDirectory(String directoryPath) {
        // 创建 File 对象
        File directory = new File(directoryPath);
        // 检查文件夹是否存在
        if (directory.exists() && directory.isDirectory()) {
            // 列出文件夹中的所有文件
            File[] files = directory.listFiles();
            if (files != null) {
                Map<String, String> fileContents = new HashMap<>();

                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            // 读取文件内容
                            String content = readFileContent(file);
                            // 将文件名和内容存储到 Map 中
                            fileContents.put(file.getName(), content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return fileContents;
            } else {
                System.out.println("文件夹为空");
            }
        } else {
            System.out.println("文件夹不存在");
        }

        return new HashMap<>();
    }

    private static String readFileContent(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            // 读取文件内容
            return IOUtils.toString(fis, String.valueOf(StandardCharsets.UTF_8));
        }
    }

    public Submission judgeTask(Submission submission, Problem problem)  {
        Submission result = null;
        try {
            result = this.SubmitTaskThreadPool(new FutureTask<>(() -> {
                return judge(submission, problem);
            }));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Submission judge(Submission submission, Problem problem) throws IOException {
        if (submission.getLanguage() == 2 || submission.getLanguage() == 3) {
            problem.setTimeLimit(problem.getTimeLimit() * 2);
            problem.setMemoryLimit(problem.getMemoryLimit() * 2);
        }
        JSONObject compileResult = compile(submission, problem);
        String status = (String) compileResult.get("status");
        if (!status.equals("Accepted")) {
            submission.setStatus(Judge.getStatusByName(status));
            submission.setTime(null);
            submission.setMemory(null);
            return submission;
        }
        String filedId = null;
        if (submission.getLanguage() == 0) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("main");
        } else if (submission.getLanguage() == 1) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("a");
        } else if (submission.getLanguage() == 2) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("Main.class");
        } else if (submission.getLanguage() == 3) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("main.py");
        }

        // 获取测试样例集合
        String directoryPath = path + submission.getProblemId();
        // 读取文件夹中的所有文件
        Map<String, String> map = readFilesInDirectory(directoryPath);
        System.out.println(directoryPath);
        System.out.println(map);
        if (map.isEmpty()) {
            // 文件出错
            submission.setStatus(11);
            submission.setTime(null);
            submission.setMemory(null);
            return submission;
        }
        HashMap<String, String> testCaseMap = new HashMap<>();
        for (String key : map.keySet()) {
            if (key.contains(".in")) {
                testCaseMap.put(map.get(key), map.get(key.replace(".in", ".out")));
            }
        }
        for (String key : testCaseMap.keySet()) {
            JSONObject result = testCase(submission, problem, key, filedId);
            status = (String) result.get("status");
            if (!status.equals("Accepted")) {
                submission.setStatus(Judge.getStatusByName(status));
                submission.setTime(null);
                submission.setMemory(null);
                restTemplate.delete("http://" + ip + ":5050/file/" + filedId);
                return submission;
            }
            // 比对是否相同
            String output = ((JSONObject) result.get("files")).getStr("stdout");
            String userOutputMd5 = DigestUtils.md5DigestAsHex(rtrim(output).getBytes(StandardCharsets.UTF_8));
            String userOutputMd52 = DigestUtils.md5DigestAsHex(rtrim(testCaseMap.get(key)).getBytes(StandardCharsets.UTF_8));
            if (!userOutputMd5.equals(userOutputMd52)) {
                submission.setStatus(2);
                submission.setTime(null);
                submission.setMemory(null);
                restTemplate.delete("http://" + ip + ":5050/file/" + filedId);
                return submission;
            }
            Long time = Long.valueOf(result.get("time").toString());
            submission.setTime((int) (time / 1000000L));
            Long memory = Long.valueOf(result.get("memory").toString());
            submission.setMemory((int) (memory / 1024));
        }
        System.out.println("delete");
        restTemplate.delete("http://" + ip + ":5050/file/" + filedId);
        submission.setStatus(1);
        return submission;
    }

    protected String rtrim(String value) {
        if (value == null) return null;
        return Pattern.compile("[^\\S\\n]+(?=\\n)").matcher(StrUtil.trimEnd(value)).replaceAll("");
    }

    private JSONObject testCase(Submission submission, Problem problem, String testInput, String filedId) {
        JSONObject cmd = new JSONObject();
        List<String> args = new ArrayList<>();
        if (submission.getLanguage() == 0) {
            args.add("main");
        } else if (submission.getLanguage() == 1) {
            args.add("a");
        } else if (submission.getLanguage() == 2) {
            args.add("/usr/local/jdk1.8.0_371/bin/java");
            args.add("Main");
        } else if (submission.getLanguage() == 3) {
            args.add("/usr/bin/python3");
            args.add("main");
        }

        cmd.set("args", args);
        List<String> envs = new ArrayList<>();
        envs.add("PATH=/usr/bin:/bin");
        cmd.set("env", envs);
        JSONArray COMPILE_FILES = new JSONArray();
        JSONObject content = new JSONObject();
        content.set("content", testInput);

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * 32);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * 32);
        COMPILE_FILES.put(content);
        COMPILE_FILES.put(stdout);
        COMPILE_FILES.put(stderr);
        cmd.set("files", COMPILE_FILES);
        cmd.set("cpuLimit", problem.getTimeLimit() * 1000 * 1000L);
        cmd.set("memoryLimit", problem.getMemoryLimit() * 1024 * 1024L);
        cmd.set("procLimit", 50);
        JSONObject fileId = new JSONObject();
        fileId.set("fileId", filedId);
        JSONObject copyIn = new JSONObject();
        if (submission.getLanguage() == 0) {
            copyIn.set("main", fileId);
        } else if (submission.getLanguage() == 1) {
            copyIn.set("a", fileId);
        } else if (submission.getLanguage() == 2) {
            copyIn.set("Main.class", fileId);
        } else if (submission.getLanguage() == 3) {
            copyIn.set("main", fileId);
        }
        cmd.set("copyIn", copyIn);

        JSONObject param = new JSONObject();
        param.set("cmd", new JSONArray().put(cmd));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(JSONUtil.toJsonStr(param), headers);
        System.out.println(param);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://" + ip + ":5050/run", request, String.class);
        JSONArray result = JSONUtil.parseArray(postForEntity.getBody());
        JSONObject processResult = (JSONObject) result.get(0);
        return processResult;

    }

    private JSONObject compile(Submission submission, Problem problem) {

        JSONObject cmd = new JSONObject();
        List<String> args = new ArrayList<>();

        if (submission.getLanguage() == 0) {
            args.add("/usr/bin/gcc");
            args.add("main.c");
            args.add("-o");
            args.add("main");
        } else if (submission.getLanguage() == 1) {
            args.add("/usr/bin/g++");
            args.add("a.cc");
            args.add("-o");
            args.add("a");
        } else if (submission.getLanguage() == 2) {
            args.add("/usr/local/jdk1.8.0_371/bin/javac");
            args.add("-source");
            args.add("8");
            args.add("Main.java");
        } else if (submission.getLanguage() == 3) {
            args.add("/usr/bin/python3");
            args.add("-m");
            args.add("py_compile");
            args.add("main.py");
        }

        cmd.set("args", args);
        List<String> envs = new ArrayList<>();
        envs.add("PATH=/usr/bin:/bin");
        cmd.set("env", envs);
        JSONArray COMPILE_FILES = new JSONArray();
        JSONObject content = new JSONObject();
        content.set("content", "");
        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * 32);
        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * 32);
        COMPILE_FILES.put(content);
        COMPILE_FILES.put(stdout);
        COMPILE_FILES.put(stderr);
        cmd.set("files", COMPILE_FILES);
        cmd.set("cpuLimit", problem.getTimeLimit() * 10000 * 1000L);
        cmd.set("memoryLimit", problem.getMemoryLimit() * 1024 * 1024L);
        cmd.set("clockLimit", problem.getTimeLimit() * 2 * 1000 * 1000L);
        cmd.set("procLimit", 50);

        JSONObject fileContent = new JSONObject();
        fileContent.set("content", submission.getCode());
        JSONObject copyIn = new JSONObject();
        if (submission.getLanguage() == 0) {
            copyIn.set("main.c", fileContent);
        } else if (submission.getLanguage() == 1) {
            copyIn.set("a.cc", fileContent);
        } else if (submission.getLanguage() == 2) {
            copyIn.set("Main.java", fileContent);
        } else if (submission.getLanguage() == 3) {
            copyIn.set("main.py", fileContent);
        }

        cmd.set("copyIn", copyIn);
        cmd.set("copyOut", new JSONArray().put("stdout").put("stderr"));
        if (submission.getLanguage() == 0) {
            cmd.set("copyOutCached", new JSONArray().put("main"));
        } else if (submission.getLanguage() == 1) {
            cmd.set("copyOutCached", new JSONArray().put("a"));
        } else if (submission.getLanguage() == 2) {
            cmd.set("copyOutCached", new JSONArray().put("Main.class"));
        } else if (submission.getLanguage() == 3) {
            cmd.set("copyOutCached", new JSONArray().put("main.py"));
        }
        JSONObject param = new JSONObject();
        param.set("cmd", new JSONArray().put(cmd));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(JSONUtil.toJsonStr(param), headers);
//            System.out.println(param);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://" + ip + ":5050/run", request, String.class);
        JSONArray result = JSONUtil.parseArray(postForEntity.getBody());
        JSONObject compileResult = (JSONObject) result.get(0);
        return compileResult;
    }

    private Submission SubmitTaskThreadPool(FutureTask<Submission> futureTask) throws ExecutionException, InterruptedException {
        // 提交到线程池进行执行
        ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
//        while (true) {
//            if (futureTask.isDone() && !futureTask.isCancelled()) {
//                // 获取线程返回结果
//                return futureTask.get();
//            } else {
//                Thread.sleep(10); // 避免CPU高速运转，这里休息10毫秒
//            }
//        }
        return futureTask.get();
    }

    public JudgeTestRespDTO testJudge(Submission submission, Problem problem, List<SampleGetListByProblemIdRespDTO> sampleList) throws ExecutionException, InterruptedException {
        JudgeTestRespDTO judgeTestRespDTO = new JudgeTestRespDTO();
        List<JudgeResult> resultList = new ArrayList<>();
        judgeTestRespDTO.setResultList(resultList);
        if (submission.getLanguage() == 2 || submission.getLanguage() == 3) {
            problem.setTimeLimit(problem.getTimeLimit() * 2);
            problem.setMemoryLimit(problem.getMemoryLimit() * 2);
        }
        JSONObject compileResult = compile(submission, problem);
        String status = (String) compileResult.get("status");
        if (!status.equals("Accepted")) {
            for (SampleGetListByProblemIdRespDTO sample : sampleList) {
                JudgeResult judgeResult = new JudgeResult();
                judgeResult.setInput(sample.getInput());
                judgeResult.setOutput(sample.getOutput());
                judgeResult.setStatus(Judge.getStatusByName(status));
                resultList.add(judgeResult);
            }
            return judgeTestRespDTO;
        }
        String filedId = null;
        if (submission.getLanguage() == 0) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("main");
        } else if (submission.getLanguage() == 1) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("a");
        } else if (submission.getLanguage() == 2) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("Main.class");
        } else if (submission.getLanguage() == 3) {
            filedId = ((JSONObject) compileResult.get("fileIds")).getStr("main.py");
        }
        List<FutureTask<JudgeResult>> futureTasks = new ArrayList<>();
        for (SampleGetListByProblemIdRespDTO sample : sampleList) {
            String input = sample.getInput();
            String output = sample.getOutput();
            String filedId2 = filedId;
//            JSONObject result = SubmitTaskThreadPool(new FutureTask<>(() -> {
//                return testCase(submission, problem, input, filedId2);
//            }));
            futureTasks.add(new FutureTask<>(() -> {
                JSONObject result = testCase(submission, problem, input, filedId2);
                return testResult(result, input, output);
            }));
//            JSONObject result = testCase(submission, problem, input, filedId);

        }
        List<JudgeResult> jsonObjects = SubmitBatchTask2ThreadPool(futureTasks);
        judgeTestRespDTO.setResultList(jsonObjects);
        restTemplate.delete("http://" + ip + ":5050/file/" + filedId);
        return judgeTestRespDTO;
//        status = (String) result.get("status");
//        if (!status.equals("Accepted")) {
//            JudgeResult judgeResult = new JudgeResult();
//            judgeResult.setInput(input);
//            judgeResult.setOutput(output);
//            judgeResult.setStatus(Judge.getStatusByName(status));
//            resultList.add(judgeResult);
//            continue;
//        }
//        // 比对是否相同
//        String realOutput = ((JSONObject) result.get("files")).getStr("stdout");
//        String userOutputMd5 = DigestUtils.md5DigestAsHex(rtrim(realOutput).getBytes(StandardCharsets.UTF_8));
//        String userOutputMd52 = DigestUtils.md5DigestAsHex(rtrim(output).getBytes(StandardCharsets.UTF_8));
//        JudgeResult judgeResult = new JudgeResult();
//        judgeResult.setInput(input);
//        judgeResult.setOutput(output);
//        judgeResult.setRealOutput(realOutput);
//        judgeResult.setTime(Long.parseLong(result.get("time").toString()) / 1000000);
//        judgeResult.setMemory(Long.parseLong(result.get("memory").toString()) / 1024);
//        if (!userOutputMd5.equals(userOutputMd52)) {
//            judgeResult.setStatus(2);
//        } else {
//            judgeResult.setStatus(1);
//        }
//        resultList.add(judgeResult);

    }

    private JudgeResult testResult(JSONObject result, String input, String output) {

        String status = (String) result.get("status");
        if (!status.equals("Accepted")) {
            JudgeResult judgeResult = new JudgeResult();
            judgeResult.setInput(input);
            judgeResult.setOutput(output);
            judgeResult.setStatus(Judge.getStatusByName(status));
            return judgeResult;
        }
        // 比对是否相同
        String realOutput = ((JSONObject) result.get("files")).getStr("stdout");
        System.out.println(realOutput);
        String userOutputMd5 = DigestUtils.md5DigestAsHex(rtrim(realOutput).getBytes(StandardCharsets.UTF_8));
        String userOutputMd52 = DigestUtils.md5DigestAsHex(rtrim(output).getBytes(StandardCharsets.UTF_8));
        System.out.println(userOutputMd5);
        System.out.println(userOutputMd52);
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setInput(input);
        judgeResult.setOutput(output);
        judgeResult.setRealOutput(realOutput);
        judgeResult.setTime(Long.parseLong(result.get("time").toString()) / 1000000);
        judgeResult.setMemory(Long.parseLong(result.get("memory").toString()) / 1024);
        if (!userOutputMd5.equals(userOutputMd52)) {
            judgeResult.setStatus(2);
        } else {
            judgeResult.setStatus(1);
        }
        return judgeResult;
    }

    private List<JudgeResult> SubmitBatchTask2ThreadPool(List<FutureTask<JudgeResult>> futureTasks)
            throws InterruptedException, ExecutionException {
        // 提交到线程池进行执行
        for (FutureTask<JudgeResult> futureTask : futureTasks) {
            ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
        }
        List<JudgeResult> result = new LinkedList<>();
        while (futureTasks.size() > 0) {
            Iterator<FutureTask<JudgeResult>> iterable = futureTasks.iterator();
            //遍历一遍
            while (iterable.hasNext()) {
                FutureTask<JudgeResult> future = iterable.next();
                if (future.isDone() && !future.isCancelled()) {
                    // 获取线程返回结果
                    JudgeResult tmp = future.get();
                    result.add(tmp);
                    // 任务完成移除任务
                    iterable.remove();
                } else {
                    Thread.sleep(10); // 避免CPU高速运转，这里休息10毫秒
                }
            }
        }
        return result;
    }
}
