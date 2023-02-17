package com.cdmtc.datasource.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.cdmtc.datasource.dao.InstructionDao;
import com.cdmtc.entity.Instruction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

/**
 * @ClassName InstructionDaoImpl
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/27 16:33
 */
@Slf4j
@RequiredArgsConstructor
public class InstructionDaoImpl implements InstructionDao {

    /**
     * 分割标识符
     */
    private static final String DIVISION_SIGN = ",";

    /**
     * 新增sql
     */
    private static final String INSERT_SQL="INSERT INTO `instruction` (`request_id`, `order_id`, `user_id`, `topic_name`, `callback_parameters`," +
            "`parameters`, `outside_status`, `result`, `errMsg`, `outside_file_name`, `within_fiLe_name`, `createTime`, `updateTime`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    /**
     * 修改sql
     */
    private static final String UPDATE_SQL="UPDATE `instruction` " +
            "SET `order_id` = ?, `user_id` = ?, `topic_name` = ?, " +
            "`callback_parameters` = ?, `parameters` = ?, `outside_status` = ?, `result` = ?, " +
            "`errMsg` = ?, `outside_file_name` = ?, `within_fiLe_name` = ?, " +
            "`createTime` = ?, `updateTime` = ? " +
            "WHERE `request_id` = ?;";

    final JdbcTemplate jdbcTemplate;

    @Override
    public void insertInstruction(Instruction instruction) {
        this.jdbcTemplate.update(INSERT_SQL, instruction.getRequestId(),instruction.getOrderId(),instruction.getUserId(),
                instruction.getTopicName(), JSON.toJSONString(instruction.getCallbackParameters()),JSON.toJSONString(instruction.getParameters()),
                instruction.getOutsideStatus(),instruction.getResult(),instruction.getErrMsg(), CollUtil.join(instruction.getOutsideFileName(),DIVISION_SIGN),
                CollUtil.join(instruction.getWithinFiLeName(),DIVISION_SIGN),new Date(),new Date());
    }

    @Override
    public void updateInstruction(Instruction instruction) {
        this.jdbcTemplate.update(UPDATE_SQL, instruction.getOrderId(),instruction.getUserId(),
                instruction.getTopicName(), JSON.toJSONString(instruction.getCallbackParameters()),JSON.toJSONString(instruction.getParameters()),
                instruction.getOutsideStatus(),instruction.getResult(),instruction.getErrMsg(), CollUtil.join(instruction.getOutsideFileName(),DIVISION_SIGN),
                CollUtil.join(instruction.getWithinFiLeName(),DIVISION_SIGN), DateUtil.parse(instruction.getCreateTime()),DateUtil.parse(instruction.getUpdateTime()),
                instruction.getRequestId());
    }
}

