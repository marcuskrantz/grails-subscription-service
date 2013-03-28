package subser

import org.springframework.security.core.GrantedAuthority

class Citizen {

    String civicRegistrationNumber
    String name
    String givenName
    String surName
    String gender
    int age

    static constraints = {
        civicRegistrationNumber unique: true, minSize: 12, maxSize: 12
    }
}
