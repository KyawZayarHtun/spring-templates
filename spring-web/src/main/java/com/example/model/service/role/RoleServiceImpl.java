package com.example.model.service.role;

import com.example.model.entity.Role;
import com.example.model.entity.RoleAccess;
import com.example.model.entity.Role_;
import com.example.model.repo.RoleAccessRepo;
import com.example.model.repo.RoleRepo;
import com.example.model.service.table.TableService;
import com.example.util.exception.RoleNotFoundException;
import com.example.util.payload.dto.role.RoleForm;
import com.example.util.payload.dto.role.RoleListDto;
import com.example.util.payload.dto.role.RoleSearchDto;
import com.example.util.payload.dto.roleAccess.RoleAccessByRoleForm;
import com.example.util.payload.dto.table.TableResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final TableService tableService;
    private final RoleAccessRepo roleAccessRepo;

    @Override
    public String getCurrentUserRoleName() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return getRoleNameFromAuthentication(auth);
    }

    @Override
    public String getCurrentUserRoleName(Authentication auth) {
        return getRoleNameFromAuthentication(auth);
    }

    @Override
    public Optional<RoleForm> findByRoleId(Long id) {
        Function<CriteriaBuilder, CriteriaQuery<RoleForm>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleForm.class);
            var root = cq.from(Role.class);
            cq.multiselect(
                    root.get(Role_.id),
                    root.get(Role_.name)
            );
            cq.where(cb.equal(root.get(Role_.id), id));
            return cq;
        };
        return roleRepo.findOne(searchQuery);
    }

    @Override
    public Optional<RoleForm> findByRoleName(String name) {
        Function<CriteriaBuilder, CriteriaQuery<RoleForm>> searchQuery = cb -> {
            var cq = cb.createQuery(RoleForm.class);
            var root = cq.from(Role.class);
            cq.multiselect(
                    root.get(Role_.id),
                    root.get(Role_.name)
            );
            cq.where(cb.equal(root.get(Role_.name), name));
            return cq;
        };
        return roleRepo.findOne(searchQuery);
    }

    @Transactional
    @Override
    public void manageRole(RoleForm dto) {
        if (dto.getId() == null || dto.getId() <= 0) {
            createRole(dto.getName());
        } else {
            editRole(dto);
        }
    }

    private void editRole(RoleForm dto) {
        var role = roleRepo.findById(dto.getId()).orElseThrow();
        role.setName(dto.getName());
    }

    private void createRole(String name) {
        var role = new Role();
        role.setName(name);
        roleRepo.save(role);
    }

    @Override
    public TableResponse<RoleListDto> getRoleList(RoleSearchDto searchDto) {
        Function<CriteriaBuilder, CriteriaQuery<RoleListDto>> searchFunction = cb -> {
            var cq = cb.createQuery(RoleListDto.class);
            var root = cq.from(Role.class);
            RoleListDto.select(cq, root);
            tableService.sort(cb, cq, root, searchDto.getSortColumnName(), searchDto.getSortDir());
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };

        Function<CriteriaBuilder, CriteriaQuery<Long>> countFunction = cb -> {
            var cq = cb.createQuery(Long.class);
            var root = cq.from(Role.class);
            cq.select(cb.count(root.get(Role_.id)));
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };

        var roleList = roleRepo.findAll(searchFunction, countFunction, searchDto.getPageNo(), searchDto.getSize());

        return new TableResponse<>(roleRepo.count(), roleList.getTotalElements(),
                roleList.getTotalPages(), roleList.getContent());
    }

    @Override
    public boolean roleNameExist(String roleName) {
        return !roleRepo.existsByName(roleName);
    }

    @Override
    public boolean roleNameExist(@Nullable Long id, String roleName) {
        if (id == null)
            return roleRepo.existsByName(roleName);

        var roleById = roleRepo.findById(id);
        if (roleById.isPresent()) {
            var role = roleById.get();
            if (role.getName().equalsIgnoreCase(roleName)) {
                return false;
            } else {
                return findByRoleName(roleName).isPresent();
            }
        }

        return false;
    }

    @Transactional
    @Override
    public void saveRoleAccessByRoleId(RoleAccessByRoleForm form) {
        var role = roleRepo.findById(form.getRoleId()).orElseThrow();
        List<RoleAccess> roleAccesses = new ArrayList<>();
        form.getIdList().forEach(id -> roleAccesses.add(roleAccessRepo.findById(id).orElseThrow()));
        role.setRoleAccesses(roleAccesses);
    }

    private String getRoleNameFromAuthentication(Authentication auth) {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
                .orElseThrow(RoleNotFoundException::new);
    }
}
