package com.NOK_NOK.order.controller;

import com.NOK_NOK.order.domain.dto.CategoryRequest;
import com.NOK_NOK.order.domain.dto.CategoryResponse;
import com.NOK_NOK.order.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "필터링 조건에 따라 카테고리 목록을 조회합니다")
    public ResponseEntity<List<CategoryResponse.CategoryList>> getCategories(
            @Parameter(description = "활성 상태 필터") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword
    ) {
        log.info("카테고리 목록 조회 요청 - 활성: {}, 키워드: {}", isActive, keyword);

        CategoryRequest.Search request = CategoryRequest.Search.builder()
                .keyword(keyword)
                .build();

        List<CategoryResponse.CategoryList> categories = categoryService.getCategories(request);
        
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 상세 조회", description = "특정 카테고리의 상세 정보를 조회합니다")
    public ResponseEntity<CategoryResponse.CategoryDetail> getCategoryDetail(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId
    ) {
        log.info("카테고리 상세 조회 요청 - ID: {}", categoryId);

        CategoryResponse.CategoryDetail category = categoryService.getCategoryDetail(categoryId);
        
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다")
    public ResponseEntity<CategoryResponse.CategoryDetail> createCategory(
            @Valid @RequestBody CategoryRequest.Create request
    ) {
        log.info("카테고리 생성 요청 - 이름: {}", request.getName());

        CategoryResponse.CategoryDetail category = categoryService.createCategory(request);
        
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다")
    public ResponseEntity<CategoryResponse.CategoryDetail> updateCategory(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest.Update request
    ) {
        log.info("카테고리 수정 요청 - ID: {}", categoryId);

        CategoryResponse.CategoryDetail category = categoryService.updateCategory(categoryId, request);
        
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 논리 삭제합니다")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "카테고리 ID") @PathVariable Long categoryId
    ) {
        log.info("카테고리 삭제 요청 - ID: {}", categoryId);

        categoryService.deleteCategory(categoryId);
        
        return ResponseEntity.noContent().build();
    }
}
