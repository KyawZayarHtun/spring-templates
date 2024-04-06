package com.example.model.service.roleAccess;

import com.example.model.entity.Role;
import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.model.entity.Role_;
import com.example.model.repo.RoleAccessRepo;
import com.example.model.repo.RoleRepo;
import com.example.model.service.table.TableService;
import com.example.util.payload.dto.role.RoleWithRoleAccessList;
import com.example.util.payload.dto.roleAccess.*;
import com.example.util.payload.dto.table.TableResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleAccessServiceImpl implements RoleAccessService {

    private final RoleAccessRepo roleAccessRepo;
    private final TableService tableService;
    private final RoleRepo roleRepo;

    @Override
    public List<RoleAccessDetail> findRoleAccessByRoleId(Long roleId) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDetail>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessDetail.class);
            var root = cq.from(RoleAccess.class);
            var role = root.join(RoleAccess_.roles);
            RoleAccessDetail.select(cq, root);
            cq.where(cb.equal(role.get(Role_.id), roleId));
            return cq;
        };
        return roleAccessRepo.findAll(searchQuery);
    }

    @Override
    public List<RoleAccessDetail> findRoleAccessByRoleName(String roleName) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDetail>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessDetail.class);
            var root = cq.from(RoleAccess.class);
            var role = root.join(RoleAccess_.roles);
            RoleAccessDetail.select(cq, root);
            cq.where(cb.equal(role.get(Role_.name), roleName));
            return cq;
        };
        return roleAccessRepo.findAll(searchQuery);
    }

    @Override
    public Optional<RoleAccessDetail> findRoleAccessById(Long id) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDetail>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessDetail.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessDetail.select(cq, root);
            cq.where(cb.equal(root.get(RoleAccess_.id), id));
            return cq;
        };
        return roleAccessRepo.findOne(searchQuery);
    }

    @Override
    public Optional<RoleAccessDetail> findRoleAccessByName(String roleAccessName) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDetail>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessDetail.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessDetail.select(cq, root);
            cq.where(cb.equal(root.get(RoleAccess_.name), roleAccessName));
            return cq;
        };
        return roleAccessRepo.findOne(searchQuery);
    }

    @Override
    public boolean roleAccessNameExists(@Nullable Long id, String roleAccessName) {

        if (id == null) {
            return roleAccessRepo.existsByName(roleAccessName);
        }

        var roleAccessById = roleAccessRepo.findById(id);
        if (roleAccessById.isPresent()) {
            var roleAccess = roleAccessById.get();
            if (roleAccess.getName().equalsIgnoreCase(roleAccessName)) {
                return false;
            } else {
                return findRoleAccessByName(roleAccessName).isPresent();
            }
        }

        return false;
    }

    @Override
    public TableResponse<RoleAccessDetail> getRoleAccessList(RoleAccessSearchDto searchDto) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDetail>> searchFunction = cb -> {
            var cq = cb.createQuery(RoleAccessDetail.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessDetail.select(cq, root);
            tableService.sort(cb, cq, root, searchDto.getSortColumnName(), searchDto.getSortDir());
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };

        Function<CriteriaBuilder, CriteriaQuery<Long>> countFunction = cb -> {
            var cq = cb.createQuery(Long.class);
            var root = cq.from(RoleAccess.class);
            cq.select(cb.count(root.get(Role_.id)));
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };

        var roleAccessList = roleAccessRepo.findAll(searchFunction, countFunction, searchDto.getPageNo(), searchDto.getSize());

        return new TableResponse<>(roleAccessRepo.count(), roleAccessList.getTotalElements(),
                roleAccessList.getTotalPages(), roleAccessList.getContent());
    }

    @Override
    public List<RoleAccessDetail> findAllRoleAccess() {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDetail>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessDetail.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessDetail.select(cq, root);
            return cq;
        };
        return roleAccessRepo.findAll(searchQuery);
    }

    public List<RoleWithRoleAccessList> convertToRoleAccessDetail(List<RoleAccessDetail> roleAccessList) {
        return roleAccessList.stream()
                .collect(Collectors.groupingBy(ra -> RoleAccessDetail.getUrlStartName(ra.url())))
                .entrySet().stream()
                .map(RoleWithRoleAccessList::new)
                .sorted((r, r1) -> r1.getRoleAccessList().size() - r.getRoleAccessList().size())
                .toList();
    }

    @Transactional
    @Override
    public void deleteRoleAccessWithAllInheritance(Long roleAccessId) {
        var ra = roleAccessRepo.findById(roleAccessId).orElseThrow();
        var raIdList = ra.getRoles().stream().map(Role::getId).toList();
        raIdList.forEach(id -> {
            var role = roleRepo.findById(id).orElseThrow();
            var newRoleAccessWithoutGivenRoleAccessId = role.getRoleAccesses().stream().filter(roleAccess -> !roleAccess.getId().equals(roleAccessId)).toList();
            role.setRoleAccesses(newRoleAccessWithoutGivenRoleAccessId);
        });
        roleAccessRepo.deleteById(roleAccessId);
    }

    @Override
    @Transactional
    public Long updateRoleAccess(RoleAccessUpdateForm dto) throws BadRequestException {
        var roleAccess = roleAccessRepo.findById(dto.id()).orElseThrow(() -> new BadRequestException("Given id doesn't exist!"));
        roleAccess.setName(dto.name());
        roleAccess.setUrl(roleAccessUrlWithSlash(dto.url()));
        roleAccess.setRequestMethod(dto.requestMethod());
        roleAccess.setCrudOperation(dto.requestOperation());
        roleAccess.setDescription(dto.description());
        return dto.id();
    }

    @Override
    @Transactional
    public Long createRoleAccess(RoleAccessCreateForm dto) {
        var roleAccess = new RoleAccess();
        roleAccess.setName(dto.name());
        roleAccess.setUrl(roleAccessUrlWithSlash(dto.url()));
        roleAccess.setRequestMethod(dto.requestMethod());
        roleAccess.setCrudOperation(dto.requestOperation());
        roleAccess.setDescription(dto.description());
        return roleAccessRepo.save(roleAccess).getId();
    }

    private String roleAccessUrlWithSlash(String url) {
        if (!url.startsWith("/"))
           return "/".concat(url);
        return url;
    }


}
