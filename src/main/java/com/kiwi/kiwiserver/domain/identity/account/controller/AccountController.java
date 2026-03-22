package com.kiwi.kiwiserver.domain.identity.account.controller;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.ChangePasswordRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.DeleteAccountRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.LoginRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.request.RefreshTokenRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.LoginResponse;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.RefreshTokenResponse;
import com.kiwi.kiwiserver.domain.identity.account.service.AccountService;
import com.kiwi.kiwiserver.domain.identity.common.dto.request.SignUpRequest;
import com.kiwi.kiwiserver.domain.identity.common.dto.response.SignUpResponse;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account", description = "계정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "회원가입", description = "계정과 사용자 정보를 함께 생성합니다")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        SignUpResponse response = accountService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = accountService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 access token을 재발급합니다")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        RefreshTokenResponse response = accountService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "비밀번호 변경", description = "현재 로그인한 사용자의 비밀번호를 변경합니다")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        accountService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "로그아웃", description = "현재 로그인한 계정의 리프레시 토큰을 삭제합니다")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        accountService.logout();
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 계정을 탈퇴 처리합니다")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount(
            @Valid @RequestBody DeleteAccountRequest request
    ) {
        accountService.deleteMyAccount(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
