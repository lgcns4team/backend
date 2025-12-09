package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.CategoryRequest;
import com.NOK_NOK.order.domain.dto.CategoryResponse;
import com.NOK_NOK.order.domain.entity.Category;
import com.NOK_NOK.order.exceptions.CategoryNotFoundException;
import com.NOK_NOK.order.exceptions.DuplicateCategoryNameException;
import com.NOK_NOK.order.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록 조회
     */
    public List<CategoryResponse.CategoryList> getCategories(CategoryRequest.Search request) {
        log.debug("카테고리 목록 조회 시작 - 키워드: {}", request.getKeyword());

        List<Category> categories;

        // 키워드에 따른 조회
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            categories = categoryRepository.findByNameContaining(request.getKeyword());
        } else {
            categories = categoryRepository.findAllByOrderByDisplayOrderAsc();
        }

        log.debug("카테고리 {} 개 조회 완료", categories.size());

        return categories.stream()
                .map(CategoryResponse.CategoryList::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 상세 조회
     */
    public CategoryResponse.CategoryDetail getCategoryDetail(Long categoryId) {
        log.debug("카테고리 상세 조회 시작 - ID: {}", categoryId);

        Category category = categoryRepository.findByIdWithMenus(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + categoryId));

        log.debug("카테고리 상세 조회 완료 - 이름: {}, 메뉴 개수: {}", 
                category.getName(), category.getMenuItems().size());

        return CategoryResponse.CategoryDetail.from(category);
    }

    /**
     * 카테고리 생성
     */
    @Transactional
    public CategoryResponse.CategoryDetail createCategory(CategoryRequest.Create request) {
        log.info("카테고리 생성 시작 - 이름: {}", request.getName());

        // 중복 체크
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateCategoryNameException("이미 존재하는 카테고리명입니다: " + request.getName());
        }

        Category category = Category.builder()
                .name(request.getName())
                .displayOrder(request.getDisplayOrder())
                .build();

        Category savedCategory = categoryRepository.save(category);
        log.info("카테고리 생성 완료 - ID: {}, 이름: {}", savedCategory.getCategoryId(), savedCategory.getName());

        return CategoryResponse.CategoryDetail.from(savedCategory);
    }

    /**
     * 카테고리 수정
     */
    @Transactional
    public CategoryResponse.CategoryDetail updateCategory(Long categoryId, CategoryRequest.Update request) {
        log.info("카테고리 수정 시작 - ID: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + categoryId));

        // 이름 변경 시 중복 체크
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByNameAndCategoryIdNot(request.getName(), categoryId)) {
                throw new DuplicateCategoryNameException("이미 존재하는 카테고리명입니다: " + request.getName());
            }
        }

        // 정보 업데이트
        category.updateInfo(request.getName(), request.getDisplayOrder());

        log.info("카테고리 수정 완료 - ID: {}, 이름: {}", category.getCategoryId(), category.getName());

        return CategoryResponse.CategoryDetail.from(category);
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        log.info("카테고리 삭제 시작 - ID: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + categoryId));

        categoryRepository.delete(category);
        log.info("카테고리 삭제 완료 - ID: {}", categoryId);
    }
}
