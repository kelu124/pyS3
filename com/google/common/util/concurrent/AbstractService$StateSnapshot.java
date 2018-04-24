package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Service.State;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
final class AbstractService$StateSnapshot {
    @Nullable
    final Throwable failure;
    final boolean shutdownWhenStartupFinishes;
    final State state;

    AbstractService$StateSnapshot(State internalState) {
        this(internalState, false, null);
    }

    AbstractService$StateSnapshot(State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure) {
        int i;
        boolean z = true;
        boolean z2 = !shutdownWhenStartupFinishes || internalState == State.STARTING;
        Preconditions.checkArgument(z2, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", (Object) internalState);
        if (failure != null) {
            i = 1;
        } else {
            i = 0;
        }
        if ((i ^ (internalState == State.FAILED ? 1 : 0)) != 0) {
            z = false;
        }
        Preconditions.checkArgument(z, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", (Object) internalState, (Object) failure);
        this.state = internalState;
        this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
        this.failure = failure;
    }

    State externalState() {
        if (this.shutdownWhenStartupFinishes && this.state == State.STARTING) {
            return State.STOPPING;
        }
        return this.state;
    }

    Throwable failureCause() {
        Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
        return this.failure;
    }
}
