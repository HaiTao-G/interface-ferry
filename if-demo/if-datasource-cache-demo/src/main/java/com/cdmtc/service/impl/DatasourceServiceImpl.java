package com.cdmtc.service.impl;

import com.cdmtc.datasource.dao.InstructionDao;
import com.cdmtc.entity.Instruction;
import com.cdmtc.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private InstructionDao instructionDao;

    @Override
    public void insertInstruction(Instruction instruction) {
        instructionDao.insertInstruction(instruction);
    }

    @Override
    @Cacheable(value = "fpcache",key = "#id")
    public Instruction selectById(Integer id) {
        System.out.println("走数据库！！！");
        return instructionDao.selectById(id);
    }
}
