package com.todo.controller;

import com.todo.dto.CreateTodoRequest;
import com.todo.dto.UpdateTodoRequest;
import com.todo.dto.TodoDTO;
import com.todo.dto.ApiResponse;
import com.todo.dto.PageResponse;
import com.todo.entity.Todo;
import com.todo.service.TodoService;
import com.todo.security.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 待办事项控制器
 * RESTful API 接口
 */
@Slf4j
@RestController
@RequestMapping("/todos")
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    /**
     * 创建待办事项
     * POST /todos
     */
    @PostMapping
    public ApiResponse<TodoDTO> createTodo(@Valid @RequestBody CreateTodoRequest request) {
        log.info("创建待办请求");
        Long creatorId = SecurityContextHolder.getUserId();
        Todo todo = todoService.create(request, creatorId);
        return ApiResponse.success(todoService.convertToDTO(todo), "待办创建成功");
    }
    
    /**
     * 获取待办详情
     * GET /todos/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<TodoDTO> getTodo(@PathVariable Long id) {
        log.info("查询待办详情: id={}", id);
        Todo todo = todoService.findById(id);
        return ApiResponse.success(todoService.convertToDTO(todo));
    }
    
    /**
     * 更新待办信息
     * PUT /todos/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<TodoDTO> updateTodo(@PathVariable Long id, 
                                           @Valid @RequestBody UpdateTodoRequest request) {
        log.info("更新待办: id={}", id);
        Long userId = SecurityContextHolder.getUserId();
        Todo todo = todoService.update(id, request, userId);
        return ApiResponse.success(todoService.convertToDTO(todo), "待办更新成功");
    }
    
    /**
     * 更新优先级
     * PATCH /todos/{id}/priority
     */
    @PatchMapping("/{id}/priority")
    public ApiResponse<TodoDTO> updatePriority(@PathVariable Long id, 
                                               @RequestParam String priority) {
        log.info("更新优先级: id={}, priority={}", id, priority);
        Long userId = SecurityContextHolder.getUserId();
        Todo todo = todoService.updatePriority(id, priority, userId);
        return ApiResponse.success(todoService.convertToDTO(todo), "优先级更新成功");
    }
    
    /**
     * 分配待办
     * PATCH /todos/{id}/assign
     */
    @PatchMapping("/{id}/assign")
    public ApiResponse<TodoDTO> assignTodo(@PathVariable Long id, 
                                          @RequestParam Long assigneeId) {
        log.info("分配待办: id={}, assigneeId={}", id, assigneeId);
        Long creatorId = SecurityContextHolder.getUserId();
        Todo todo = todoService.assignTodo(id, assigneeId, creatorId);
        return ApiResponse.success(todoService.convertToDTO(todo), "待办分配成功");
    }
    
    /**
     * 开始处理待办
     * PATCH /todos/{id}/start
     */
    @PatchMapping("/{id}/start")
    public ApiResponse<TodoDTO> startTodo(@PathVariable Long id) {
        log.info("开始处理待办: id={}", id);
        Long userId = SecurityContextHolder.getUserId();
        Todo todo = todoService.startTodo(id, userId);
        return ApiResponse.success(todoService.convertToDTO(todo), "待办已开始处理");
    }
    
    /**
     * 标记完成待办
     * PATCH /todos/{id}/complete
     */
    @PatchMapping("/{id}/complete")
    public ApiResponse<TodoDTO> completeTodo(@PathVariable Long id) {
        log.info("标记完成待办: id={}", id);
        Long userId = SecurityContextHolder.getUserId();
        Todo todo = todoService.completeTodo(id, userId);
        return ApiResponse.success(todoService.convertToDTO(todo), "待办已标记为完成");
    }
    
    /**
     * 删除待办
     * DELETE /todos/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTodo(@PathVariable Long id) {
        log.info("删除待办: id={}", id);
        Long userId = SecurityContextHolder.getUserId();
        todoService.deleteTodo(id, userId);
        return ApiResponse.success(null, "待办已删除");
    }
    
    /**
     * 查询我创建的待办
     * GET /todos/my/created
     */
    @GetMapping("/my/created")
    public ApiResponse<PageResponse<TodoDTO>> getMyCreatedTodos(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询我创建的待办");
        Long userId = SecurityContextHolder.getUserId();
        PageResponse<TodoDTO> response = todoService.findByCreatorId(userId, page, pageSize);
        return ApiResponse.success(response);
    }
    
    /**
     * 查询分配给我的待办
     * GET /todos/my/assigned
     */
    @GetMapping("/my/assigned")
    public ApiResponse<PageResponse<TodoDTO>> getMyAssignedTodos(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询分配给我的待办");
        Long userId = SecurityContextHolder.getUserId();
        PageResponse<TodoDTO> response = todoService.findByAssigneeId(userId, page, pageSize);
        return ApiResponse.success(response);
    }
    
    /**
     * 按状态查询待办列表
     * GET /todos/status/{status}
     */
    @GetMapping("/status/{status}")
    public ApiResponse<PageResponse<TodoDTO>> getTodosByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String order) {
        log.info("按状态查询待办: status={}", status);
        PageResponse<TodoDTO> response = todoService.findByStatus(status, page, pageSize, sortBy, order);
        return ApiResponse.success(response);
    }
}
