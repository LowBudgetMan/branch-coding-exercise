package net.nickreuter.branch_coding_exercise.userprofile;

import net.nickreuter.branch_coding_exercise.userprofile.dto.UserProfileDto;
import net.nickreuter.branch_coding_exercise.userprofile.exceptions.UserProfileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable String username) {
        try {
            return ResponseEntity.ok(UserProfileDto.fromUserProfile(userProfileService.getUserProfile(username)));
        } catch (UserProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
