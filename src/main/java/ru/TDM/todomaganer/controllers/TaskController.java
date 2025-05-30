package ru.TDM.todomaganer.controllers;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.TDM.todomaganer.entities.Task;
import ru.TDM.todomaganer.entities.User;
import ru.TDM.todomaganer.services.TaskService;
import ru.TDM.todomaganer.services.UserService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/ui/users")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/{userId}/tasks/add")
    public String addTask(@ModelAttribute Task task,
                          BindingResult result,
                          @PathVariable Long userId) {
        if (result.hasErrors()) {
            System.out.println("Ошибки валидации формы:");
            result.getAllErrors().forEach(System.out::println);
            return "redirect:/ui/users/" + userId + "?error=InvalidInput";
        }
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            taskService.addTaskToUser(task, userOptional.get());
            return "redirect:/ui/users/" + userId;
        } else
            return "redirect:/ui/users?error=UserNotFound";

    }

    @PostMapping("/{userId}/tasks/delete/{taskID}")
    public String deleteTask(@PathVariable Long taskID, @PathVariable Long userId) {
        taskService.deleteTaskById(taskID);
        return "redirect:/ui/users/" + userId;
    }

    @PostMapping("/{userId}/tasks/isCompleted/{taskID}")
    public String isCompletedChange(@PathVariable Long taskID, @PathVariable Long userId) {
        taskService.changeIsCompleted(taskID);
        return "redirect:/ui/users/" + userId;
    }

    // TODO
    @PostMapping("/{userId}/tasks/edit/{taskId}")
    public String editTask(@PathVariable Long userId,
                           @PathVariable Long taskId,
                           @ModelAttribute Task task) {
        task.setId(taskId);
        taskService.editTask(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
        return "redirect:/ui/users/" + userId;
    }



}
