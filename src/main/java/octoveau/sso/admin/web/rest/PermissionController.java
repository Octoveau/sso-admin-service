package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.AddPermissionDTO;
import octoveau.sso.admin.dto.EditPermissionDTO;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.entity.PageObject;
import octoveau.sso.admin.service.PermissionService;
import octoveau.sso.admin.vo.PermissionTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = "权限相关接口")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // 增加权限接口
    @ApiOperation(value = "添加权限组与权限",httpMethod = "POST")
    @PostMapping("/perm/add")
    public ResponseDTO<Boolean> addPermission(@RequestBody AddPermissionDTO addPermissionDTO) {
        return ResponseDTO.ok(permissionService.addPermission(addPermissionDTO));
    }
    // 删除权限接口
    @ApiOperation(value = "权限删除", httpMethod = "DELETE")
    @DeleteMapping("/perm/del")
    public ResponseDTO<Boolean> deletedPerm(@RequestBody List<Long> ids) {
        return ResponseDTO.ok(permissionService.deletedPerm(ids));
    }
    // 修改权限接口id
    @ApiOperation(value = "根据权限组id修改权限", httpMethod = "PUT")
    @PutMapping("/perm/update/{id}")
    public ResponseDTO<Boolean> updatePerm(@PathVariable(value = "id") Long id, @RequestBody EditPermissionDTO editPermissionDTO) {
        return ResponseDTO.ok(permissionService.updatePerm(id,editPermissionDTO));
    }
    // 分页查询所有权限接口
    @ApiOperation(value = "获取权限",httpMethod = "GET")
    @GetMapping("/perms/query")
    public ResponseDTO<PageObject<PermissionTreeVO>> queryAllPerm(
            @SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC) Pageable pageable,
            String permName) {
        Page<PermissionTreeVO> permissionPage = permissionService.queryAllPerm(permName,pageable);
        PageObject<PermissionTreeVO> pageObject = PageObject.of(permissionPage.getTotalElements(), permissionPage.getContent());
        return ResponseDTO.ok(pageObject);
    }

}
