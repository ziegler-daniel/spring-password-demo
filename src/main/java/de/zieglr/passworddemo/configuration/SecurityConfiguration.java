package de.zieglr.passworddemo.configuration;

import de.zieglr.passworddemo.configuration.password.LegacyMd5PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter  {

    private static final String PASSWORD_ENCODER_MD5 = "md5";
    private static final String PASSWORD_ENCODER_BCRYPT = "bcrypt";
    private static final String PASSWORD_ENCODER_ARGON = "argon2";
    private static final String PASSWORD_ENCODER_SCRYPT = "scrypt";

    @Value("${app.password-encoder}")
    private String passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        final LegacyMd5PasswordEncoder legacyMd5PasswordEncoder =
                new LegacyMd5PasswordEncoder(!PASSWORD_ENCODER_MD5.equals(passwordEncoder));

        final Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(PASSWORD_ENCODER_BCRYPT, new BCryptPasswordEncoder());
        encoders.put(PASSWORD_ENCODER_MD5, legacyMd5PasswordEncoder);
        encoders.put(PASSWORD_ENCODER_ARGON, new Argon2PasswordEncoder());
        encoders.put(PASSWORD_ENCODER_SCRYPT, new SCryptPasswordEncoder());

        final DelegatingPasswordEncoder delegatingPasswordEncoder =
                new DelegatingPasswordEncoder(passwordEncoder, encoders);

        // Password encoder if there was no match
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(legacyMd5PasswordEncoder);
        return delegatingPasswordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers( "/api/v1/user/register").permitAll()
            .anyRequest().authenticated()
        .and()
            .formLogin();
        // @formatter:on
    }
}
