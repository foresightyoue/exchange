package soul.test.http.controller;

import org.springframework.web.bind.annotation.*;
import soul.test.http.dto.UserDTO;

@RestController
@RequestMapping("/test")
public class HttpTestController {
    @PostMapping("/payment")
    public UserDTO post(@RequestBody final UserDTO userDTO) {
        return userDTO;
    }

    @GetMapping("/findByUserId")
    public String findByUserId() {
        return "helloWorld!";
    }
}
