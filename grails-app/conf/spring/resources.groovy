import subser.sec.SamlAuthenticationEntryPoint

// Place your Spring DSL code here
beans = {
    userDetailsService(subser.CitizenDetailsService)

    samlAuthenticationProvider(subser.sec.SamlAuthenticationProvider) {
        userDetailsService = ref('userDetailsService')
    }

    samlAuthenticationFilter(subser.sec.SamlAuthenticationFilter) {
        samlService = ref('samlService')
        authenticationManager = ref('authenticationManager')
    }

    authenticationEntryPoint(subser.sec.SamlAuthenticationEntryPoint) {
        samlService = ref('samlService')
        grailsApplication = ref('grailsApplication')
    }
}
