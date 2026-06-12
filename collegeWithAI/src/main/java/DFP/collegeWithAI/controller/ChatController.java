package DFP.collegeWithAI.controller;

import org.springframework.web.bind.annotation.*;
import DFP.collegeWithAI.service.ChatService;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/preguntar")
    public String preguntar(@RequestBody ChatRequest request) {
        return chatService.consulta(request.mensaje());
    }

    public record ChatRequest(String mensaje) {}
}
