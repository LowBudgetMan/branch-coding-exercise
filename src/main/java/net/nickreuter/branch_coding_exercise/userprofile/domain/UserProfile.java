package net.nickreuter.branch_coding_exercise.userprofile.domain;

import java.net.URI;
import java.time.Instant;
import java.util.List;

public record UserProfile(
        String user_name,
        String display_name,
        URI avatar,
        String geo_location,
        String email,
        URI url,
        Instant created_at,
        List<CodeRepository> repos
) {}
