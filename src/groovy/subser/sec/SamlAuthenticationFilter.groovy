package subser.sec

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

import javax.servlet.http.HttpServletRequest

class SamlAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    def samlService

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest req) {

    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest req) {
        return ""
    }
}
