package com.NOK_NOK.order.service;

import com.NOK_NOK.order.domain.dto.MenuRequest;
import com.NOK_NOK.order.domain.dto.MenuResponse;
import com.NOK_NOK.order.domain.entity.Category;
import com.NOK_NOK.order.domain.entity.MenuItem;
import com.NOK_NOK.order.exceptions.CategoryNotFoundException;
import com.NOK_NOK.order.exceptions.DuplicateMenuNameException;
import com.NOK_NOK.order.exceptions.MenuNotFoundException;
import com.NOK_NOK.order.repository.CategoryRepository;
import com.NOK_NOK.order.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 메뉴 목록 조회 (페이징, 필터링)
     */
    public MenuResponse.MenuPage getMenus(MenuRequest.Search request) {
        log.debug("메뉴 목록 조회 시작 - 카테고리ID: {}, 키워드: {}", 
                request.getCategoryId(), request.getKeyword());

        // 정렬 설정
        Sort.Direction direction = "DESC".equalsIgnoreCase(request.getSortDirection()) 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                request.getPage(), 
                request.getSize(), 
                Sort.by(direction, request.getSortBy())
        );

        Page<MenuItem> menuPage;

        // 조건에 따른 조회
        if (request.getCategoryId() != null && request.getKeyword() != null && !request.getKeyword().isBlank()) {
            // 카테고리 + 키워드 검색
            menuPage = menuRepository.searchByCategoryAndKeyword(
                    request.getCategoryId(), request.getKeyword(), pageable);
        } else if (request.getCategoryId() != null) {
            // 카테고리별 조회
            menuPage = menuRepository.findByCategoryCategoryId(request.getCategoryId(), pageable);
        } else if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            // 키워드 검색
            menuPage = menuRepository.searchByKeyword(request.getKeyword(), pageable);
        } else {
            // 전체 조회
            menuPage = menuRepository.findAll(pageable);
        }

        log.debug("메뉴 {} 개 조회 완료 (전체: {})", menuPage.getContent().size(), menuPage.getTotalElements());

        // Response 변환
        List<MenuResponse.MenuList> menuList = menuPage.getContent().stream()
                .map(MenuResponse.MenuList::from)
                .collect(Collectors.toList());

        return MenuResponse.MenuPage.builder()
                .content(menuList)
                .pageNumber(menuPage.getNumber())
                .pageSize(menuPage.getSize())
                .totalElements(menuPage.getTotalElements())
                .totalPages(menuPage.getTotalPages())
                .isLast(menuPage.isLast())
                .isFirst(menuPage.isFirst())
                .build();
    }

    /**
     * 메뉴 상세 조회
     */
    public MenuResponse.MenuDetail getMenuDetail(Long menuId) {
        log.debug("메뉴 상세 조회 시작 - ID: {}", menuId);

        // MenuItem menuItem = menuRepository.findByIdWithOptions(menuId)
                // .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다. ID: " + menuId));
        log.info("히히 파인드바이메뉴");
        MenuItem menuItem = menuRepository.findById(menuId).get();

        log.info("메뉴 상세 조회 시작");
        // log.debug("메뉴 상세 조회 완료 - 이름: {}, 옵션 그룹 개수: {}", 
        //         menuItem.getName(), menuItem.getOptionGroups().size());

        return MenuResponse.MenuDetail.from(menuItem);
    }

    /**
     * 추천 메뉴 조회
     */
    public MenuResponse.RecommendedMenu getRecommendedMenus(MenuRequest.Recommend request) {
        log.debug("추천 메뉴 조회 시작 - 연령대: {}, 성별: {}, 시간대: {}", 
                request.getAgeGroup(), request.getGender(), request.getTimeSlot());

        // TODO: 연령대, 성별, 시간대 기반 추천 알고리즘 구현
        // 현재는 최신 메뉴를 반환
        Pageable pageable = PageRequest.of(0, request.getLimit());
        List<MenuItem> menuItems = menuRepository.findRecommendedMenus(pageable);

        String reason;
        if (request.getAgeGroup() != null || request.getGender() != null || request.getTimeSlot() != null) {
            reason = String.format("연령대(%s), 성별(%s), 시간대(%s) 기반 추천 (추후 알고리즘 구현 예정)", 
                    request.getAgeGroup(), request.getGender(), request.getTimeSlot());
        } else {
            reason = "기본 추천 메뉴";
        }

        log.debug("추천 메뉴 {} 개 조회 완료", menuItems.size());

        List<MenuResponse.MenuList> menuList = menuItems.stream()
                .map(MenuResponse.MenuList::from)
                .collect(Collectors.toList());

        return MenuResponse.RecommendedMenu.builder()
                .menus(menuList)
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 메뉴 생성
     */
    @Transactional
    public MenuResponse.MenuDetail createMenu(MenuRequest.Create request) {
        log.info("메뉴 생성 시작 - 이름: {}, 카테고리ID: {}", request.getName(), request.getCategoryId());

        // 카테고리 존재 확인
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));

        // 중복 체크
        if (menuRepository.existsByCategoryCategoryIdAndName(request.getCategoryId(), request.getName())) {
            throw new DuplicateMenuNameException("해당 카테고리에 이미 존재하는 메뉴명입니다: " + request.getName());
        }

        MenuItem menuItem = MenuItem.builder()
                .category(category)
                .name(request.getName())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .build();

        MenuItem savedMenuItem = menuRepository.save(menuItem);
        log.info("메뉴 생성 완료 - ID: {}, 이름: {}", savedMenuItem.getMenuId(), savedMenuItem.getName());

        return MenuResponse.MenuDetail.from(savedMenuItem);
    }

    /**
     * 메뉴 수정
     */
    @Transactional
    public MenuResponse.MenuDetail updateMenu(Long menuId, MenuRequest.Update request) {
        log.info("메뉴 수정 시작 - ID: {}", menuId);

        MenuItem menuItem = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다. ID: " + menuId));

        // 카테고리 변경
        if (request.getCategoryId() != null && !request.getCategoryId().equals(menuItem.getCategory().getCategoryId())) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다. ID: " + request.getCategoryId()));
            menuItem.updateCategory(newCategory);
        }

        // 이름 변경 시 중복 체크
        if (request.getName() != null && !request.getName().equals(menuItem.getName())) {
            Long targetCategoryId = request.getCategoryId() != null ? 
                    request.getCategoryId() : menuItem.getCategory().getCategoryId();
            
            if (menuRepository.existsByCategoryIdAndNameAndMenuIdNot(targetCategoryId, request.getName(), menuId)) {
                throw new DuplicateMenuNameException("해당 카테고리에 이미 존재하는 메뉴명입니다: " + request.getName());
            }
        }

        // 정보 업데이트
        menuItem.updateInfo(request.getName(), request.getPrice(), request.getImageUrl());

        log.info("메뉴 수정 완료 - ID: {}, 이름: {}", menuItem.getMenuId(), menuItem.getName());

        return MenuResponse.MenuDetail.from(menuItem);
    }

    /**
     * 메뉴 삭제
     */
    @Transactional
    public void deleteMenu(Long menuId) {
        log.info("메뉴 삭제 시작 - ID: {}", menuId);

        MenuItem menuItem = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다. ID: " + menuId));

        menuRepository.delete(menuItem);
        log.info("메뉴 삭제 완료 - ID: {}", menuId);
    }
}
