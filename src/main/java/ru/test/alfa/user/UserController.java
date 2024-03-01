package ru.test.alfa.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.test.alfa.user.pojo.ChangeUserInfoRequest;
import ru.test.alfa.user.pojo.SearchRequest;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addNumber")
    private void addNumber(@RequestHeader("Authorization") String authorizationHeader,
                           @RequestBody ChangeUserInfoRequest request){
        String token = extractTokenFromHeader(authorizationHeader);
        userService.addNumber(token, request.getPhoneNumber());
    }

    @PostMapping("/changeNumber")
    private void changeNumber(@RequestHeader("Authorization") String authorizationHeader,
                              @RequestBody ChangeUserInfoRequest request){
        String token = extractTokenFromHeader(authorizationHeader);
        userService.updateNumber(token, request.getOldPhoneNumber(), request.getPhoneNumber());
    }

    @PostMapping("/deleteNumber")
    private void deleteNumber(@RequestHeader("Authorization") String authorizationHeader,
                              @RequestBody ChangeUserInfoRequest request){
        String token = extractTokenFromHeader(authorizationHeader);
        userService.deleteNumber(token, request.getPhoneNumber());
    }

    @PostMapping("/addEmail")
    private void addEmail(@RequestHeader("Authorization") String authorizationHeader,
                          @RequestBody ChangeUserInfoRequest request){
        String token = extractTokenFromHeader(authorizationHeader);
        userService.addEmail(token, request.getEmail());
    }

    @PostMapping("/changeEmail")
    private void changeEmail(@RequestHeader("Authorization") String authorizationHeader,
                             @RequestBody ChangeUserInfoRequest request){
        String token = extractTokenFromHeader(authorizationHeader);
        userService.updateEmail(token, request.getOldEmail(), request.getEmail());
    }

    @PostMapping("/deleteEmail")
    private void deleteEmail(@RequestHeader("Authorization") String authorizationHeader,
                             @RequestBody ChangeUserInfoRequest request){
        String token = extractTokenFromHeader(authorizationHeader);
        userService.deleteEmail(token, request.getEmail());
    }

    @GetMapping("/searchUser")
    private List<User> findByCriteria(@RequestBody SearchRequest request) {
        System.out.println(request);
        return userService.findAllByCriteria(request);
    }

        private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
