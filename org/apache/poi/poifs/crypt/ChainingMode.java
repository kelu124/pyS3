package org.apache.poi.poifs.crypt;

public enum ChainingMode {
    ecb("ECB", 1),
    cbc("CBC", 2),
    cfb("CFB8", 3);
    
    public final int ecmaId;
    public final String jceId;

    private ChainingMode(String jceId, int ecmaId) {
        this.jceId = jceId;
        this.ecmaId = ecmaId;
    }
}
