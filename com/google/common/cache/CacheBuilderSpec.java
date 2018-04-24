package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtIncompatible
public final class CacheBuilderSpec {
    private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(Strength.WEAK)).put("softValues", new ValueStrengthParser(Strength.SOFT)).put("weakValues", new ValueStrengthParser(Strength.WEAK)).put("recordStats", new RecordStatsParser()).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).build();
    @VisibleForTesting
    long accessExpirationDuration;
    @VisibleForTesting
    TimeUnit accessExpirationTimeUnit;
    @VisibleForTesting
    Integer concurrencyLevel;
    @VisibleForTesting
    Integer initialCapacity;
    @VisibleForTesting
    Strength keyStrength;
    @VisibleForTesting
    Long maximumSize;
    @VisibleForTesting
    Long maximumWeight;
    @VisibleForTesting
    Boolean recordStats;
    @VisibleForTesting
    long refreshDuration;
    @VisibleForTesting
    TimeUnit refreshTimeUnit;
    private final String specification;
    @VisibleForTesting
    Strength valueStrength;
    @VisibleForTesting
    long writeExpirationDuration;
    @VisibleForTesting
    TimeUnit writeExpirationTimeUnit;

    private interface ValueParser {
        void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2);
    }

    static abstract class DurationParser implements ValueParser {
        protected abstract void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit);

        DurationParser() {
        }

        public void parse(CacheBuilderSpec spec, String key, String value) {
            boolean z;
            if (value == null || value.isEmpty()) {
                z = false;
            } else {
                z = true;
            }
            Preconditions.checkArgument(z, "value of key %s omitted", (Object) key);
            try {
                TimeUnit timeUnit;
                switch (value.charAt(value.length() - 1)) {
                    case 'd':
                        timeUnit = TimeUnit.DAYS;
                        break;
                    case 'h':
                        timeUnit = TimeUnit.HOURS;
                        break;
                    case 'm':
                        timeUnit = TimeUnit.MINUTES;
                        break;
                    case 's':
                        timeUnit = TimeUnit.SECONDS;
                        break;
                    default:
                        throw new IllegalArgumentException(CacheBuilderSpec.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", key, value));
                }
                parseDuration(spec, Long.parseLong(value.substring(0, value.length() - 1)), timeUnit);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value));
            }
        }
    }

    static class AccessDurationParser extends DurationParser {
        AccessDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.accessExpirationTimeUnit == null, "expireAfterAccess already set");
            spec.accessExpirationDuration = duration;
            spec.accessExpirationTimeUnit = unit;
        }
    }

    static abstract class IntegerParser implements ValueParser {
        protected abstract void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i);

        IntegerParser() {
        }

        public void parse(CacheBuilderSpec spec, String key, String value) {
            boolean z = (value == null || value.isEmpty()) ? false : true;
            Preconditions.checkArgument(z, "value of key %s omitted", (Object) key);
            try {
                parseInteger(spec, Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }

    static class ConcurrencyLevelParser extends IntegerParser {
        ConcurrencyLevelParser() {
        }

        protected void parseInteger(CacheBuilderSpec spec, int value) {
            Preconditions.checkArgument(spec.concurrencyLevel == null, "concurrency level was already set to ", spec.concurrencyLevel);
            spec.concurrencyLevel = Integer.valueOf(value);
        }
    }

    static class InitialCapacityParser extends IntegerParser {
        InitialCapacityParser() {
        }

        protected void parseInteger(CacheBuilderSpec spec, int value) {
            Preconditions.checkArgument(spec.initialCapacity == null, "initial capacity was already set to ", spec.initialCapacity);
            spec.initialCapacity = Integer.valueOf(value);
        }
    }

    static class KeyStrengthParser implements ValueParser {
        private final Strength strength;

        public KeyStrengthParser(Strength strength) {
            this.strength = strength;
        }

        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            boolean z;
            boolean z2 = true;
            if (value == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "key %s does not take values", (Object) key);
            if (spec.keyStrength != null) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "%s was already set to %s", (Object) key, spec.keyStrength);
            spec.keyStrength = this.strength;
        }
    }

    static abstract class LongParser implements ValueParser {
        protected abstract void parseLong(CacheBuilderSpec cacheBuilderSpec, long j);

        LongParser() {
        }

        public void parse(CacheBuilderSpec spec, String key, String value) {
            boolean z = (value == null || value.isEmpty()) ? false : true;
            Preconditions.checkArgument(z, "value of key %s omitted", (Object) key);
            try {
                parseLong(spec, Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }

    static class MaximumSizeParser extends LongParser {
        MaximumSizeParser() {
        }

        protected void parseLong(CacheBuilderSpec spec, long value) {
            boolean z;
            boolean z2 = true;
            if (spec.maximumSize == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "maximum size was already set to ", spec.maximumSize);
            if (spec.maximumWeight != null) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "maximum weight was already set to ", spec.maximumWeight);
            spec.maximumSize = Long.valueOf(value);
        }
    }

    static class MaximumWeightParser extends LongParser {
        MaximumWeightParser() {
        }

        protected void parseLong(CacheBuilderSpec spec, long value) {
            boolean z;
            boolean z2 = true;
            if (spec.maximumWeight == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "maximum weight was already set to ", spec.maximumWeight);
            if (spec.maximumSize != null) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "maximum size was already set to ", spec.maximumSize);
            spec.maximumWeight = Long.valueOf(value);
        }
    }

    static class RecordStatsParser implements ValueParser {
        RecordStatsParser() {
        }

        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            boolean z;
            boolean z2 = false;
            if (value == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "recordStats does not take values");
            if (spec.recordStats == null) {
                z2 = true;
            }
            Preconditions.checkArgument(z2, "recordStats already set");
            spec.recordStats = Boolean.valueOf(true);
        }
    }

    static class RefreshDurationParser extends DurationParser {
        RefreshDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.refreshTimeUnit == null, "refreshAfterWrite already set");
            spec.refreshDuration = duration;
            spec.refreshTimeUnit = unit;
        }
    }

    static class ValueStrengthParser implements ValueParser {
        private final Strength strength;

        public ValueStrengthParser(Strength strength) {
            this.strength = strength;
        }

        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            boolean z = true;
            Preconditions.checkArgument(value == null, "key %s does not take values", (Object) key);
            if (spec.valueStrength != null) {
                z = false;
            }
            Preconditions.checkArgument(z, "%s was already set to %s", (Object) key, spec.valueStrength);
            spec.valueStrength = this.strength;
        }
    }

    static class WriteDurationParser extends DurationParser {
        WriteDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.writeExpirationTimeUnit == null, "expireAfterWrite already set");
            spec.writeExpirationDuration = duration;
            spec.writeExpirationTimeUnit = unit;
        }
    }

    private CacheBuilderSpec(String specification) {
        this.specification = specification;
    }

    public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
        CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        if (!cacheBuilderSpecification.isEmpty()) {
            for (Object keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
                boolean z;
                List<String> keyAndValue = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
                if (keyAndValue.isEmpty()) {
                    z = false;
                } else {
                    z = true;
                }
                Preconditions.checkArgument(z, "blank key-value pair");
                if (keyAndValue.size() <= 2) {
                    z = true;
                } else {
                    z = false;
                }
                Preconditions.checkArgument(z, "key-value pair %s with more than one equals sign", keyValuePair);
                Object key = (String) keyAndValue.get(0);
                ValueParser valueParser = (ValueParser) VALUE_PARSERS.get(key);
                if (valueParser != null) {
                    z = true;
                } else {
                    z = false;
                }
                Preconditions.checkArgument(z, "unknown key %s", key);
                valueParser.parse(spec, key, keyAndValue.size() == 1 ? null : (String) keyAndValue.get(1));
            }
        }
        return spec;
    }

    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }

    CacheBuilder<Object, Object> toCacheBuilder() {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (this.initialCapacity != null) {
            builder.initialCapacity(this.initialCapacity.intValue());
        }
        if (this.maximumSize != null) {
            builder.maximumSize(this.maximumSize.longValue());
        }
        if (this.maximumWeight != null) {
            builder.maximumWeight(this.maximumWeight.longValue());
        }
        if (this.concurrencyLevel != null) {
            builder.concurrencyLevel(this.concurrencyLevel.intValue());
        }
        if (this.keyStrength != null) {
            switch (this.keyStrength) {
                case WEAK:
                    builder.weakKeys();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.valueStrength != null) {
            switch (this.valueStrength) {
                case WEAK:
                    builder.weakValues();
                    break;
                case SOFT:
                    builder.softValues();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.recordStats != null && this.recordStats.booleanValue()) {
            builder.recordStats();
        }
        if (this.writeExpirationTimeUnit != null) {
            builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
        }
        if (this.accessExpirationTimeUnit != null) {
            builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
        }
        if (this.refreshTimeUnit != null) {
            builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
        }
        return builder;
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return MoreObjects.toStringHelper((Object) this).addValue(toParsableString()).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit));
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
            return false;
        }
        CacheBuilderSpec that = (CacheBuilderSpec) obj;
        if (Objects.equal(this.initialCapacity, that.initialCapacity) && Objects.equal(this.maximumSize, that.maximumSize) && Objects.equal(this.maximumWeight, that.maximumWeight) && Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && Objects.equal(this.keyStrength, that.keyStrength) && Objects.equal(this.valueStrength, that.valueStrength) && Objects.equal(this.recordStats, that.recordStats) && Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(that.refreshDuration, that.refreshTimeUnit))) {
            return true;
        }
        return false;
    }

    @Nullable
    private static Long durationInNanos(long duration, @Nullable TimeUnit unit) {
        return unit == null ? null : Long.valueOf(unit.toNanos(duration));
    }

    private static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}
