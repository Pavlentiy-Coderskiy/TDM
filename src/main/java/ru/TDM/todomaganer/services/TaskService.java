package ru.TDM.todomaganer.services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.TDM.todomaganer.LogMessages;
import ru.TDM.todomaganer.entities.Task;
import ru.TDM.todomaganer.entities.User;
import ru.TDM.todomaganer.repos.TaskRepository;
import ru.TDM.todomaganer.repos.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TaskService {
    public final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);


    public void addTaskToUser(Task task, User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow();
        task.setUser(existingUser);
        task.setId(null);
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);
        LOGGER.info(LogMessages.INFO.TASK_CREATED, user.getId(), task.getId());
    }

    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
        LOGGER.info(LogMessages.INFO.TASK_DELETED, taskId);
    }

    public void changeIsCompleted(Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        LOGGER.info(LogMessages.INFO.TASK_IS_COMPLETE_CHANGING, taskId, task.isCompleted());
    }
    // TODO
    public void editTask(Long id,String title, String description, Boolean isCompleted) {
        Task task = new Task(id, title, description, isCompleted);
        taskRepository.save(task);
        // log
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow();
    }


}
