package subser

import grails.util.GrailsWebUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.bind.annotation.RequestParam

class SamlController {

    def SamlService

    static allowedMethods = [index: 'GET', complete: 'POST']

    def authenticate() {

        def webutil = WebUtils.retrieveGrailsWebRequest()

        // Generate auth request
        def xml = samlService.generateAuthnRequest()

        def enc = xml.bytes.encodeBase64().toString()
        def urlenc = URLEncoder.encode(enc, "UTF-8")

        println("Redirecting to idp...")
        webutil.getCurrentResponse().sendRedirect(grailsApplication.config.saml.idp.url + "&SAMLRequest=${urlenc}")
    }

    def complete() {
        println("Returning from idp...")

        def decoded = new String(params.SAMLResponse.decodeBase64())
        println decoded

        def xml = new XmlSlurper().parseText(decoded)
        xml.declareNamespace(samlp: "urn:oasis:names:tc:SAML:2.0:protocol", saml: "urn:oasis:names:tc:SAML:2.0:assertion", ds: "http://www.w3.org/2000/09/xmldsig#")

        println xml

    }
}
