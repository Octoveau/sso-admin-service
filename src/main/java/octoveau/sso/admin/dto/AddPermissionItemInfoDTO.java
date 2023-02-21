package octoveau.sso.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AddPermissionItemInfoDTO", description="添加单个权限DTO")
public class AddPermissionItemInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限名称",required = true)
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @ApiModelProperty(value = "权限类型")
    private String action;

    @ApiModelProperty(value = "权限值")
    private Integer permValue;

    @ApiModelProperty(value = "所属权限组id-父级id，为0的就是父级")
    private Long parentId;
}
