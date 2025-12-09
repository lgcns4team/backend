package com.NOK_NOK.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "option_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_id")
    private Long optionGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_required", nullable = false)
    @Builder.Default
    private Boolean isRequired = false;

    @Column(name = "selection_type", length = 50)
    private String selectionType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OptionItem> optionItems = new ArrayList<>();

    // 비즈니스 메서드
    public void updateInfo(String name, Boolean isRequired, String selectionType) {
        if (name != null) {
            this.name = name;
        }
        if (isRequired != null) {
            this.isRequired = isRequired;
        }
        if (selectionType != null) {
            this.selectionType = selectionType;
        }
    }
}
