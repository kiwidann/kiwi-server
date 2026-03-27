package com.kiwi.kiwiserver.domain.identity.user.controller;

import com.kiwi.kiwiserver.domain.identity.user.dto.request.UpdateNicknameRequest;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import com.kiwi.kiwiserver.domain.identity.user.dto.response.UserResponse;
import com.kiwi.kiwiserver.domain.identity.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        UserResponse response = userService.getMyInfo();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "닉네임 변경", description = "현재 로그인한 사용자의 닉네임을 변경합니다")
    @PatchMapping("/me/nickname")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyNickname(
            @Valid @RequestBody UpdateNicknameRequest request
    ) {
        UserResponse response = userService.updateMyNickname(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
