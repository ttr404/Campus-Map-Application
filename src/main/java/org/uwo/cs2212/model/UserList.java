package org.uwo.cs2212.model;

import java.util.List;

public class UserList {
    private List<UserConfig> accountList;

    public List<UserConfig> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<UserConfig> accountList) {
        this.accountList = accountList;
    }
}
