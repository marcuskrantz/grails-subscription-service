import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class BootStrap {

    def init = { servletContext ->
        SpringSecurityUtils.registerFilter("samlAuthenticationFilter", SecurityFilterPosition.PRE_AUTH_FILTER)
    }
    def destroy = {
    }
}
