package org.example.htuoj.project.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class OJCrawler {


    @Value("${htuoj.python}")
    private String python;

    @Value("${htuoj.path}")
    private String path;

    public Integer getProblemCountByLeetcode(String userName) throws IOException {
        return getInfo(userName, "leetcodeAcCount.py");
    }

    public Integer getProblemCountByLuogu(String userName) throws IOException {
        return getInfo(userName, "luoguAcCount.py");
    }

    public Integer getProblemCountByNowcoder(String userName) throws IOException {
        return getInfo(userName, "nowcoderAcCount.py");
    }

    public Integer getProblemCountByAtCoder(String userName) throws IOException {
        return getInfo(userName, "atcoderAcCount.py");
    }

    public Integer getProblemCountByCodeforces(String userName) throws IOException {
        return getInfo(userName, "cfAcCount.py");
    }


    public Integer getRatingByLeetcode(String userName) throws IOException {
        return getInfo(userName, "LeetcodeRating.py");
    }

    public Integer getRatingByLuogu(String userName) throws IOException {
        return getInfo(userName, "luoguRating.py");
    }

    public Integer getRatingByNowcoder(String userName) throws IOException {
        return getInfo(userName, "nowcoderRating.py");
    }

    public Integer getRatingByAtCoder(String userName) throws IOException {
        return getInfo(userName, "atcoderRating.py");
    }

    public Integer getRatingByCodeforces(String userName) throws IOException {
        return getInfo(userName, "cfRating.py");
    }

    public Integer getRatingByAcWing(String userName) throws IOException {
        return getInfo(userName, "acwingRating.py");
    }

    public Integer getInfo(String userName, String fileName) {
        // 执行Python脚本
        ProcessBuilder processBuilder = new ProcessBuilder(python, path + fileName, userName);
        StringBuilder result = new StringBuilder();
        try {
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println(fileName + " Python script execution failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            // handle exception
        }
        if (result.toString().isEmpty())
            return 0;
        return Integer.parseInt(result.toString());
    }
}

