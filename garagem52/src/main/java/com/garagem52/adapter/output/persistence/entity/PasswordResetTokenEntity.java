package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Era @ManyToOne UserEntity — agora userId é String (ObjectId).
 */
@Document(collection = "password_reset_token")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordResetTokenEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    @Field("user_id")
    private String userId;

    @Field("expires_at")
    private LocalDateTime expiresAt;

    private boolean used;
}
