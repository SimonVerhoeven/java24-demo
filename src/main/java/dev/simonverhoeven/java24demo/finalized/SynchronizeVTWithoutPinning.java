package dev.simonverhoeven.java24demo.finalized;

import java.io.IOException;
import java.net.Socket;


///  JEP 491: Synchronize Virtual Threads without Pinning

public class SynchronizeVTWithoutPinning {

    synchronized byte[] getDataFromGivenSocket(Socket socket) throws IOException {
        final var buffer = new byte[1024];
        final var read = socket.getInputStream().read(buffer); // We could potentially get blocked here
        // ...

        return new byte[1024];
    }
}
