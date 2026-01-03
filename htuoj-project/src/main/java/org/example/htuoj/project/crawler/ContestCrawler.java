package org.example.htuoj.project.crawler;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;
import org.example.htuoj.common.dto.resp.ContestGetContestListByOtherOJRespDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.*;

@Component
public class ContestCrawler {

    @Value("${htuoj.python}")
    private String python;

    @Value("${htuoj.path}")
    private String path;

//    public static void main(String[] args) {
////        System.out.println();
////        int currentYear = LocalDate.now().getYear();
////        int currentMonth = LocalDate.now().getMonthValue();
////        System.out.println(currentYear+" "+currentMonth);
//        for (ContestGetContestListByOtherOJRespDTO contestGetContestListByOtherOJRespDTO : AllContest()) {
//            System.out.println(contestGetContestListByOtherOJRespDTO);
//        }
//
////        System.out.println();
//    }

    public  List<ContestGetContestListByOtherOJRespDTO> getNowCoder(String currentDate) {
        List<ContestGetContestListByOtherOJRespDTO> respDTOS = new ArrayList<>();
        try {
            // 执行Python文件的命令
            ProcessBuilder pb = new ProcessBuilder(python, path + "nowcoderContest.py", currentDate);
            Process process = pb.start();

            // 获取Python文件的输出流
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                // 假设Python输出是JSON格式的字符串，这里将其解析为Java对象
                result.append(line);
            }

            // 关闭流
            br.close();
            is.close();

            JSONObject jsonObject = new JSONObject(result);
//            JSONObject currentData = (JSONObject) jsonObject.get("currentData");
//            JSONObject contests = (JSONObject) currentData.get("contests");
            JSONArray resultArray = jsonObject.getJSONArray("data");
//            System.out.println(resultArray);
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject contestObject = resultArray.getJSONObject(i);
                int id = (int) contestObject.get("contestId");
                String name = (String) contestObject.get("contestName");
                long startTime = contestObject.getLong("startTime") / 1000;
                long endTime = contestObject.getLong("endTime") / 1000;
                int durationSeconds = (int) (endTime - startTime);
                String link = (String) contestObject.get("link");
                Date date = new Date();
//                System.out.println(startTime+" aaaa "+endTime);
                long time = date.getTime();
                if (endTime * 1000 < time) {
                    continue;
                }
                ContestGetContestListByOtherOJRespDTO respDTO = new ContestGetContestListByOtherOJRespDTO();
                respDTO.setUrl(link);
                respDTO.setTitle(name);
                respDTO.setOJ("nowcoder");
                respDTO.setStartTime(new Date(startTime * 1000));
                respDTO.setDuration(durationSeconds);
                respDTOS.add(respDTO);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respDTOS;
    }

    public  List<ContestGetContestListByOtherOJRespDTO> getCf() {
        List<ContestGetContestListByOtherOJRespDTO> respDTOS = new ArrayList<>();
        try {
            // 执行Python文件的命令
            ProcessBuilder pb = new ProcessBuilder(python, path + "cfContest.py");
            Process process = pb.start();

            // 获取Python文件的输出流
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                // 假设Python输出是JSON格式的字符串，这里将其解析为Java对象
                result.append(line);
            }

            // 关闭流
            br.close();
            is.close();

            JSONObject jsonObject = new JSONObject(result);
            JSONArray resultArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject contestObject = resultArray.getJSONObject(i);
                int id = (int) contestObject.get("id");
                String name = (String) contestObject.get("name");
                String type = (String) contestObject.get("type");
                long startTimeSeconds = contestObject.getLong("startTimeSeconds");
                String phase = (String) contestObject.get("phase");
                long relativeTimeSeconds = contestObject.getLong("relativeTimeSeconds");
                int durationSeconds = (int) contestObject.get("durationSeconds");

                if (phase.equals("FINISHED")) {
                    break;
                }
                ContestGetContestListByOtherOJRespDTO respDTO = new ContestGetContestListByOtherOJRespDTO();
                respDTO.setUrl("https://codeforces.com/contest/" + id);
                respDTO.setTitle(name);
                respDTO.setOJ("codeforces");
                respDTO.setStartTime(new Date((startTimeSeconds) * 1000));
                respDTO.setDuration(durationSeconds);
                respDTOS.add(respDTO);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return respDTOS;
    }

    public  List<ContestGetContestListByOtherOJRespDTO> getLeetcode() {
        List<ContestGetContestListByOtherOJRespDTO> respDTOS = new ArrayList<>();
        try {
            // 执行Python文件的命令
            ProcessBuilder pb = new ProcessBuilder(python, path + "leetcodeContest.py");
            Process process = pb.start();

            // 获取Python文件的输出流
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                // 假设Python输出是JSON格式的字符串，这里将其解析为Java对象
                result.append(line);
            }

            // 关闭流
            br.close();
            is.close();

            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray resultArray = data.getJSONArray("contestUpcomingContests");
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject contestObject = resultArray.getJSONObject(i);
                String id = (String) contestObject.get("titleSlug");
                String name = (String) contestObject.get("title");
                long startTime = contestObject.getLong("startTime");
//                long endTime = contestObject.getLong("endTime") / 1000;
                int durationSeconds = (int) contestObject.get("duration");
//                int durationSeconds = (int) (endTime - startTime);
//                Date date = new Date();
//                System.out.println(startTime+" aaaa "+endTime);
//                long time = date.getTime();
//                if (endTime * 1000 < time) {
//                    continue;
//                }
                ContestGetContestListByOtherOJRespDTO respDTO = new ContestGetContestListByOtherOJRespDTO();
                respDTO.setUrl("https://leetcode.cn/contest/" + id);
                respDTO.setTitle(name);
                respDTO.setOJ("leetcode");
                respDTO.setStartTime(new Date(startTime * 1000));
                respDTO.setDuration(durationSeconds);
                respDTOS.add(respDTO);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return respDTOS;
    }

    public  List<ContestGetContestListByOtherOJRespDTO> AllContest() {
        List<ContestGetContestListByOtherOJRespDTO> respDTOS = new ArrayList<>();

        // 获取当前年份和月份
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        String currentDate = currentYear + "-" + currentMonth;
        List<ContestGetContestListByOtherOJRespDTO> nowCoder = getNowCoder(currentDate);
        respDTOS.addAll(nowCoder);
        // 下一个月
        //  当前月的下一个月的时间
        LocalDate currentDate1 = LocalDate.now();
        LocalDate nextMonthDate = currentDate1.plusMonths(1);
        String nextMonth = nextMonthDate.getYear() + "-" + nextMonthDate.getMonthValue();
        List<ContestGetContestListByOtherOJRespDTO> nowCoder1 = getNowCoder(nextMonth);
        respDTOS.addAll(nowCoder1);

        List<ContestGetContestListByOtherOJRespDTO> cf = getCf();
        respDTOS.addAll(cf);

        List<ContestGetContestListByOtherOJRespDTO> acwing = getAcwing();
        respDTOS.addAll(acwing);

        List<ContestGetContestListByOtherOJRespDTO> leetcode = getLeetcode();
        respDTOS.addAll(leetcode);
//        System.out.println(respDTOS);
        return respDTOS;
    }

    private  List<ContestGetContestListByOtherOJRespDTO> getAcwing() {
        List<ContestGetContestListByOtherOJRespDTO> respDTOS = new ArrayList<>();
        try {
            // 执行Python文件的命令

            ProcessBuilder pb = new ProcessBuilder(python, path + "acwingContest.py");

            Process process = pb.start();

            // 获取Python文件的输出流
            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                // 假设Python输出是JSON格式的字符串，这里将其解析为Java对象
                result.append(line);
//                System.out.println(line);
            }

            // 关闭流
            br.close();
            is.close();
//            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
//            System.out.println(jsonObject.toString());
            JSONArray resultArray = jsonObject.getJSONArray("contests");
//            System.out.println(resultArray);
            for (int i = 0; i < resultArray.size(); i++) {
                JSONObject contestObject = resultArray.getJSONObject(i);
                String title = (String) contestObject.get("title");
                String OJ = "acwing";
                String startTime = (String) contestObject.get("start_time");
                int durationSeconds = 75 * 60;
                String url = "https://www.acwing.com/" + contestObject.get("link");
                ContestGetContestListByOtherOJRespDTO respDTO = new ContestGetContestListByOtherOJRespDTO();
                respDTO.setUrl(url);
                respDTO.setTitle(title);
                respDTO.setOJ(OJ);
                respDTO.setStartTime(new Date(parseDateTimeToSeconds(startTime) * 1000));
                respDTO.setDuration(durationSeconds);
                respDTOS.add(respDTO);
//                System.out.println(respDTO);
            }
            Collections.sort(respDTOS, new Comparator<ContestGetContestListByOtherOJRespDTO>() {
                @Override
                public int compare(ContestGetContestListByOtherOJRespDTO o1, ContestGetContestListByOtherOJRespDTO o2) {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respDTOS;
    }


    public  long parseDateTimeToSeconds(String dateTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = sdf.parse(dateTimeStr);
            return date.getTime() / 1000; // 转换为秒
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // 或者抛出异常，根据需求处理
        }
    }
}
