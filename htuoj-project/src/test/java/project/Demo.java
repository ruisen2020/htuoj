package project;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.example.htuoj.common.dao.*;
import org.example.htuoj.common.dto.req.ArticleGetArticleListReqDTO;
import org.example.htuoj.common.dto.resp.SubmissionAddRespDTO;
import org.example.htuoj.common.mapper.*;
import org.example.htuoj.project.HtuOJApplication;
import org.example.htuoj.project.config.OssConfig;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alibaba.nacos.api.selector.SelectorType.label;

@SpringBootTest(classes = HtuOJApplication.class)
public class Demo {
    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AwardInfoMapper awardInfoMapper;
    @Autowired
    private RBloomFilter<String> userNameBloomFilter;

    @Autowired
    private RBloomFilter<String> numberBloomFilter;

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private SampleMapper sampleMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private ProblemLabelRelationsMapper problemLabelRelationsMapper;

    @Autowired
    private ProblemSourceRelationsMapper problemSourceRelationsMapper;

    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ContestAcceptMapper contestAcceptMapper;

    @Test
    public void test20() {
        LambdaQueryWrapper<ContestAccept> contestAcceptWrapper = new LambdaQueryWrapper<>();
        contestAcceptWrapper.eq(ContestAccept::getUserId, 2);
        contestAcceptWrapper.eq(ContestAccept::getContestId, 2);
        contestAcceptWrapper.eq(ContestAccept::getProblemId, 961);
        ContestAccept contestAccept = contestAcceptMapper.selectOne(contestAcceptWrapper);
        System.out.println(contestAccept);
    }

    @Test
    public void test19() throws IOException {
        LambdaQueryWrapper<Submission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Submission::getContestId, 2);
//        wrapper.eq(Submission::getUserId,2);
        wrapper.select(Submission::getSubmissionId);
        List<Submission> submissions = submissionMapper.selectList(wrapper);
        for (Submission submission : submissions) {
            //    提交成功，返回提交记录id
            Long submissionId = submission.getSubmissionId();
//            System.out.println(submissionId);
            // 加入消息队列中
            // 队列名称
            String queueName = "judge.queue";
            // 消息
            String message = submissionId.toString();
            // 发送消息,只需要知道ID是谁就行了
            rabbitTemplate.convertAndSend(queueName, message);
        }

    }

    @Test
    public void test18() throws IOException {
        ArticleGetArticleListReqDTO reqDTO = new ArticleGetArticleListReqDTO();
        reqDTO.setSearchContent("测试背书包");
        reqDTO.setCurrent(1);
        reqDTO.setSize(10);
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://117.72.110.81:9200")
        ));
        SearchRequest searchRequest = new SearchRequest("article");
        // 创建 SearchSourceBuilder 对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 设置 from 和 size
        sourceBuilder.from((int) ((reqDTO.getCurrent() - 1) * reqDTO.getSize()));
        sourceBuilder.size((int) reqDTO.getSize());

        // 设置 track_total_hits 为 true 以获取精确的总数
        sourceBuilder.trackTotalHits(true);
//        // 2.1.准备bool查询
//        BoolQueryBuilder bool = QueryBuilders.boolQuery();
//        // 2.2.关键字搜索
//        bool.must(QueryBuilders.matchQuery("name", "脱脂牛奶"));
//        // 2.3.品牌过滤
//        bool.filter(QueryBuilders.termQuery("brand", "德亚"));
//        // 2.4.价格过滤
//        bool.filter(QueryBuilders.rangeQuery("price").lte(30000));
//        request.source().query(bool);
        // 创建 MultiMatchQueryBuilder 对象
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(reqDTO.getSearchContent(), "title", "userName", "content");
        // 创建 TermQueryBuilder 对象来过滤 categoryId 为 1
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categoryId", 1);

        // 创建 BoolQueryBuilder 对象并将 multiMatchQueryBuilder 和 termQueryBuilder 添加到 must 子句中
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(multiMatchQueryBuilder)
                .filter(termQueryBuilder);
        // 设置查询
        sourceBuilder.query(boolQueryBuilder);

        // 创建 HighlightBuilder 对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
        highlightTitle.preTags("<em>").postTags("</em>");
        HighlightBuilder.Field highlightUserName = new HighlightBuilder.Field("userName");
        highlightUserName.preTags("<em>").postTags("</em>");
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
        highlightContent.preTags("<em>").postTags("</em>");

        // 添加高亮字段
        highlightBuilder.field(highlightTitle);
        highlightBuilder.field(highlightUserName);
        highlightBuilder.field(highlightContent);

        // 设置高亮
        sourceBuilder.highlighter(highlightBuilder);

        // 将 SearchSourceBuilder 设置到 SearchRequest
        searchRequest.source(sourceBuilder);

        // 执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 处理响应
        System.out.println(searchResponse);
        SearchHits searchHits = searchResponse.getHits();
        // 1.获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");
        // 2.遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            // 3.得到_source，也就是原始json文档
            String source = hit.getSourceAsString();

            // 4.反序列化并打印
            ArticleDoc item = JSONUtil.toBean(source, ArticleDoc.class);
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            for (String key : highlightFields.keySet()) {
//                if (key.equals("userName")) {
//                    HighlightField userName = highlightFields.get("userName");
//                    item.setUserName(userName.getFragments()[0].string());
//                } else if (key.equals("title")) {
//                    HighlightField title = highlightFields.get("title");
//                    item.setTitle(title.getFragments()[0].string());
//                } else if (key.equals("content")) {
//                    HighlightField content = highlightFields.get("content");
//                    item.setContent(content.getFragments()[0].string());
//                }
//            }
//            System.out.println(item);
        }
    }

    @Test
    public void test17() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://117.72.110.81:9200")
        ));
        List<Article> articles = articleMapper.selectList(null);

        for (Article article : articles) {
            ArticleDoc addArticleDoc = articleMapper.addArticleDoc(article.getArticleId());
            String doc = JSONUtil.toJsonStr(addArticleDoc);
            // 1.准备Request对象
            IndexRequest request = new IndexRequest("article").id(String.valueOf(article.getArticleId()));
            // 2.准备Json文档
            request.source(doc, XContentType.JSON);
            // 3.发送请求
            client.index(request, RequestOptions.DEFAULT);
        }

    }

    @Test
    public void test16() {
        String excelFilePath = "/Users/1587138602qq.com/IdeaProjects/htuoj/htuoj-project/src/main/resources/school.xls"; // 替换为你的 Excel 文件路径

        EasyExcel.read(excelFilePath, new AnalysisEventListener<Map<Integer, String>>() {
            Long provinceId = 0L;

            @Override
            public void invoke(Map<Integer, String> data, AnalysisContext context) {
//                System.out.println("读取到一行数据: " + data);
                //

                if (data.get(1) != null) {
//                    System.out.println(data.get(0) + " " + data.get(1));
                    School school = new School();
                    school.setSchoolName(data.get(1));
                    school.setProvinceId(provinceId);
                    schoolMapper.insert(school);

                } else {
//                    System.out.println(data.get(0));
//                    System.out.println(data.get(0).indexOf('（'));
//                    System.out.println(data.get(0).substring(0, data.get(0).indexOf('(')));
                    String provinceName = data.get(0).substring(0, data.get(0).indexOf('（'));
                    System.out.println(provinceName);
                    Province province = new Province();
                    province.setProvinceName(provinceName);
                    provinceMapper.insert(province);
                    provinceId = province.getProvinceId();
//                    System.out.println(data.get(0));
                }

            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("所有数据读取完成！");
            }

        }).sheet().doRead();
    }


    public static void main(String[] args) {


    }

    @Test
    public void test15() {
        List<Problem> problems = problemMapper.selectList(null);
        for (Problem problem : problems) {
            if (problem.getContent() != null) {
                String content = problem.getContent();
                problem.setContent(content.replace("&lt;", "<"));
            }
            if (problem.getInputDescription() != null) {
                String replace = problem.getInputDescription().replace("&lt;", "<");
                problem.setInputDescription(replace);
            }

            if (problem.getOutputDescription() != null) {
                String replace1 = problem.getOutputDescription().replace("&lt;", "<");
                problem.setOutputDescription(replace1);
            }
            if (problem.getNote() != null) {
                String replace2 = problem.getNote().replace("&lt;", "<");
                problem.setNote(replace2);
            }
            problemMapper.updateById(problem);
        }
    }


    @Test
    public void test14() {
        File file = new File("D:\\htuoj\\htuoj-project\\src\\main\\resources\\problem.mdmd");
        StringBuilder content = new StringBuilder();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Problem problem = new Problem();
        String string = content.toString();
        String[] sections = string.split("## ");
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < sections.length; i++) {
            String section = sections[i];
            if (section.trim().isEmpty()) continue;
            String[] lines = section.split("\n");
            String sectionTitle = lines[0].trim();
            StringBuilder sectionContent = new StringBuilder();
            for (int j = 1; j < lines.length; j++) {
                sectionContent.append(lines[j]).append("\n");
            }
            System.out.println("Section: " + sectionTitle);
            System.out.println("Content: " + sectionContent.toString());
            System.out.println("------");
            if (i == 0) {
                problem.setTitle(sectionContent.toString().replace("#", ""));
            }
            if (sectionTitle.equals("题目描述"))
                problem.setContent(sectionContent.toString());
            else if (sectionTitle.equals("输入格式"))
                problem.setInputDescription(sectionContent.toString());
            else if (sectionTitle.equals("输出格式"))
                problem.setOutputDescription(sectionContent.toString());
            else if (sectionTitle.equals("说明"))
                problem.setNote(sectionContent.toString());
            else if (sectionTitle.contains("样例输入")) {
                hashMap.put(sectionTitle, sectionContent.toString().replace("```", "").replace("#", "").trim());
            } else if (sectionTitle.contains("样例输出")) {
                hashMap.put(sectionTitle, sectionContent.toString().replace("```", "").replace("#", "").trim());
            }
        }
        System.out.println(problem);
    }

    @Test
    public void test13() {
        // 执行Python脚本
        for (int k = 113; k <= 116; k++) {
            String listName = String.valueOf(k);
            ProcessBuilder processBuilder = new ProcessBuilder("python", "D:\\htuoj\\htuoj-project\\src\\main\\resources\\list.py", listName);
            StringBuilder result = new StringBuilder();
            try {
                Process process = processBuilder.start();
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.out.println(" Python script execution failed with exit code: " + exitCode);
                }

            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
                // handle exception
            }

            String string = result.toString();
            System.out.println(result);
            String[] split = string.split("\\n");

            for (String s : split) {
//            System.out.println(s);
                System.out.println("-------------");
                String name = s;
                System.out.println(s);
                processBuilder = new ProcessBuilder("python", "D:\\htuoj\\htuoj-project\\src\\main\\resources\\test.py", name);
                result = new StringBuilder();
                try {
                    Process process = processBuilder.start();
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        System.out.println(" Python script execution failed with exit code: " + exitCode);
                    }
                } catch (IOException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // handle exception
                }
                System.out.println(result);

                {
                    File file = new File("D:\\htuoj\\htuoj-project\\src\\main\\resources\\problem.md");
                    StringBuilder content = new StringBuilder();
                    try (FileInputStream inputStream = new FileInputStream(file)) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append(System.lineSeparator());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Problem problem = new Problem();
                    String string1 = content.toString();
                    String[] sections = string1.split("## ");
                    HashMap<String, String> hashMap = new HashMap<>();
                    for (int i = 0; i < sections.length; i++) {
                        String section = sections[i];
                        if (section.trim().isEmpty()) continue;
                        String[] lines = section.split("\n");
                        String sectionTitle = lines[0].trim();
                        StringBuilder sectionContent = new StringBuilder();
                        for (int j = 1; j < lines.length; j++) {
                            sectionContent.append(lines[j]).append("\n");
                        }
                        System.out.println("Section: " + sectionTitle);
                        System.out.println("Content: " + sectionContent.toString());
                        System.out.println("------");
                        if (i == 0) {
                            problem.setTitle(sectionContent.toString().replace("#", ""));
                        }
                        if (sectionTitle.equals("题目描述"))
                            problem.setContent(sectionContent.toString());
                        else if (sectionTitle.equals("输入格式"))
                            problem.setInputDescription(sectionContent.toString());
                        else if (sectionTitle.equals("输出格式"))
                            problem.setOutputDescription(sectionContent.toString());
                        else if (sectionTitle.equals("说明"))
                            problem.setNote(sectionContent.toString());
                        else if (sectionTitle.contains("输入样例")) {
                            hashMap.put(sectionTitle, sectionContent.toString().replace("```", "").replace("#", "").trim());
                        } else if (sectionTitle.contains("输出样例")) {
                            hashMap.put(sectionTitle, sectionContent.toString().replace("```", "").replace("#", "").trim());
                        }
                    }
                    problem.setDifficultyLevel(0);
                    problem.setTimeLimit(1000);
                    problem.setMemoryLimit(512);
                    problemMapper.insert(problem);
                    System.out.println(problem);
                    // 插入来源
                    ProblemSourceRelations problemSourceRelations = new ProblemSourceRelations();
                    problemSourceRelations.setProblemId(problem.getId());
                    problemSourceRelations.setSourceId(4L);
                    problemSourceRelationsMapper.insert(problemSourceRelations);
                    // 插入样例
                    for (String key : hashMap.keySet()) {
                        System.out.println(key);
                        if (key.contains("输入样例")) {
                            Sample sample = new Sample();
                            sample.setProblemId(problem.getId());
                            sample.setSampleInput(hashMap.get(key));
                            sample.setSampleOutput(hashMap.get(key.replace("输入样例", "输出样例")));
                            sampleMapper.insert(sample);
                        }
                    }
                }
            }
        }
//        File file = new File("D:\\htuoj\\htuoj-project\\src\\main\\resources\\problem.md");
//        StringBuilder content = new StringBuilder();
//        try (FileInputStream inputStream = new FileInputStream(file)) {
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//            BufferedReader reader = new BufferedReader(inputStreamReader);
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append(System.lineSeparator());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Problem problem = new Problem();
//        String string = content.toString();
//        String[] sections = string.split("## ");
//        HashMap<String, String> hashMap = new HashMap<>();
//        for (int i = 0; i < sections.length; i++) {
//            String section = sections[i];
//            if (section.trim().isEmpty()) continue;
//            String[] lines = section.split("\n");
//            String sectionTitle = lines[0].trim();
//            StringBuilder sectionContent = new StringBuilder();
//            for (int j = 1; j < lines.length; j++) {
//                sectionContent.append(lines[j]).append("\n");
//            }
//            System.out.println("Section: " + sectionTitle);
//            System.out.println("Content: " + sectionContent.toString());
//            System.out.println("------");
//            if (i == 0) {
//                problem.setTitle(sectionContent.toString().replace("#", ""));
//            }
//            if (sectionTitle.equals("题目描述"))
//                problem.setContent(sectionContent.toString());
//            else if (sectionTitle.equals("输入格式"))
//                problem.setInputDescription(sectionContent.toString());
//            else if (sectionTitle.equals("输出格式"))
//                problem.setOutputDescription(sectionContent.toString());
//            else if (sectionTitle.equals("说明"))
//                problem.setNote(sectionContent.toString());
//            else if (sectionTitle.contains("输入样例")) {
//                hashMap.put(sectionTitle, sectionContent.toString().replace("```", "").replace("#", "").trim());
//            } else if (sectionTitle.contains("输出样例")) {
//                hashMap.put(sectionTitle, sectionContent.toString().replace("```", "").replace("#", "").trim());
//            }
//        }
//        problem.setDifficultyLevel(0);
//        problem.setTimeLimit(1000);
//        problem.setMemoryLimit(512);
//        problemMapper.insert(problem);
//        System.out.println(problem);
//        // 插入来源
//        ProblemSourceRelations problemSourceRelations = new ProblemSourceRelations();
//        problemSourceRelations.setProblemId(problem.getId());
//        problemSourceRelations.setSourceId(4L);
//        problemSourceRelationsMapper.insert(problemSourceRelations);
//        // 插入样例
//        for (String key : hashMap.keySet()) {
//            System.out.println(key);
//            if (key.contains("输入样例")) {
//                Sample sample = new Sample();
//                sample.setProblemId(problem.getId());
//                sample.setSampleInput(hashMap.get(key));
//                sample.setSampleOutput(hashMap.get(key.replace("输入样例", "输出样例")));
//                sampleMapper.insert(sample);
//            }
//        }
    }


    @Test
    public void test12() {
//        List<Problem> problems = problemMapper.selectList(null);
//
//        for (Problem problem : problems) {
//            String content = problem.getContent();
//            String inputDescription = problem.getInputDescription();
//            String outputDescription = problem.getOutputDescription();
//            String note = problem.getNote();
//            content = content.replace("\\r\\n", "\n");
//            problem.setContent(content);
//            if (inputDescription != null)
//                inputDescription = inputDescription.replace("\\r\\n", "\n");
//            problem.setInputDescription(inputDescription);
//            if (outputDescription != null) {
//                outputDescription = outputDescription.replace("\\r\\n", "\n");
//            }
//            problem.setOutputDescription(outputDescription);
//            if (note != null)
//                note = note.replace("\\r\\n", "\n");
//            problem.setNote(note);
//
//            problemMapper.updateById(problem);
//        }

        List<Sample> samples = sampleMapper.selectList(null);
        for (Sample sample : samples) {
            String sampleInput = sample.getSampleInput();
            String sampleOutput = sample.getSampleOutput();
            if (sampleInput != null)
                sampleInput = sampleInput.replace("\\n", "\n");
            sample.setSampleInput(sampleInput);
            if (sampleOutput != null)
                sampleOutput = sampleOutput.replace("\\n", "\n");
            sample.setSampleOutput(sampleOutput);
            sampleMapper.updateById(sample);
        }


    }

    @Test
    public void test11() throws IOException {
        // 读取一个文件夹种的所有文件
        File folder = new File("C:\\Users\\48453\\Downloads\\一本通提高");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                Problem problem = new Problem();
                File[] files1 = file.listFiles();
                System.out.println(file.getName());
                List<String> labels = new ArrayList<>();
                if (files1 != null) {
                    for (File file1 : files1) {
                        if (file1.getName().equals("problem.yaml")) {
                            // 读取yaml文件 owner: 2
                            //title: 「一本通 6.7 练习 2」巧克力棒
                            //tag:
                            //  - 搜索
                            //  - 博弈论
                            //nSubmit: 0
                            //nAccept: 0
                            Yaml yaml = new Yaml();
                            try (InputStream inputStream = new FileInputStream(file1)) {
                                Map<String, Object> map = yaml.load(inputStream);
                                String title = (String) map.get("title");
                                List<String> tag = (List<String>) map.get("tag");
                                problem.setTitle(title);
                                for (String s : tag) {
                                    if (s.equals("早于 2010")) {
                                        continue;
                                    }
                                    // 标签
                                    labels.add(s);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (file1.getName().equals("problem_zh.md")) {
                            // 读取JSON格式的文本
                            StringBuilder content = new StringBuilder();
                            try (InputStream inputStream = new FileInputStream(file1)) {
                                // 指定文件编码格式为UTF-8
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                                BufferedReader reader = new BufferedReader(inputStreamReader);
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    content.append(line).append(System.lineSeparator());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Pattern pattern = Pattern.compile("\"sectionTitle\":\"([^\"]+)\",\"type\":\"([^\"]+)\",\"text\":\"([^\"]+)\"");
                            Matcher matcher = pattern.matcher(content);

                            Map<String, String> sections = new HashMap<>();
                            while (matcher.find()) {
                                String sectionTitle = matcher.group(1);
                                String type = matcher.group(2);
                                String text = matcher.group(3);
//                                String payload = matcher.group(4);
                                System.out.println("Section Title: " + sectionTitle);
                                System.out.println("Type: " + type);
                                System.out.println("Text: " + text);
//                                System.out.println("Payload: " + payload);
                                sections.put(sectionTitle, text);
                            }
                            for (Map.Entry<String, String> entry : sections.entrySet()) {
                                if (entry.getKey().equals("输入格式")) {
                                    problem.setInputDescription(entry.getValue());
                                } else if (entry.getKey().equals("输出格式")) {
                                    problem.setOutputDescription(entry.getValue());
                                } else if (entry.getKey().equals("数据范围与提示")) {
                                    problem.setNote(entry.getValue());
                                } else if (entry.getKey().equals("题目描述")) {
                                    problem.setContent(entry.getValue());
                                }

                            }
                            problem.setDifficultyLevel(1);
                            problem.setTimeLimit(1000);
                            problem.setMemoryLimit(512);
                            problemMapper.insert(problem);
                            // 定义正则表达式来匹配 payload 中的数据集合
                            String regex = "\"payload\":\\[\"(.*?)\",\"(.*?)\"\\]";
                            pattern = Pattern.compile(regex);
                            matcher = pattern.matcher(content);

                            List<String> dataList = new ArrayList<>();

                            if (matcher.find()) {
                                String data1 = matcher.group(1);
                                String data2 = matcher.group(2);

                                // 将数据添加到列表中
                                dataList.add(data1);
                                dataList.add(data2);
                            }

                            // 输出提取的数据集合
                            for (String data : dataList) {
                                System.out.println(data);
                            }
                            Sample sample = new Sample();

                            sample.setProblemId(problem.getId());
                            if (dataList.size() > 1) {
                                sample.setSampleInput(dataList.get(0));
                                sample.setSampleOutput(dataList.get(1));
                            }

                            sampleMapper.insert(sample);
                            for (int i = 0; i < labels.size(); i++) {
                                LambdaQueryWrapper<Label> queryWrapper = new LambdaQueryWrapper<>();
                                queryWrapper.eq(Label::getLabelName, labels.get(i));
                                List<Label> labels1 = labelMapper.selectList(queryWrapper);
                                if (labels1 == null || labels1.isEmpty()) {
                                    Label label = new Label();
                                    label.setLabelName(labels.get(i));
                                    labelMapper.insert(label);
                                    problemLabelRelationsMapper.insert(new ProblemLabelRelations().setProblemId(problem.getId()).setLabelId(label.getId()));
                                } else {
                                    problemLabelRelationsMapper.insert(new ProblemLabelRelations().setProblemId(problem.getId()).setLabelId(labels1.get(0).getId()));
                                }
                            }
                            ProblemSourceRelations problemSourceRelations = new ProblemSourceRelations();
                            problemSourceRelations.setProblemId(problem.getId()).setSourceId(3L);
                            problemSourceRelationsMapper.insert(problemSourceRelations);
                            System.out.println(problem);
                            // 复制一个文件夹中的所有文件到一个新文件夹中
                            FileUtils.copyDirectory(new File(file.getAbsolutePath() + "\\testdata"), new File("C:\\Users\\48453\\Documents\\1" + "\\" + problem.getId()));
                        }
                    }
                }
            }
        } else {
            System.out.println("文件夹为空");
        }

    }

    @Test
    public void test10() {
        List<AwardInfo> awardInfoList = awardInfoMapper.selectList(null);
        for (AwardInfo awardInfo : awardInfoList) {
            String trackName = awardInfo.getTrackName();
            if (trackName.equals("C/C++程序设计大学B组")) {
                awardInfo.setTrackName("C/C++程序设计大学 B 组");
                awardInfoMapper.updateById(awardInfo);
            } else if (trackName.equals("Java软件开发大学B组")) {
                awardInfo.setTrackName("Java软件开发大学 B 组");
                awardInfoMapper.updateById(awardInfo);
            }

        }
    }

    @Test
    public void test9() {
        List<AwardInfo> awardInfoList = awardInfoMapper.selectList(null);
        for (AwardInfo awardInfo : awardInfoList) {
            String awardLevel = awardInfo.getAwardLevel();
            switch (awardLevel) {
                case "国家级一等奖":
                    awardInfo.setAwardLevel("1");
                    break;
                case "国家级二等奖":
                    awardInfo.setAwardLevel("2");
                    break;
                case "国家级三等奖":
                    awardInfo.setAwardLevel("3");
                    break;
                case "国家级优秀奖":
                    awardInfo.setAwardLevel("4");
                    break;
                case "省级一等奖":
                    awardInfo.setAwardLevel("5");
                    break;
                case "省级二等奖":
                    awardInfo.setAwardLevel("6");
                    break;
                case "省级三等奖":
                    awardInfo.setAwardLevel("7");
                    break;
            }
            awardInfoMapper.updateById(awardInfo);
        }
    }

    @Test
    public void test8() {
        String filePath = "D:\\htuoj\\htuoj-project\\src\\main\\resources\\工作簿1.xlsx";
        // 检验文件是否存在
        if (!new File(filePath).exists()) {
            System.out.println("文件不存在");
            return;
        }
        else{
             System.out.println("文件存在");
        }
        // 读取 Excel 文件并将数据转换为实体类列表
        EasyExcel.read(new File(filePath), new AnalysisEventListener<LinkedHashMap<Integer, String>>() {
            @Override
            public void invoke(LinkedHashMap<Integer, String> data, AnalysisContext analysisContext) {
                System.out.println("学校名称: " + data.get(0));
                System.out.println("考生姓名: " + data.get(1));
                System.out.println("科目名称: " + data.get(2));
                // 处理每一行数据
                if (data != null && !data.isEmpty()) {
                    String schoolName = data.get(0); // 假设学校名称在第二列
                    if ("河南师范大学".equals(schoolName)) {

                        AwardInfo awardInfo = new AwardInfo();
                        awardInfo.setAwardLevel("省级" + data.get(3));
                        String awardLevel = awardInfo.getAwardLevel();
                        switch (awardLevel) {
                            case "国家级一等奖":
                                awardInfo.setAwardLevel("1");
                                break;
                            case "国家级二等奖":
                                awardInfo.setAwardLevel("2");
                                break;
                            case "国家级三等奖":
                                awardInfo.setAwardLevel("3");
                                break;
                            case "国家级优秀奖":
                                awardInfo.setAwardLevel("4");
                                break;
                            case "省级一等奖":
                                awardInfo.setAwardLevel("5");
                                break;
                            case "省级二等奖":
                                awardInfo.setAwardLevel("6");
                                break;
                            case "省级三等奖":
                                awardInfo.setAwardLevel("7");
                                break;
                        }
                        awardInfo.setStudentName(data.get(1));
                        awardInfo.setAwardTime("2025-04");
                        awardInfo.setCompetitionName("蓝桥杯大赛");
                        awardInfo.setTrackName(data.get(2));
                        awardInfoMapper.insert(awardInfo);
                    }
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                // 完成读取后的操作
                System.out.println("导入完成，共导入 " + " 条数据。");
            }
        }).sheet().doRead();
    }

    @Test
    public void test7() {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getDelFlag, 0);
        commentLambdaQueryWrapper.eq(Comment::getState, 0);
        commentLambdaQueryWrapper.isNotNull(Comment::getParentId);
        List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
        for (Comment comment : comments) {
            try {
                Notice notice = new Notice();
                notice.setSenderId(comment.getUserId());
                notice.setTargetId(comment.getCommentId());
                notice.setTargetType(3);
                LambdaQueryWrapper<Comment> commentLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                commentLambdaQueryWrapper1.eq(Comment::getCommentId, comment.getParentId());
                commentLambdaQueryWrapper1.eq(Comment::getDelFlag, 0);
                commentLambdaQueryWrapper1.eq(Comment::getState, 0);
                Comment comment1 = commentMapper.selectOne(commentLambdaQueryWrapper1);
                notice.setReceiverId(comment1.getUserId());
                notice.setCreateTime(comment.getCreateTime());
                noticeMapper.insert(notice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test6() {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getDelFlag, 0);
        commentLambdaQueryWrapper.eq(Comment::getState, 0);
        commentLambdaQueryWrapper.isNull(Comment::getParentId);
        List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
        for (Comment comment : comments) {
            try {
                Notice notice = new Notice();
                notice.setSenderId(comment.getUserId());
                notice.setTargetId(comment.getCommentId());
                notice.setTargetType(2);
                LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                articleLambdaQueryWrapper.eq(Article::getArticleId, comment.getArticleId());
                articleLambdaQueryWrapper.eq(Article::getDelFlag, 0);
                Article article = articleMapper.selectOne(articleLambdaQueryWrapper);
                notice.setReceiverId(article.getUserId());
                notice.setCreateTime(comment.getCreateTime());
                noticeMapper.insert(notice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test5() {
        LambdaQueryWrapper<Collect> collectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        collectLambdaQueryWrapper.eq(Collect::getDelFlag, 0);
        collectLambdaQueryWrapper.eq(Collect::getState, 0);
        collectLambdaQueryWrapper.eq(Collect::getTargetType, 0);
        List<Collect> collects = collectMapper.selectList(null);
        for (Collect collect : collects) {
            try {
                Notice notice = new Notice();
                notice.setSenderId(collect.getUserId());
                notice.setTargetId(collect.getTargetId());
                notice.setTargetType(5);
                LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                articleLambdaQueryWrapper.eq(Article::getArticleId, collect.getTargetId());
                articleLambdaQueryWrapper.eq(Article::getDelFlag, 0);
                Article article = articleMapper.selectOne(articleLambdaQueryWrapper);
                notice.setReceiverId(article.getUserId());
                notice.setCreateTime(collect.getCreateTime());
                noticeMapper.insert(notice);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Test
    public void test3() {
        List<Follow> follows = followMapper.selectList(null);
        for (Follow follow : follows) {
            Notice notice = new Notice();
            notice.setSenderId(follow.getFollowForm());
            notice.setReceiverId(follow.getFollowTo());
            notice.setTargetType(4);
            notice.setCreateTime(follow.getCreateTime());
            noticeMapper.insert(notice);
        }
    }

    @Test
    public void test4() {
        LambdaQueryWrapper<Like> likeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        likeLambdaQueryWrapper.eq(Like::getDelFlag, 0);
        likeLambdaQueryWrapper.eq(Like::getState, 0);
        List<Like> likes = likeMapper.selectList(likeLambdaQueryWrapper);
        for (Like like : likes) {
            try {
                Notice notice = new Notice();
                notice.setSenderId(like.getUserId());
                if (like.getTargetType() == 0) {
                    notice.setTargetType(0);
                    LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    articleLambdaQueryWrapper.eq(Article::getArticleId, like.getTargetId());
                    articleLambdaQueryWrapper.eq(Article::getDelFlag, 0);
                    Article article = articleMapper.selectOne(articleLambdaQueryWrapper);
                    if (article != null) {
                        notice.setReceiverId(article.getUserId());
                        notice.setTargetId(article.getArticleId());
                    }
                } else {
                    notice.setTargetType(1);
                    LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    commentLambdaQueryWrapper.eq(Comment::getCommentId, like.getTargetId());
                    commentLambdaQueryWrapper.eq(Comment::getDelFlag, 0);
                    Comment comment = commentMapper.selectOne(commentLambdaQueryWrapper);
                    commentLambdaQueryWrapper.eq(Comment::getCommentId, comment.getParentId());
                    commentLambdaQueryWrapper.eq(Comment::getDelFlag, 0);
                    Comment comment1 = commentMapper.selectOne(commentLambdaQueryWrapper);
                    notice.setReceiverId(comment1.getUserId());
                    notice.setTargetId(comment.getCommentId());
                }
                notice.setCreateTime(like.getCreateTime());
                noticeMapper.insert(notice);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Test
    public void test2() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
//            System.out.println(userNameBloomFilter.contains(user.getUserName()));
//            userNameBloomFilter.add(user.getUserName());
//            System.out.println(userNameBloomFilter.contains(user.getUserName()+"aaa"));
            if (!userNameBloomFilter.contains(user.getUserName())) {
                userNameBloomFilter.add(user.getUserName());
            } else {
                System.out.println("AAAA");
            }
            if (!numberBloomFilter.contains(user.getNumber())) {
                numberBloomFilter.add(user.getNumber());
            } else {
                System.out.println("BBBB");
            }
        }
    }

    @Test
    public void test1() {
        List<AwardInfo> awardInfoList = awardInfoMapper.selectList(null);
        for (AwardInfo awardInfo : awardInfoList) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUserName, awardInfo.getStudentName());
            User user = userMapper.selectOne(wrapper);
            if (user != null) {
                awardInfo.setUserId(user.getId());
                awardInfo.setStudentNumber(user.getNumber());
                awardInfoMapper.updateById(awardInfo);
            }
        }
    }

    @Test
    public void test() {
        String filePath = "D:\\htuoj\\htuoj-project\\src\\test\\java\\project\\蓝桥杯获奖名单.xls";
        List<AwardInfo> awardInfoList = new ArrayList<>();
        File file = new File(filePath);

        // 读取Excel文件并将数据转换为实体类列表
        EasyExcel.read(new File(filePath), AwardInfo.class, new AnalysisEventListener<AwardInfo>() {
            @Override
            public void invoke(AwardInfo userInfo, AnalysisContext analysisContext) {
//                System.out.println(userInfo);
                userInfo.setAwardLevel("省级" + userInfo.getAwardLevel());
                userInfo.setCompetitionName("蓝桥杯大赛");
                userInfo.setStudentCollege("软件学院");
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(User::getUserName, userInfo.getStudentName());
                User user = userMapper.selectOne(wrapper);
                if (user != null) {
                    userInfo.setUserId(user.getId());
                    userInfo.setStudentNumber(user.getNumber());
                }

                userInfo.setAwardTime("2024-04");

                awardInfoList.add(userInfo);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("导入完成，共导入 " + awardInfoList.size() + " 条数据。");
            }


//            @Override
//            public void doAnalysisComplete(AnalysisContext analysisContext) {
//                // 数据读取完成后，可以在这里进行后续处理，比如将数据保存到数据库等
//                System.out.println("导入完成，共导入 " + awardInfoList.size() + " 条数据。");
//            }
        }).sheet().doRead();

        // 这里可以对导入的数据进行进一步处理，比如保存到数据库等
        // 假设这里只是简单打印导入的数据
        for (AwardInfo user : awardInfoList) {
            System.out.println(user);
            awardInfoMapper.insert(user);
        }
    }

//
//    public List<ContestGetContestListByOtherOJRespDTO> getContestListByOtherOJ() {
//        List<ContestGetContestListByOtherOJRespDTO> result = new ArrayList<>();
//        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(CONTEST_LIST_KEY);
//        ObjectMapper objectMapper = new ObjectMapper();
//        if (!entries.isEmpty()) {
//            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
//                try {
//                    ContestGetContestListByOtherOJRespDTO userGetUserTopListRespDTO = objectMapper.readValue((String) entry.getValue(), ContestGetContestListByOtherOJRespDTO.class);
//                    result.add(userGetUserTopListRespDTO);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } else {
//            // 查询
//            // 1.查询Codeforces
////            result.add(getCFContestList());
////            result = userMapper.getUserTopList(reqDTO);
////            for (int i = 0; i < result.getRecords().size(); i++) {
////                UserGetUserTopListRespDTO userGetUserTopListRespDTO = result.getRecords().get(i);
////                try {
////                    stringRedisTemplate.opsForHash().put(RedisConstant.USER_TOP_LIST_KEY, String.valueOf(i + 1), objectMapper.writeValueAsString(userGetUserTopListRespDTO));
////                    stringRedisTemplate.expire(RedisConstant.USER_TOP_LIST_KEY, 1, TimeUnit.DAYS);
////                } catch (JsonProcessingException e) {
////                    throw new RuntimeException(e);
////                }
////            }
//        }
//        Collections.sort(result, new Comparator<ContestGetContestListByOtherOJRespDTO>() {
//            @Override
//            public int compare(ContestGetContestListByOtherOJRespDTO o1, ContestGetContestListByOtherOJRespDTO o2) {
//                return o1.getStartTime().compareTo(o2.getStartTime());
//            }
//        });
//        return result;
//    }
//
//    private ContestGetContestListByOtherOJRespDTO getCFContestList() {
//        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://codeforces.com/api/contest.list", String.class);
//        System.out.println(forEntity.getBody());
//        return null;
//    }
}