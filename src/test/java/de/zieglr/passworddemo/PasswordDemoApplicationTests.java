package de.zieglr.passworddemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest
class PasswordDemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void givenPassword_whenHashing_thenVerifying() throws NoSuchAlgorithmException {
		String hash = "35454B055CC325EA1AF2126E27707052";
		String password = "ILoveJava";

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		String myHash = DatatypeConverter.printHexBinary(digest);



		assertThat(myHash.equals(hash)).isTrue();
	}
}
