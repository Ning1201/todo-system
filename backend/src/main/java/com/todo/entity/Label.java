package com.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {
    
    /**
     * 标签ID
     */
    private Long id;
    
    /**
     * 标签名
     */
    private String name;
    
    /**
     * 标签颜色
     */
    private String color;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
