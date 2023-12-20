package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.dao.GenCodeDao;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shisong02
 * @since 2023-12-19
 */
@Service
public class GenCodeServiceImpl implements GenCodeService {

    @Resource
    private GenCodeDao genCodeDao;

    @Override
    public List<TableInfo> queryAllTableList() {
        return genCodeDao.selectAllTableList();
    }
}
