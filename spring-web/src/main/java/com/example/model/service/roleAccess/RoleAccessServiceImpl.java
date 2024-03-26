package com.example.model.service.roleAccess;

import com.example.model.entity.Role;
import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.model.entity.Role_;
import com.example.model.repo.RoleAccessRepo;
import com.example.model.service.table.TableService;
import com.example.util.payload.dto.role.RoleListDto;
import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import com.example.util.payload.dto.roleAccess.RoleAccessForm;
import com.example.util.payload.dto.roleAccess.RoleAccessListDto;
import com.example.util.payload.dto.roleAccess.RoleAccessSearchDto;
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

@Service
@RequiredArgsConstructor
public class RoleAccessServiceImpl implements RoleAccessService {

    private final RoleAccessRepo roleAccessRepo;
    private final TableService tableService;

    @Override
    public List<RoleAccessDto> findRoleAccessByRole(String roleName) {
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

    private void editRoleAccess(RoleAccessForm dto) {
        var roleAccess = roleAccessRepo.findById(dto.getId()).orElseThrow();
        roleAccess.setName(dto.getName());
        roleAccess.setUrl(dto.getUrl());
        roleAccess.setRequestMethod(dto.getRequestMethod());
        roleAccess.setDescription(dto.getDescription());
    }

    private void createRoleAccess(RoleAccessForm dto) {
        var roleAccess = new RoleAccess();
        roleAccess.setName(dto.getName());
        roleAccess.setUrl(dto.getUrl());
        roleAccess.setRequestMethod(dto.getRequestMethod());
        roleAccess.setDescription(dto.getDescription());
        roleAccessRepo.save(roleAccess);
    }


}
