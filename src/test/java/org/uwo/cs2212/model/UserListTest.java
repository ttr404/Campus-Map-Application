package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class UserListTest {

    private UserList userList;
    private UserConfig userConfig1;
    private UserConfig userConfig2;

    @BeforeEach
    void setUp() {
        userList = new UserList();
        userConfig1 = new UserConfig();
        userConfig1.setUsername("user1");
        userConfig1.setPassword("password1");
        userConfig2 = new UserConfig();
        userConfig2.setUsername("user2");
        userConfig2.setPassword("password2");
    }

    @Test
    void testGetAccountListInitiallyEmpty() {
        assertNull(userList.getAccountList());
    }

    @Test
    void testSetAndGetAccountList() {
        List<UserConfig> accountList = new ArrayList<>();
        accountList.add(userConfig1);
        accountList.add(userConfig2);
        userList.setAccountList(accountList);
        assertEquals(accountList, userList.getAccountList());
    }

    @Test
    void testSetAndGetAccountListWithNull() {
        userList.setAccountList(null);
        assertNull(userList.getAccountList());
    }

    @Test
    void testSetAndGetAccountListWithEmptyList() {
        List<UserConfig> accountList = new ArrayList<>();
        userList.setAccountList(accountList);
        assertEquals(accountList, userList.getAccountList());
    }

    @Test
    void testSetAndGetAccountListWithDuplicateUsers() {
        List<UserConfig> accountList = new ArrayList<>();
        accountList.add(userConfig1);
        accountList.add(userConfig1);
        userList.setAccountList(accountList);
        assertEquals(accountList, userList.getAccountList());
    }

    @Test
    void testSetAndGetAccountListWithNullUserConfig() {
        List<UserConfig> accountList = new ArrayList<>();
        accountList.add(null);
        userList.setAccountList(accountList);
        assertEquals(accountList, userList.getAccountList());
    }
}
