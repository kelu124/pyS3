package org.apache.poi.poifs.storage;

import java.io.IOException;

public interface ListManagedBlock {
    byte[] getData() throws IOException;
}
