package com.example.payload.todo;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddTodoResponse {    
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
}
