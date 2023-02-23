package com.cdmtc.service;

import com.cdmtc.entity.Instruction;

public interface DatasourceService {

    void insertInstruction(Instruction instruction);

    Instruction selectById(Integer id);
}
