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
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="EditPermissionItemDTO", description="编辑单条权限DTO")
public class EditPermissionItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    private Long id;

    @ApiModelProperty(value = "权限名称",required = true)
    @NotBlank(message = "权限名称不能为空")
    private String permName;

    @ApiModelProperty(value = "权限类型")
    private String permAction;

    @ApiModelProperty(value = "权限值")
    private String permValue;

    @ApiModelProperty(value = "所属权限组id-父级id，为0的就是父级")
    private Long parentId;
}
