package org.bytedeco.javacpp.tools;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

class Tokenizer implements Closeable {
    StringBuilder buffer = new StringBuilder();
    File file = null;
    int lastChar = -1;
    int lineNumber = 1;
    String lineSeparator = null;
    Reader reader = null;

    Tokenizer(Reader reader) {
        this.reader = reader;
    }

    Tokenizer(String string) {
        this.reader = new StringReader(string);
    }

    Tokenizer(File file) throws FileNotFoundException {
        this.file = file;
        this.reader = new BufferedReader(new FileReader(file));
    }

    public void filterLines(String[] patterns, boolean skip) throws IOException {
        if (patterns != null) {
            StringBuilder lines = new StringBuilder();
            BufferedReader lineReader = this.reader instanceof BufferedReader ? (BufferedReader) this.reader : new BufferedReader(this.reader);
            while (true) {
                String line = lineReader.readLine();
                if (line != null) {
                    int i = 0;
                    while (i < patterns.length && !line.matches(patterns[i])) {
                        i += 2;
                    }
                    if (i < patterns.length) {
                        if (!skip) {
                            lines.append(line + "\n");
                        }
                        while (i + 1 < patterns.length) {
                            line = lineReader.readLine();
                            if (line != null) {
                                if (!skip) {
                                    lines.append(line + "\n");
                                }
                                if (line.matches(patterns[i + 1])) {
                                    break;
                                }
                            }
                            break;
                        }
                    } else if (skip) {
                        lines.append(line + "\n");
                    }
                } else {
                    this.reader.close();
                    this.reader = new StringReader(lines.toString());
                    return;
                }
            }
        }
    }

    public void close() throws IOException {
        this.reader.close();
    }

    int readChar() throws IOException {
        int c2 = -1;
        if (this.lastChar != -1) {
            int c = this.lastChar;
            this.lastChar = -1;
            return c;
        }
        c = this.reader.read();
        if (c == 13 || c == 10) {
            this.lineNumber++;
            if (c == 13) {
                c2 = this.reader.read();
            }
            if (this.lineSeparator == null) {
                String str = (c == 13 && c2 == 10) ? "\r\n" : c == 13 ? "\r" : "\n";
                this.lineSeparator = str;
            }
            if (c2 != 10) {
                this.lastChar = c2;
            }
            c = 10;
        }
        return c;
    }

    public Token nextToken() throws IOException {
        Token token = new Token();
        int c = readChar();
        this.buffer.setLength(0);
        if (Character.isWhitespace(c)) {
            this.buffer.append((char) c);
            while (true) {
                c = readChar();
                if (c == -1 || !Character.isWhitespace(c)) {
                    break;
                }
                this.buffer.append((char) c);
            }
        }
        token.file = this.file;
        token.lineNumber = this.lineNumber;
        token.spacing = this.buffer.toString();
        this.buffer.setLength(0);
        if (Character.isLetter(c) || c == 95) {
            token.type = 5;
            this.buffer.append((char) c);
            while (true) {
                c = readChar();
                if (c == -1 || !(Character.isDigit(c) || Character.isLetter(c) || c == 95)) {
                    token.value = this.buffer.toString();
                    this.lastChar = c;
                } else {
                    this.buffer.append((char) c);
                }
            }
            token.value = this.buffer.toString();
            this.lastChar = c;
        } else if (Character.isDigit(c) || c == 46 || c == 45 || c == 43) {
            if (c == 46) {
                c2 = readChar();
                if (c2 == 46) {
                    int c3 = readChar();
                    if (c3 == 46) {
                        token.type = 6;
                        token.value = "...";
                    } else {
                        this.lastChar = c3;
                    }
                } else {
                    this.lastChar = c2;
                }
            }
            token.type = c == 46 ? 2 : 1;
            this.buffer.append((char) c);
            prevc = 0;
            boolean exp = false;
            boolean large = false;
            boolean unsigned = false;
            boolean hex = false;
            while (true) {
                c = readChar();
                if (c == -1 || !(Character.isDigit(c) || c == 46 || c == 45 || c == 43 || ((c >= 97 && c <= 102) || c == 108 || c == 117 || c == 120 || ((c >= 65 && c <= 70) || c == 76 || c == 85 || c == 88)))) {
                    if (!hex && (exp || prevc == 102 || prevc == 70)) {
                        token.type = 2;
                    }
                    if (token.type == 1 && !large) {
                        try {
                            long high = Long.decode(this.buffer.toString()).longValue() >> 32;
                            large = (high == 0 || high == -1) ? false : true;
                        } catch (NumberFormatException e) {
                        }
                    }
                    if (token.type == 1 && (large || (unsigned && !hex))) {
                        this.buffer.append('L');
                    }
                    token.value = this.buffer.toString();
                    this.lastChar = c;
                } else {
                    switch (c) {
                        case 46:
                            token.type = 2;
                            break;
                        case 69:
                        case 101:
                            exp = true;
                            break;
                        case 76:
                        case 108:
                            large = true;
                            break;
                        case 85:
                        case 117:
                            unsigned = true;
                            break;
                        case 88:
                        case 120:
                            hex = true;
                            break;
                    }
                    if (!(c == 108 || c == 76 || c == 117 || c == 85)) {
                        this.buffer.append((char) c);
                    }
                    prevc = c;
                }
            }
        } else if (c == 39) {
            token.type = 1;
            this.buffer.append('\'');
            while (true) {
                c = readChar();
                if (c == -1 || c == 39) {
                    this.buffer.append('\'');
                    token.value = this.buffer.toString();
                } else {
                    this.buffer.append((char) c);
                    if (c == 92) {
                        this.buffer.append((char) readChar());
                    }
                }
            }
            this.buffer.append('\'');
            token.value = this.buffer.toString();
        } else if (c == 34) {
            token.type = 3;
            this.buffer.append('\"');
            while (true) {
                c = readChar();
                if (c == -1 || c == 34) {
                    this.buffer.append('\"');
                    token.value = this.buffer.toString();
                } else {
                    this.buffer.append((char) c);
                    if (c == 92) {
                        this.buffer.append((char) readChar());
                    }
                }
            }
            this.buffer.append('\"');
            token.value = this.buffer.toString();
        } else if (c == 47) {
            c = readChar();
            if (c == 47) {
                token.type = 4;
                this.buffer.append('/').append('/');
                prevc = 0;
                while (true) {
                    c = readChar();
                    if (c == -1 || (prevc != 92 && c == 10)) {
                        token.value = this.buffer.toString();
                        this.lastChar = c;
                    } else {
                        this.buffer.append((char) c);
                        prevc = c;
                    }
                }
                token.value = this.buffer.toString();
                this.lastChar = c;
            } else if (c == 42) {
                token.type = 4;
                this.buffer.append('/').append('*');
                prevc = 0;
                while (true) {
                    c = readChar();
                    if (c == -1 || (prevc == 42 && c == 47)) {
                        this.buffer.append('/');
                        token.value = this.buffer.toString();
                    } else {
                        this.buffer.append((char) c);
                        prevc = c;
                    }
                }
                this.buffer.append('/');
                token.value = this.buffer.toString();
            } else {
                this.lastChar = c;
                token.type = 47;
            }
        } else if (c == 58) {
            c2 = readChar();
            if (c2 == 58) {
                token.type = 6;
                token.value = "::";
            } else {
                token.type = c;
                this.lastChar = c2;
            }
        } else if (c == 38) {
            c2 = readChar();
            if (c2 == 38) {
                token.type = 6;
                token.value = "&&";
            } else {
                token.type = c;
                this.lastChar = c2;
            }
        } else if (c == 35) {
            c2 = readChar();
            if (c2 == 35) {
                token.type = 6;
                token.value = "##";
            } else {
                token.type = c;
                this.lastChar = c2;
            }
        } else {
            if (c == 92) {
                c2 = readChar();
                if (c2 == 10) {
                    token.type = 4;
                    token.value = "\n";
                } else {
                    this.lastChar = c2;
                }
            }
            token.type = c;
        }
        return token;
    }

    Token[] tokenize() {
        ArrayList<Token> tokens = new ArrayList();
        while (true) {
            try {
                Token token = nextToken();
                if (token.isEmpty()) {
                    return (Token[]) tokens.toArray(new Token[tokens.size()]);
                }
                tokens.add(token);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
