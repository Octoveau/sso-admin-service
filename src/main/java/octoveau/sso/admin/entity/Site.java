package octoveau.sso.admin.entity;

import octoveau.sso.admin.constant.ApprovalStatus;
import octoveau.sso.admin.constant.SiteState;
import octoveau.sso.admin.dto.SiteBasicDTO;
import octoveau.sso.admin.dto.SiteDTO;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.Instant;

/**
 * Site
 *
 * @author yifanzheng
 */
@Entity
@Table(name = "site")
@SQLDelete(sql = "UPDATE synchronizer SET is_deleted = 1 WHERE id = ?")
@Where(clause = "is_deleted = 0 OR is_deleted IS NULL")
public class Site extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1660398082289246129L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_name", columnDefinition = "varchar(30)", nullable = false, unique = true)
    private String siteName;

    @Column(name = "site_key", columnDefinition = "char(32)", nullable = false, unique = true)
    private String siteKey;

    @Column(name = "callback_url", columnDefinition = "varchar(124)", nullable = false)
    private String callbackUrl;

    @Column(name = "state", columnDefinition = "varchar(15)")
    @Enumerated(EnumType.STRING)
    private SiteState state;

    @Column(name = "site_secret", columnDefinition = "char(64)", nullable = false)
    private String siteSecret;

    @Column(name = "approval_status", columnDefinition = "varchar(15)")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @Column(name = "approver", columnDefinition = "varchar(25)")
    private String approver;

    @Column(name = "approve_date")
    private Instant approveDate;

    @Column(name = "is_deleted")
    private Boolean deleted = false;

    public SiteDTO toDTO() {
        SiteDTO siteDTO = new SiteDTO();
        BeanUtils.copyProperties(this, siteDTO);

        return siteDTO;
    }

    public SiteBasicDTO toBasicDTO() {
        SiteBasicDTO siteBasicDTO = new SiteBasicDTO();
        BeanUtils.copyProperties(this, siteBasicDTO);

        return siteBasicDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public SiteState getState() {
        return state;
    }

    public void setState(SiteState state) {
        this.state = state;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteSecret() {
        return siteSecret;
    }

    public void setSiteSecret(String siteSecret) {
        this.siteSecret = siteSecret;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public Instant getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Instant approveDate) {
        this.approveDate = approveDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
