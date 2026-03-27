package com.kiwi.kiwiserver.domain.identity.account.controller;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.*;
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

    @Operation(summary = "이메일 인증 코드 발송", description = "회원가입한 이메일로 인증 코드를 발송합니다")
    @PostMapping("/email/send-verification")
    public ResponseEntity<ApiResponse<Void>> sendVerificationEmail(
            @Valid @RequestBody SendVerificationEmailRequest request
    ) {
        accountService.sendVerificationEmail(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "이메일 인증 코드를 검증합니다")
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmailCode(
            @Valid @RequestBody VerifyEmailCodeRequest request
    ) {
        accountService.verifyEmailCode(request);
        return ResponseEntity.ok(ApiResponse.success());
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

    @Operation(summary = "비밀번호 재설정 코드 발송", description = "비밀번호 재설정을 위한 인증 코드를 이메일로 발송합니다")
    @PostMapping("/password/send-reset-code")
    public ResponseEntity<ApiResponse<Void>> sendResetPasswordCode(
            @Valid @RequestBody SendResetPasswordCodeRequest request
    ) {
        accountService.sendResetPasswordCode(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "비밀번호 재설정 코드 검증", description = "비밀번호 재설정용 인증 코드를 검증합니다")
    @PostMapping("/password/verify-reset-code")
    public ResponseEntity<ApiResponse<Void>> verifyResetPasswordCode(
            @Valid @RequestBody VerifyResetPasswordCodeRequest request
    ) {
        accountService.verifyResetPasswordCode(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "비밀번호 재설정", description = "인증 완료 후 새 비밀번호로 재설정합니다")
    @PatchMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        accountService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
