package octoveau.sso.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.Instant;
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

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant createDate;

    @ApiModelProperty(value = "修改者")
    private String lastModifiedBy;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant lastModifiedDate;

    @ApiModelProperty(value = "权限集合")
    private List<PermissionItemInfoVO> perms;

}
