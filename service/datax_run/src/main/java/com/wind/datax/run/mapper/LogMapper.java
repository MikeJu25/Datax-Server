package com.wind.datax.run.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wind.entity.log.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
}
