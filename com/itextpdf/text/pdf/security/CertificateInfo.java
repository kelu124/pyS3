package com.itextpdf.text.pdf.security;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.ASN1String;
import org.spongycastle.asn1.ASN1TaggedObject;

public class CertificateInfo {

    public static class X500Name {
        public static final ASN1ObjectIdentifier f163C = new ASN1ObjectIdentifier("2.5.4.6");
        public static final ASN1ObjectIdentifier CN = new ASN1ObjectIdentifier("2.5.4.3");
        public static final ASN1ObjectIdentifier DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
        public static final Map<ASN1ObjectIdentifier, String> DefaultSymbols = new HashMap();
        public static final ASN1ObjectIdentifier f164E = EmailAddress;
        public static final ASN1ObjectIdentifier EmailAddress = new ASN1ObjectIdentifier("1.2.840.113549.1.9.1");
        public static final ASN1ObjectIdentifier GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
        public static final ASN1ObjectIdentifier GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
        public static final ASN1ObjectIdentifier INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
        public static final ASN1ObjectIdentifier f165L = new ASN1ObjectIdentifier("2.5.4.7");
        public static final ASN1ObjectIdentifier f166O = new ASN1ObjectIdentifier("2.5.4.10");
        public static final ASN1ObjectIdentifier OU = new ASN1ObjectIdentifier("2.5.4.11");
        public static final ASN1ObjectIdentifier SN = new ASN1ObjectIdentifier("2.5.4.5");
        public static final ASN1ObjectIdentifier ST = new ASN1ObjectIdentifier("2.5.4.8");
        public static final ASN1ObjectIdentifier SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
        public static final ASN1ObjectIdentifier f167T = new ASN1ObjectIdentifier("2.5.4.12");
        public static final ASN1ObjectIdentifier UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
        public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");
        public Map<String, ArrayList<String>> values = new HashMap();

        static {
            DefaultSymbols.put(f163C, "C");
            DefaultSymbols.put(f166O, "O");
            DefaultSymbols.put(f167T, "T");
            DefaultSymbols.put(OU, "OU");
            DefaultSymbols.put(CN, "CN");
            DefaultSymbols.put(f165L, "L");
            DefaultSymbols.put(ST, "ST");
            DefaultSymbols.put(SN, "SN");
            DefaultSymbols.put(EmailAddress, "E");
            DefaultSymbols.put(DC, "DC");
            DefaultSymbols.put(UID, "UID");
            DefaultSymbols.put(SURNAME, "SURNAME");
            DefaultSymbols.put(GIVENNAME, "GIVENNAME");
            DefaultSymbols.put(INITIALS, "INITIALS");
            DefaultSymbols.put(GENERATION, "GENERATION");
        }

        public X500Name(ASN1Sequence seq) {
            Enumeration<ASN1Set> e = seq.getObjects();
            while (e.hasMoreElements()) {
                ASN1Set set = (ASN1Set) e.nextElement();
                for (int i = 0; i < set.size(); i++) {
                    ASN1Sequence s = (ASN1Sequence) set.getObjectAt(i);
                    String id = (String) DefaultSymbols.get(s.getObjectAt(0));
                    if (id != null) {
                        ArrayList<String> vs = (ArrayList) this.values.get(id);
                        if (vs == null) {
                            vs = new ArrayList();
                            this.values.put(id, vs);
                        }
                        vs.add(((ASN1String) s.getObjectAt(1)).getString());
                    }
                }
            }
        }

        public X500Name(String dirName) {
            X509NameTokenizer nTok = new X509NameTokenizer(dirName);
            while (nTok.hasMoreTokens()) {
                String token = nTok.nextToken();
                int index = token.indexOf(61);
                if (index == -1) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("badly.formated.directory.string", new Object[0]));
                }
                String id = token.substring(0, index).toUpperCase();
                String value = token.substring(index + 1);
                ArrayList<String> vs = (ArrayList) this.values.get(id);
                if (vs == null) {
                    vs = new ArrayList();
                    this.values.put(id, vs);
                }
                vs.add(value);
            }
        }

        public String getField(String name) {
            List<String> vs = (List) this.values.get(name);
            return vs == null ? null : (String) vs.get(0);
        }

        public List<String> getFieldArray(String name) {
            return (List) this.values.get(name);
        }

        public Map<String, ArrayList<String>> getFields() {
            return this.values;
        }

        public String toString() {
            return this.values.toString();
        }
    }

    public static class X509NameTokenizer {
        private StringBuffer buf = new StringBuffer();
        private int index;
        private String oid;

        public X509NameTokenizer(String oid) {
            this.oid = oid;
            this.index = -1;
        }

        public boolean hasMoreTokens() {
            return this.index != this.oid.length();
        }

        public String nextToken() {
            if (this.index == this.oid.length()) {
                return null;
            }
            int end = this.index + 1;
            boolean quoted = false;
            boolean escaped = false;
            this.buf.setLength(0);
            while (end != this.oid.length()) {
                char c = this.oid.charAt(end);
                if (c == '\"') {
                    if (escaped) {
                        this.buf.append(c);
                    } else {
                        quoted = !quoted;
                    }
                    escaped = false;
                } else if (escaped || quoted) {
                    this.buf.append(c);
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == ',') {
                    break;
                } else {
                    this.buf.append(c);
                }
                end++;
            }
            this.index = end;
            return this.buf.toString().trim();
        }
    }

    public static X500Name getIssuerFields(X509Certificate cert) {
        try {
            return new X500Name((ASN1Sequence) getIssuer(cert.getTBSCertificate()));
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static ASN1Primitive getIssuer(byte[] enc) {
        try {
            ASN1Sequence seq = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(enc)).readObject();
            return (ASN1Primitive) seq.getObjectAt(seq.getObjectAt(0) instanceof ASN1TaggedObject ? 3 : 2);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    public static X500Name getSubjectFields(X509Certificate cert) {
        if (cert == null) {
            return null;
        }
        try {
            return new X500Name((ASN1Sequence) getSubject(cert.getTBSCertificate()));
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public static ASN1Primitive getSubject(byte[] enc) {
        try {
            ASN1Sequence seq = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(enc)).readObject();
            return (ASN1Primitive) seq.getObjectAt(seq.getObjectAt(0) instanceof ASN1TaggedObject ? 5 : 4);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
}
