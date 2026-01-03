package org.example.htuoj.project.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.htuoj.common.mapper.LabelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class LabelCollection {
    @Autowired
    private LabelMapper labelMapper;

    public static final String[] labels = {"动态规划", "数学", "二分查找", "二叉树", "树", "链表", "字符串", "栈", "队列", "哈希表", "排序", "图", "并查集", "位运算", "模拟", "递归", "二分", "滑动窗口", "前缀和", "树状数组", "线段树", "动态规划", "数学", "二分查找", "二叉树", "树", "链表", "字符串", "栈", "队列", "哈希表", "排序", "图", "并查集"};
    private static final HashMap<String, Long> labelMap = new HashMap<>();

    // 静态方法
    static {
        for (int i = 1; i <= labels.length; i++) {
            labelMap.put(labels[i - 1], (long) i);
        }
    }

    public static List<Long> getLabelIds(List<String> labels) {
        List<Long> result = new ArrayList<>();
        for (String label : labels) {
            result.add(labelMap.get(label));
        }
        return result;
    }

    public static List<String> getLabelNames(List<Long> labelIds) {
        List<String> result = new ArrayList<>();
        for (Long labelId : labelIds) {
            result.add(labels[(int) (labelId - 1)]);
        }
        return result;
    }
}
