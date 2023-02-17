package com.cdmtc.screenshottooldemo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.cdmtc.screenshottooldemo.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/20 14:54
 */
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    @PostMapping("/fileUpload")
    public ResponseEntity<String> fileUpload(MultipartFile file) throws Exception {
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        if (StrUtil.equals(fileSuffix, ".xlsx")) {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            List<Map<String,Object>> readAll = reader.readAll();
            indexService.fileUpload(readAll, fileName);
        } else {
            return new ResponseEntity<>("上传文件格式不符合要求!", HttpStatus.OK);
        }
        return new ResponseEntity<>("文件上传成功!", HttpStatus.OK);
    }

}
