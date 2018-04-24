package org.apache.poi.poifs.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.poifs.storage.ListManagedBlock;

class PropertyFactory {
    private PropertyFactory() {
    }

    static List<Property> convertToProperties(ListManagedBlock[] blocks) throws IOException {
        List<Property> properties = new ArrayList();
        for (ListManagedBlock data : blocks) {
            convertToProperties(data.getData(), properties);
        }
        return properties;
    }

    static void convertToProperties(byte[] data, List<Property> properties) throws IOException {
        int property_count = data.length / 128;
        int offset = 0;
        for (int k = 0; k < property_count; k++) {
            switch (data[offset + 66]) {
                case (byte) 1:
                    properties.add(new DirectoryProperty(properties.size(), data, offset));
                    break;
                case (byte) 2:
                    properties.add(new DocumentProperty(properties.size(), data, offset));
                    break;
                case (byte) 5:
                    properties.add(new RootProperty(properties.size(), data, offset));
                    break;
                default:
                    properties.add(null);
                    break;
            }
            offset += 128;
        }
    }
}
