package octoveau.sso.admin.dto;

import lombok.Data;
import octoveau.sso.admin.constant.ApprovalStatus;
import octoveau.sso.admin.constant.SiteState;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.Instant;

/**
 * SiteInfoDTO
 *
 * @author yifanzheng
 */
@Data
public class SiteDTO {

    private String siteName;

    private String callbackUrl;

    private String siteKey;

    private SiteState state;

    private String siteSecret;

    private ApprovalStatus status;

    private String createBy;

    private Instant createDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

}
