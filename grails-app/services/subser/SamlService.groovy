package subser

import groovy.xml.MarkupBuilder
import org.codehaus.groovy.grails.commons.GrailsApplication

class SamlService {

    static transactional = false

    def GrailsApplication

    def generateAuthnRequest() {

        log.debug("Generating a SAML AuthnRequest...")

        def requestId = UUID.randomUUID().toString()
        def ts = Calendar.instance.getTime().format("yyyy-MM-dd'T'HH:mm:ss'Z'")

        def idpUrl = GrailsApplication.config.saml.idp.url
        def assertionUrl = GrailsApplication.config.saml.assertion.url

        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        builder.doubleQuotes = true
        builder."samlp:AuthnRequest"("xmlns:samlp": "urn:oasis:names:tc:SAML:2.0:protocol",
                AssertionConsumerServiceIndex: "0", AssertionConsumerServiceURL: assertionUrl,
                ForceAuthn: "true", ID: requestId, IsPassive: "false", IssueInstant: ts,
                ProtocolBinding: "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect",
                ProviderName: idpUrl, VERSION: "2.0") {
            "saml:Issuer"("xmlns:saml":"urn:oasis:names:tc:SAML:2.0:assertion", idpUrl)
            "samlp:NameIDPolicy"(AllowCreate: "true")
        }

        return writer.toString()
    }

    def parseSamlResponse(String decoded) {

        log.debug("Parsing SAML response...")

        def xml = new XmlSlurper().parseText(decoded).declareNamespace(samlp: "urn:oasis:names:tc:SAML:2.0:protocol"
                , saml: "urn:oasis:names:tc:SAML:2.0:assertion", ds: "http://www.w3.org/2000/09/xmldsig#")

        def citizenData = [:]
        xml.'Assertion'.'AttributeStatement'.'Attribute'.each {
            citizenData[it.@Name.text()] = it.'AttributeValue'.text()
        }

        return citizenData
    }
}
