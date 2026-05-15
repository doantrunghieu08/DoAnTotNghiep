package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.GenderType;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Enum.UserStatusType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExcelService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    ClassRepository classRepository;

    @Transactional
    public List<String> importUsersFromExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                try {
                    User user = new User();

                    // 1. Đọc ID và Username
                    String id = formatter.formatCellValue(row.getCell(0)).trim();
                    String username = formatter.formatCellValue(row.getCell(2)).trim();

                    // Check trùng nhanh để báo lỗi chuyên nghiệp
                    if (userRepository.existsById(id)) {
                        errors.add("Dòng " + (i + 1) + ": ID " + id + " đã tồn tại.");
                        continue;
                    }

                    user.setId(id);
                    user.setFullName(formatter.formatCellValue(row.getCell(1)));
                    user.setUsername(username);

                    // 2. Password & Role
                    String rawPass = formatter.formatCellValue(row.getCell(3));
                    user.setPassword(passwordEncoder.encode(rawPass));
                    user.setRole(RoleType.valueOf(formatter.formatCellValue(row.getCell(4)).toUpperCase().trim()));

                    // 3. Birthday (Sếp nhớ vụ LocalDate nhé)
                    String bday = formatter.formatCellValue(row.getCell(5)).trim();
                    if (!bday.isEmpty()) user.setBirthday(LocalDate.parse(bday));

                    user.setEmail(formatter.formatCellValue(row.getCell(6)));
                    user.setGender(GenderType.valueOf(formatter.formatCellValue(row.getCell(7)).toUpperCase().trim()));
                    user.setStatus(UserStatusType.valueOf(formatter.formatCellValue(row.getCell(8)).toUpperCase().trim()));
                    user.setIsRequirePasswordChange(false);
                    userList.add(user);

                } catch (Exception e) {
                    // Nếu dòng này lỗi (sai Enum, sai ngày tháng...), ta gom lỗi lại và chạy tiếp dòng sau
                    errors.add("Dòng " + (i + 1) + ": Dữ liệu không hợp lệ (" + e.getMessage() + ")");
                }
            }

            // CHỐT: Nếu không có lỗi nào mới lưu vào DB
            if (errors.isEmpty() && !userList.isEmpty()) {
                userRepository.saveAll(userList);
            }

        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_FORMAT);
        }

        return errors; // Trả về list lỗi (nếu trống tức là thành công 100%)
    }

    @Transactional
    public List<String> importClassesFromExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        List<Classes> classList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Classes cls = new Classes();
                    cls.setId(formatter.formatCellValue(row.getCell(0)).trim());
                    cls.setClassName(formatter.formatCellValue(row.getCell(1)));
                    cls.setSemester(formatter.formatCellValue(row.getCell(2)));
                    cls.setAcademicYear(formatter.formatCellValue(row.getCell(3)));
                    cls.setRoomNumber(formatter.formatCellValue(row.getCell(4)));

                    // Xử lý Teacher (nếu có mã giảng viên trong cột F)
                    String tId = formatter.formatCellValue(row.getCell(5)).trim();
                    if (!tId.isEmpty()) {
                        userRepository.findById(tId).ifPresent(cls::setTeacher);
                    }

                    classList.add(cls);
                } catch (Exception e) {
                    errors.add("Dòng " + (i + 1) + ": " + e.getMessage());
                }
            }

            if (errors.isEmpty()) {
                classRepository.saveAll(classList);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_FORMAT);
        }
        return errors;
    }

    // Hàm phụ để bỏ qua dòng trắng trong Excel
    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

}
