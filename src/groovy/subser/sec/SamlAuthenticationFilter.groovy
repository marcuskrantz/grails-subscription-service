package subser.sec

import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.session.SessionAuthenticationException

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SamlAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    def samlService

    protected SamlAuthenticationFilter() {
        super("/sso/complete")
    }

    @Override
    Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        /*
         * Okey, this will be executed when the user has completed the authentication
         * at the idp. Now the idp will call us to complete the auth
         */
        if (req.method == "POST") {
            log.debug("Returning from idp...")

            def relay = req.getParameter("RelayState");
            def sessionRelay = req.session.getAttribute("RelayState")
            log.debug("RelayState: ${relay}, Session: ${sessionRelay}")

            req.session.removeAttribute("RelayState")
            if (!sessionRelay.equals(relay)) {
                throw new SessionAuthenticationException("The relay state that was sent from the idp does not match the one we have in the session")
            }

            def decoded = new String(req.getParameter("SAMLResponse").decodeBase64())
            def user = samlService.parseSamlResponse(decoded)

            /*
             * Since our saml authentication provider is registered with the authentication manager,
             * the following line will cause the authentication provider to authenticate our citizen
             */
            return authenticationManager.authenticate(new SamlAuthentication(user))
        }

        throw new AuthenticationServiceException("Invalid method: ${req.method} for authentication")
    }
}
