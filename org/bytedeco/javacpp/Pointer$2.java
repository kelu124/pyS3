package org.bytedeco.javacpp;

import java.nio.Buffer;

class Pointer$2 implements Pointer$Deallocator {
    Buffer bb = this.val$b;
    final /* synthetic */ Pointer this$0;
    final /* synthetic */ Buffer val$b;

    Pointer$2(Pointer this$0, Buffer buffer) {
        this.this$0 = this$0;
        this.val$b = buffer;
    }

    public void deallocate() {
        this.bb = null;
    }
}
