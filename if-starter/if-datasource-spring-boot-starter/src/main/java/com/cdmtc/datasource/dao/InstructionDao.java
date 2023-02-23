package com.cdmtc.datasource.dao;

import com.cdmtc.entity.Instruction;

/**
 * @ClassName InstructionDao
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/27 16:32
 */
public interface InstructionDao {

    /**
     * 新增指令标识
     * @param instruction
     */
    void insertInstruction(Instruction instruction);

    /**
     * 修改指令标识
     * @param instruction
     */
    void updateInstruction(Instruction instruction);

    /**
     * 根据主键标识获取
     * @param id
     * @return
     */
    Instruction selectById(Integer id);
}
