package subser

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.springframework.security.core.GrantedAuthority

class CitizenDetails extends GrailsUser {

    String name

    CitizenDetails(Long id, String crn, String name) {
        super(crn, '', true, true, true, true, [new GrantedAuthority() {
            @Override
            String getAuthority() {
                return "ROLE_CITIZEN"
            }
        }], id)

        this.name = name
    }

    static newFromCitizen(Citizen citizen) {
        new CitizenDetails(citizen.id, citizen.civicRegistrationNumber, citizen.name)
    }

    static newFromSamlData(data) {
        new CitizenDetails(-1, data["Subject_SerialNumber"], data["Subject_CommonName"])
    }
}
