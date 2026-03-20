package com.kiwi.kiwiserver.domain.identity.account.controller;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.CreateAccountRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.AccountResponse;
import com.kiwi.kiwiserver.domain.identity.account.service.AccountService;
import com.kiwi.kiwiserver.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AccountResponse>> signUp(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        AccountResponse response = accountService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
