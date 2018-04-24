package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;

final class MacHashFunction extends AbstractStreamingHashFunction {
    private final int bits = (this.prototype.getMacLength() * 8);
    private final Key key;
    private final Mac prototype;
    private final boolean supportsClone = supportsClone(this.prototype);
    private final String toString;

    private static final class MacHasher extends AbstractByteHasher {
        private boolean done;
        private final Mac mac;

        private MacHasher(Mac mac) {
            this.mac = mac;
        }

        protected void update(byte b) {
            checkNotDone();
            this.mac.update(b);
        }

        protected void update(byte[] b) {
            checkNotDone();
            this.mac.update(b);
        }

        protected void update(byte[] b, int off, int len) {
            checkNotDone();
            this.mac.update(b, off, len);
        }

        private void checkNotDone() {
            Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
        }

        public HashCode hash() {
            checkNotDone();
            this.done = true;
            return HashCode.fromBytesNoCopy(this.mac.doFinal());
        }
    }

    MacHashFunction(String algorithmName, Key key, String toString) {
        this.prototype = getMac(algorithmName, key);
        this.key = (Key) Preconditions.checkNotNull(key);
        this.toString = (String) Preconditions.checkNotNull(toString);
    }

    public int bits() {
        return this.bits;
    }

    private static boolean supportsClone(Mac mac) {
        try {
            mac.clone();
            return true;
        } catch (CloneNotSupportedException e) {
            return false;
        }
    }

    private static Mac getMac(String algorithmName, Key key) {
        try {
            Mac mac = Mac.getInstance(algorithmName);
            mac.init(key);
            return mac;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public Hasher newHasher() {
        if (this.supportsClone) {
            try {
                return new MacHasher((Mac) this.prototype.clone());
            } catch (CloneNotSupportedException e) {
            }
        }
        return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key));
    }

    public String toString() {
        return this.toString;
    }
}
