package com.example.model.service.user;

import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.model.repo.UserRepo;
import com.example.model.service.document.excel.exported.ExcelService;
import com.example.util.payload.dto.document.excel.ExcelColumn;
import com.example.util.payload.dto.document.excel.ExcelExportHeadersAndByteStream;
import com.example.util.payload.dto.document.excel.ExcelRow;
import com.example.util.payload.dto.document.excel.ExcelSetting;
import com.example.util.payload.dto.table.TableResponse;
import com.example.util.payload.dto.user.UserListDto;
import com.example.util.payload.dto.user.UserSearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ExcelService service;

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
        return service.generateExcel(excelSetting, response);
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
