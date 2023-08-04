package systems.btx.Classes;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Server {
    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    public Ticker ticker = new Ticker(20);
    public Thread tickerThread = new Thread(ticker);
    public int maxPlayers = 20;
    public int playerCount = 0;
    public String motd = "Sync Craft";
    public Player[] players = new Player[maxPlayers];
    public String RegistryCodecRaw = "";

    public void start() {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            keyPair = keyPairGenerator.generateKeyPair();

            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
            Instant now = Instant.now();
            Date validFrom = Date.from(now);
            Date validTo = Date.from(now.plus(365, ChronoUnit.DAYS));
            X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                    new X500Name("CN=Sync Craft, O=btx.systems, L=Auckland, ST=Auckland, C=NZ"),
                    BigInteger.ONE,
                    validFrom,
                    validTo,
                    new X500Name("CN=Sync Craft, O=btx.systems, L=Auckland, ST=Auckland, C=NZ"),
                    subjectPublicKeyInfo
            );
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA")
                    .setProvider(new BouncyCastleProvider())
                    .build(keyPair.getPrivate());

            Certificate certificate = certificateBuilder.build(contentSigner).toASN1Structure();

            ServerSocket serverSocket = new ServerSocket(25565);
            System.out.println("Server started and listening on port 25565...");

            tickerThread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Start a new thread to handle the client
                Thread clientThread = new Thread(new Client(clientSocket, ticker, this));
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
