// Place your Spring DSL code here
beans = {
    userDetailsService(subser.CitizenDetailsService)

    samlAuthenticationFilter(subser.sec.SamlAuthenticationFilter) {
        samlService = ref('samlService')
        authenticationManager = ref('authenticationManager')
    }
}
