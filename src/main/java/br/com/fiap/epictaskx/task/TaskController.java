package br.com.fiap.epictaskx.task;

import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final RabbitTemplate rabbitTemplate;

    public TaskController(TaskService taskService, RabbitTemplate rabbitTemplate) {
        this.taskService = taskService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user){
        var tasks = taskService.findAll();

        var avatar = user.getAttributes().get("avatar_url") != null ?
                            user.getAttributes().get("avatar_url") :
                            user.getAttributes().get("picture");

        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);
        model.addAttribute("avatar", avatar);
        return "index";
    }

    @GetMapping("/form")
    public String form(Task task){
        return "form";
    }

    @PostMapping("/task")
    public String create(@Valid Task task, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()) return "form";
        redirect.addFlashAttribute("message", "Tarefa cadastrada com sucesso");
        taskService.create(task);

        rabbitTemplate.convertAndSend("email-queue", "Nova tarefa: " + task.title);
        return "redirect:/";
    }

    @DeleteMapping("/task/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes redirect){
        taskService.delete(id);
        redirect.addFlashAttribute("message", "Tarefa apagada com sucesso");
        return "redirect:/";
    }


}
