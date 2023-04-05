package org.uwo.cs2212.model;

import java.util.List;

/**
 * This class is used to store the list of users, the list contains the
 * user's configuration (username and password)
 *
 * @author Yaopeng Xie
 * @author Jarrett Boersen
 */
public class UserList {
    /**
     * This variable is used to store the list of user's in the program
     */
    private List<UserConfig> accountList;

    /**
     * This method is used to get the list of users
     *
     * @return Returns the list of users
     */
    public List<UserConfig> getAccountList() {
        return accountList;
    }

    /**
     * This method is used to set the entire list of user's to the given list
     *
     * @param accountList The list of users to overwrite the current list
     */
    public void setAccountList(List<UserConfig> accountList) {
        this.accountList = accountList;
    }
}
