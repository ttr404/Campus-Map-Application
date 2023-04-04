package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class UserConfigTest {

    private UserConfig userConfig;

    @BeforeEach
    void setUp() {
        userConfig = new UserConfig();
    }

    @Test
    void testUsername() {
        // Test with normal username
        userConfig.setUsername("testUser");
        assertEquals("testUser", userConfig.getUsername());

        // Test with empty string
        userConfig.setUsername("");
        assertEquals("", userConfig.getUsername());

        // Test with null value
        userConfig.setUsername(null);
        assertNull(userConfig.getUsername());

        // Test with special characters
        userConfig.setUsername("!@#$%^");
        assertEquals("!@#$%^", userConfig.getUsername());

        // Test with a long username
        String longUsername = "a".repeat(100);
        userConfig.setUsername(longUsername);
        assertEquals(longUsername, userConfig.getUsername());
    }

    @Test
    void testPassword() {
        // Test with normal password
        userConfig.setPassword("testPassword");
        assertEquals("testPassword", userConfig.getPassword());

        // Test with empty string
        userConfig.setPassword("");
        assertEquals("", userConfig.getPassword());

        // Test with null value
        userConfig.setPassword(null);
        assertNull(userConfig.getPassword());

        // Test with special characters
        userConfig.setPassword("!@#$%^");
        assertEquals("!@#$%^", userConfig.getPassword());

        // Test with a long password
        String longPassword = "a".repeat(100);
        userConfig.setPassword(longPassword);
        assertEquals(longPassword, userConfig.getPassword());
    }
}
