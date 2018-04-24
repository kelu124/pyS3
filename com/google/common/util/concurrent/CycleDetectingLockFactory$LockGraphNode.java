package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.CycleDetectingLockFactory.ExampleStackTrace;
import com.google.common.util.concurrent.CycleDetectingLockFactory.Policy;
import com.google.common.util.concurrent.CycleDetectingLockFactory.PotentialDeadlockException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

class CycleDetectingLockFactory$LockGraphNode {
    final Map<CycleDetectingLockFactory$LockGraphNode, ExampleStackTrace> allowedPriorLocks = new MapMaker().weakKeys().makeMap();
    final Map<CycleDetectingLockFactory$LockGraphNode, PotentialDeadlockException> disallowedPriorLocks = new MapMaker().weakKeys().makeMap();
    final String lockName;

    CycleDetectingLockFactory$LockGraphNode(String lockName) {
        this.lockName = (String) Preconditions.checkNotNull(lockName);
    }

    String getLockName() {
        return this.lockName;
    }

    void checkAcquiredLocks(Policy policy, List<CycleDetectingLockFactory$LockGraphNode> acquiredLocks) {
        int size = acquiredLocks.size();
        for (int i = 0; i < size; i++) {
            checkAcquiredLock(policy, (CycleDetectingLockFactory$LockGraphNode) acquiredLocks.get(i));
        }
    }

    void checkAcquiredLock(Policy policy, CycleDetectingLockFactory$LockGraphNode acquiredLock) {
        Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank %s", acquiredLock.getLockName());
        if (!this.allowedPriorLocks.containsKey(acquiredLock)) {
            PotentialDeadlockException previousDeadlockException = (PotentialDeadlockException) this.disallowedPriorLocks.get(acquiredLock);
            if (previousDeadlockException != null) {
                policy.handlePotentialDeadlock(new PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace(), null));
                return;
            }
            ExampleStackTrace path = acquiredLock.findPathTo(this, Sets.newIdentityHashSet());
            if (path == null) {
                this.allowedPriorLocks.put(acquiredLock, new ExampleStackTrace(acquiredLock, this));
                return;
            }
            PotentialDeadlockException exception = new PotentialDeadlockException(acquiredLock, this, path, null);
            this.disallowedPriorLocks.put(acquiredLock, exception);
            policy.handlePotentialDeadlock(exception);
        }
    }

    @Nullable
    private ExampleStackTrace findPathTo(CycleDetectingLockFactory$LockGraphNode node, Set<CycleDetectingLockFactory$LockGraphNode> seen) {
        if (!seen.add(this)) {
            return null;
        }
        ExampleStackTrace found = (ExampleStackTrace) this.allowedPriorLocks.get(node);
        if (found != null) {
            return found;
        }
        for (Entry<CycleDetectingLockFactory$LockGraphNode, ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
            CycleDetectingLockFactory$LockGraphNode preAcquiredLock = (CycleDetectingLockFactory$LockGraphNode) entry.getKey();
            found = preAcquiredLock.findPathTo(node, seen);
            if (found != null) {
                ExampleStackTrace path = new ExampleStackTrace(preAcquiredLock, this);
                path.setStackTrace(((ExampleStackTrace) entry.getValue()).getStackTrace());
                path.initCause(found);
                return path;
            }
        }
        return null;
    }
}
