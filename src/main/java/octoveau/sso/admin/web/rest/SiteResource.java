package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.dto.SiteDTO;
import octoveau.sso.admin.entity.PageObject;
import octoveau.sso.admin.exception.NotFoundException;
import octoveau.sso.admin.service.SiteService;
import octoveau.sso.admin.web.rest.request.SiteQueryRequest;
import octoveau.sso.admin.web.rest.request.SiteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * SiteResource
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Site Resource")
public class SiteResource {

    @Autowired
    private SiteService siteService;

    @GetMapping("/sites")
    @ApiOperation(value = "查询站点列表")
    public ResponseDTO<PageObject<SiteDTO>> listSites(
            @SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC) Pageable pageable,
            SiteQueryRequest queryParam) {
        Page<SiteDTO> sitePage = siteService.querySites(queryParam, pageable);
        PageObject<SiteDTO> pageObject = PageObject.of(sitePage.getTotalElements(), sitePage.getContent());

        return ResponseDTO.ok(pageObject);
    }

    @GetMapping("/sites/{siteKey}/info")
    @ApiOperation(value = "获取站点信息")
    public ResponseDTO<SiteDTO> getSiteInfo(@PathVariable("siteKey") String siteKey) {
        Optional<SiteDTO> siteOptional = siteService.getSite(siteKey);
        return ResponseDTO.ok(siteOptional
                .orElseThrow(() -> new NotFoundException("Not found site by siteKey: " + siteKey)));
    }

    @PostMapping("/sites")
    @ApiOperation(value = "创建站点信息")
    public ResponseDTO<Void> createSite(@RequestBody SiteRequest siteRequest) {
        siteService.save(siteRequest);
        return ResponseDTO.ok();
    }

    @PutMapping("/sites/{siteKey}")
    @ApiOperation(value = "更新站点信息")
    public ResponseDTO<Void> updateSite(@PathVariable("siteKey") String siteKey,
                                        @RequestBody SiteRequest siteRequest) {
        siteService.update(siteKey, siteRequest);
        return ResponseDTO.ok();
    }

    @DeleteMapping("/sites/{siteKey}")
    @ApiOperation(value = "删除站点信息")
    public ResponseDTO<Void> deleteSite(@PathVariable("siteKey") String siteKey) {
        siteService.delete(siteKey);
        return ResponseDTO.ok();
    }

}
