package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.CreationErrorRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ErrorResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ErrorTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorTypeController {
    ErrorTypeService errorTypeService;

    @PostMapping("/api/teacher/error")
    public ResponseEntity<ApiResponse<ErrorResponse>> creationError(@RequestBody CreationErrorRequest request){
        return ResponseEntity.ok(errorTypeService.creationError(request));
    }

    @GetMapping("/api/teacher/error/{idTeacher}")
    public ResponseEntity<ApiResponse<Page<ErrorResponse>>> getErrors(@PathVariable String idTeacher, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")int size){
        return ResponseEntity.ok(errorTypeService.getErrors(idTeacher, page, size));
    }

    @DeleteMapping("/api/teacher/error/{id}")
    public ResponseEntity<ApiResponse<String>> deleteError(@PathVariable int id){
        errorTypeService.deleteError(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xoa loi thanh cong", null));
    }

    @PutMapping("/api/teacher/error/{id}")
    public ResponseEntity<ApiResponse<String>> restoreError(@PathVariable int id){
        errorTypeService.restoreError(id);

        return ResponseEntity.ok(new ApiResponse<>(200, "Khoi phuc thanh cong", null));
    }
}
