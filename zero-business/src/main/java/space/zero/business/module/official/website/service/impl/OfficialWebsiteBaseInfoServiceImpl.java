package space.zero.business.module.official.website.service.impl;

import space.zero.business.module.official.website.dao.OfficialWebsiteBaseInfoMapper;
import space.zero.business.module.official.website.model.OfficialWebsiteBaseInfo;
import space.zero.business.module.official.website.service.OfficialWebsiteBaseInfoService;
import space.zero.core.service.AbstractDeleteFlagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by PG_shen on 2018/09/26.
 */
@Service
@Transactional
public class OfficialWebsiteBaseInfoServiceImpl extends AbstractDeleteFlagService<OfficialWebsiteBaseInfo> implements OfficialWebsiteBaseInfoService {
    @Resource
    private OfficialWebsiteBaseInfoMapper officialWebsiteBaseInfoMapper;

}
