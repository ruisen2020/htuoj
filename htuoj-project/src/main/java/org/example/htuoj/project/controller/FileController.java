package org.example.htuoj.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.project.utils.OssUtil;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private OssUtil ossUtil;

    @PostMapping("uploadArticlePictureAndFile")
    @SaCheckLogin
    public Result<String> uploadArticlePictureAndFile(@RequestParam("file") MultipartFile file) {
        String url = ossUtil.uploadArticlePictureAndFile(file);
        return Results.success(url);
    }

    @PostMapping("uploadArticleCover")
    @SaCheckLogin
    public Result<String> uploadArticleCover(@RequestParam("file") MultipartFile file) {
        String url = ossUtil.uploadArticleCover(file);
        return Results.success(url);
    }
}
