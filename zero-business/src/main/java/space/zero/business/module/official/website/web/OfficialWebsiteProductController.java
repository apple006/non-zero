package space.zero.business.module.official.website.web;

import org.springframework.web.multipart.MultipartFile;
import space.zero.business.module.official.website.model.OfficialWebsiteNews;
import space.zero.business.module.official.website.param.request.CondRequest;
import space.zero.business.module.official.website.param.response.ClazzListResponse;
import space.zero.business.module.official.website.service.OfficialWebsiteClassMateService;
import space.zero.common.utils.FileUploadEnum;
import space.zero.common.utils.FileUploadUtils;
import space.zero.common.utils.StringUtils;
import space.zero.core.result.Result;
import space.zero.core.result.ResultGenerator;
import space.zero.business.module.official.website.model.OfficialWebsiteProduct;
import space.zero.business.module.official.website.service.OfficialWebsiteProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.zero.core.constant.Constant.*;

/**
* Created by PG_shen on 2018/09/26.
*/
@RestController
@RequestMapping("/official/website/product")
public class OfficialWebsiteProductController {
    @Resource
    private OfficialWebsiteProductService officialWebsiteProductService;

    @Resource
    private OfficialWebsiteClassMateService classMateService;

    @PostMapping
    public Result add(@RequestBody OfficialWebsiteProduct officialWebsiteProduct) {
        officialWebsiteProduct.setProductPic(officialWebsiteProduct.getProductPic().replace("http://localhost:8088/", ""));
        OfficialWebsiteProduct tmp = officialWebsiteProductService.save(officialWebsiteProduct);
        return ResultGenerator.genSuccessResult(tmp);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        officialWebsiteProductService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody OfficialWebsiteProduct officialWebsiteProduct) {
        officialWebsiteProduct.setProductPic(officialWebsiteProduct.getProductPic().replace("http://localhost:8088/",""));
        OfficialWebsiteProduct tmp = officialWebsiteProductService.update(officialWebsiteProduct);
        return ResultGenerator.genSuccessResult(tmp);
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {
        OfficialWebsiteProduct officialWebsiteProduct = officialWebsiteProductService.findById(id);
        officialWebsiteProduct.setProductPic("http://localhost:8088/"+officialWebsiteProduct.getProductPic());
        return ResultGenerator.genSuccessResult(officialWebsiteProduct);
    }

    @PostMapping("/list")
    public Result list(@RequestBody CondRequest condRequest) {
        PageHelper.startPage(condRequest.getPage(), condRequest.getSize(), StringUtils.HumpToUnderline(condRequest.getOrder()) + " desc");
        Iterator<String> iterator = condRequest.getCond().keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            if ("isEnable".equals(key) && (condRequest.getCond().get(key).equals("-1") || condRequest.getCond().equals(""))){
                iterator.remove();
            }
            if ("firstHeading".equals(key) && condRequest.getCond().get(key).equals("")){
                iterator.remove();
            }
        }
        List<OfficialWebsiteProduct> list = officialWebsiteProductService.findBy(condRequest.getCond());
//        获取所有的类别,并转成map
        List<ClazzListResponse> productClazz = classMateService.findClazz("PRODUCT");
        Map<String, String> productClazzMap = productClazz.stream().collect(Collectors.toMap(ClazzListResponse::getId, ClazzListResponse::getClazzValue,(k1, k2)->k1));
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.getList().forEach(item ->{
            OfficialWebsiteProduct product = (OfficialWebsiteProduct) item;
            product.setProductPic("http://localhost:8088/"+product.getProductPic());
            product.setProductClass(productClazzMap.get(product.getProductClass()));
        });
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/checkout/{id}")
    public Result checkoutStatus(@PathVariable String id){
        OfficialWebsiteProduct product = officialWebsiteProductService.findById(id);
        if (ENABLE_FLAG_FALSE.equals(product.getIsEnable())){
            product.setIsEnable(ENABLE_FLAG_TRUE);
        }else if (ENABLE_FLAG_TRUE.equals(product.getIsEnable())){
            product.setIsEnable(ENABLE_FLAG_FALSE);
        }
        officialWebsiteProductService.update(product);
        return ResultGenerator.genSuccessResult(product);
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file){
        FileUploadUtils fileUploadUtils = new FileUploadUtils();
        String filePath = null;
        try {
            filePath = "http://localhost:8088/" + fileUploadUtils.uploadFile(file, FileUploadEnum.PRODUCT);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("fail");
        }
        return ResultGenerator.genSuccessResult(filePath, "success");
    }
}
