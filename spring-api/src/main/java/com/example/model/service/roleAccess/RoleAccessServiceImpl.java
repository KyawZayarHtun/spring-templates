package com.example.model.service.roleAccess;

import com.example.model.entity.Role;
import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.model.entity.Role_;
import com.example.model.repo.RoleAccessRepo;
import com.example.model.repo.RoleRepo;
import com.example.model.service.table.TableService;
import com.example.util.payload.dto.roleAccess.*;
import com.example.util.payload.dto.table.TableResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
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
    public List<RoleAccessDto> findRoleAccessByRoleId(Long roleId) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDto>> searchQuery = cb -> {
          var cq = cb.createQuery(RoleAccessDto.class);
          var root = cq.from(RoleAccess.class);
          var role = root.join(RoleAccess_.roles);
          RoleAccessDto.select(cq, root);
          cq.where(cb.equal(role.get(Role_.id), roleId));
          return cq;
        };
        return roleAccessRepo.findAll(searchQuery);
    }

    @Override
    public List<RoleAccessDto> findRoleAccessByRoleName(String roleName) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDto>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessDto.class);
            var root = cq.from(RoleAccess.class);
            var role = root.join(RoleAccess_.roles);
            RoleAccessDto.select(cq, root);
            cq.where(cb.equal(role.get(Role_.name), roleName));
            return cq;
        };
        return roleAccessRepo.findAll(searchQuery);
    }

    @Override
    public Optional<RoleAccessForm> findRoleAccessById(Long id) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessForm>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessForm.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessForm.select(cq, root);
            cq.where(cb.equal(root.get(RoleAccess_.id), id));
            return cq;
        };
        return roleAccessRepo.findOne(searchQuery);
    }

    @Override
    public Optional<RoleAccessForm> findRoleAccessByName(String roleAccessName) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessForm>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleAccessForm.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessForm.select(cq, root);
            cq.where(cb.equal(root.get(RoleAccess_.name), roleAccessName));
            return cq;
        };
        return roleAccessRepo.findOne(searchQuery);
    }

    @Transactional
    @Override
    public void manageRoleAccess(RoleAccessForm dto) {
        if (dto.getId() == null || dto.getId() <= 0) {
            createRoleAccess(dto);
        } else {
            editRoleAccess(dto);
        }
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
    public TableResponse<RoleAccessListDto> getRoleAccessList(RoleAccessSearchDto searchDto) {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessListDto>> searchFunction = cb -> {
            var cq = cb.createQuery(RoleAccessListDto.class);
            var root = cq.from(RoleAccess.class);
            RoleAccessListDto.select(cq, root);
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
    public List<RoleAccessDto> findAllRoleAccess() {
        Function<CriteriaBuilder, CriteriaQuery<RoleAccessDto>> searchQuery = cb -> {
          var cq = cb.createQuery(RoleAccessDto.class);
          var root = cq.from(RoleAccess.class);
          RoleAccessDto.select(cq, root);
          return cq;
        };
        return roleAccessRepo.findAll(searchQuery);
    }

    public List<RoleAccessDetail> convertToRoleAccessDetail(List<RoleAccessDto> roleAccessList) {
        return roleAccessList.stream()
                .collect(Collectors.groupingBy(ra -> RoleAccessDto.getUrlStartName(ra.url())))
                .entrySet().stream()
                .map(RoleAccessDetail::new)
                .sorted((r, r1) -> r1.getRoleAccessList().size() - r.getRoleAccessList().size())
                .toList();
    }

    @Transactional
    @Override
    public void deleteRoleAccessWithAllInheritance(Long roleAccessId) {
//        Function<CriteriaBuilder, CriteriaQuery<>>Role
        var ra = roleAccessRepo.findById(roleAccessId).orElseThrow();
        var raIdList = ra.getRoles().stream().map(Role::getId).toList();
        raIdList.forEach(id -> {
            var role = roleRepo.findById(id).orElseThrow();
            var newRoleAccessWithoutGivenRoleAccessId = role.getRoleAccesses().stream().filter(roleAccess -> !roleAccess.getId().equals(roleAccessId)).toList();
            role.setRoleAccesses(newRoleAccessWithoutGivenRoleAccessId);
        });
        roleAccessRepo.deleteById(roleAccessId);
    }

    private void editRoleAccess(RoleAccessForm dto) {
        var roleAccess = roleAccessRepo.findById(dto.getId()).orElseThrow();
        roleAccess.setName(dto.getName());
        roleAccess.setUrl(dto.getUrl());
        roleAccess.setRequestMethod(dto.getRequestMethod());
        roleAccess.setCrudOperation(dto.getRequestOperation());
        roleAccess.setDescription(dto.getDescription());
    }

    private void createRoleAccess(RoleAccessForm dto) {
        var roleAccess = new RoleAccess();
        roleAccess.setName(dto.getName());
        roleAccess.setUrl(dto.getUrl());
        roleAccess.setRequestMethod(dto.getRequestMethod());
        roleAccess.setCrudOperation(dto.getRequestOperation());
        roleAccess.setDescription(dto.getDescription());
        roleAccessRepo.save(roleAccess);
    }


}
