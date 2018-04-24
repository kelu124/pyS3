package org.apache.poi.hpsf;

import org.apache.poi.util.Internal;

@Internal
class VersionedStream {
    private IndirectPropertyName _streamName;
    private GUID _versionGuid;

    VersionedStream(byte[] data, int offset) {
        this._versionGuid = new GUID(data, offset);
        this._streamName = new IndirectPropertyName(data, offset + 16);
    }

    int getSize() {
        return this._streamName.getSize() + 16;
    }
}
