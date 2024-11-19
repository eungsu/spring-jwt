package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payload.Response;
import com.example.payload.todo.AddTodoRequest;
import com.example.payload.todo.AddTodoResponse;
import com.example.payload.todo.TodoResponse;
import com.example.payload.todo.UpdateTodoRequest;
import com.example.security.LoginUser;
import com.example.service.TodoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<Response<AddTodoResponse>> save(@RequestBody AddTodoRequest request,
        @AuthenticationPrincipal LoginUser loginUser) {

        AddTodoResponse data = todoService.addTodo(request, loginUser.getId());
        
        return ResponseEntity.ok()
            .body(Response.success(data, "새 일정이 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<Response<List<TodoResponse>>> list(@AuthenticationPrincipal LoginUser loginUser) {
        List<TodoResponse> data = todoService.getTodos(loginUser.getId());

        return ResponseEntity.ok()
            .body(Response.success(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<TodoResponse>> detail(@PathVariable("id") long todoId,
        @AuthenticationPrincipal LoginUser loginUser) {
        
        TodoResponse data = todoService.getTodo(todoId, loginUser.getId());

        return ResponseEntity.ok()
            .body(Response.success(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<TodoResponse>> update(@PathVariable("id") long todoId,
        @RequestBody UpdateTodoRequest request,
        @AuthenticationPrincipal LoginUser loginUser) {
        
        TodoResponse data = todoService.updateTodo(todoId, request, loginUser.getId());

        return ResponseEntity.ok()
            .body(Response.success(data));    
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable("id") long todoId,
    @AuthenticationPrincipal LoginUser loginUser) {
        todoService.deleteTodo(todoId, loginUser.getId());

        return ResponseEntity.ok()
            .body(Response.success("일정이 삭제되었습니다."));   
    }
    
}
