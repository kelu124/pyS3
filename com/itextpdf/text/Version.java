package com.itextpdf.text;

public final class Version {
    private static Version version = null;
    private String iText = "iText®";
    private String iTextVersion = (this.iText + " " + this.release + " ©2000-2014 iText Group NV");
    private String key = null;
    private String release = "5.5.4";

    public static Version getInstance() {
        if (version == null) {
            version = new Version();
            synchronized (version) {
                StringBuilder stringBuilder;
                Version version;
                try {
                    Class<?> klass = Class.forName("com.itextpdf.license.LicenseKey");
                    String[] info = (String[]) klass.getMethod("getLicenseeInfo", new Class[0]).invoke(klass.newInstance(), new Object[0]);
                    if (info[3] == null || info[3].trim().length() <= 0) {
                        version.key = "Trial version ";
                        if (info[5] == null) {
                            stringBuilder = new StringBuilder();
                            version = version;
                            version.key = stringBuilder.append(version.key).append("unauthorised").toString();
                        } else {
                            stringBuilder = new StringBuilder();
                            version = version;
                            version.key = stringBuilder.append(version.key).append(info[5]).toString();
                        }
                    } else {
                        version.key = info[3];
                    }
                    if (info[4] != null && info[4].trim().length() > 0) {
                        version.iTextVersion = info[4];
                    } else if (info[2] != null && info[2].trim().length() > 0) {
                        stringBuilder = new StringBuilder();
                        version = version;
                        version.iTextVersion = stringBuilder.append(version.iTextVersion).append(" (").append(info[2]).toString();
                        if (version.key.toLowerCase().startsWith("trial")) {
                            stringBuilder = new StringBuilder();
                            version = version;
                            version.iTextVersion = stringBuilder.append(version.iTextVersion).append("; ").append(version.key).append(")").toString();
                        } else {
                            stringBuilder = new StringBuilder();
                            version = version;
                            version.iTextVersion = stringBuilder.append(version.iTextVersion).append("; licensed version)").toString();
                        }
                    } else if (info[0] == null || info[0].trim().length() <= 0) {
                        throw new Exception();
                    } else {
                        stringBuilder = new StringBuilder();
                        version = version;
                        version.iTextVersion = stringBuilder.append(version.iTextVersion).append(" (").append(info[0]).toString();
                        if (version.key.toLowerCase().startsWith("trial")) {
                            stringBuilder = new StringBuilder();
                            version = version;
                            version.iTextVersion = stringBuilder.append(version.iTextVersion).append("; ").append(version.key).append(")").toString();
                        } else {
                            stringBuilder = new StringBuilder();
                            version = version;
                            version.iTextVersion = stringBuilder.append(version.iTextVersion).append("; licensed version)").toString();
                        }
                    }
                } catch (Exception e) {
                    stringBuilder = new StringBuilder();
                    version = version;
                    version.iTextVersion = stringBuilder.append(version.iTextVersion).append(" (AGPL-version)").toString();
                }
            }
        }
        return version;
    }

    public String getProduct() {
        return this.iText;
    }

    public String getRelease() {
        return this.release;
    }

    public String getVersion() {
        return this.iTextVersion;
    }

    public String getKey() {
        return this.key;
    }
}
