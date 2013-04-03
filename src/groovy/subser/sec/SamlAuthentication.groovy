package subser.sec

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class SamlAuthentication extends AbstractAuthenticationToken {
    def userData = [:]


    SamlAuthentication(data) {
        super(null)
        userData = data
    }

    def getCivicRegistrationNumber() {
        return userData["Subject_SerialNumber"]
    }

    @Override
    Object getCredentials() {
        return ""
    }

    @Override
    Object getPrincipal() {
        return userData
    }
}
