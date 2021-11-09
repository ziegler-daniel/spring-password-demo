package de.zieglr.passworddemo.configuration;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter  {

    private static class LegacyMd5PasswordEncoder implements PasswordEncoder {
        @SneakyThrows
        @Override
        public String encode(CharSequence rawPassword) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(rawPassword.toString().getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest);
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equalsIgnoreCase(encodedPassword);
        }

        @Override
        public boolean upgradeEncoding(String encodedPassword) {
            return true;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        final Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("md5", new LegacyMd5PasswordEncoder());

        final DelegatingPasswordEncoder delegatingPasswordEncoder =
                new DelegatingPasswordEncoder("bcrypt", encoders);

        // Password encoder if there was no match
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new LegacyMd5PasswordEncoder());
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
