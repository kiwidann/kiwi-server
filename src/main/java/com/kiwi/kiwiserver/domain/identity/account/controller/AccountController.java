package com.kiwi.kiwiserver.domain.identity.account.controller;

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
}
