package de.zieglr.passworddemo.configuration.password;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

@AllArgsConstructor
public class LegacyMd5PasswordEncoder implements PasswordEncoder {

    private final boolean updateEncoding;

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
        return updateEncoding;
    }
}
