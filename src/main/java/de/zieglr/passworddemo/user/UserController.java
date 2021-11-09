package de.zieglr.passworddemo.user;

import de.zieglr.passworddemo.user.dto.RegisterUserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest request) {
        log.info("Register user {}", request.getUsername());
        userService.createUser(request);
        return ResponseEntity.ok().build();
    }
}
