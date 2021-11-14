package com.alameyo.furiouscinema

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.PUT
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .cors().disable()
            .csrf().disable()
            .authorizeRequests()
            .mvcMatchers(PUT, "/furious/timeTable").authenticated()
            .and().httpBasic()
    }
}
