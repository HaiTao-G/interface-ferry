package com.cdmtc.controller;

import com.alibaba.fastjson.JSON;
import com.cdmtc.entity.Instruction;
import com.cdmtc.service.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DatasourceController {

    @Autowired
    private DatasourceService datasourceService;

    @GetMapping("/insert")
    public void insert(){
        Instruction instruction = new Instruction();
        instruction.setRequestId("123");
        datasourceService.insertInstruction(instruction);
    }

    @GetMapping("/get")
    public String get(){
        Instruction instruction = datasourceService.selectById(1);
        return JSON.toJSONString(instruction);
    }
}
