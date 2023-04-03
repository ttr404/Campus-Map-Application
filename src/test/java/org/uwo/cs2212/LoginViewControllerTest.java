package org.uwo.cs2212;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginViewControllerTest {
    @Test
    void testGetSHA() {
        String input = "test";
        byte[] expected = new byte[]{-97, -14, 120, 121, -97, -47};
        byte[] actual = LoginViewController.getSHA(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testToHexString() {
        byte[] input = {1,2,3};
        String expected = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
        String actual = LoginViewController.toHexString(input);
        assertEquals(expected, actual);
    }
}