package octoveau.sso.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="QueryPermissionDTO", description="查询权限条件DTO")
public class QueryPermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限名称")
    private String permName;

    @ApiModelProperty(value = "权限组id")
    private Long id;
}
