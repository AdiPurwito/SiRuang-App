// model/Admin.java
package model;

import util.Role;

public class Admin extends User {
    public Admin(String username) {
        super(username, Role.ADMIN);
    }
}
