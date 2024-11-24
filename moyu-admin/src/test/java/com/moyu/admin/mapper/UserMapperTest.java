package com.moyu.admin.mapper;

import com.moyu.admin.BaseTest;
import com.moyu.admin.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shisong
 * @since 2024-11-25
 */
@Slf4j
class UserMapperTest extends BaseTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(e -> log.info(e.toString()));
    }
}