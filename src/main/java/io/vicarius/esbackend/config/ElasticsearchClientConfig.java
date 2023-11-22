package io.vicarius.esbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Configuration
@EnableElasticsearchRepositories(basePackages = "io.vicarius.esbackend.repo")
@ComponentScan(basePackages = { "io.vicarius.esbackend" })
public class ElasticsearchClientConfig extends
        ElasticsearchConfiguration {

    @Value("${client.certificate}")
    String certificateFile;

    @Value("${spring.elasticsearch.rest.uris}")
    String connectionUrl;

    @Value("${spring.elasticsearch.rest.username}")
    String connectionUsername;

    @Value("${spring.elasticsearch.rest.password}")
    String connectionPassword;

    @Override
    public ClientConfiguration clientConfiguration() {
        try {
            return ClientConfiguration.builder()
                    .connectedTo(connectionUrl).usingSsl(getSSLContext())
                    .withBasicAuth(connectionUsername, connectionPassword)
                    .build();
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException |
                 KeyManagementException e) {
            throw new WebServerException("Couldn't create client config", e);
        }
    }

    private SSLContext getSSLContext() throws
            CertificateException,
            IOException, NoSuchAlgorithmException,
            KeyStoreException,
            KeyManagementException
    {

        Certificate cert;
        try (FileInputStream fis = new FileInputStream(certificateFile)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = cf.generateCertificate(fis);
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("cert", cert);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }

}
