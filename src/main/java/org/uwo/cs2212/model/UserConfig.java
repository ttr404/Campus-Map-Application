package org.uwo.cs2212.model;

/**
 * This class is used to store the user's configuration data
 */
public class UserConfig {
    /**
     * This variable is used to store the username of the user
     */
    private String username;
    /**
     * This variable is used to store the password of the user
     */
    private String password;

    /**
     * This method is used to get the user's username
     *
     * @return Returns the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method is used to set the user's username
     *
     * @param username The username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method is used to get the user's password
     *
     * @return Returns the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method is used to set the user's password
     *
     * @param password The password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
