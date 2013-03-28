package subser

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
import org.springframework.dao.DataAccessException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CitizenDetailsService implements GrailsUserDetailsService {

    def findCitizenByCivicRegistrationNumber(String crn) {
        Citizen cit = Citizen.findByCivicRegistrationNumber(crn)
        if (!cit) {
            throw new UsernameNotFoundException("User not found", crn)
        }

        CitizenDetails.newFromCitizen(cit)
    }

    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException, DataAccessException {
        findCitizenByCivicRegistrationNumber(username)
    }

    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException, DataAccessException {
        findCitizenByCivicRegistrationNumber(s)
    }
}
