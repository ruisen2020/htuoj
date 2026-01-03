//package project;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.concurrent.*;
//
//public class Judge {
//
//    private static final int TIME_LIMIT_MS = 2000;  // 时间限制为2秒
//    private static final int MEMORY_LIMIT_MB = 64;  // 内存限制为64MB
//
//    public static void main(String[] args) throws Exception {
//        String sourceFilePath = "D:\\study\\java\\project\\thinkcoder\\src\\test\\java\\org\\thinkcoder\\com\\Main.java";
//
//        // 编译代码
//        if (!compileJava(sourceFilePath)) {
//            System.out.println("Compile Error");
//            return;
//        }
//
//        // 执行代码
//        String result = runJavaWithTimeout("Main", TIME_LIMIT_MS);
//        if (result == null) {
//            System.out.println("Time Limit Exceeded");
//        } else {
//            System.out.println("Program Output: " + result);
//            // 比对输出与标准答案...
//        }
//    }
//
//    // 编译 Java 文件
//    private static boolean compileJava(String filePath) throws IOException {
//        System.out.println(filePath);
//        ProcessBuilder pb = new ProcessBuilder("javac", filePath);
//        Process process = pb.start();
//
//        try {
//            process.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return false;
//        }
//        System.out.println(process.exitValue());
//        // 判断编译结果
//        return process.exitValue() == 0;
//    }
//
//    // 运行 Java 程序，带时间限制
//    private static String runJavaWithTimeout(String className, int timeoutMs) {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Future<String> future = executor.submit(() -> runJavaProgram(className));
//        try {
//            return future.get(timeoutMs, TimeUnit.MILLISECONDS);
//        } catch (TimeoutException e) {
//            future.cancel(true);
//            return null;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            executor.shutdown();
//        }
//    }
//
//    // 实际执行 Java 程序的逻辑
//    private static String runJavaProgram(String className) throws IOException {
//        ProcessBuilder pb = new ProcessBuilder("java", className);
//        Process process = pb.start();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        StringBuilder output = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            output.append(line).append("\n");
//        }
//        try {
//            process.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return output.toString();
//    }
//}
