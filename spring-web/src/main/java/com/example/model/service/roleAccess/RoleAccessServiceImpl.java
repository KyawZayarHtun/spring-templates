package com.example.model.service.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.model.entity.Role_;
import com.example.model.repo.RoleAccessRepo;
import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import com.example.util.payload.dto.roleAccess.RoleAccessForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RoleAccessServiceImpl implements RoleAccessService {

    private final RoleAccessRepo roleAccessRepo;

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
    public boolean roleAccessNameExists(String roleAccessName) {
        return !roleAccessRepo.existsByName(roleAccessName);
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
