// model/User.java
package model;

import util.Role;

public abstract class User {
    protected String username;
    protected Role role;

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
