package com.springboot.starling.adapter.api.response;

import java.util.ArrayList;
import java.util.List;

import com.springboot.starling.core.domain.model.Account;

public class GetAccountsResponse {
    private List<Account> accounts;

    public GetAccountsResponse() {
        this.accounts = new ArrayList<>();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Object thenReturn(GetAccountsResponse accountResponse) {
        throw new UnsupportedOperationException("Unimplemented method 'thenReturn'");
    }
}
