package com.kiwi.kiwiserver.domain.identity.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "닉네임은 필수입니다")
    @Size(max = 50, message = "닉네임은 50자 이하여야 합니다")
    private String nickname;

    @Size(max = 255, message = "프로필 이미지 URL은 255자 이하여야 합니다")
    private String profileImageUrl;
}