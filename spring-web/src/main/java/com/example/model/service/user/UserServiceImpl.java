package com.example.model.service.user;

import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.model.repo.UserRepo;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public boolean isUniqueEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public TableResponse<UserListDto> userList(UserSearchDto searchDto) {
        Function<CriteriaBuilder, CriteriaQuery<UserListDto>> queryFunction = cb -> {
            var cq = cb.createQuery(UserListDto.class);
            var root = cq.from(User.class);
            UserListDto.select(cq, root);
            UserListDto.sort(cb, cq, root, searchDto.getSortColumnName(), searchDto.getSortDir());
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };

        Function<CriteriaBuilder, CriteriaQuery<Long>> countFunction = cb -> {
            var cq = cb.createQuery(Long.class);
            var root = cq.from(User.class);
            cq.select(cb.count(root.get(User_.id)));
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };

        var userList = userRepo.findAll(queryFunction, countFunction, searchDto.getPageNo(),
                searchDto.getSize());

        return new TableResponse<>(userRepo.count(), userList.getTotalElements(),
                userList.getTotalPages(), userList.getContent());
    }
}
