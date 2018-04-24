package com.itextpdf.text.pdf.security;

import com.google.common.net.HttpHeaders;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.codec.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.cmp.PKIFailureInfo;
import org.spongycastle.tsp.TSPException;
import org.spongycastle.tsp.TimeStampRequest;
import org.spongycastle.tsp.TimeStampRequestGenerator;
import org.spongycastle.tsp.TimeStampResponse;
import org.spongycastle.tsp.TimeStampToken;
import org.spongycastle.tsp.TimeStampTokenInfo;

public class TSAClientBouncyCastle implements TSAClient {
    public static final String DEFAULTHASHALGORITHM = "SHA-256";
    public static final int DEFAULTTOKENSIZE = 4096;
    private static final Logger LOGGER = LoggerFactory.getLogger(TSAClientBouncyCastle.class);
    protected String digestAlgorithm;
    protected int tokenSizeEstimate;
    protected TSAInfoBouncyCastle tsaInfo;
    protected String tsaPassword;
    protected String tsaURL;
    protected String tsaUsername;

    public TSAClientBouncyCastle(String url) {
        this(url, null, null, 4096, "SHA-256");
    }

    public TSAClientBouncyCastle(String url, String username, String password) {
        this(url, username, password, 4096, "SHA-256");
    }

    public TSAClientBouncyCastle(String url, String username, String password, int tokSzEstimate, String digestAlgorithm) {
        this.tsaURL = url;
        this.tsaUsername = username;
        this.tsaPassword = password;
        this.tokenSizeEstimate = tokSzEstimate;
        this.digestAlgorithm = digestAlgorithm;
    }

    public void setTSAInfo(TSAInfoBouncyCastle tsaInfo) {
        this.tsaInfo = tsaInfo;
    }

    public int getTokenSizeEstimate() {
        return this.tokenSizeEstimate;
    }

    public MessageDigest getMessageDigest() throws GeneralSecurityException {
        return new BouncyCastleDigest().getMessageDigest(this.digestAlgorithm);
    }

    public byte[] getTimeStampToken(byte[] imprint) throws IOException, TSPException {
        TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
        tsqGenerator.setCertReq(true);
        TimeStampRequest request = tsqGenerator.generate(new ASN1ObjectIdentifier(DigestAlgorithms.getAllowedDigests(this.digestAlgorithm)), imprint, BigInteger.valueOf(System.currentTimeMillis()));
        TimeStampResponse response = new TimeStampResponse(getTSAResponse(request.getEncoded()));
        response.validate(request);
        PKIFailureInfo failure = response.getFailInfo();
        if ((failure == null ? 0 : failure.intValue()) != 0) {
            throw new IOException(MessageLocalization.getComposedMessage("invalid.tsa.1.response.code.2", this.tsaURL, String.valueOf(failure == null ? 0 : failure.intValue())));
        }
        TimeStampToken tsToken = response.getTimeStampToken();
        if (tsToken == null) {
            throw new IOException(MessageLocalization.getComposedMessage("tsa.1.failed.to.return.time.stamp.token.2", this.tsaURL, response.getStatusString()));
        }
        TimeStampTokenInfo tsTokenInfo = tsToken.getTimeStampInfo();
        byte[] encoded = tsToken.getEncoded();
        LOGGER.info("Timestamp generated: " + tsTokenInfo.getGenTime());
        if (this.tsaInfo != null) {
            this.tsaInfo.inspectTimeStampTokenInfo(tsTokenInfo);
        }
        this.tokenSizeEstimate = encoded.length + 32;
        return encoded;
    }

    protected byte[] getTSAResponse(byte[] requestBytes) throws IOException {
        try {
            URLConnection tsaConnection = new URL(this.tsaURL).openConnection();
            tsaConnection.setDoInput(true);
            tsaConnection.setDoOutput(true);
            tsaConnection.setUseCaches(false);
            tsaConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/timestamp-query");
            tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
            if (!(this.tsaUsername == null || this.tsaUsername.equals(""))) {
                tsaConnection.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeBytes((this.tsaUsername + ":" + this.tsaPassword).getBytes(), 8));
            }
            OutputStream out = tsaConnection.getOutputStream();
            out.write(requestBytes);
            out.close();
            InputStream inp = tsaConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = inp.read(buffer, 0, buffer.length);
                if (bytesRead < 0) {
                    break;
                }
                baos.write(buffer, 0, bytesRead);
            }
            byte[] respBytes = baos.toByteArray();
            String encoding = tsaConnection.getContentEncoding();
            if (encoding == null || !encoding.equalsIgnoreCase("base64")) {
                return respBytes;
            }
            return Base64.decode(new String(respBytes));
        } catch (IOException e) {
            throw new IOException(MessageLocalization.getComposedMessage("failed.to.get.tsa.response.from.1", this.tsaURL));
        }
    }
}
