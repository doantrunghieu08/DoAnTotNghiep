package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest.CreationClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest.TeachingAssignmentRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest.UpdationClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.ClassMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import com.cloudinary.Api;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassService {
    ClassRepository classRepository;
    ClassMapper classMapper;
    UserRepository userRepository;

    public ApiResponse<ClassResponse> createClass(CreationClassRequest request){
        Classes classes = new Classes();

        classes.setId(request.getId());

        classes.setClassName(request.getClassName());

        classes.setSemester(request.getSemester());

        classes.setAcademicYear(request.getAcademicYear());

        classes.setRoomNumber(request.getRoomNumber());

        classes.setTeacher(null);
        classRepository.save(classes);
        ClassResponse classResponse = classMapper.toResponse(classes);
        return new ApiResponse<>(201, "Thêm lớp học thành công", classResponse);
    }

    public ApiResponse<Page<ClassResponse>> getClassList(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu phân trang từ DB
        Page<Classes> classesPage = classRepository.findAll(pageable);

        // Dùng .map() của Page để chuyển sang DTO mà KHÔNG dùng .getContent()
        Page<ClassResponse> classResponses = classesPage.map(classMapper::toResponse);

        return new ApiResponse<>(200, "Lấy danh sách lớp thành công", classResponses);
    }

    public ApiResponse<ClassResponse> teachingAssignment(TeachingAssignmentRequest request){
        Classes classes = classRepository.findById(request.getIdClass()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        User user = userRepository.findById(request.getIdTeacher()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        if (!user.getRole().equals(RoleType.TEACHER)){
            return new ApiResponse<>(403, "User không phải là giảng viên", null);
        }
        classes.setTeacher(user);
        classRepository.save(classes);
        ClassResponse classResponse = classMapper.toResponse(classes);
        return new ApiResponse<>(200, "Phân công giảng viên thành công", classResponse);
    }

    public ApiResponse<ClassResponse> updateClass(String id,UpdationClassRequest request){
        Classes classes = classRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        classes.setClassName(request.getClassName());
        classes.setSemester(request.getSemester());
        classes.setAcademicYear(request.getAcademicYear());
        classes.setRoomNumber(request.getRoomNumber());
        classRepository.save(classes);
        ClassResponse response = classMapper.toResponse(classes);
        return new ApiResponse<>(200, "Cập nhật thông tin thành công", response);
    }


    public ApiResponse<Map<String, Long>> getStudentCountPerClass() {
        List<Object[]> results = classRepository.countStudentsByClassId();

        // Chuyển List Object sang Map cho FE dễ xài
        Map<String, Long> countMap = results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0], // classId
                        row -> (Long) row[1]    // số lượng
                ));

        return new ApiResponse<>(200, "Thống kê số lượng sinh viên theo lớp", countMap);
    }

    @Transactional
    public List<String> importStudentsToClass(MultipartFile file, String classId) {
        DataFormatter formatter = new DataFormatter();
        List<String> errors = new ArrayList<>();
        // 1. Tìm thông tin lớp trong DB trước
        Classes studentClass = classRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // Đọc ID sinh viên từ file Excel cũ
                    String studentId = formatter.formatCellValue(row.getCell(0)).trim();

                    // 2. Tìm sinh viên đã tồn tại (vì sếp đã add tài khoản trước đó rồi)
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new Exception("Sinh viên chưa có tài khoản hệ thống"));

                    if (student.getRole() != RoleType.STUDENT) {
                        errors.add("Dòng " + (i + 1) + ": Không thể thêm " + student.getRole() + " vào lớp sinh viên!");
                        continue;
                    }

                    // 3. TỰ ĐỘNG GÁN MÃ LỚP (Dù trong Excel không có cột này)
                    student.setStudentClass(studentClass);

                    // Lưu cập nhật
                    userRepository.save(student);

                } catch (Exception e) {
                    errors.add("Dòng " + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_FORMAT);
        }
        return errors;
    }

    public ApiResponse<ClassResponse> removeClass(String id){
        Classes classes = classRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        classRepository.delete(classes);
        return new ApiResponse<>(200, "Xóa lớp thành công", classMapper.toResponse(classes));
    }

    public ApiResponse<Page<ClassResponse>> getClassesByTeacher(String id, int page, int size) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));


        if (!RoleType.TEACHER.equals(user.getRole())) {
            return new ApiResponse<>(ErrorCode.INVALID_ROLE.getCode(), ErrorCode.INVALID_ROLE.getMessage(), null);
        }


        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Classes> classesPage = classRepository.findAllByTeacher(user, pageable);

        Page<ClassResponse> classResponses = classesPage.map(classMapper::toResponse);

        return new ApiResponse<>(200, "Danh sách lớp theo giáo viên thành công", classResponses);
    }
}
