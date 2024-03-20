package com.example.model.service.roleAccess;

import com.example.model.entity.RoleAccess;
import com.example.model.entity.RoleAccess_;
import com.example.model.entity.Role_;
import com.example.model.repo.RoleAccessRepo;
import com.example.util.payload.dto.roleAccess.RoleAccessDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

}
