package org.apache.poi.poifs.dev;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.property.Property;
import org.apache.poi.poifs.property.PropertyTable;
import org.apache.poi.poifs.storage.BlockAllocationTableReader;
import org.apache.poi.poifs.storage.HeaderBlock;
import org.apache.poi.poifs.storage.ListManagedBlock;
import org.apache.poi.poifs.storage.RawDataBlockList;
import org.apache.poi.poifs.storage.SmallBlockTableReader;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IntList;

public class POIFSHeaderDumper {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        for (String viewFile : args) {
            viewFile(viewFile);
        }
    }

    public static void viewFile(String filename) throws Exception {
        System.out.println("Dumping headers from: " + filename);
        InputStream inp = new FileInputStream(filename);
        HeaderBlock header_block = new HeaderBlock(inp);
        displayHeader(header_block);
        POIFSBigBlockSize bigBlockSize = header_block.getBigBlockSize();
        RawDataBlockList data_blocks = new RawDataBlockList(inp, bigBlockSize);
        displayRawBlocksSummary(data_blocks);
        displayBATReader("Big Blocks", new BlockAllocationTableReader(header_block.getBigBlockSize(), header_block.getBATCount(), header_block.getBATArray(), header_block.getXBATCount(), header_block.getXBATIndex(), data_blocks));
        PropertyTable properties = new PropertyTable(header_block, data_blocks);
        displayBATReader("Small Blocks", SmallBlockTableReader._getSmallDocumentBlockReader(bigBlockSize, data_blocks, properties.getRoot(), header_block.getSBATStart()));
        displayPropertiesSummary(properties);
    }

    public static void displayHeader(HeaderBlock header_block) throws Exception {
        System.out.println("Header Details:");
        System.out.println(" Block size: " + header_block.getBigBlockSize().getBigBlockSize());
        System.out.println(" BAT (FAT) header blocks: " + header_block.getBATArray().length);
        System.out.println(" BAT (FAT) block count: " + header_block.getBATCount());
        if (header_block.getBATCount() > 0) {
            System.out.println(" BAT (FAT) block 1 at: " + header_block.getBATArray()[0]);
        }
        System.out.println(" XBAT (FAT) block count: " + header_block.getXBATCount());
        System.out.println(" XBAT (FAT) block 1 at: " + header_block.getXBATIndex());
        System.out.println(" SBAT (MiniFAT) block count: " + header_block.getSBATCount());
        System.out.println(" SBAT (MiniFAT) block 1 at: " + header_block.getSBATStart());
        System.out.println(" Property table at: " + header_block.getPropertyStart());
        System.out.println("");
    }

    public static void displayRawBlocksSummary(RawDataBlockList data_blocks) throws Exception {
        System.out.println("Raw Blocks Details:");
        System.out.println(" Number of blocks: " + data_blocks.blockCount());
        for (int i = 0; i < Math.min(16, data_blocks.blockCount()); i++) {
            ListManagedBlock block = data_blocks.get(i);
            byte[] data = new byte[Math.min(48, block.getData().length)];
            System.arraycopy(block.getData(), 0, data, 0, data.length);
            System.out.println(" Block #" + i + ":");
            System.out.println(HexDump.dump(data, 0, 0));
        }
        System.out.println("");
    }

    public static void displayBATReader(String type, BlockAllocationTableReader batReader) throws Exception {
        System.out.println("Sectors, as referenced from the " + type + " FAT:");
        IntList entries = batReader.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            int bn = entries.get(i);
            String bnS = Integer.toString(bn);
            if (bn == -2) {
                bnS = "End Of Chain";
            } else if (bn == -4) {
                bnS = "DI Fat Block";
            } else if (bn == -3) {
                bnS = "Normal Fat Block";
            } else if (bn == -1) {
                bnS = "Block Not Used (Free)";
            }
            System.out.println("  Block  # " + i + " -> " + bnS);
        }
        System.out.println("");
    }

    public static void displayPropertiesSummary(PropertyTable properties) {
        System.out.println("Mini Stream starts at " + properties.getRoot().getStartBlock());
        System.out.println("Mini Stream length is " + properties.getRoot().getSize());
        System.out.println();
        System.out.println("Properties and their block start:");
        displayProperties(properties.getRoot(), "");
        System.out.println("");
    }

    public static void displayProperties(DirectoryProperty prop, String indent) {
        String nextIndent = indent + "  ";
        System.out.println(indent + "-> " + prop.getName());
        Iterator i$ = prop.iterator();
        while (i$.hasNext()) {
            Property cp = (Property) i$.next();
            if (cp instanceof DirectoryProperty) {
                displayProperties((DirectoryProperty) cp, nextIndent);
            } else {
                System.out.println(nextIndent + "=> " + cp.getName());
                System.out.print(nextIndent + "   " + cp.getSize() + " bytes in ");
                if (cp.shouldUseSmallBlocks()) {
                    System.out.print("mini");
                } else {
                    System.out.print("main");
                }
                System.out.println(" stream, starts at " + cp.getStartBlock());
            }
        }
    }
}
