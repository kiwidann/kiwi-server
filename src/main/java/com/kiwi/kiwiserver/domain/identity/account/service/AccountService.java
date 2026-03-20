package com.kiwi.kiwiserver.domain.identity.account.service;

import com.kiwi.kiwiserver.domain.identity.account.dto.request.CreateAccountRequest;
import com.kiwi.kiwiserver.domain.identity.account.dto.response.AccountResponse;

public interface AccountService {

    AccountResponse signUp(CreateAccountRequest request);
}
