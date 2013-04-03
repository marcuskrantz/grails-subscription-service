package subser.sec

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.session.SessionAuthenticationException

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SamlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    static log = LoggerFactory.getLogger(SamlAuthenticationEntryPoint.class)

    def samlService
    def grailsApplication

    @Override
    void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        log.debug("Authentication entry point executing. Redirecting to idp...")

        log.debug("Redirecting to idp...")
        def xml = samlService.generateAuthnRequest()
        def enc = xml.bytes.encodeBase64().toString()
        def urlenc = URLEncoder.encode(enc, "UTF-8")
        def relay = UUID.randomUUID().toString()

        req.session.setAttribute("RelayState", relay)

        res.sendRedirect(grailsApplication.config.saml.idp.url + "&SAMLRequest=${urlenc}&RelayState=${relay}")
    }
}
