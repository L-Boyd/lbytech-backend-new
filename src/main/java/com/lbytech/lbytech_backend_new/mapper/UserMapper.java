package com.lbytech.lbytech_backend_new.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbytech.lbytech_backend_new.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
