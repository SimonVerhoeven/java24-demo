package dev.simonverhoeven.java24demo.preview;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.NamedParameterSpec;
import javax.crypto.DecapsulateException;
import javax.crypto.KEM;

/// JEP 496: Quantum-Resistant Module-Lattice-Based Key Encapsulation Mechanism

public class QuantRestModLatBasedKEM {

    public static void main() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, DecapsulateException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("ML-KEM");
        keyPairGenerator.initialize(NamedParameterSpec.ML_KEM_1024);
        final var keyPair = keyPairGenerator.generateKeyPair();

        final var kem = KEM.getInstance("ML-KEM");
        final var encapsulator = kem.newEncapsulator(keyPair.getPublic());
        final var encapsulated = encapsulator.encapsulate();
        final var message = encapsulated.encapsulation(); // Message to send to the receiver
        final var secretKeySender = encapsulated.key(); //

        final var decapsulator = kem.newDecapsulator(keyPair.getPrivate());
        // Recovers the secret key from the KEM sent by the sender
        final var secretKeyReceiver = decapsulator.decapsulate(message);
    }
}
