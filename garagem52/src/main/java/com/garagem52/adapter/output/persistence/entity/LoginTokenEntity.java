package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "login_tokens")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LoginTokenEntity {
    @Id
    private String id;

    @Indexed
    @Field("user_id")
    private String userId;

    @Indexed(unique = true)
    private String token;

    @Field("expires_at")
    private LocalDateTime expiresAt;

    private boolean used;
}
