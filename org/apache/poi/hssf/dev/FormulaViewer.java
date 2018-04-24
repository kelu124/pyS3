package org.apache.poi.hssf.dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.formula.ptg.ExpPtg;
import org.apache.poi.ss.formula.ptg.FuncPtg;
import org.apache.poi.ss.formula.ptg.Ptg;

public class FormulaViewer {
    private String file;
    private boolean list = false;

    public void run() throws IOException {
        NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.file), true);
        InputStream is;
        try {
            is = BiffViewer.getPOIFSInputStream(fs);
            for (Record record : RecordFactory.createRecords(is)) {
                if (record.getSid() == (short) 6) {
                    if (this.list) {
                        listFormula((FormulaRecord) record);
                    } else {
                        parseFormulaRecord((FormulaRecord) record);
                    }
                }
            }
            is.close();
            fs.close();
        } catch (Throwable th) {
            fs.close();
        }
    }

    private void listFormula(FormulaRecord record) {
        String numArg;
        String sep = "~";
        Ptg[] tokens = record.getParsedExpression();
        int numptgs = tokens.length;
        Ptg token = tokens[numptgs - 1];
        if (token instanceof FuncPtg) {
            numArg = String.valueOf(numptgs - 1);
        } else {
            numArg = String.valueOf(-1);
        }
        StringBuilder buf = new StringBuilder();
        if (!(token instanceof ExpPtg)) {
            buf.append(token.toFormulaString());
            buf.append(sep);
            switch (token.getPtgClass()) {
                case (byte) 0:
                    buf.append("REF");
                    break;
                case (byte) 32:
                    buf.append("VALUE");
                    break;
                case (byte) 64:
                    buf.append("ARRAY");
                    break;
                default:
                    throwInvalidRVAToken(token);
                    break;
            }
            buf.append(sep);
            if (numptgs > 1) {
                token = tokens[numptgs - 2];
                switch (token.getPtgClass()) {
                    case (byte) 0:
                        buf.append("REF");
                        break;
                    case (byte) 32:
                        buf.append("VALUE");
                        break;
                    case (byte) 64:
                        buf.append("ARRAY");
                        break;
                    default:
                        throwInvalidRVAToken(token);
                        break;
                }
            }
            buf.append("VALUE");
            buf.append(sep);
            buf.append(numArg);
            System.out.println(buf.toString());
        }
    }

    public void parseFormulaRecord(FormulaRecord record) {
        System.out.println("==============================");
        System.out.print("row = " + record.getRow());
        System.out.println(", col = " + record.getColumn());
        System.out.println("value = " + record.getValue());
        System.out.print("xf = " + record.getXFIndex());
        System.out.print(", number of ptgs = " + record.getParsedExpression().length);
        System.out.println(", options = " + record.getOptions());
        System.out.println("RPN List = " + formulaString(record));
        System.out.println("Formula text = " + composeFormula(record));
    }

    private String formulaString(FormulaRecord record) {
        StringBuilder buf = new StringBuilder();
        for (Ptg token : record.getParsedExpression()) {
            buf.append(token.toFormulaString());
            switch (token.getPtgClass()) {
                case (byte) 0:
                    buf.append("(R)");
                    break;
                case (byte) 32:
                    buf.append("(V)");
                    break;
                case (byte) 64:
                    buf.append("(A)");
                    break;
                default:
                    throwInvalidRVAToken(token);
                    break;
            }
            buf.append(' ');
        }
        return buf.toString();
    }

    private static void throwInvalidRVAToken(Ptg token) {
        throw new IllegalStateException("Invalid RVA type (" + token.getPtgClass() + "). This should never happen.");
    }

    private static String composeFormula(FormulaRecord record) {
        return HSSFFormulaParser.toFormulaString((HSSFWorkbook) null, record.getParsedExpression());
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public static void main(String[] args) throws IOException {
        if (args == null || args.length > 2 || args[0].equals("--help")) {
            System.out.println("FormulaViewer .8 proof that the devil lies in the details (or just in BIFF8 files in general)");
            System.out.println("usage: Give me a big fat file name");
        } else if (args[0].equals("--listFunctions")) {
            viewer = new FormulaViewer();
            viewer.setFile(args[1]);
            viewer.setList(true);
            viewer.run();
        } else {
            viewer = new FormulaViewer();
            viewer.setFile(args[0]);
            viewer.run();
        }
    }
}
