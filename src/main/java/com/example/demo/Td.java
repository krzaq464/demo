package com.example.demo;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.subtle.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Td {
  private static final String MODE_ENCRYPT = "encrypt";
  private static final String MODE_DECRYPT = "decrypt";

  public static void main(String[] args) throws Exception {

    String mode = "encrypt";
    File keyFile = new File(
        "c:/temp/default_keyset.json");
    File inputFile = new File("C:/temp/inputat.json");
    File outputFile = new File("C:/temp/resultat.json");
    byte[] associatedData = new byte[0];

    // Register all AEAD key types with the Tink runtime.
    AeadConfig.register();

    // Read the keyset into a KeysetHandle.
    KeysetHandle handle = null;
    try {
      handle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keyFile));
    } catch (GeneralSecurityException | IOException ex) {
      System.err.println("Cannot read keyset, got error: " + ex);
      System.exit(1);
    }

    // Get the primitive.
    Aead aead = null;
    try {
      aead = handle.getPrimitive(Aead.class);
    } catch (GeneralSecurityException ex) {
      System.err.println("Cannot create primitive, got error: " + ex);
      System.exit(1);
    }

    // Use the primitive to encrypt/decrypt files.
    if (MODE_ENCRYPT.equals(mode)) {
      byte[] plaintext = Files.readAllBytes(inputFile.toPath());
      byte[] ciphertext = aead.encrypt(plaintext, associatedData);

      System.out.println("cipher: " + Base64.encode(ciphertext));

      try (FileOutputStream stream = new FileOutputStream(outputFile)) {
        stream.write(ciphertext);
      }
    } else if (MODE_DECRYPT.equals(mode)) {
      byte[] ciphertext = Files.readAllBytes(inputFile.toPath());
      byte[] plaintext = aead.decrypt(ciphertext, associatedData);
      try (FileOutputStream stream = new FileOutputStream(outputFile)) {
        stream.write(plaintext);
      }
    } else {
      System.err.println("The first argument must be either encrypt or decrypt, got: " + mode);
      System.exit(1);
    }

    System.exit(0);
  }

}
