package octoveau.sso.admin.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DeletePermDTO", description="删除权限DTO")
public class DeletePermDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id集合",required = true)
    @NotBlank(message = "id集合不能为空")
    private List<Long> ids;

}
