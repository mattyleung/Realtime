package com.example.shiva.try1;

/**
 * Created by Matthew on 24-10-2019.
 */
class User {
    /**
     * The Displayname.
     */
    String Displayname;


    /**
     * The Email.
     */
    String Email;
    /**
     * The Created at.
     */
    long createdAt;

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    ;

    /**
     * Instantiates a new User.
     *
     * @param displayname the displayname
     * @param email       the email
     * @param createdAt   the created at
     */
    public User(String displayname, String email, long createdAt) {
        this.Displayname = displayname;
        this.Email = email;
        this.createdAt = createdAt;
    }


    /**
     * Gets displayname.
     *
     * @return the displayname
     */
    public String getDisplayname() {
        return Displayname;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public long getCreatedAt() {
        return createdAt;
    }

}
