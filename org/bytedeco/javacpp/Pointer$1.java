package org.bytedeco.javacpp;

class Pointer$1 implements Pointer$Deallocator {
    final /* synthetic */ Pointer this$0;
    final /* synthetic */ Pointer val$p;

    Pointer$1(Pointer this$0, Pointer pointer) {
        this.this$0 = this$0;
        this.val$p = pointer;
    }

    public void deallocate() {
        this.val$p.deallocate();
    }
}
