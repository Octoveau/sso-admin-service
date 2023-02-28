package octoveau.sso.admin.service;

import octoveau.sso.admin.dto.AddPermissionDTO;
import octoveau.sso.admin.dto.EditPermissionDTO;
import octoveau.sso.admin.dto.QueryPermissionDTO;
import octoveau.sso.admin.entity.Perm;
import octoveau.sso.admin.exception.AlreadyExistsException;
import octoveau.sso.admin.exception.NotFoundException;
import octoveau.sso.admin.exception.ServiceException;
import octoveau.sso.admin.repository.PermissionRepository;
import octoveau.sso.admin.security.SecurityUtils;
import octoveau.sso.admin.vo.PermissionItemInfoVO;
import octoveau.sso.admin.vo.PermissionTreeVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


    @Transactional(rollbackFor = Exception.class)
    public Boolean addPermission(AddPermissionDTO addPermissionDTO){
        // 权限组名称唯一
        List<Perm> permGroups = permissionRepository.findByPermNameAndParentId(addPermissionDTO.getPermGroupName(), 0L);
        if (!CollectionUtils.isEmpty(permGroups)) {
            throw new ServiceException("权限名称已存在");
        }
        // 添加权限组
        Perm permGroup = new Perm();
        permGroup.setPermName(addPermissionDTO.getPermGroupName());
        permGroup.setRemark(addPermissionDTO.getRemark());
        permGroup.setParentId(0L);
        permissionRepository.saveAndFlush(permGroup);
        // 添加权限
        List<Perm> perms = addPermissionDTO.getPerms().stream().map(permission -> {
            Perm perm = new Perm();
            BeanUtils.copyProperties(permission, perm);
            perm.setParentId(permGroup.getId());
            return perm;
        }).collect(Collectors.toList());
        permissionRepository.saveAll(perms);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deletedPerm(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("请选择至少一个权限进行删除");
        }
        permissionRepository.deleteAllByIdInBatch(ids);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePerm(Long id, EditPermissionDTO editPermissionDTO) {
        // 查询权限以及权限组信息
        List<Perm> permGroups = permissionRepository.findAllByIdOrParentId(id,id);
        if (CollectionUtils.isEmpty(permGroups)) {
            throw new NotFoundException("Not found perm by id: " + id);
        }
        List<Perm> permGroupByNewName = permissionRepository.findByPermNameAndParentId(editPermissionDTO.getPermGroupName(), 0L);
        // 若存在除本权限组以外与修改的权限组名称重复的权限组，抛出错误
        if (!CollectionUtils.isEmpty(permGroupByNewName) && !permGroupByNewName.get(0).getId().equals(id)) {
            throw new AlreadyExistsException(String.format(
                    "The permGroupName[%s] already exists.",
                    editPermissionDTO.getPermGroupName()));
        }
        // 获取原权限组信息
        Perm permGroup = permGroups.stream().filter(item ->
                item.getParentId().equals(0L)).collect(Collectors.toList()).get(0);
        permGroup.setPermName(editPermissionDTO.getPermGroupName());
        permGroup.setRemark(editPermissionDTO.getRemark());
        // 修改权限组信息
        permissionRepository.saveAndFlush(permGroup);
        String userLogin = SecurityUtils.getCurrentUserLogin().orElse("system");
        List<Perm> permList = editPermissionDTO.getPerms().stream().map(item -> {
            Perm perm = new Perm();
            BeanUtils.copyProperties(item,perm);
            perm.setParentId(permGroup.getId());
            perm.setCreateBy(userLogin);
            perm.setCreateDate(Instant.now());
            perm.setLastModifiedDate(Instant.now());
            return perm;
        }).collect(Collectors.toList());
        permissionRepository.saveAll(permList);
        return Boolean.TRUE;
    }

    public Page<PermissionTreeVO> queryAllPerm(QueryPermissionDTO queryPermissionDTO, Pageable pageable) {
        Specification<Perm> specification = getSpecification(queryPermissionDTO);
        Page<Perm> page = permissionRepository.findAll(specification,pageable);
        if (CollectionUtils.isEmpty(page.getContent())) {
            return page.map(Perm::toPermTreeVO);
        }
        List<Long> permGroupId = page.getContent().stream().map(Perm::getId).collect(Collectors.toList());
        // 查询所有的权限
        List<Perm> permList = permissionRepository.findAllByParentIdIn(permGroupId);
        if (CollectionUtils.isEmpty(permList)) {
            return page.map(Perm::toPermTreeVO);
        }
        Map<Long, List<Perm>> permGroupMap = page.getContent().stream().collect(Collectors.groupingBy(Perm::getId));
        // 组装权限组与权限数据
        Map<Long, List<PermissionItemInfoVO>> permMap = permList.stream().map(perm -> {
            PermissionItemInfoVO permissionItemInfoVO = new PermissionItemInfoVO();
            BeanUtils.copyProperties(perm, permissionItemInfoVO);
            String permPath = permGroupMap.get(perm.getParentId()).get(0).getPermName() + "-"
                    + perm.getPermAction() + "-" + perm.getPermValue();
            permissionItemInfoVO.setPermPath(permPath);
            return permissionItemInfoVO;
        }).collect(Collectors.groupingBy(PermissionItemInfoVO::getParentId));
        Page<PermissionTreeVO> permissionTreeVOPage = page.map(Perm::toPermTreeVO);
        permissionTreeVOPage.forEach(item -> {
            item.setPerms(permMap.get(item.getId()));
        });
        return permissionTreeVOPage;
    }

    private Specification<Perm> getSpecification(QueryPermissionDTO queryPermissionDTO) {
        Specification<Perm> specification = new Specification<Perm>() {
            @Override
            public Predicate toPredicate(Root<Perm> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(queryPermissionDTO.getPermName())) {
                    predicates.add(criteriaBuilder.like(root.get("permName"), "%"+ queryPermissionDTO.getPermName() +"%"));
                }
                if (!ObjectUtils.isEmpty(queryPermissionDTO.getId())) {
                    predicates.add(criteriaBuilder.equal(root.get("id"),queryPermissionDTO.getId()));
                }
                predicates.add(criteriaBuilder.equal(root.get("parentId"), 0L));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

}
