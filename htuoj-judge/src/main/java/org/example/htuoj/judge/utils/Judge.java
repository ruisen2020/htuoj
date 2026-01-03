package org.example.htuoj.judge.utils;

public enum Judge {
//    0: "Pending",
//        1: "Accepted",
//        2: "Wrong Answer",
//        3: "Time Limit Exceeded",
//        4: "Memory Limit Exceeded",
//        5: "Runtime Error",
//        6: "Compile Error",
    // 提交失败
//    STATUS_NOT_SUBMITTED(-10, "Not Submitted"),
//    STATUS_CANCELLED(-4, "Cancelled"),
//    STATUS_PRESENTATION_ERROR(-3, "Presentation Error"),
    STATUS_PENDING(0, "Pending"),
    STATUS_ACCEPTED(1, "Accepted"),
    STATUS_WRONG_ANSWER(2, "Wrong Answer"),
    STATUS_TIME_LIMIT_EXCEEDED(3, "Time Limit Exceeded"),
    STATUS_MEMORY_LIMIT_EXCEEDED(4, "Memory Limit Exceeded"),
    STATUS_RUNTIME_ERROR(5, "Runtime Error"),
    STATUS_COMPILE_ERROR(6, "Compile Error"),
    STATUS_PRESENTATION_ERROR(7, "Presentation Error"),
    STATUS_SYSTEM_ERROR(8, "System Error"),
    STATUS_NONZEOR_EXIT_STATUS(9, "Nonzero Exit Status"),
    STATUS_INTERNAL_ERROR(10, "Internal Error"),

    STATUS_FILE_ERROR(11, "File Error"),
    STATUS_OUTPUT_Limit_Exceeded(12, "Output Limit Exceeded"),
    STATUS_SIGNALLED(13, "Signalled"),
    STATUS_NULL(15, "No Status");

    private final Integer status;
    private final String name;

    private Judge(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public static Judge getTypeByStatus(int status) {
        for (Judge judge : Judge.values()) {
            if (judge.getStatus() == status) {
                return judge;
            }
        }
        return STATUS_NULL;
    }

    public static Integer getStatusByName(String name) {
        for (Judge judge : Judge.values()) {
            if (judge.getName().equals(name)) {
                return judge.getStatus();
            }
        }
        return STATUS_NULL.getStatus();
    }

}