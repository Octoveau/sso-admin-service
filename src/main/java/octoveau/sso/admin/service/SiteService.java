package octoveau.sso.admin.service;

import octoveau.sso.admin.constant.ApprovalStatus;
import octoveau.sso.admin.constant.SiteState;
import octoveau.sso.admin.dto.SiteBasicDTO;
import octoveau.sso.admin.dto.SiteDTO;
import octoveau.sso.admin.entity.Site;
import octoveau.sso.admin.exception.AlreadyExistsException;
import octoveau.sso.admin.exception.BadRequestAlertException;
import octoveau.sso.admin.exception.NotFoundException;
import octoveau.sso.admin.exception.ServiceException;
import octoveau.sso.admin.repository.SiteRepository;
import octoveau.sso.admin.util.IDGeneratorUtil;
import octoveau.sso.admin.web.rest.request.SiteQueryRequest;
import octoveau.sso.admin.web.rest.request.SiteRequest;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * SiteService
 *
 * @author yifanzheng
 */
@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    public Page<SiteDTO> querySites(SiteQueryRequest queryRequest, Pageable pageable) {
        Page<Site> page;
        if (StringUtils.isEmpty(queryRequest.getSiteName())) {
            page = siteRepository.findAll(pageable);
        } else {
            page = siteRepository.findAllBySiteNameLike(queryRequest.getSiteName(), pageable);
        }
        return page.map(Site::toDTO);
    }

    public Optional<SiteDTO> getSite(String siteKey) {
        Optional<Site> siteOptional = siteRepository.findBySiteKey(siteKey);
        return siteOptional.map(Site::toDTO);
    }

    public Optional<SiteBasicDTO> getSiteBasic(String siteKey) {
        Optional<Site> siteOptional = siteRepository.findBySiteKey(siteKey);
        return siteOptional.map(Site::toBasicDTO);
    }

    public void save(SiteRequest siteRequest) {
        Site site = new Site();
        site.setSiteName(siteRequest.getSiteName());
        site.setCallbackUrl(siteRequest.getCallbackUrl());
        site.setState(SiteState.DISBALE);
        site.setSiteKey(IDGeneratorUtil.generateUUID());
        site.setSiteSecret(String.format("%s%s", IDGeneratorUtil.generateUUID(), IDGeneratorUtil.generateUUID()));
        site.setStatus(ApprovalStatus.PENDING);

        try {
            siteRepository.save(site);
        } catch (DataIntegrityViolationException e) {
            // 判断是否是约束异常
            throw new AlreadyExistsException(String.format(
                    "The Site[%s] already exists.",
                    siteRequest.getSiteName()));
        } catch (Exception e) {
            throw new ServiceException(String.format("Save Site[%s] failed", siteRequest.getSiteName()), e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(String siteKey, SiteRequest siteRequest) {
        Optional<Site> siteOptional = siteRepository.findBySiteKey(siteKey);
        if (!siteOptional.isPresent()) {
            throw new NotFoundException("Not found site by siteKey: " + siteKey);
        }
        Site site = siteOptional.get();
        site.setSiteName(siteRequest.getSiteName());
        site.setCallbackUrl(siteRequest.getCallbackUrl());
        site.setLastModifiedDate(Instant.now());
        try {
            siteRepository.save(site);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException(String.format(
                    "The Site[%s] already exists.",
                    siteRequest.getSiteName()));
        } catch (Exception e) {
            throw new ServiceException(String.format("Update Site[%s] failed", siteRequest.getSiteName()), e);
        }
    }

    public void delete(String siteKey) {
        Optional<Site> siteOptional = siteRepository.findBySiteKey(siteKey);
        if (!siteOptional.isPresent()) {
            throw new NotFoundException("Not found site by siteKey: " + siteKey);
        }
        // 仅做非物理删除
        siteRepository.delete(siteOptional.get());
    }


}
