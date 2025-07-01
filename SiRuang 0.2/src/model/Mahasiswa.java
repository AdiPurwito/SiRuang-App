// model/Mahasiswa.java
package model;

import util.Role;

public class Mahasiswa extends User {
    public Mahasiswa(String username) {
        super(username, Role.MAHASISWA);
    }

}
