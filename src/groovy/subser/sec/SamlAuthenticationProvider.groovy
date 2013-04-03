package subser.sec

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import subser.CitizenDetails

class SamlAuthenticationProvider extends DaoAuthenticationProvider {

    static log = LoggerFactory.getLogger(SamlAuthenticationProvider.class)

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Authentication started...")

        if (authentication instanceof SamlAuthentication) {
            log.debug("We have a valid SamlAuthentication")

            def samlAuth = (SamlAuthentication) authentication
            def user
            try {
                user = userDetailsService.loadUserByUsername(samlAuth.getCivicRegistrationNumber())
                log.debug("Found citizen in the system...")
            } catch (UsernameNotFoundException e) {
                user = CitizenDetails.newFromSamlData(samlAuth.userData)
                log.debug("Citizen does not exist in the system yet...")
            }

            log.debug("Create successful authentication")
            return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
        }

        throw new InsufficientAuthenticationException("Could not find a valid SAMLAuthentication object")
    }

    @Override
    boolean supports(Class<? extends Object> authentication) {
        return SamlAuthentication.class.isAssignableFrom(authentication)
    }
}
