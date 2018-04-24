package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.itextpdf.text.xml.xmp.PdfSchema;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
@Beta
public final class MediaType {
    public static final MediaType AAC_AUDIO = createConstant(AUDIO_TYPE, "aac");
    public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", WILDCARD);
    public static final MediaType ANY_AUDIO_TYPE = createConstant(AUDIO_TYPE, WILDCARD);
    public static final MediaType ANY_IMAGE_TYPE = createConstant(IMAGE_TYPE, WILDCARD);
    public static final MediaType ANY_TEXT_TYPE = createConstant(TEXT_TYPE, WILDCARD);
    public static final MediaType ANY_TYPE = createConstant(WILDCARD, WILDCARD);
    public static final MediaType ANY_VIDEO_TYPE = createConstant(VIDEO_TYPE, WILDCARD);
    public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
    public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
    public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
    private static final String APPLICATION_TYPE = "application";
    public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
    public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
    private static final String AUDIO_TYPE = "audio";
    public static final MediaType BASIC_AUDIO = createConstant(AUDIO_TYPE, "basic");
    public static final MediaType BMP = createConstant(IMAGE_TYPE, "bmp");
    public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
    public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8(TEXT_TYPE, "cache-manifest");
    private static final String CHARSET_ATTRIBUTE = "charset";
    public static final MediaType CRW = createConstant(IMAGE_TYPE, "x-canon-crw");
    public static final MediaType CSS_UTF_8 = createConstantUtf8(TEXT_TYPE, "css");
    public static final MediaType CSV_UTF_8 = createConstantUtf8(TEXT_TYPE, "csv");
    public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
    public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
    public static final MediaType EPUB = createConstant("application", "epub+zip");
    public static final MediaType FLV_VIDEO = createConstant(VIDEO_TYPE, "x-flv");
    public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
    public static final MediaType GIF = createConstant(IMAGE_TYPE, "gif");
    public static final MediaType GZIP = createConstant("application", "x-gzip");
    public static final MediaType HTML_UTF_8 = createConstantUtf8(TEXT_TYPE, "html");
    public static final MediaType ICO = createConstant(IMAGE_TYPE, "vnd.microsoft.icon");
    private static final String IMAGE_TYPE = "image";
    public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8(TEXT_TYPE, "calendar");
    public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
    public static final MediaType JPEG = createConstant(IMAGE_TYPE, "jpeg");
    public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
    public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
    public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
    public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
    private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
    public static final MediaType L24_AUDIO = createConstant(AUDIO_TYPE, "l24");
    private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
    public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
    public static final MediaType MBOX = createConstant("application", "mbox");
    public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
    public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
    public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
    public static final MediaType MP4_AUDIO = createConstant(AUDIO_TYPE, "mp4");
    public static final MediaType MP4_VIDEO = createConstant(VIDEO_TYPE, "mp4");
    public static final MediaType MPEG_AUDIO = createConstant(AUDIO_TYPE, "mpeg");
    public static final MediaType MPEG_VIDEO = createConstant(VIDEO_TYPE, "mpeg");
    public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
    public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
    public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
    public static final MediaType OGG_AUDIO = createConstant(AUDIO_TYPE, "ogg");
    public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
    public static final MediaType OGG_VIDEO = createConstant(VIDEO_TYPE, "ogg");
    public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
    public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
    public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
    public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
    private static final MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
    public static final MediaType PDF = createConstant("application", PdfSchema.DEFAULT_XPATH_ID);
    public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8(TEXT_TYPE, "plain");
    public static final MediaType PNG = createConstant(IMAGE_TYPE, "png");
    public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
    public static final MediaType PROTOBUF = createConstant("application", "protobuf");
    public static final MediaType PSD = createConstant(IMAGE_TYPE, "vnd.adobe.photoshop");
    public static final MediaType QUICKTIME = createConstant(VIDEO_TYPE, "quicktime");
    private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
    public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
    public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
    public static final MediaType SFNT = createConstant("application", "font-sfnt");
    public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
    public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
    public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
    public static final MediaType SVG_UTF_8 = createConstantUtf8(IMAGE_TYPE, "svg+xml");
    public static final MediaType TAR = createConstant("application", "x-tar");
    public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8(TEXT_TYPE, "javascript");
    private static final String TEXT_TYPE = "text";
    public static final MediaType THREE_GPP2_VIDEO = createConstant(VIDEO_TYPE, "3gpp2");
    public static final MediaType THREE_GPP_VIDEO = createConstant(VIDEO_TYPE, "3gpp");
    public static final MediaType TIFF = createConstant(IMAGE_TYPE, "tiff");
    private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii().and(CharMatcher.javaIsoControl().negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
    public static final MediaType TSV_UTF_8 = createConstantUtf8(TEXT_TYPE, "tab-separated-values");
    private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of(CHARSET_ATTRIBUTE, Ascii.toLowerCase(Charsets.UTF_8.name()));
    public static final MediaType VCARD_UTF_8 = createConstantUtf8(TEXT_TYPE, "vcard");
    private static final String VIDEO_TYPE = "video";
    public static final MediaType VND_REAL_AUDIO = createConstant(AUDIO_TYPE, "vnd.rn-realaudio");
    public static final MediaType VND_WAVE_AUDIO = createConstant(AUDIO_TYPE, "vnd.wave");
    public static final MediaType VORBIS_AUDIO = createConstant(AUDIO_TYPE, "vorbis");
    public static final MediaType VTT_UTF_8 = createConstantUtf8(TEXT_TYPE, "vtt");
    public static final MediaType WAX_AUDIO = createConstant(AUDIO_TYPE, "x-ms-wax");
    public static final MediaType WEBM_AUDIO = createConstant(AUDIO_TYPE, "webm");
    public static final MediaType WEBM_VIDEO = createConstant(VIDEO_TYPE, "webm");
    public static final MediaType WEBP = createConstant(IMAGE_TYPE, "webp");
    private static final String WILDCARD = "*";
    public static final MediaType WMA_AUDIO = createConstant(AUDIO_TYPE, "x-ms-wma");
    public static final MediaType WML_UTF_8 = createConstantUtf8(TEXT_TYPE, "vnd.wap.wml");
    public static final MediaType WMV = createConstant(VIDEO_TYPE, "x-ms-wmv");
    public static final MediaType WOFF = createConstant("application", "font-woff");
    public static final MediaType WOFF2 = createConstant("application", "font-woff2");
    public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
    public static final MediaType XML_UTF_8 = createConstantUtf8(TEXT_TYPE, "xml");
    public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
    public static final MediaType ZIP = createConstant("application", "zip");
    @LazyInit
    private int hashCode;
    private final ImmutableListMultimap<String, String> parameters;
    private final String subtype;
    @LazyInit
    private String toString;
    private final String type;

    class C07621 implements Function<Collection<String>, ImmutableMultiset<String>> {
        C07621() {
        }

        public ImmutableMultiset<String> apply(Collection<String> input) {
            return ImmutableMultiset.copyOf((Iterable) input);
        }
    }

    class C07632 implements Function<String, String> {
        C07632() {
        }

        public String apply(String value) {
            return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
        }
    }

    private static final class Tokenizer {
        final String input;
        int position = 0;

        Tokenizer(String input) {
            this.input = input;
        }

        String consumeTokenIfPresent(CharMatcher matcher) {
            Preconditions.checkState(hasMore());
            int startPosition = this.position;
            this.position = matcher.negate().indexIn(this.input, startPosition);
            return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
        }

        String consumeToken(CharMatcher matcher) {
            int startPosition = this.position;
            String token = consumeTokenIfPresent(matcher);
            Preconditions.checkState(this.position != startPosition);
            return token;
        }

        char consumeCharacter(CharMatcher matcher) {
            Preconditions.checkState(hasMore());
            char c = previewChar();
            Preconditions.checkState(matcher.matches(c));
            this.position++;
            return c;
        }

        char consumeCharacter(char c) {
            Preconditions.checkState(hasMore());
            Preconditions.checkState(previewChar() == c);
            this.position++;
            return c;
        }

        char previewChar() {
            Preconditions.checkState(hasMore());
            return this.input.charAt(this.position);
        }

        boolean hasMore() {
            return this.position >= 0 && this.position < this.input.length();
        }
    }

    private static MediaType createConstant(String type, String subtype) {
        return addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
    }

    private static MediaType createConstantUtf8(String type, String subtype) {
        return addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
    }

    private static MediaType addKnownType(MediaType mediaType) {
        KNOWN_TYPES.put(mediaType, mediaType);
        return mediaType;
    }

    private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
        this.type = type;
        this.subtype = subtype;
        this.parameters = parameters;
    }

    public String type() {
        return this.type;
    }

    public String subtype() {
        return this.subtype;
    }

    public ImmutableListMultimap<String, String> parameters() {
        return this.parameters;
    }

    private Map<String, ImmutableMultiset<String>> parametersAsMap() {
        return Maps.transformValues(this.parameters.asMap(), new C07621());
    }

    public Optional<Charset> charset() {
        ImmutableSet<String> charsetValues = ImmutableSet.copyOf(this.parameters.get(CHARSET_ATTRIBUTE));
        switch (charsetValues.size()) {
            case 0:
                return Optional.absent();
            case 1:
                return Optional.of(Charset.forName((String) Iterables.getOnlyElement(charsetValues)));
            default:
                throw new IllegalStateException("Multiple charset values defined: " + charsetValues);
        }
    }

    public MediaType withoutParameters() {
        return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
    }

    public MediaType withParameters(Multimap<String, String> parameters) {
        return create(this.type, this.subtype, parameters);
    }

    public MediaType withParameter(String attribute, String value) {
        Preconditions.checkNotNull(attribute);
        Preconditions.checkNotNull(value);
        Object normalizedAttribute = normalizeToken(attribute);
        Builder<String, String> builder = ImmutableListMultimap.builder();
        Iterator i$ = this.parameters.entries().iterator();
        while (i$.hasNext()) {
            Entry<String, String> entry = (Entry) i$.next();
            Object key = (String) entry.getKey();
            if (!normalizedAttribute.equals(key)) {
                builder.put(key, entry.getValue());
            }
        }
        builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
        MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
        return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
    }

    public MediaType withCharset(Charset charset) {
        Preconditions.checkNotNull(charset);
        return withParameter(CHARSET_ATTRIBUTE, charset.name());
    }

    public boolean hasWildcard() {
        return WILDCARD.equals(this.type) || WILDCARD.equals(this.subtype);
    }

    public boolean is(MediaType mediaTypeRange) {
        return (mediaTypeRange.type.equals(WILDCARD) || mediaTypeRange.type.equals(this.type)) && ((mediaTypeRange.subtype.equals(WILDCARD) || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters.entries().containsAll(mediaTypeRange.parameters.entries()));
    }

    public static MediaType create(String type, String subtype) {
        return create(type, subtype, ImmutableListMultimap.of());
    }

    static MediaType createApplicationType(String subtype) {
        return create("application", subtype);
    }

    static MediaType createAudioType(String subtype) {
        return create(AUDIO_TYPE, subtype);
    }

    static MediaType createImageType(String subtype) {
        return create(IMAGE_TYPE, subtype);
    }

    static MediaType createTextType(String subtype) {
        return create(TEXT_TYPE, subtype);
    }

    static MediaType createVideoType(String subtype) {
        return create(VIDEO_TYPE, subtype);
    }

    private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(subtype);
        Preconditions.checkNotNull(parameters);
        String normalizedType = normalizeToken(type);
        String normalizedSubtype = normalizeToken(subtype);
        boolean z = !WILDCARD.equals(normalizedType) || WILDCARD.equals(normalizedSubtype);
        Preconditions.checkArgument(z, "A wildcard type cannot be used with a non-wildcard subtype");
        Builder<String, String> builder = ImmutableListMultimap.builder();
        for (Entry<String, String> entry : parameters.entries()) {
            Object attribute = normalizeToken((String) entry.getKey());
            builder.put(attribute, normalizeParameterValue(attribute, (String) entry.getValue()));
        }
        MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
        return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
    }

    private static String normalizeToken(String token) {
        Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
        return Ascii.toLowerCase(token);
    }

    private static String normalizeParameterValue(String attribute, String value) {
        return CHARSET_ATTRIBUTE.equals(attribute) ? Ascii.toLowerCase(value) : value;
    }

    public static MediaType parse(String input) {
        Preconditions.checkNotNull(input);
        Tokenizer tokenizer = new Tokenizer(input);
        try {
            String type = tokenizer.consumeToken(TOKEN_MATCHER);
            tokenizer.consumeCharacter('/');
            String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
            Builder<String, String> parameters = ImmutableListMultimap.builder();
            while (tokenizer.hasMore()) {
                Object value;
                tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
                tokenizer.consumeCharacter(';');
                tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
                Object attribute = tokenizer.consumeToken(TOKEN_MATCHER);
                tokenizer.consumeCharacter('=');
                if ('\"' == tokenizer.previewChar()) {
                    tokenizer.consumeCharacter('\"');
                    StringBuilder valueBuilder = new StringBuilder();
                    while ('\"' != tokenizer.previewChar()) {
                        if ('\\' == tokenizer.previewChar()) {
                            tokenizer.consumeCharacter('\\');
                            valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii()));
                        } else {
                            valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
                        }
                    }
                    value = valueBuilder.toString();
                    tokenizer.consumeCharacter('\"');
                } else {
                    value = tokenizer.consumeToken(TOKEN_MATCHER);
                }
                parameters.put(attribute, value);
            }
            return create(type, subtype, parameters.build());
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Could not parse '" + input + "'", e);
        }
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MediaType)) {
            return false;
        }
        MediaType that = (MediaType) obj;
        if (this.type.equals(that.type) && this.subtype.equals(that.subtype) && parametersAsMap().equals(that.parametersAsMap())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int h = this.hashCode;
        if (h != 0) {
            return h;
        }
        h = Objects.hashCode(this.type, this.subtype, parametersAsMap());
        this.hashCode = h;
        return h;
    }

    public String toString() {
        String result = this.toString;
        if (result != null) {
            return result;
        }
        result = computeToString();
        this.toString = result;
        return result;
    }

    private String computeToString() {
        StringBuilder builder = new StringBuilder().append(this.type).append('/').append(this.subtype);
        if (!this.parameters.isEmpty()) {
            builder.append("; ");
            PARAMETER_JOINER.appendTo(builder, Multimaps.transformValues(this.parameters, new C07632()).entries());
        }
        return builder.toString();
    }

    private static String escapeAndQuote(String value) {
        StringBuilder escaped = new StringBuilder(value.length() + 16).append('\"');
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == '\r' || ch == '\\' || ch == '\"') {
                escaped.append('\\');
            }
            escaped.append(ch);
        }
        return escaped.append('\"').toString();
    }
}
