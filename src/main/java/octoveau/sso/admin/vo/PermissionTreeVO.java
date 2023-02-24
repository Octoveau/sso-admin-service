package octoveau.sso.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PermissionTreeVO", description="权限树集合Vo")
public class PermissionTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限组id")
    private Long id;

    @ApiModelProperty(value = "权限组名称")
    private String permGroupName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "权限集合")
    private List<PermissionItemInfoVO> perms;
}
