package org.uwo.cs2212;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Junit5 testing for encoding methods in LoginViewController
 *
 * @author Tingrui Zhang
 */
class LoginViewControllerTest {
    @Test
    void testGetSHA() {
        String input = "test";
        // The correct SHA-256 hash for the input string "test"
        byte[] expected = new byte[]{
                (byte) 0x9f, (byte) 0x86, (byte) 0xd0, (byte) 0x81,
                (byte) 0x88, (byte) 0x4c, (byte) 0x7d, (byte) 0x65,
                (byte) 0x9a, (byte) 0x2f, (byte) 0xea, (byte) 0xa0,
                (byte) 0xc5, (byte) 0x5a, (byte) 0xd0, (byte) 0x15,
                (byte) 0xa3, (byte) 0xbf, (byte) 0x4f, (byte) 0x1b,
                (byte) 0x2b, (byte) 0x0b, (byte) 0x82, (byte) 0x2c,
                (byte) 0xd1, (byte) 0x5d, (byte) 0x6c, (byte) 0x15,
                (byte) 0xb0, (byte) 0xf0, (byte) 0x0a, (byte) 0x08
        };
        byte[] actual = LoginViewController.getSHA(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testToHexString() {
        byte[] input = {1, 2, 3};
        // The correct hexadecimal representation for the input byte array {1, 2, 3}, padded with leading zeros
        String expected = "0000000000000000000000000000000000000000000000000000000000010203";
        String actual = LoginViewController.toHexString(input);
        assertEquals(expected, actual);
    }
}
