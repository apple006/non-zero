package space.zero.business.module.official.website.service.impl;

import space.zero.business.module.official.website.base.WebsiteDeleteFlagService;
import space.zero.business.module.official.website.dao.OfficialWebsiteProductMapper;
import space.zero.business.module.official.website.model.OfficialWebsiteProduct;
import space.zero.business.module.official.website.service.OfficialWebsiteProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by PG_shen on 2018/09/26.
 */
@Service
@Transactional
public class OfficialWebsiteProductServiceImpl extends WebsiteDeleteFlagService<OfficialWebsiteProduct> implements OfficialWebsiteProductService {
    @Resource
    private OfficialWebsiteProductMapper officialWebsiteProductMapper;

}
