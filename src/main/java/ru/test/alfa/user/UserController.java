package ru.test.alfa.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.test.alfa.user.dto.ChangeUserInfoRequest;
import ru.test.alfa.user.dto.SearchRequest;
import ru.test.alfa.user.dto.UserDto;

import java.util.List;

@Tag(name = "user_controller")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/addNumber")
    private ResponseEntity<Void> addNumber(@RequestBody ChangeUserInfoRequest request){
        userService.addNumber(request.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateNumber")
    private ResponseEntity<Void> changeNumber(@RequestBody ChangeUserInfoRequest request){
        userService.updateNumber(request.getOldPhoneNumber(), request.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteNumber")
    private ResponseEntity<Void> deleteNumber(@RequestBody ChangeUserInfoRequest request){
        userService.deleteNumber(request.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addEmail")
    private ResponseEntity<Void> addEmail(@RequestBody ChangeUserInfoRequest request){
        userService.addEmail(request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/changeEmail")
    private ResponseEntity<Void> changeEmail(@RequestBody ChangeUserInfoRequest request){
        userService.updateEmail(request.getOldEmail(), request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteEmail")
    private ResponseEntity<Void> deleteEmail(@RequestBody ChangeUserInfoRequest request){
        userService.deleteEmail(request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/filter")
    private ResponseEntity<List<UserDto>> findByCriteria(@RequestBody SearchRequest request,
                                                         @RequestParam(value = "pageNumber", defaultValue = "1", required = false)
                                                         Integer pageNumber,
                                                         @RequestParam(value = "limit", defaultValue = "10", required = false)
                                                             Integer limit,
                                                         @RequestParam(value = "ascending", defaultValue = "false", required = false)
                                                             boolean ascending) {
        return new ResponseEntity<>(userService.findAllByCriteria(pageNumber, limit, request, ascending), HttpStatus.OK);
    }
}
