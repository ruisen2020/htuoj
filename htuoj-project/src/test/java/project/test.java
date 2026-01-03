//package project;
//
//
//import cn.hutool.core.util.RandomUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
//import org.apache.commons.compress.utils.IOUtils;
//
//import org.example.htuoj.project.dao.*;
//import org.example.htuoj.common.dto.req.SubmissionAddReqDTO;
//import org.example.htuoj.project.mapper.*;
//import org.example.htuoj.project.service.IArticleService;
//import org.example.htuoj.project.service.ISubmissionService;
//import org.example.htuoj.project.service.IUserPreferencesService;
//import org.example.htuoj.project.service.IUserService;
//import org.example.htuoj.project.utils.MailUtil;
//import org.example.htuoj.project.utils.OssUtil;
//import org.junit.jupiter.api.Test;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.DigestUtils;
//import org.springframework.web.client.RestTemplate;
//
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//import java.util.zip.ZipInputStream;
//
//@SpringBootTest(classes = HtuOJApplicationMain.class)
//public class test {
//    @Autowired
//    private AwardInfoMapper awardInfoMapper;
//    @Autowired
//    private MailUtil mailUtil;
//    @Autowired
//    private LabelMapper labelMapper;
//
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private IArticleService articleService;
//
//    @Autowired
//    private ArticleMapper articleMapper;
//
//    @Autowired
//    private SubmissionMapper submissionMapper;
//
//    @Autowired
//    private TrainingMapper trainingMapper;
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private ISubmissionService submissionService;
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private LikeMapper likeMapper;
//
//    @Autowired
//    private OssUtil ossUtil;
//
//    @Test
//    public void demo22() {
//        ossUtil.getZipFile("1.zip");
//    }
//
//    @Test
//    public void demo122() throws IOException {
//        HashMap<String, String> map = new HashMap<>();
////        ossUtil.getZipFile("1.zip");
//        InputStream stream = new FileInputStream("2.zip");
//        ZipArchiveInputStream inputStream = new ZipArchiveInputStream(stream);
//        ZipArchiveEntry entry = null;
//        while ((entry = inputStream.getNextZipEntry()) != null) {
//            System.out.println(entry.getName());
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            //读取内容
//            IOUtils.copy(inputStream, outputStream);
//            System.out.println(outputStream.toString());
//            map.put(entry.getName(), outputStream.toString());
//        }
//        inputStream.close();
//        stream.close();
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        for (String key : map.keySet()) {
//            if (key.contains(".in")) {
//                stringStringHashMap.put(map.get(key), map.get(key.replace(".in", ".out")));
//                System.out.println(map.get(key) + ":" + map.get(key.replace(".in", ".out")));
//            }
////            System.out.println(key);
////            System.out.println(map.get(key));
//        }
//    }
//
//    @Test
//    public void demo21() {
//        Like like = new Like();
//        like.setUserId(1L);
//        like.setTargetType(1);
//        like.setTargetId(133L);
//        likeMapper.insert(like);
//        System.out.println(like);
//    }
//
//    @Test
//    public void testJudge2() {
//        JSONObject cmd = new JSONObject();
//        List<String> args = new ArrayList<>();
//        args.add("a");
//        cmd.set("args", args);
//        List<String> envs = new ArrayList<>();
//        envs.add("PATH=/usr/bin:/bin");
//        cmd.set("env", envs);
//        JSONArray COMPILE_FILES = new JSONArray();
//        JSONObject content = new JSONObject();
//        content.set("content", "1 345");
//
//        JSONObject stdout = new JSONObject();
//        stdout.set("name", "stdout");
//        stdout.set("max", 1024 * 1024 * 32);
//
//        JSONObject stderr = new JSONObject();
//        stderr.set("name", "stderr");
//        stderr.set("max", 1024 * 1024 * 32);
//        COMPILE_FILES.put(content);
//        COMPILE_FILES.put(stdout);
//        COMPILE_FILES.put(stderr);
//        cmd.set("files", COMPILE_FILES);
//        cmd.set("cpuLimit", 10000000000L);
//        cmd.set("memoryLimit", 104857600);
//        cmd.set("procLimit", 50);
//
//        JSONObject fileId = new JSONObject();
//        fileId.set("fileId", "PFM5VVJW");
//        JSONObject copyIn = new JSONObject();
//        copyIn.set("a", fileId);
//        cmd.set("copyIn", copyIn);
//
//        JSONObject param = new JSONObject();
//        param.set("cmd", new JSONArray().put(cmd));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> request = new HttpEntity<>(JSONUtil.toJsonStr(param), headers);
//        System.out.println(param);
//        ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://117.72.110.81:5050/run", request, String.class);
//        JSONArray result = JSONUtil.parseArray(postForEntity.getBody());
//        JSONObject compileResult = (JSONObject) result.get(0);
//        System.out.println(compileResult);
//        String status = (String) compileResult.get("status");
//        System.out.println(status);
//
//        String output = ((JSONObject) compileResult.get("files")).getStr("stdout");
//        System.out.println(output);
//        String userOutputMd5 = DigestUtils.md5DigestAsHex(rtrim(output).getBytes(StandardCharsets.UTF_8));
//        System.out.println(userOutputMd5);
//        String userOutputMd52 = DigestUtils.md5DigestAsHex(rtrim("346").getBytes(StandardCharsets.UTF_8));
//        System.out.println(userOutputMd52);
//    }
//
//    protected static String rtrim(String value) {
//        if (value == null) return null;
//        return Pattern.compile("[^\\S\\n]+(?=\\n)").matcher(StrUtil.trimEnd(value)).replaceAll("");
//    }
//
//    public static void main(String[] args) {
//        String userOutputMd5 = DigestUtils.md5DigestAsHex(rtrim("2").getBytes(StandardCharsets.UTF_8));
//        System.out.println(userOutputMd5);
//    }
//
//    @Test
//    public void testJudge() {
//        JSONObject cmd = new JSONObject();
//        List<String> args = new ArrayList<>();
//        args.add("/usr/bin/g++");
//        args.add("a.cc");
//        args.add("-o");
//        args.add("a");
//        cmd.set("args", args);
//        List<String> envs = new ArrayList<>();
//        envs.add("PATH=/usr/bin:/bin");
//        cmd.set("env", envs);
//        JSONArray COMPILE_FILES = new JSONArray();
//        JSONObject content = new JSONObject();
//        content.set("content", "");
//
//        JSONObject stdout = new JSONObject();
//        stdout.set("name", "stdout");
//        stdout.set("max", 1024 * 1024 * 32);
//
//        JSONObject stderr = new JSONObject();
//        stderr.set("name", "stderr");
//        stderr.set("max", 1024 * 1024 * 32);
//        COMPILE_FILES.put(content);
//        COMPILE_FILES.put(stdout);
//        COMPILE_FILES.put(stderr);
//        cmd.set("files", COMPILE_FILES);
//        cmd.set("cpuLimit", 10000000000L);
//        cmd.set("memoryLimit", 104857600);
//        cmd.set("procLimit", 10);
//
//        JSONObject fileContent = new JSONObject();
//        fileContent.set("content", "#include <iostream>\nusing namespace std;\nint main() {\nint a, b;\ncin >> a >> b;\ncout << a + b << endl;\n}");
//        JSONObject copyIn = new JSONObject();
//        copyIn.set("a.cc", fileContent);
//        cmd.set("copyIn", copyIn);
//        cmd.set("copyOut", new JSONArray().put("stdout").put("stderr"));
//        cmd.set("copyOutCached", new JSONArray().put("a"));
//        JSONObject param = new JSONObject();
//        param.set("cmd", new JSONArray().put(cmd));
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> request = new HttpEntity<>(JSONUtil.toJsonStr(param), headers);
//        System.out.println(param);
//        ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://117.72.110.81:5050/run", request, String.class);
//        JSONArray result = JSONUtil.parseArray(postForEntity.getBody());
//        JSONObject compileResult = (JSONObject) result.get(0);
//        String fileId = ((JSONObject) compileResult.get("fileIds")).getStr("a");
//        System.out.println(fileId);
//
//    }
//
//    @Test
//    public void testSimpleQueue() {
//        SubmissionAddReqDTO submissionAddReqDTO = new SubmissionAddReqDTO();
//        submissionAddReqDTO.setCode("#include <iostream>\\nusing namespace std;\\nint main() {\\nint a, b;\\ncin >> a >> b;\\ncout << a + b << endl;\\n}");
//        submissionAddReqDTO.setLanguage(1);
//        submissionAddReqDTO.setProblemId(1L);
//        submissionAddReqDTO.setUserId(1L);
//
//
//        submissionService.addSubmission(submissionAddReqDTO);
//    }
//
//    @Test
//    public void demo1() {
//        Long userIdByNumber = userService.getUserIdByNumber("2028224186");
//        System.out.println(userIdByNumber);
//    }
//
//
//    @Test
//    public void demo() {
//        AwardInfo awardInfoDO = new AwardInfo();
//        awardInfoDO.setAwardTime("2023-06");
//        awardInfoDO.setAwardLevel("国家级一等奖");
//        awardInfoDO.setCompetitionName("第十五届蓝桥杯全国软件和信息技术专业人才大赛");
//        awardInfoDO.setStudentCollege("软件学院");
//        awardInfoDO.setStudentName("高新想");
//        int insert = awardInfoMapper.insert(awardInfoDO);
//        System.out.println(insert);
//    }
//
//    @Test
//    public void demo3() {
////        String[] labels = LabelCollection.labels;
////        for (String label : labels) {
////            Label algorithmLabel = new Label();
////            algorithmLabel.setLabelName(label);
////            labelMapper.insert(algorithmLabel);
////        }
//        List<Label> labels = labelMapper.selectList(null);
//        for (Label label : labels) {
//            label.setCreateTime(new Date());
//            label.setUpdateTime(new Date());
//            label.setDelFlag(0);
//            labelMapper.updateById(label);
//        }
////        System.out.println(labels);
//    }
//
//    @Test
//    public void demo4() {
//        String name = "ruisen18";
//        String rating = getRating(name);
//        System.out.println(rating);
//    }
//
//    public static String getData(String baseurl) {
//        try {
//            URL url = new URL(baseurl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36 SLBrowser/7.0.0.6241 SLBChan/103");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                return response.toString();
//            } else {
//                System.out.println("HTTP Error code: " + responseCode);
//                return "Error";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error";
//        }
//    }
//
//    public static Integer myRe(String s) {
//        Pattern pattern = Pattern.compile("\"rating\":(\\d+)");
//        Matcher matcher = pattern.matcher(s);
//
//        if (matcher.find()) {
//            return Integer.parseInt(matcher.group(1));
//        } else {
//            return null;
//        }
//    }
//
//    public static String getRating(String name) {
//        String baseurl = "https://codeforces.com/api/user.info?handles=" + name;
//        String data = getData(baseurl);
//        Integer rating = myRe(data);
//        if (rating == null) {
//            return "None";
//        } else {
//            return String.valueOf(rating);
//        }
//    }
//
//    @Test
//    public void test5() {
//        Mail mail = new Mail();
//        mail.setRecipient("gao484539372@163.com");
//        mail.setSubject("测试发送邮件功能");
//        mail.setContent("Hello World");
//        mailUtil.sendSimpleMail(mail);
//    }
//
//    @Test
//    public void test6() {
//        for (int i = 0; i < 20; i++) {
////            ArticleAddReqDTO articleAddReqDTO = new ArticleAddReqDTO();
////            articleAddReqDTO.setCategoryId(1);
////            articleAddReqDTO.setContent("测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容测试文章内容" + (i + 1));
////            articleAddReqDTO.setTitle("测试文章标题" + i);
////            articleAddReqDTO.setCoverUrl("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
//            Article article = new Article();
////            BeanUtils.copyProperties(articleAddReqDTO, article);
//            article.setCollectCount(RandomUtil.randomInt(1000));
//            article.setLikeCount(RandomUtil.randomInt(1000));
//            article.setCommentCount(RandomUtil.randomInt(1000));
//            article.setWatchCount(RandomUtil.randomInt(1000));
//            article.setUserId(1L);
//            article.setArticleId(i + 1L);
//            articleMapper.updateById(article);
//        }
//
//
//    }
//
//    @Test
//    public void test7() {
//
//        for (int i = 0; i < 100; i++) {
//            Submission submission = new Submission();
//            if (i % 2 == 0)
//                submission.setUserId(1L);
//            else
//                submission.setUserId(2L);
//            submission.setProblemId((long) (i % 10));
//            submission.setProblemTitle("测试提交" + (i + 1));
//            submission.setStatus((1 % 3));
//            submission.setTime(RandomUtil.randomInt(1000));
//            submission.setMemory(RandomUtil.randomInt(1000));
//            submission.setLanguage(RandomUtil.randomInt(5));
//            submission.setCreateTime(new Date());
//            submission.setUpdateTime(new Date());
//            submissionMapper.insert(submission);
//        }
//    }
//
//    @Test
//    public void test8() {
//
//        for (int i = 0; i < 100; i++) {
//            LambdaUpdateWrapper<Training> wrapper = new LambdaUpdateWrapper<>();
//            wrapper.eq(Training::getTrainingId, (long) (i + 1));
//            wrapper.set(Training::getProblemCount, RandomUtil.randomInt(1000));
//            wrapper.set(Training::getCollectCount, RandomUtil.randomInt(1000));
//            wrapper.set(Training::getType, 1);
//            trainingMapper.update(null, wrapper);
//        }
//    }
//
//    @Test
//    public void test10() {
//
//        for (int i = 0; i < 50; i++) {
//            Training training = new Training();
//            training.setType(1);
//            training.setTitle("官方推荐题单" + (i + 1));
//            training.setDescription("官方推荐题单" + (i + 1) + "的描述");
//            training.setProblemCount(RandomUtil.randomInt(1000));
//            training.setCollectCount(RandomUtil.randomInt(1000));
//            training.setUserId(1L);
//            training.setState(0);
//            training.setPriority(i + 1);
//            training.setCreateTime(new Date());
//            training.setUpdateTime(new Date());
//            training.setDelFlag(0);
//            trainingMapper.insert(training);
//        }
//    }
//
//
//    @Autowired
//    private IUserPreferencesService userPreferencesService;
//
//    @Test
//    public void test9() {
//
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setUserName("用户" + i);
//            user.setPassword("123456");
//            user.setNumber("2028" + i);
//            user.setMail("qq@163.com" + i);
//            user.setAvatar("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
//            user.setProfile(" 测试用户" + i + "的简介 ");
//            userService.save(user);
//            Long id = user.getId();
//            UserPreferences userPreferences = new UserPreferences();
//            userPreferences.setUserId(id);
//            userPreferences.setAcceptCount(RandomUtil.randomInt(1000));
//            userPreferencesService.save(userPreferences);
//
//        }
//    }
//
//    @Test
//    public void test11() {
//        User user = new User();
//        user.setUserName("12312312");
//        user.setPassword("123456");
//        user.setNumber("1232131123123121212121121654");
//        user.setMail("gao484539372@163.com");
//        user.setAvatar("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
//        user.setProfile(" 测试用户1211111321121的简介 ");
//        userService.save(user);
//
//    }
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Test
//    public void test13() {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getId, 32L);
//        User user = userMapper.selectOne(wrapper);
//        System.out.println(user.getMail());
////        User user = userMapper.selectById(1L);
////        System.out.println(user);
//    }
//
//
//    @Test
//    public void test12() {
//        for (int i = 0; i < 20; i++) {
//            Article article = new Article();
//            article.setUserId(2L);
//            article.setCategoryId(RandomUtil.randomInt(5));
//            article.setTitle("题解 | 测试题解文章Hou" + i);
//            article.setContent("# 测试题解文章" + i);
//            article.setCoverUrl("https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg");
//            article.setState(0);
//            article.setLikeCount(RandomUtil.randomInt(1000));
//            article.setCollectCount(RandomUtil.randomInt(1000));
//            article.setCommentCount(RandomUtil.randomInt(1000));
//            article.setWatchCount(RandomUtil.randomInt(1000));
//            article.setCreateTime(new Date());
//            article.setUpdateTime(new Date());
//            article.setDelFlag(0);
//            article.setProblemId(1L);
//            articleService.save(article);
//        }
//
//    }
//}
