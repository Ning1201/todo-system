package com.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
    
    /**
     * 待办ID
     */
    private Long id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    /**
     * 分配者ID
     */
    private Long assigneeId;
    
    /**
     * 状态：DRAFT, PENDING, IN_PROGRESS, COMPLETED, ARCHIVED, DELETED
     */
    private String status;
    
    /**
     * 优先级：LOW, MEDIUM, HIGH, URGENT
     */
    private String priority;
    
    /**
     * 截止日期
     */
    private LocalDateTime dueDate;
    
    /**
     * 开始日期
     */
    private LocalDateTime startDate;
    
    /**
     * 完成日期
     */
    private LocalDateTime endDate;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 删除时间
     */
    private LocalDateTime deletedAt;
    
    /**
     * 关联的标签（用于查询结果）
     */
    private List<Label> labels;
    
    /**
     * 创建者信息（用于查询结果）
     */
    private User creator;
    
    /**
     * 分配者信息（用于查询结果）
     */
    private User assignee;
}
