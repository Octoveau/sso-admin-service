package octoveau.sso.admin.web.rest.open;

import io.swagger.annotations.Api;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.dto.SiteBasicDTO;
import octoveau.sso.admin.exception.NotFoundException;
import octoveau.sso.admin.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * SiteOpenResource
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/openapi/sites")
@Api(tags = "Site Open Resource")
public class SiteOpenResource {

    @Autowired
    private SiteService siteService;

    @GetMapping("/{siteKey}/basicInfo")
    public ResponseDTO<SiteBasicDTO> getSiteBasicInfo(@PathVariable("siteKey") String siteKey) {
        Optional<SiteBasicDTO> siteBasicOptional = siteService.getSiteBasic(siteKey);
        return ResponseDTO.ok(siteBasicOptional
                .orElseThrow(() -> new NotFoundException("Not found site by siteKey: " + siteKey)));
    }
}
