package com.example.model.service.user;

import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.model.repo.UserRepo;
import com.example.model.service.document.excel.exported.ExcelService;
import com.example.model.service.image.ImageService;
import com.example.util.exception.LoginUserNotFoundException;
import com.example.util.payload.dto.document.excel.ExcelColumn;
import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.document.excel.ExcelRow;
import com.example.util.payload.dto.document.excel.ExcelSetting;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserDetailDtoForProfile;
import com.example.util.payload.dto.user.UserDetailDtoForSecurity;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ExcelService excelService;
    private final ImageService imageService;

    @Override
    public boolean isUniqueEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public TableResponse<UserListDto> userList(UserSearchDto searchDto) {
        var queryFunction = userSearchQuery(searchDto);

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

    @Override
    public ExcelExportHeadersAndByteStream generateExcelExport(UserSearchDto searchDto,
                                                               HttpServletResponse response) throws IOException {
        var excelSetting = new ExcelSetting();
        excelSetting.setTitle("User List");
        excelSetting.setSheetName("User");

        var userList = userRepo.findAll(userSearchQuery(searchDto));

        for (var dto : userList) {
            var excelRow = new ExcelRow();
            excelRow.addExcelColumn(
                    new ExcelColumn(0, "Name", dto.name()),
                    new ExcelColumn(1, "Email", dto.email()),
                    new ExcelColumn(2, "Phone Number", dto.phoneNo())
            );
            excelSetting.getRows().add(excelRow);
        }

        excelSetting.setExcludeAutoSizeColumn(List.of(2));
        return excelService.generateExcel(excelSetting, response);
    }

    @Override
    public Optional<UserDetailDtoForSecurity> getUserDetailByEmail(String email) {
        Function<CriteriaBuilder, CriteriaQuery<UserDetailDtoForSecurity>> searchByEmail = cb -> {
            var cq = cb.createQuery(UserDetailDtoForSecurity.class);
            var root = cq.from(User.class);
            UserDetailDtoForSecurity.select(cq, root);
            cq.where(cb.equal(root.get(User_.email), email));
            return cq;
        };
        return userRepo.findOne(searchByEmail);
    }

    @Override
    public String getLoginUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            throw new LoginUserNotFoundException("You need to login first!");

        return auth.getName();
    }

    @Override
    public UserDetailDtoForProfile getUserProfile() {

        Function<CriteriaBuilder, CriteriaQuery<UserDetailDtoForProfile>> searchQuery = cb -> {
            var cq = cb.createQuery(UserDetailDtoForProfile.class);
            var root = cq.from(User.class);
            UserDetailDtoForProfile.select(cq, root);
            cq.where(cb.equal(root.get(User_.email), getLoginUserEmail()));
            return cq;
        };

        return userRepo.findOne(searchQuery).orElseThrow();
    }

    @Override
    public String getLoginUsername() {
        Function<CriteriaBuilder, CriteriaQuery<String>> searchQuery = cb -> {
            var cq = cb.createQuery(String.class);
            var root = cq.from(User.class);
            cq.select(root.get(User_.name));
            cq.where(cb.equal(root.get(User_.email), getLoginUserEmail()));
            return cq;
        };
        return userRepo.findOne(searchQuery).orElseThrow();
    }

    @Transactional
    @Override
    public void updateUserProfile(UserDetailDtoForProfile userDetail) throws IOException {

        var currentUser = userRepo.findByEmail(getLoginUserEmail()).orElseThrow();
        currentUser.setName(userDetail.getName());
        currentUser.setPhoneNo(userDetail.getPhoneNo());
        currentUser.setDob(userDetail.getDob());

        var fileName = imageService.saveImageToDesireLocation("profile", userDetail.getProfileImage());

        if (StringUtils.hasLength(fileName))
            currentUser.setProfileImagePath(fileName);
    }

    Function<CriteriaBuilder, CriteriaQuery<UserListDto>> userSearchQuery(UserSearchDto searchDto) {
        return cb -> {
            var cq = cb.createQuery(UserListDto.class);
            var root = cq.from(User.class);
            UserListDto.select(cq, root);
            UserListDto.sort(cb, cq, root, searchDto.getSortColumnName(), searchDto.getSortDir());
            cq.where(searchDto.predicates(cb, root));
            return cq;
        };
    }
}
