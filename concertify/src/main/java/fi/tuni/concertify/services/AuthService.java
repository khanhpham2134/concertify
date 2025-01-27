package fi.tuni.concertify.services;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.*;

/**
 * Service class responsible for handling password hashing and verification
 * using bcrypt.
 */
public class AuthService {
  private static final int SALT_ROUNDS = 12;
  private static final String PEPPER = "WHOKILLEDLAURAPALMER?";
  private static final BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, SALT_ROUNDS);

  /**
   * Hashes the provided password using bcrypt with salt and a pepper.
   * 
   * @param password the password to hash
   * @return the hashed password
   */
  public String hash(String password) {
    Hash hash = Password.hash(password).addPepper(PEPPER).with(bcrypt);
    return hash.getResult();
  }

  /**
   * Verifies if the provided password matches the stored hashed password.
   * 
   * @param password       the password to verify
   * @param hashedPassword the hashed password stored for comparison
   * @return true if the password matches the hash, false otherwise
   */
  public boolean verify(String password, String hashedPassword) {
    boolean verification = Password.check(password, hashedPassword).addPepper(PEPPER).with(bcrypt);

    return verification;
  }
}
