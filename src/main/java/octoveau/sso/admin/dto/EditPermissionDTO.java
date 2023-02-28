package octoveau.sso.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="EditPermissionDTO", description="编辑权限DTO")
public class EditPermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限组id", required = true)
    @NotNull(message = "权限组id不能为空")
    private Long id;

    @ApiModelProperty(value = "权限组名称", required = true)
    @NotBlank(message = "权限组名称不能为空")
    private String permGroupName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "权限集合")
    private List<EditPermissionItemDTO> perms;
}
