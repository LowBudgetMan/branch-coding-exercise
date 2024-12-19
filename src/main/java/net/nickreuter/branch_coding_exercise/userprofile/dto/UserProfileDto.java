package net.nickreuter.branch_coding_exercise.userprofile.dto;

import net.nickreuter.branch_coding_exercise.userprofile.domain.CodeRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.UserProfile;

import java.net.URI;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record UserProfileDto (
    String user_name,
    String display_name,
    URI avatar,
    String geo_location,
    String email,
    URI url,
    String created_at,
    List<CodeRepository> repos
) {
    private static final String UTC_ZONE_ID = "UTC";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of(UTC_ZONE_ID));

    public static UserProfileDto fromUserProfile(UserProfile userProfile) {
        return new UserProfileDto(
                userProfile.user_name(),
                userProfile.display_name(),
                userProfile.avatar(),
                userProfile.geo_location(),
                userProfile.email(),
                userProfile.url(),
                DATE_TIME_FORMATTER.format(userProfile.created_at()),
                userProfile.repos()
        );
    }
}
