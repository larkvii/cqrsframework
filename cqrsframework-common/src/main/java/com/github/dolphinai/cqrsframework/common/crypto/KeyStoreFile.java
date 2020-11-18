package com.github.dolphinai.cqrsframework.common.crypto;

import com.github.dolphinai.cqrsframework.common.util.StringHelper;
import lombok.SneakyThrows;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.Objects;

/**
 *
 */
public final class KeyStoreFile {

  private final KeyStore keystore;
  private String defaultAlias;

  public KeyStoreFile(final String storeType, final InputStream keystoreStream, final String keystorePassword) throws GeneralSecurityException {
    Objects.requireNonNull(keystoreStream);
    Objects.requireNonNull(keystorePassword);

    keystore = KeyStore.getInstance(storeType);
    try {
      keystore.load(keystoreStream, keystorePassword.toCharArray());
    } catch (IOException e) {
      throw new GeneralSecurityException(e);
    }
    // alias
    if(keystore.aliases().hasMoreElements()) {
      this.defaultAlias = keystore.aliases().nextElement();
    }
  }

  public String getDefaultAlias() {
    return defaultAlias;
  }

  @SneakyThrows
  public Enumeration<String> getAliases() {
    return this.keystore.aliases();
  }

  public Key getKey(final String alias, final String keyPassword) throws GeneralSecurityException {
    Objects.requireNonNull(keyPassword);
    Objects.requireNonNull(keystore);
    return keystore.getKey(alias, keyPassword.toCharArray());
  }

  public KeyPair getKeyPair(final String alias, final String keyPassword) throws GeneralSecurityException {
    PrivateKey privateKey = (PrivateKey) getKey(alias, keyPassword);
    return new KeyPair(getCertificate(alias).getPublicKey(), privateKey);
  }

  public Certificate getCertificate(final String alias)throws GeneralSecurityException {
    return keystore.getCertificate(alias);
  }

  public static SecretKey generateKey(final String algorithm, final String seed) {
    KeyGenerator generator;
    try {
      generator = KeyGenerator.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
    // init.
    if (seed != null && seed.length() > 0) {
      SecureRandom random = new SecureRandom(StringHelper.getBytesUtf8(seed));
      // size: DES=56, AES=128
      generator.init(random);
    }
    return generator.generateKey();
  }

  public static PublicKey getPublicKey(final InputStream inputStream) throws CertificateException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate crt = cf.generateCertificate(inputStream);
    return crt.getPublicKey();
  }
}