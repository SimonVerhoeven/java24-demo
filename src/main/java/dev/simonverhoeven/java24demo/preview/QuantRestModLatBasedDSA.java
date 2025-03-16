package dev.simonverhoeven.java24demo.preview;

import javax.crypto.DecapsulateException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.NamedParameterSpec;

/// JEP 496: Quantum-Resistant Module-Lattice-Based Digital Signature Algorithm

public class QuantRestModLatBasedDSA {

    public static void main() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, DecapsulateException, SignatureException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("ML-DSA");
        keyPairGenerator.initialize(NamedParameterSpec.ML_DSA_87);
        final var keyPair = keyPairGenerator.generateKeyPair();

        final var messageBytes = "demo".getBytes(StandardCharsets.UTF_8);

        final var signature = createSignature(messageBytes, keyPair);

        final var verified = verifyMessage(keyPair, messageBytes, signature);

        System.out.println(verified);
    }

    private static byte[] createSignature(byte[] messageBytes, KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final var dsaSigner = Signature.getInstance("ML-DSA");
        dsaSigner.initSign(keyPair.getPrivate());
        dsaSigner.update(messageBytes);
        return dsaSigner.sign();
    }

    private static boolean verifyMessage(KeyPair keyPair, byte[] messageBytes, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final var signatureVerifier = Signature.getInstance("ML-DSA");
        signatureVerifier.initVerify(keyPair.getPublic());
        signatureVerifier.update(messageBytes);
        return signatureVerifier.verify(signature);
    }
}
