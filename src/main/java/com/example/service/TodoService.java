package com.example.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Todo;
import com.example.model.User;
import com.example.payload.todo.AddTodoRequest;
import com.example.payload.todo.AddTodoResponse;
import com.example.payload.todo.TodoResponse;
import com.example.payload.todo.UpdateTodoRequest;
import com.example.repository.TodoRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {    
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public AddTodoResponse addTodo(AddTodoRequest request, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없습니다."));
        Todo todo = modelMapper.map(request, Todo.class);
        todo.setUser(user);
        todoRepository.save(todo);

        return modelMapper.map(todo, AddTodoResponse.class);
    }

    public List<TodoResponse> getTodos(Long userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        return todos.stream().map(todo -> modelMapper.map(todo, TodoResponse.class)).toList();
    }

    public TodoResponse getTodo(long todoId, Long userId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("할일정보가 없습니다."));
        if (todo.getUser().getId() != userId) {
            throw new IllegalArgumentException("다른 사용자의 일정은 조회할 수 없습니다.");
        }

        return modelMapper.map(todo, TodoResponse.class);
    }

    @Transactional
    public TodoResponse updateTodo(long todoId, UpdateTodoRequest request, Long userId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("할일정보가 없습니다."));
        if (todo.getUser().getId() != userId) {
            throw new IllegalArgumentException("다른 사용자의 일정은 변경할 수 없습니다.");
        }

        BeanUtils.copyProperties(request, todo);

        return modelMapper.map(todo, TodoResponse.class);
    }

    @Transactional
    public void deleteTodo(long todoId, Long userId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("할일정보가 없습니다."));
        if (todo.getUser().getId() != userId) {
            throw new IllegalArgumentException("다른 사용자의 일정은 삭제할 수 없습니다.");
        }

        todoRepository.delete(todo);
    }
}
