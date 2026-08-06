package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Wrapper dùng để cache dữ liệu phân trang vào Redis.
 * Spring's Page<T> là interface, Jackson không thể deserialize lại được.
 * Class này lưu các thông tin cần thiết của Page dưới dạng plain object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    /**
     * Tạo PagedResponse từ Spring Page object.
     */
    public static <T> PagedResponse<T> of(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
