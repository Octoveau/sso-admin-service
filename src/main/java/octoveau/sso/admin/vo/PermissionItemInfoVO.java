package octoveau.sso.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PermissionItemInfoVO", description="权限信息VO")
public class PermissionItemInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    private Long id;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限类型")
    private String action;

    @ApiModelProperty(value = "权限值")
    private Integer permValue;

    @ApiModelProperty(value = "所属权限组id-父级id，为0的就是父级")
    private Long parentId;

    @ApiModelProperty(value = "权限组名称+权限类型+权限值组成")
    private String permPath;

}
