package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.avcodec.AVBitStreamFilterContext;
import org.bytedeco.javacpp.avcodec.AVCodec;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVCodecParameters;
import org.bytedeco.javacpp.avcodec.AVCodecParserContext;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avcodec.AVPacketSideData;
import org.bytedeco.javacpp.avutil.AVClass;
import org.bytedeco.javacpp.avutil.AVDictionary;
import org.bytedeco.javacpp.avutil.AVFrame;
import org.bytedeco.javacpp.avutil.AVRational;

public class avformat extends org.bytedeco.javacpp.presets.avformat {
    public static final int AVFMTCTX_NOHEADER = 1;
    public static final int AVFMT_ALLOW_FLUSH = 65536;
    public static final int AVFMT_DURATION_FROM_BITRATE = 2;
    public static final int AVFMT_DURATION_FROM_PTS = 0;
    public static final int AVFMT_DURATION_FROM_STREAM = 1;
    public static final int AVFMT_GENERIC_INDEX = 256;
    public static final int AVFMT_GLOBALHEADER = 64;
    public static final int AVFMT_NEEDNUMBER = 2;
    public static final int AVFMT_NOBINSEARCH = 8192;
    public static final int AVFMT_NODIMENSIONS = 2048;
    public static final int AVFMT_NOFILE = 1;
    public static final int AVFMT_NOGENSEARCH = 16384;
    public static final int AVFMT_NOSTREAMS = 4096;
    public static final int AVFMT_NOTIMESTAMPS = 128;
    public static final int AVFMT_NO_BYTE_SEEK = 32768;
    public static final int AVFMT_RAWPICTURE = 32;
    public static final int AVFMT_SEEK_TO_PTS = 67108864;
    public static final int AVFMT_SHOW_IDS = 8;
    public static final int AVFMT_TBCF_AUTO = -1;
    public static final int AVFMT_TBCF_DECODER = 0;
    public static final int AVFMT_TBCF_DEMUXER = 1;
    public static final int AVFMT_TBCF_R_FRAMERATE = 2;
    public static final int AVFMT_TS_DISCONT = 512;
    public static final int AVFMT_TS_NEGATIVE = 262144;
    public static final int AVFMT_TS_NONSTRICT = 131072;
    public static final int AVFMT_VARIABLE_FPS = 1024;
    public static final int AVIO_DATA_MARKER_BOUNDARY_POINT = 2;
    public static final int AVIO_DATA_MARKER_HEADER = 0;
    public static final int AVIO_DATA_MARKER_SYNC_POINT = 1;
    public static final int AVIO_DATA_MARKER_TRAILER = 4;
    public static final int AVIO_DATA_MARKER_UNKNOWN = 3;
    public static final int AVIO_ENTRY_BLOCK_DEVICE = 1;
    public static final int AVIO_ENTRY_CHARACTER_DEVICE = 2;
    public static final int AVIO_ENTRY_DIRECTORY = 3;
    public static final int AVIO_ENTRY_FILE = 7;
    public static final int AVIO_ENTRY_NAMED_PIPE = 4;
    public static final int AVIO_ENTRY_SERVER = 8;
    public static final int AVIO_ENTRY_SHARE = 9;
    public static final int AVIO_ENTRY_SOCKET = 6;
    public static final int AVIO_ENTRY_SYMBOLIC_LINK = 5;
    public static final int AVIO_ENTRY_UNKNOWN = 0;
    public static final int AVIO_ENTRY_WORKGROUP = 10;
    public static final int AVIO_FLAG_DIRECT = 32768;
    public static final int AVIO_FLAG_NONBLOCK = 8;
    public static final int AVIO_FLAG_READ = 1;
    public static final int AVIO_FLAG_READ_WRITE = 3;
    public static final int AVIO_FLAG_WRITE = 2;
    public static final int AVIO_SEEKABLE_NORMAL = 1;
    public static final int AVPROBE_PADDING_SIZE = 32;
    public static final int AVPROBE_SCORE_EXTENSION = 50;
    public static final int AVPROBE_SCORE_MAX = 100;
    public static final int AVPROBE_SCORE_MIME = 75;
    public static final int AVPROBE_SCORE_RETRY = AVPROBE_SCORE_RETRY();
    public static final int AVPROBE_SCORE_STREAM_RETRY = AVPROBE_SCORE_STREAM_RETRY();
    public static final int AVSEEK_FLAG_ANY = 4;
    public static final int AVSEEK_FLAG_BACKWARD = 1;
    public static final int AVSEEK_FLAG_BYTE = 2;
    public static final int AVSEEK_FLAG_FRAME = 8;
    public static final int AVSEEK_FORCE = 131072;
    public static final int AVSEEK_SIZE = 65536;
    public static final int AVSTREAM_INIT_IN_INIT_OUTPUT = 1;
    public static final int AVSTREAM_INIT_IN_WRITE_HEADER = 0;
    public static final int AVSTREAM_PARSE_FULL = 1;
    public static final int AVSTREAM_PARSE_FULL_ONCE = 4;
    public static final int AVSTREAM_PARSE_FULL_RAW = AVSTREAM_PARSE_FULL_RAW();
    public static final int AVSTREAM_PARSE_HEADERS = 2;
    public static final int AVSTREAM_PARSE_NONE = 0;
    public static final int AVSTREAM_PARSE_TIMESTAMPS = 3;
    public static final int AV_DISPOSITION_ATTACHED_PIC = 1024;
    public static final int AV_DISPOSITION_CAPTIONS = 65536;
    public static final int AV_DISPOSITION_CLEAN_EFFECTS = 512;
    public static final int AV_DISPOSITION_COMMENT = 8;
    public static final int AV_DISPOSITION_DEFAULT = 1;
    public static final int AV_DISPOSITION_DESCRIPTIONS = 131072;
    public static final int AV_DISPOSITION_DUB = 2;
    public static final int AV_DISPOSITION_FORCED = 64;
    public static final int AV_DISPOSITION_HEARING_IMPAIRED = 128;
    public static final int AV_DISPOSITION_KARAOKE = 32;
    public static final int AV_DISPOSITION_LYRICS = 16;
    public static final int AV_DISPOSITION_METADATA = 262144;
    public static final int AV_DISPOSITION_ORIGINAL = 4;
    public static final int AV_DISPOSITION_TIMED_THUMBNAILS = 2048;
    public static final int AV_DISPOSITION_VISUAL_IMPAIRED = 256;
    public static final int AV_FRAME_FILENAME_FLAGS_MULTIPLE = 1;
    public static final int AV_PROGRAM_RUNNING = 1;
    public static final int AV_PTS_WRAP_ADD_OFFSET = 1;
    public static final int AV_PTS_WRAP_IGNORE = 0;
    public static final int AV_PTS_WRAP_SUB_OFFSET = -1;

    @Opaque
    public static class AVBPrint extends Pointer {
        public AVBPrint() {
            super((Pointer) null);
        }

        public AVBPrint(Pointer p) {
            super(p);
        }
    }

    public static class AVChapter extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"int64_t"})
        public native long end();

        public native AVChapter end(long j);

        public native int id();

        public native AVChapter id(int i);

        public native AVChapter metadata(AVDictionary aVDictionary);

        public native AVDictionary metadata();

        @Cast({"int64_t"})
        public native long start();

        public native AVChapter start(long j);

        public native AVChapter time_base(AVRational aVRational);

        @ByRef
        public native AVRational time_base();

        static {
            Loader.load();
        }

        public AVChapter() {
            super((Pointer) null);
            allocate();
        }

        public AVChapter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVChapter(Pointer p) {
            super(p);
        }

        public AVChapter position(long position) {
            return (AVChapter) super.position(position);
        }
    }

    @Opaque
    public static class AVCodecTag extends Pointer {
        public AVCodecTag() {
            super((Pointer) null);
        }

        public AVCodecTag(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class AVDeviceCapabilitiesQuery extends Pointer {
        public AVDeviceCapabilitiesQuery() {
            super((Pointer) null);
        }

        public AVDeviceCapabilitiesQuery(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class AVDeviceInfoList extends Pointer {
        public AVDeviceInfoList() {
            super((Pointer) null);
        }

        public AVDeviceInfoList(Pointer p) {
            super(p);
        }
    }

    public static class AVFormatContext extends Pointer {
        public static final int AVFMT_AVOID_NEG_TS_AUTO = -1;
        public static final int AVFMT_AVOID_NEG_TS_MAKE_NON_NEGATIVE = 1;
        public static final int AVFMT_AVOID_NEG_TS_MAKE_ZERO = 2;
        public static final int AVFMT_EVENT_FLAG_METADATA_UPDATED = 1;
        public static final int AVFMT_FLAG_AUTO_BSF = 2097152;
        public static final int AVFMT_FLAG_BITEXACT = 1024;
        public static final int AVFMT_FLAG_CUSTOM_IO = 128;
        public static final int AVFMT_FLAG_DISCARD_CORRUPT = 256;
        public static final int AVFMT_FLAG_FAST_SEEK = 524288;
        public static final int AVFMT_FLAG_FLUSH_PACKETS = 512;
        public static final int AVFMT_FLAG_GENPTS = 1;
        public static final int AVFMT_FLAG_IGNDTS = 8;
        public static final int AVFMT_FLAG_IGNIDX = 2;
        public static final int AVFMT_FLAG_KEEP_SIDE_DATA = 262144;
        public static final int AVFMT_FLAG_MP4A_LATM = 32768;
        public static final int AVFMT_FLAG_NOBUFFER = 64;
        public static final int AVFMT_FLAG_NOFILLIN = 16;
        public static final int AVFMT_FLAG_NONBLOCK = 4;
        public static final int AVFMT_FLAG_NOPARSE = 32;
        public static final int AVFMT_FLAG_PRIV_OPT = 131072;
        public static final int AVFMT_FLAG_SHORTEST = 1048576;
        public static final int AVFMT_FLAG_SORT_DTS = 65536;
        public static final int FF_FDEBUG_TS = 1;

        public static class C0798xf8645eea extends FunctionPointer {
            private native void allocate();

            @Deprecated
            public native int call(AVFormatContext aVFormatContext, @Cast({"AVIOContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, int i, @Const AVIOInterruptCB aVIOInterruptCB, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

            static {
                Loader.load();
            }

            public C0798xf8645eea(Pointer p) {
                super(p);
            }

            protected C0798xf8645eea() {
                allocate();
            }
        }

        private native void allocate();

        private native void allocateArray(long j);

        public native AVCodec audio_codec();

        public native AVFormatContext audio_codec(AVCodec aVCodec);

        @Cast({"AVCodecID"})
        public native int audio_codec_id();

        public native AVFormatContext audio_codec_id(int i);

        public native int audio_preload();

        public native AVFormatContext audio_preload(int i);

        @MemberGetter
        @Const
        public native AVClass av_class();

        public native int avio_flags();

        public native AVFormatContext avio_flags(int i);

        public native int avoid_negative_ts();

        public native AVFormatContext avoid_negative_ts(int i);

        @Cast({"int64_t"})
        public native long bit_rate();

        public native AVFormatContext bit_rate(long j);

        @MemberGetter
        @Cast({"AVChapter**"})
        public native PointerPointer chapters();

        public native AVChapter chapters(int i);

        public native AVFormatContext chapters(int i, AVChapter aVChapter);

        @Cast({"char*"})
        public native BytePointer codec_whitelist();

        public native AVFormatContext codec_whitelist(BytePointer bytePointer);

        public native AVFormatContext control_message_cb(av_format_control_message org_bytedeco_javacpp_avformat_av_format_control_message);

        public native av_format_control_message control_message_cb();

        @Cast({"unsigned int"})
        public native int correct_ts_overflow();

        public native AVFormatContext correct_ts_overflow(int i);

        public native int ctx_flags();

        public native AVFormatContext ctx_flags(int i);

        public native AVCodec data_codec();

        public native AVFormatContext data_codec(AVCodec aVCodec);

        @Cast({"AVCodecID"})
        public native int data_codec_id();

        public native AVFormatContext data_codec_id(int i);

        public native int debug();

        public native AVFormatContext debug(int i);

        @Cast({"uint8_t*"})
        public native BytePointer dump_separator();

        public native AVFormatContext dump_separator(BytePointer bytePointer);

        @Cast({"int64_t"})
        public native long duration();

        public native AVFormatContext duration(long j);

        @Cast({"AVDurationEstimationMethod"})
        public native int duration_estimation_method();

        public native AVFormatContext duration_estimation_method(int i);

        public native int error_recognition();

        public native AVFormatContext error_recognition(int i);

        public native int event_flags();

        public native AVFormatContext event_flags(int i);

        @Cast({"char"})
        public native byte filename(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer filename();

        public native AVFormatContext filename(int i, byte b);

        public native int flags();

        public native AVFormatContext flags(int i);

        public native int flush_packets();

        public native AVFormatContext flush_packets(int i);

        public native int format_probesize();

        public native AVFormatContext format_probesize(int i);

        @Cast({"char*"})
        public native BytePointer format_whitelist();

        public native AVFormatContext format_whitelist(BytePointer bytePointer);

        public native int fps_probe_size();

        public native AVFormatContext fps_probe_size(int i);

        public native AVFormatContext iformat(AVInputFormat aVInputFormat);

        public native AVInputFormat iformat();

        public native AVFormatContext internal(AVFormatInternal aVFormatInternal);

        public native AVFormatInternal internal();

        public native AVFormatContext interrupt_callback(AVIOInterruptCB aVIOInterruptCB);

        @ByRef
        public native AVIOInterruptCB interrupt_callback();

        public native Io_close_AVFormatContext_AVIOContext io_close();

        public native AVFormatContext io_close(Io_close_AVFormatContext_AVIOContext io_close_AVFormatContext_AVIOContext);

        public native Io_open_AVFormatContext_PointerPointer_BytePointer_int_PointerPointer io_open();

        public native AVFormatContext io_open(Io_open_AVFormatContext_PointerPointer_BytePointer_int_PointerPointer io_open_AVFormatContext_PointerPointer_BytePointer_int_PointerPointer);

        public native int io_repositioned();

        public native AVFormatContext io_repositioned(int i);

        @MemberGetter
        @Cast({"const uint8_t*"})
        public native BytePointer key();

        public native int keylen();

        public native AVFormatContext keylen(int i);

        @Cast({"int64_t"})
        public native long max_analyze_duration();

        public native AVFormatContext max_analyze_duration(long j);

        public native int max_chunk_duration();

        public native AVFormatContext max_chunk_duration(int i);

        public native int max_chunk_size();

        public native AVFormatContext max_chunk_size(int i);

        public native int max_delay();

        public native AVFormatContext max_delay(int i);

        @Cast({"unsigned int"})
        public native int max_index_size();

        public native AVFormatContext max_index_size(int i);

        @Cast({"int64_t"})
        public native long max_interleave_delta();

        public native AVFormatContext max_interleave_delta(long j);

        @Cast({"unsigned int"})
        public native int max_picture_buffer();

        public native AVFormatContext max_picture_buffer(int i);

        public native int max_ts_probe();

        public native AVFormatContext max_ts_probe(int i);

        public native AVFormatContext metadata(AVDictionary aVDictionary);

        public native AVDictionary metadata();

        public native int metadata_header_padding();

        public native AVFormatContext metadata_header_padding(int i);

        @Cast({"unsigned int"})
        public native int nb_chapters();

        public native AVFormatContext nb_chapters(int i);

        @Cast({"unsigned int"})
        public native int nb_programs();

        public native AVFormatContext nb_programs(int i);

        @Cast({"unsigned int"})
        public native int nb_streams();

        public native AVFormatContext nb_streams(int i);

        public native AVFormatContext oformat(AVOutputFormat aVOutputFormat);

        public native AVOutputFormat oformat();

        public native Pointer opaque();

        public native AVFormatContext opaque(Pointer pointer);

        public native C0798xf8645eea open_cb();

        public native AVFormatContext open_cb(C0798xf8645eea c0798xf8645eea);

        @Cast({"int64_t"})
        public native long output_ts_offset();

        public native AVFormatContext output_ts_offset(long j);

        @Cast({"unsigned int"})
        public native int packet_size();

        public native AVFormatContext packet_size(int i);

        public native AVFormatContext pb(AVIOContext aVIOContext);

        public native AVIOContext pb();

        public native Pointer priv_data();

        public native AVFormatContext priv_data(Pointer pointer);

        public native int probe_score();

        public native AVFormatContext probe_score(int i);

        @Cast({"int64_t"})
        public native long probesize();

        public native AVFormatContext probesize(long j);

        @MemberGetter
        @Cast({"AVProgram**"})
        public native PointerPointer programs();

        public native AVFormatContext programs(int i, AVProgram aVProgram);

        public native AVProgram programs(int i);

        @Cast({"char*"})
        public native BytePointer protocol_blacklist();

        public native AVFormatContext protocol_blacklist(BytePointer bytePointer);

        @Cast({"char*"})
        public native BytePointer protocol_whitelist();

        public native AVFormatContext protocol_whitelist(BytePointer bytePointer);

        public native int seek2any();

        public native AVFormatContext seek2any(int i);

        @Cast({"int64_t"})
        public native long skip_initial_bytes();

        public native AVFormatContext skip_initial_bytes(long j);

        @Cast({"int64_t"})
        public native long start_time();

        public native AVFormatContext start_time(long j);

        @Cast({"int64_t"})
        public native long start_time_realtime();

        public native AVFormatContext start_time_realtime(long j);

        @MemberGetter
        @Cast({"AVStream**"})
        public native PointerPointer streams();

        public native AVFormatContext streams(int i, AVStream aVStream);

        public native AVStream streams(int i);

        public native int strict_std_compliance();

        public native AVFormatContext strict_std_compliance(int i);

        public native AVCodec subtitle_codec();

        public native AVFormatContext subtitle_codec(AVCodec aVCodec);

        @Cast({"AVCodecID"})
        public native int subtitle_codec_id();

        public native AVFormatContext subtitle_codec_id(int i);

        public native int ts_id();

        public native AVFormatContext ts_id(int i);

        public native int use_wallclock_as_timestamps();

        public native AVFormatContext use_wallclock_as_timestamps(int i);

        public native AVCodec video_codec();

        public native AVFormatContext video_codec(AVCodec aVCodec);

        @Cast({"AVCodecID"})
        public native int video_codec_id();

        public native AVFormatContext video_codec_id(int i);

        static {
            Loader.load();
        }

        public AVFormatContext() {
            super((Pointer) null);
            allocate();
        }

        public AVFormatContext(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFormatContext(Pointer p) {
            super(p);
        }

        public AVFormatContext position(long position) {
            return (AVFormatContext) super.position(position);
        }
    }

    @Opaque
    public static class AVFormatInternal extends Pointer {
        public AVFormatInternal() {
            super((Pointer) null);
        }

        public AVFormatInternal(Pointer p) {
            super(p);
        }
    }

    public static class AVFrac extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"int64_t"})
        public native long den();

        public native AVFrac den(long j);

        @Cast({"int64_t"})
        public native long num();

        public native AVFrac num(long j);

        @Cast({"int64_t"})
        public native long val();

        public native AVFrac val(long j);

        static {
            Loader.load();
        }

        public AVFrac() {
            super((Pointer) null);
            allocate();
        }

        public AVFrac(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVFrac(Pointer p) {
            super(p);
        }

        public AVFrac position(long position) {
            return (AVFrac) super.position(position);
        }
    }

    public static class AVIOContext extends Pointer {

        public static class Read_seek_Pointer_int_long_int extends FunctionPointer {
            private native void allocate();

            @Cast({"int64_t"})
            public native long call(Pointer pointer, int i, @Cast({"int64_t"}) long j, int i2);

            static {
                Loader.load();
            }

            public Read_seek_Pointer_int_long_int(Pointer p) {
                super(p);
            }

            protected Read_seek_Pointer_int_long_int() {
                allocate();
            }
        }

        public static class Seek_Pointer_long_int extends FunctionPointer {
            private native void allocate();

            @Cast({"int64_t"})
            public native long call(Pointer pointer, @Cast({"int64_t"}) long j, int i);

            static {
                Loader.load();
            }

            public Seek_Pointer_long_int(Pointer p) {
                super(p);
            }

            protected Seek_Pointer_long_int() {
                allocate();
            }
        }

        public static class Update_checksum_long_BytePointer_int extends FunctionPointer {
            private native void allocate();

            @Cast({"unsigned long"})
            public native long call(@Cast({"unsigned long"}) long j, @Cast({"const uint8_t*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

            static {
                Loader.load();
            }

            public Update_checksum_long_BytePointer_int(Pointer p) {
                super(p);
            }

            protected Update_checksum_long_BytePointer_int() {
                allocate();
            }
        }

        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Const
        public native AVClass av_class();

        @Cast({"unsigned char*"})
        public native BytePointer buf_end();

        public native AVIOContext buf_end(BytePointer bytePointer);

        @Cast({"unsigned char*"})
        public native BytePointer buf_ptr();

        public native AVIOContext buf_ptr(BytePointer bytePointer);

        @Cast({"unsigned char*"})
        public native BytePointer buffer();

        public native AVIOContext buffer(BytePointer bytePointer);

        public native int buffer_size();

        public native AVIOContext buffer_size(int i);

        @Cast({"int64_t"})
        public native long bytes_read();

        public native AVIOContext bytes_read(long j);

        @Cast({"unsigned long"})
        public native long checksum();

        public native AVIOContext checksum(long j);

        @Cast({"unsigned char*"})
        public native BytePointer checksum_ptr();

        public native AVIOContext checksum_ptr(BytePointer bytePointer);

        @Cast({"AVIODataMarkerType"})
        public native int current_type();

        public native AVIOContext current_type(int i);

        public native int direct();

        public native AVIOContext direct(int i);

        public native int eof_reached();

        public native AVIOContext eof_reached(int i);

        public native int error();

        public native AVIOContext error(int i);

        public native int ignore_boundary_point();

        public native AVIOContext ignore_boundary_point(int i);

        @Cast({"int64_t"})
        public native long last_time();

        public native AVIOContext last_time(long j);

        public native int max_packet_size();

        public native AVIOContext max_packet_size(int i);

        @Cast({"int64_t"})
        public native long maxsize();

        public native AVIOContext maxsize(long j);

        public native int must_flush();

        public native AVIOContext must_flush(int i);

        public native Pointer opaque();

        public native AVIOContext opaque(Pointer pointer);

        public native int orig_buffer_size();

        public native AVIOContext orig_buffer_size(int i);

        @Cast({"int64_t"})
        public native long pos();

        public native AVIOContext pos(long j);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer protocol_blacklist();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer protocol_whitelist();

        public native Read_packet_Pointer_BytePointer_int read_packet();

        public native AVIOContext read_packet(Read_packet_Pointer_BytePointer_int read_packet_Pointer_BytePointer_int);

        public native Read_pause_Pointer_int read_pause();

        public native AVIOContext read_pause(Read_pause_Pointer_int read_pause_Pointer_int);

        public native Read_seek_Pointer_int_long_int read_seek();

        public native AVIOContext read_seek(Read_seek_Pointer_int_long_int read_seek_Pointer_int_long_int);

        public native Seek_Pointer_long_int seek();

        public native AVIOContext seek(Seek_Pointer_long_int seek_Pointer_long_int);

        public native int seek_count();

        public native AVIOContext seek_count(int i);

        public native int seekable();

        public native AVIOContext seekable(int i);

        public native int short_seek_threshold();

        public native AVIOContext short_seek_threshold(int i);

        public native Update_checksum_long_BytePointer_int update_checksum();

        public native AVIOContext update_checksum(Update_checksum_long_BytePointer_int update_checksum_long_BytePointer_int);

        public native Write_data_type_Pointer_BytePointer_int_int_long write_data_type();

        public native AVIOContext write_data_type(Write_data_type_Pointer_BytePointer_int_int_long write_data_type_Pointer_BytePointer_int_int_long);

        public native int write_flag();

        public native AVIOContext write_flag(int i);

        public native Write_packet_Pointer_BytePointer_int write_packet();

        public native AVIOContext write_packet(Write_packet_Pointer_BytePointer_int write_packet_Pointer_BytePointer_int);

        public native int writeout_count();

        public native AVIOContext writeout_count(int i);

        static {
            Loader.load();
        }

        public AVIOContext() {
            super((Pointer) null);
            allocate();
        }

        public AVIOContext(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVIOContext(Pointer p) {
            super(p);
        }

        public AVIOContext position(long position) {
            return (AVIOContext) super.position(position);
        }
    }

    public static class AVIODirContext extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"URLContext*"})
        public native Pointer url_context();

        public native AVIODirContext url_context(Pointer pointer);

        static {
            Loader.load();
        }

        public AVIODirContext() {
            super((Pointer) null);
            allocate();
        }

        public AVIODirContext(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVIODirContext(Pointer p) {
            super(p);
        }

        public AVIODirContext position(long position) {
            return (AVIODirContext) super.position(position);
        }
    }

    public static class AVIODirEntry extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"int64_t"})
        public native long access_timestamp();

        public native AVIODirEntry access_timestamp(long j);

        @Cast({"int64_t"})
        public native long filemode();

        public native AVIODirEntry filemode(long j);

        @Cast({"int64_t"})
        public native long group_id();

        public native AVIODirEntry group_id(long j);

        @Cast({"int64_t"})
        public native long modification_timestamp();

        public native AVIODirEntry modification_timestamp(long j);

        @Cast({"char*"})
        public native BytePointer name();

        public native AVIODirEntry name(BytePointer bytePointer);

        @Cast({"int64_t"})
        public native long size();

        public native AVIODirEntry size(long j);

        @Cast({"int64_t"})
        public native long status_change_timestamp();

        public native AVIODirEntry status_change_timestamp(long j);

        public native int type();

        public native AVIODirEntry type(int i);

        @Cast({"int64_t"})
        public native long user_id();

        public native AVIODirEntry user_id(long j);

        public native int utf8();

        public native AVIODirEntry utf8(int i);

        static {
            Loader.load();
        }

        public AVIODirEntry() {
            super((Pointer) null);
            allocate();
        }

        public AVIODirEntry(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVIODirEntry(Pointer p) {
            super(p);
        }

        public AVIODirEntry position(long position) {
            return (AVIODirEntry) super.position(position);
        }
    }

    public static class AVIndexEntry extends Pointer {
        public static final int AVINDEX_DISCARD_FRAME = 2;
        public static final int AVINDEX_KEYFRAME = 1;

        private native void allocate();

        private native void allocateArray(long j);

        @NoOffset
        public native int flags();

        public native AVIndexEntry flags(int i);

        public native int min_distance();

        public native AVIndexEntry min_distance(int i);

        @Cast({"int64_t"})
        public native long pos();

        public native AVIndexEntry pos(long j);

        @NoOffset
        public native int size();

        public native AVIndexEntry size(int i);

        @Cast({"int64_t"})
        public native long timestamp();

        public native AVIndexEntry timestamp(long j);

        static {
            Loader.load();
        }

        public AVIndexEntry() {
            super((Pointer) null);
            allocate();
        }

        public AVIndexEntry(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVIndexEntry(Pointer p) {
            super(p);
        }

        public AVIndexEntry position(long position) {
            return (AVIndexEntry) super.position(position);
        }
    }

    public static class AVInputFormat extends Pointer {

        public static class Read_timestamp_AVFormatContext_int_LongPointer_long extends FunctionPointer {
            private native void allocate();

            @Cast({"int64_t"})
            public native long call(AVFormatContext aVFormatContext, int i, @Cast({"int64_t*"}) LongPointer longPointer, @Cast({"int64_t"}) long j);

            static {
                Loader.load();
            }

            public Read_timestamp_AVFormatContext_int_LongPointer_long(Pointer p) {
                super(p);
            }

            protected Read_timestamp_AVFormatContext_int_LongPointer_long() {
                allocate();
            }
        }

        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Cast({"const AVCodecTag*const*"})
        public native PointerPointer codec_tag();

        @MemberGetter
        @Const
        public native AVCodecTag codec_tag(int i);

        public native Create_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery create_device_capabilities();

        public native AVInputFormat create_device_capabilities(Create_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery create_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer extensions();

        public native int flags();

        public native AVInputFormat flags(int i);

        public native Free_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery free_device_capabilities();

        public native AVInputFormat free_device_capabilities(Free_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery free_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery);

        public native Get_device_list_AVFormatContext_AVDeviceInfoList get_device_list();

        public native AVInputFormat get_device_list(Get_device_list_AVFormatContext_AVDeviceInfoList get_device_list_AVFormatContext_AVDeviceInfoList);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer long_name();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer mime_type();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer name();

        public native AVInputFormat next();

        public native AVInputFormat next(AVInputFormat aVInputFormat);

        @MemberGetter
        @Const
        public native AVClass priv_class();

        public native int priv_data_size();

        public native AVInputFormat priv_data_size(int i);

        public native int raw_codec_id();

        public native AVInputFormat raw_codec_id(int i);

        public native Read_close_AVFormatContext read_close();

        public native AVInputFormat read_close(Read_close_AVFormatContext read_close_AVFormatContext);

        public native Read_header_AVFormatContext read_header();

        public native AVInputFormat read_header(Read_header_AVFormatContext read_header_AVFormatContext);

        public native Read_packet_AVFormatContext_AVPacket read_packet();

        public native AVInputFormat read_packet(Read_packet_AVFormatContext_AVPacket read_packet_AVFormatContext_AVPacket);

        public native Read_pause_AVFormatContext read_pause();

        public native AVInputFormat read_pause(Read_pause_AVFormatContext read_pause_AVFormatContext);

        public native Read_play_AVFormatContext read_play();

        public native AVInputFormat read_play(Read_play_AVFormatContext read_play_AVFormatContext);

        public native Read_probe_AVProbeData read_probe();

        public native AVInputFormat read_probe(Read_probe_AVProbeData read_probe_AVProbeData);

        public native Read_seek_AVFormatContext_int_long_int read_seek();

        public native AVInputFormat read_seek(Read_seek_AVFormatContext_int_long_int read_seek_AVFormatContext_int_long_int);

        public native Read_seek2_AVFormatContext_int_long_long_long_int read_seek2();

        public native AVInputFormat read_seek2(Read_seek2_AVFormatContext_int_long_long_long_int read_seek2_AVFormatContext_int_long_long_long_int);

        public native Read_timestamp_AVFormatContext_int_LongPointer_long read_timestamp();

        public native AVInputFormat read_timestamp(Read_timestamp_AVFormatContext_int_LongPointer_long read_timestamp_AVFormatContext_int_LongPointer_long);

        static {
            Loader.load();
        }

        public AVInputFormat() {
            super((Pointer) null);
            allocate();
        }

        public AVInputFormat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVInputFormat(Pointer p) {
            super(p);
        }

        public AVInputFormat position(long position) {
            return (AVInputFormat) super.position(position);
        }
    }

    public static class AVOutputFormat extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"AVCodecID"})
        public native int audio_codec();

        public native AVOutputFormat audio_codec(int i);

        public native Check_bitstream_AVFormatContext_AVPacket check_bitstream();

        public native AVOutputFormat check_bitstream(Check_bitstream_AVFormatContext_AVPacket check_bitstream_AVFormatContext_AVPacket);

        @MemberGetter
        @Cast({"const AVCodecTag*const*"})
        public native PointerPointer codec_tag();

        @MemberGetter
        @Const
        public native AVCodecTag codec_tag(int i);

        public native Control_message_AVFormatContext_int_Pointer_long control_message();

        public native AVOutputFormat control_message(Control_message_AVFormatContext_int_Pointer_long control_message_AVFormatContext_int_Pointer_long);

        public native Create_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery create_device_capabilities();

        public native AVOutputFormat create_device_capabilities(Create_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery create_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery);

        @Cast({"AVCodecID"})
        public native int data_codec();

        public native AVOutputFormat data_codec(int i);

        public native Deinit_AVFormatContext deinit();

        public native AVOutputFormat deinit(Deinit_AVFormatContext deinit_AVFormatContext);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer extensions();

        public native int flags();

        public native AVOutputFormat flags(int i);

        public native Free_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery free_device_capabilities();

        public native AVOutputFormat free_device_capabilities(Free_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery free_device_capabilities_AVFormatContext_AVDeviceCapabilitiesQuery);

        public native Get_device_list_AVFormatContext_AVDeviceInfoList get_device_list();

        public native AVOutputFormat get_device_list(Get_device_list_AVFormatContext_AVDeviceInfoList get_device_list_AVFormatContext_AVDeviceInfoList);

        public native Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer get_output_timestamp();

        public native AVOutputFormat get_output_timestamp(Get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer get_output_timestamp_AVFormatContext_int_LongPointer_LongPointer);

        public native Init_AVFormatContext init();

        public native AVOutputFormat init(Init_AVFormatContext init_AVFormatContext);

        public native Interleave_packet_AVFormatContext_AVPacket_AVPacket_int interleave_packet();

        public native AVOutputFormat interleave_packet(Interleave_packet_AVFormatContext_AVPacket_AVPacket_int interleave_packet_AVFormatContext_AVPacket_AVPacket_int);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer long_name();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer mime_type();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer name();

        public native AVOutputFormat next();

        public native AVOutputFormat next(AVOutputFormat aVOutputFormat);

        @MemberGetter
        @Const
        public native AVClass priv_class();

        public native int priv_data_size();

        public native AVOutputFormat priv_data_size(int i);

        public native Query_codec_int_int query_codec();

        public native AVOutputFormat query_codec(Query_codec_int_int query_codec_int_int);

        @Cast({"AVCodecID"})
        public native int subtitle_codec();

        public native AVOutputFormat subtitle_codec(int i);

        @Cast({"AVCodecID"})
        public native int video_codec();

        public native AVOutputFormat video_codec(int i);

        public native Write_header_AVFormatContext write_header();

        public native AVOutputFormat write_header(Write_header_AVFormatContext write_header_AVFormatContext);

        public native Write_packet_AVFormatContext_AVPacket write_packet();

        public native AVOutputFormat write_packet(Write_packet_AVFormatContext_AVPacket write_packet_AVFormatContext_AVPacket);

        public native Write_trailer_AVFormatContext write_trailer();

        public native AVOutputFormat write_trailer(Write_trailer_AVFormatContext write_trailer_AVFormatContext);

        public native Write_uncoded_frame_AVFormatContext_int_PointerPointer_int write_uncoded_frame();

        public native AVOutputFormat write_uncoded_frame(Write_uncoded_frame_AVFormatContext_int_PointerPointer_int write_uncoded_frame_AVFormatContext_int_PointerPointer_int);

        static {
            Loader.load();
        }

        public AVOutputFormat() {
            super((Pointer) null);
            allocate();
        }

        public AVOutputFormat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVOutputFormat(Pointer p) {
            super(p);
        }

        public AVOutputFormat position(long position) {
            return (AVOutputFormat) super.position(position);
        }
    }

    public static class AVPacketList extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native AVPacketList next();

        public native AVPacketList next(AVPacketList aVPacketList);

        @ByRef
        public native AVPacket pkt();

        public native AVPacketList pkt(AVPacket aVPacket);

        static {
            Loader.load();
        }

        public AVPacketList() {
            super((Pointer) null);
            allocate();
        }

        public AVPacketList(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVPacketList(Pointer p) {
            super(p);
        }

        public AVPacketList position(long position) {
            return (AVPacketList) super.position(position);
        }
    }

    public static class AVProbeData extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned char*"})
        public native BytePointer buf();

        public native AVProbeData buf(BytePointer bytePointer);

        public native int buf_size();

        public native AVProbeData buf_size(int i);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer filename();

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer mime_type();

        static {
            Loader.load();
        }

        public AVProbeData() {
            super((Pointer) null);
            allocate();
        }

        public AVProbeData(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVProbeData(Pointer p) {
            super(p);
        }

        public AVProbeData position(long position) {
            return (AVProbeData) super.position(position);
        }
    }

    public static class AVProgram extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"AVDiscard"})
        public native int discard();

        public native AVProgram discard(int i);

        @Cast({"int64_t"})
        public native long end_time();

        public native AVProgram end_time(long j);

        public native int flags();

        public native AVProgram flags(int i);

        public native int id();

        public native AVProgram id(int i);

        public native AVProgram metadata(AVDictionary aVDictionary);

        public native AVDictionary metadata();

        @Cast({"unsigned int"})
        public native int nb_stream_indexes();

        public native AVProgram nb_stream_indexes(int i);

        public native int pcr_pid();

        public native AVProgram pcr_pid(int i);

        public native int pmt_pid();

        public native AVProgram pmt_pid(int i);

        public native int program_num();

        public native AVProgram program_num(int i);

        public native int pts_wrap_behavior();

        public native AVProgram pts_wrap_behavior(int i);

        @Cast({"int64_t"})
        public native long pts_wrap_reference();

        public native AVProgram pts_wrap_reference(long j);

        @Cast({"int64_t"})
        public native long start_time();

        public native AVProgram start_time(long j);

        @Cast({"unsigned int*"})
        public native IntPointer stream_index();

        public native AVProgram stream_index(IntPointer intPointer);

        static {
            Loader.load();
        }

        public AVProgram() {
            super((Pointer) null);
            allocate();
        }

        public AVProgram(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVProgram(Pointer p) {
            super(p);
        }

        public AVProgram position(long position) {
            return (AVProgram) super.position(position);
        }
    }

    public static class AVStream extends Pointer {
        public static final int AVSTREAM_EVENT_FLAG_METADATA_UPDATED = 1;
        public static final int MAX_REORDER_DELAY = 16;
        public static final int MAX_STD_TIMEBASES = 399;

        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        public native AVPacket attached_pic();

        public native AVStream attached_pic(AVPacket aVPacket);

        public native AVStream avg_frame_rate(AVRational aVRational);

        @ByRef
        public native AVRational avg_frame_rate();

        @Deprecated
        public native AVCodecContext codec();

        public native AVStream codec(AVCodecContext aVCodecContext);

        public native int codec_info_nb_frames();

        public native AVStream codec_info_nb_frames(int i);

        public native AVCodecParameters codecpar();

        public native AVStream codecpar(AVCodecParameters aVCodecParameters);

        @Cast({"int64_t"})
        public native long cur_dts();

        public native AVStream cur_dts(long j);

        @Cast({"AVDiscard"})
        public native int discard();

        public native AVStream discard(int i);

        public native AVStream display_aspect_ratio(AVRational aVRational);

        @ByRef
        public native AVRational display_aspect_ratio();

        public native int disposition();

        public native AVStream disposition(int i);

        @Cast({"uint8_t"})
        public native byte dts_misordered();

        public native AVStream dts_misordered(byte b);

        @Cast({"uint8_t"})
        public native byte dts_ordered();

        public native AVStream dts_ordered(byte b);

        @Cast({"int64_t"})
        public native long duration();

        public native AVStream duration(long j);

        public native int event_flags();

        public native AVStream event_flags(int i);

        @Cast({"int64_t"})
        public native long first_discard_sample();

        public native AVStream first_discard_sample(long j);

        @Cast({"int64_t"})
        public native long first_dts();

        public native AVStream first_dts(long j);

        public native int id();

        public native AVStream id(int i);

        public native int index();

        public native AVStream index(int i);

        public native AVIndexEntry index_entries();

        public native AVStream index_entries(AVIndexEntry aVIndexEntry);

        @Cast({"unsigned int"})
        public native int index_entries_allocated_size();

        public native AVStream index_entries_allocated_size(int i);

        @Cast({"int64_t"})
        @Name({"info", ".codec_info_duration"})
        public native long info_codec_info_duration(int i);

        public native AVStream info_codec_info_duration(int i, long j);

        @Cast({"int64_t"})
        @Name({"info", ".codec_info_duration_fields"})
        public native long info_codec_info_duration_fields(int i);

        public native AVStream info_codec_info_duration_fields(int i, long j);

        @Name({"info", ".duration_count"})
        public native int info_duration_count(int i);

        public native AVStream info_duration_count(int i, int i2);

        @MemberGetter
        @Cast({"double*"})
        @Name({"info", ".duration_error"})
        public native DoublePointer info_duration_error(int i);

        @Cast({"int64_t"})
        @Name({"info", ".duration_gcd"})
        public native long info_duration_gcd(int i);

        public native AVStream info_duration_gcd(int i, long j);

        @Name({"info", ".found_decoder"})
        public native int info_found_decoder(int i);

        public native AVStream info_found_decoder(int i, int i2);

        @Cast({"int64_t"})
        @Name({"info", ".fps_first_dts"})
        public native long info_fps_first_dts(int i);

        public native AVStream info_fps_first_dts(int i, long j);

        @Name({"info", ".fps_first_dts_idx"})
        public native int info_fps_first_dts_idx(int i);

        public native AVStream info_fps_first_dts_idx(int i, int i2);

        @Cast({"int64_t"})
        @Name({"info", ".fps_last_dts"})
        public native long info_fps_last_dts(int i);

        public native AVStream info_fps_last_dts(int i, long j);

        @Name({"info", ".fps_last_dts_idx"})
        public native int info_fps_last_dts_idx(int i);

        public native AVStream info_fps_last_dts_idx(int i, int i2);

        @Cast({"int64_t"})
        @Name({"info", ".last_dts"})
        public native long info_last_dts(int i);

        public native AVStream info_last_dts(int i, long j);

        @Cast({"int64_t"})
        @Name({"info", ".last_duration"})
        public native long info_last_duration(int i);

        public native AVStream info_last_duration(int i, long j);

        @Cast({"int64_t"})
        @Name({"info", ".rfps_duration_sum"})
        public native long info_rfps_duration_sum(int i);

        public native AVStream info_rfps_duration_sum(int i, long j);

        public native int inject_global_side_data();

        public native AVStream inject_global_side_data(int i);

        @Cast({"int64_t"})
        public native long interleaver_chunk_duration();

        public native AVStream interleaver_chunk_duration(long j);

        @Cast({"int64_t"})
        public native long interleaver_chunk_size();

        public native AVStream interleaver_chunk_size(long j);

        public native AVStream internal(AVStreamInternal aVStreamInternal);

        public native AVStreamInternal internal();

        public native int last_IP_duration();

        public native AVStream last_IP_duration(int i);

        @Cast({"int64_t"})
        public native long last_IP_pts();

        public native AVStream last_IP_pts(long j);

        @Cast({"int64_t"})
        public native long last_discard_sample();

        public native AVStream last_discard_sample(long j);

        @Cast({"int64_t"})
        public native long last_dts_for_order_check();

        public native AVStream last_dts_for_order_check(long j);

        public native AVPacketList last_in_packet_buffer();

        public native AVStream last_in_packet_buffer(AVPacketList aVPacketList);

        public native AVStream metadata(AVDictionary aVDictionary);

        public native AVDictionary metadata();

        @Cast({"int64_t"})
        public native long mux_ts_offset();

        public native AVStream mux_ts_offset(long j);

        public native int nb_decoded_frames();

        public native AVStream nb_decoded_frames(int i);

        @Cast({"int64_t"})
        public native long nb_frames();

        public native AVStream nb_frames(long j);

        public native int nb_index_entries();

        public native AVStream nb_index_entries(int i);

        public native int nb_side_data();

        public native AVStream nb_side_data(int i);

        @Cast({"AVStreamParseType"})
        public native int need_parsing();

        public native AVStream need_parsing(int i);

        public native AVCodecParserContext parser();

        public native AVStream parser(AVCodecParserContext aVCodecParserContext);

        public native Pointer priv_data();

        public native AVStream priv_data(Pointer pointer);

        @Cast({"FFFrac*"})
        public native Pointer priv_pts();

        public native AVStream priv_pts(Pointer pointer);

        @ByRef
        public native AVProbeData probe_data();

        public native AVStream probe_data(AVProbeData aVProbeData);

        public native int probe_packets();

        public native AVStream probe_packets(int i);

        @ByRef
        @Deprecated
        public native AVFrac pts();

        public native AVStream pts(AVFrac aVFrac);

        @Cast({"int64_t"})
        public native long pts_buffer(int i);

        @MemberGetter
        @Cast({"int64_t*"})
        public native LongPointer pts_buffer();

        public native AVStream pts_buffer(int i, long j);

        @Cast({"int64_t"})
        public native long pts_reorder_error(int i);

        @MemberGetter
        @Cast({"int64_t*"})
        public native LongPointer pts_reorder_error();

        public native AVStream pts_reorder_error(int i, long j);

        @Cast({"uint8_t"})
        public native byte pts_reorder_error_count(int i);

        @MemberGetter
        @Cast({"uint8_t*"})
        public native BytePointer pts_reorder_error_count();

        public native AVStream pts_reorder_error_count(int i, byte b);

        public native int pts_wrap_behavior();

        public native AVStream pts_wrap_behavior(int i);

        public native int pts_wrap_bits();

        public native AVStream pts_wrap_bits(int i);

        @Cast({"int64_t"})
        public native long pts_wrap_reference();

        public native AVStream pts_wrap_reference(long j);

        public native AVStream r_frame_rate(AVRational aVRational);

        @ByRef
        public native AVRational r_frame_rate();

        @Cast({"char*"})
        public native BytePointer recommended_encoder_configuration();

        public native AVStream recommended_encoder_configuration(BytePointer bytePointer);

        public native int request_probe();

        public native AVStream request_probe(int i);

        public native AVStream sample_aspect_ratio(AVRational aVRational);

        @ByRef
        public native AVRational sample_aspect_ratio();

        public native AVPacketSideData side_data();

        public native AVStream side_data(AVPacketSideData aVPacketSideData);

        public native int skip_samples();

        public native AVStream skip_samples(int i);

        public native int skip_to_keyframe();

        public native AVStream skip_to_keyframe(int i);

        @Cast({"int64_t"})
        public native long start_skip_samples();

        public native AVStream start_skip_samples(long j);

        @Cast({"int64_t"})
        public native long start_time();

        public native AVStream start_time(long j);

        public native int stream_identifier();

        public native AVStream stream_identifier(int i);

        public native AVStream time_base(AVRational aVRational);

        @ByRef
        public native AVRational time_base();

        public native int update_initial_durations_done();

        public native AVStream update_initial_durations_done(int i);

        static {
            Loader.load();
        }

        public AVStream() {
            super((Pointer) null);
            allocate();
        }

        public AVStream(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVStream(Pointer p) {
            super(p);
        }

        public AVStream position(long position) {
            return (AVStream) super.position(position);
        }
    }

    @Opaque
    public static class AVStreamInternal extends Pointer {
        public AVStreamInternal() {
            super((Pointer) null);
        }

        public AVStreamInternal(Pointer p) {
            super(p);
        }
    }

    public static class Seek_Pointer_long_int extends FunctionPointer {
        private native void allocate();

        @Cast({"int64_t"})
        public native long call(Pointer pointer, @Cast({"int64_t"}) long j, int i);

        static {
            Loader.load();
        }

        public Seek_Pointer_long_int(Pointer p) {
            super(p);
        }

        protected Seek_Pointer_long_int() {
            allocate();
        }
    }

    @MemberGetter
    public static native int AVPROBE_SCORE_RETRY();

    @MemberGetter
    public static native int AVPROBE_SCORE_STREAM_RETRY();

    @MemberGetter
    public static native int AVSTREAM_PARSE_FULL_RAW();

    @NoException
    public static native int av_add_index_entry(AVStream aVStream, @Cast({"int64_t"}) long j, @Cast({"int64_t"}) long j2, int i, int i2, int i3);

    @NoException
    public static native int av_append_packet(AVIOContext aVIOContext, AVPacket aVPacket, int i);

    @NoException
    @Deprecated
    public static native int av_apply_bitstream_filters(AVCodecContext aVCodecContext, AVPacket aVPacket, AVBitStreamFilterContext aVBitStreamFilterContext);

    @NoException
    @Cast({"AVCodecID"})
    public static native int av_codec_get_id(@Cast({"const AVCodecTag*const*"}) PointerPointer pointerPointer, @Cast({"unsigned int"}) int i);

    @NoException
    @Cast({"AVCodecID"})
    public static native int av_codec_get_id(@ByPtrPtr @Const AVCodecTag aVCodecTag, @Cast({"unsigned int"}) int i);

    @NoException
    @Cast({"unsigned int"})
    public static native int av_codec_get_tag(@Cast({"const AVCodecTag*const*"}) PointerPointer pointerPointer, @Cast({"AVCodecID"}) int i);

    @NoException
    @Cast({"unsigned int"})
    public static native int av_codec_get_tag(@ByPtrPtr @Const AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i);

    @NoException
    public static native int av_codec_get_tag2(@Cast({"const AVCodecTag*const*"}) PointerPointer pointerPointer, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @NoException
    public static native int av_codec_get_tag2(@ByPtrPtr @Const AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @NoException
    public static native int av_codec_get_tag2(@ByPtrPtr @Const AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @NoException
    public static native int av_codec_get_tag2(@ByPtrPtr @Const AVCodecTag aVCodecTag, @Cast({"AVCodecID"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @NoException
    @Deprecated
    public static native int av_demuxer_open(AVFormatContext aVFormatContext);

    @NoException
    public static native void av_dump_format(AVFormatContext aVFormatContext, int i, String str, int i2);

    @NoException
    public static native void av_dump_format(AVFormatContext aVFormatContext, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int av_filename_number_test(String str);

    @NoException
    public static native int av_filename_number_test(@Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int av_find_best_stream(AVFormatContext aVFormatContext, @Cast({"AVMediaType"}) int i, int i2, int i3, @Cast({"AVCodec**"}) PointerPointer pointerPointer, int i4);

    @NoException
    public static native int av_find_best_stream(AVFormatContext aVFormatContext, @Cast({"AVMediaType"}) int i, int i2, int i3, @ByPtrPtr AVCodec aVCodec, int i4);

    @NoException
    public static native int av_find_default_stream_index(AVFormatContext aVFormatContext);

    @NoException
    public static native AVInputFormat av_find_input_format(String str);

    @NoException
    public static native AVInputFormat av_find_input_format(@Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native AVProgram av_find_program_from_stream(AVFormatContext aVFormatContext, AVProgram aVProgram, int i);

    @NoException
    @Cast({"AVDurationEstimationMethod"})
    public static native int av_fmt_ctx_get_duration_estimation_method(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native AVCodec av_format_get_audio_codec(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native av_format_control_message av_format_get_control_message_cb(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native AVCodec av_format_get_data_codec(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native int av_format_get_metadata_header_padding(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native Pointer av_format_get_opaque(@Const AVFormatContext aVFormatContext);

    @NoException
    @Deprecated
    public static native AVOpenCallback av_format_get_open_cb(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native int av_format_get_probe_score(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native AVCodec av_format_get_subtitle_codec(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native AVCodec av_format_get_video_codec(@Const AVFormatContext aVFormatContext);

    @NoException
    public static native void av_format_inject_global_side_data(AVFormatContext aVFormatContext);

    @NoException
    public static native void av_format_set_audio_codec(AVFormatContext aVFormatContext, AVCodec aVCodec);

    @NoException
    public static native void av_format_set_control_message_cb(AVFormatContext aVFormatContext, av_format_control_message org_bytedeco_javacpp_avformat_av_format_control_message);

    @NoException
    public static native void av_format_set_data_codec(AVFormatContext aVFormatContext, AVCodec aVCodec);

    @NoException
    public static native void av_format_set_metadata_header_padding(AVFormatContext aVFormatContext, int i);

    @NoException
    public static native void av_format_set_opaque(AVFormatContext aVFormatContext, Pointer pointer);

    @NoException
    @Deprecated
    public static native void av_format_set_open_cb(AVFormatContext aVFormatContext, AVOpenCallback aVOpenCallback);

    @NoException
    public static native void av_format_set_subtitle_codec(AVFormatContext aVFormatContext, AVCodec aVCodec);

    @NoException
    public static native void av_format_set_video_codec(AVFormatContext aVFormatContext, AVCodec aVCodec);

    @NoException
    public static native int av_get_frame_filename(@Cast({"char*"}) ByteBuffer byteBuffer, int i, String str, int i2);

    @NoException
    public static native int av_get_frame_filename(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int av_get_frame_filename(@Cast({"char*"}) BytePointer bytePointer, int i, String str, int i2);

    @NoException
    public static native int av_get_frame_filename(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"const char*"}) BytePointer bytePointer2, int i2);

    @NoException
    public static native int av_get_frame_filename(@Cast({"char*"}) byte[] bArr, int i, String str, int i2);

    @NoException
    public static native int av_get_frame_filename(@Cast({"char*"}) byte[] bArr, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int av_get_frame_filename2(@Cast({"char*"}) ByteBuffer byteBuffer, int i, String str, int i2, int i3);

    @NoException
    public static native int av_get_frame_filename2(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2, int i3);

    @NoException
    public static native int av_get_frame_filename2(@Cast({"char*"}) BytePointer bytePointer, int i, String str, int i2, int i3);

    @NoException
    public static native int av_get_frame_filename2(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"const char*"}) BytePointer bytePointer2, int i2, int i3);

    @NoException
    public static native int av_get_frame_filename2(@Cast({"char*"}) byte[] bArr, int i, String str, int i2, int i3);

    @NoException
    public static native int av_get_frame_filename2(@Cast({"char*"}) byte[] bArr, int i, @Cast({"const char*"}) BytePointer bytePointer, int i2, int i3);

    @NoException
    public static native int av_get_output_timestamp(AVFormatContext aVFormatContext, int i, @Cast({"int64_t*"}) LongBuffer longBuffer, @Cast({"int64_t*"}) LongBuffer longBuffer2);

    @NoException
    public static native int av_get_output_timestamp(AVFormatContext aVFormatContext, int i, @Cast({"int64_t*"}) LongPointer longPointer, @Cast({"int64_t*"}) LongPointer longPointer2);

    @NoException
    public static native int av_get_output_timestamp(AVFormatContext aVFormatContext, int i, @Cast({"int64_t*"}) long[] jArr, @Cast({"int64_t*"}) long[] jArr2);

    @NoException
    public static native int av_get_packet(AVIOContext aVIOContext, AVPacket aVPacket, int i);

    @NoException
    @Cast({"AVCodecID"})
    public static native int av_guess_codec(AVOutputFormat aVOutputFormat, String str, String str2, String str3, @Cast({"AVMediaType"}) int i);

    @NoException
    @Cast({"AVCodecID"})
    public static native int av_guess_codec(AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3, @Cast({"AVMediaType"}) int i);

    @NoException
    public static native AVOutputFormat av_guess_format(String str, String str2, String str3);

    @NoException
    public static native AVOutputFormat av_guess_format(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"const char*"}) BytePointer bytePointer3);

    @NoException
    @ByVal
    public static native AVRational av_guess_frame_rate(AVFormatContext aVFormatContext, AVStream aVStream, AVFrame aVFrame);

    @NoException
    @ByVal
    public static native AVRational av_guess_sample_aspect_ratio(AVFormatContext aVFormatContext, AVStream aVStream, AVFrame aVFrame);

    @NoException
    public static native void av_hex_dump(@Cast({"FILE*"}) Pointer pointer, @Cast({"const uint8_t*"}) ByteBuffer byteBuffer, int i);

    @NoException
    public static native void av_hex_dump(@Cast({"FILE*"}) Pointer pointer, @Cast({"const uint8_t*"}) BytePointer bytePointer, int i);

    @NoException
    public static native void av_hex_dump(@Cast({"FILE*"}) Pointer pointer, @Cast({"const uint8_t*"}) byte[] bArr, int i);

    @NoException
    public static native void av_hex_dump_log(Pointer pointer, int i, @Cast({"const uint8_t*"}) ByteBuffer byteBuffer, int i2);

    @NoException
    public static native void av_hex_dump_log(Pointer pointer, int i, @Cast({"const uint8_t*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native void av_hex_dump_log(Pointer pointer, int i, @Cast({"const uint8_t*"}) byte[] bArr, int i2);

    @NoException
    public static native AVInputFormat av_iformat_next(@Const AVInputFormat aVInputFormat);

    @NoException
    public static native int av_index_search_timestamp(AVStream aVStream, @Cast({"int64_t"}) long j, int i);

    @NoException
    public static native int av_interleaved_write_frame(AVFormatContext aVFormatContext, AVPacket aVPacket);

    @NoException
    public static native int av_interleaved_write_uncoded_frame(AVFormatContext aVFormatContext, int i, AVFrame aVFrame);

    @NoException
    public static native int av_match_ext(String str, String str2);

    @NoException
    public static native int av_match_ext(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @NoException
    public static native AVProgram av_new_program(AVFormatContext aVFormatContext, int i);

    @NoException
    public static native AVOutputFormat av_oformat_next(@Const AVOutputFormat aVOutputFormat);

    @NoException
    public static native void av_pkt_dump2(@Cast({"FILE*"}) Pointer pointer, @Const AVPacket aVPacket, int i, @Const AVStream aVStream);

    @NoException
    public static native void av_pkt_dump_log2(Pointer pointer, int i, @Const AVPacket aVPacket, int i2, @Const AVStream aVStream);

    @NoException
    public static native int av_probe_input_buffer(AVIOContext aVIOContext, @Cast({"AVInputFormat**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native int av_probe_input_buffer(AVIOContext aVIOContext, @ByPtrPtr AVInputFormat aVInputFormat, String str, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native int av_probe_input_buffer(AVIOContext aVIOContext, @ByPtrPtr AVInputFormat aVInputFormat, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native int av_probe_input_buffer2(AVIOContext aVIOContext, @Cast({"AVInputFormat**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native int av_probe_input_buffer2(AVIOContext aVIOContext, @ByPtrPtr AVInputFormat aVInputFormat, String str, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native int av_probe_input_buffer2(AVIOContext aVIOContext, @ByPtrPtr AVInputFormat aVInputFormat, @Cast({"const char*"}) BytePointer bytePointer, Pointer pointer, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native AVInputFormat av_probe_input_format(AVProbeData aVProbeData, int i);

    @NoException
    public static native AVInputFormat av_probe_input_format2(AVProbeData aVProbeData, int i, IntBuffer intBuffer);

    @NoException
    public static native AVInputFormat av_probe_input_format2(AVProbeData aVProbeData, int i, IntPointer intPointer);

    @NoException
    public static native AVInputFormat av_probe_input_format2(AVProbeData aVProbeData, int i, int[] iArr);

    @NoException
    public static native AVInputFormat av_probe_input_format3(AVProbeData aVProbeData, int i, IntBuffer intBuffer);

    @NoException
    public static native AVInputFormat av_probe_input_format3(AVProbeData aVProbeData, int i, IntPointer intPointer);

    @NoException
    public static native AVInputFormat av_probe_input_format3(AVProbeData aVProbeData, int i, int[] iArr);

    @NoException
    public static native void av_program_add_stream_index(AVFormatContext aVFormatContext, int i, @Cast({"unsigned int"}) int i2);

    @NoException
    public static native int av_read_frame(AVFormatContext aVFormatContext, AVPacket aVPacket);

    @NoException
    public static native int av_read_pause(AVFormatContext aVFormatContext);

    @NoException
    public static native int av_read_play(AVFormatContext aVFormatContext);

    @NoException
    public static native void av_register_all();

    @NoException
    public static native void av_register_input_format(AVInputFormat aVInputFormat);

    @NoException
    public static native void av_register_output_format(AVOutputFormat aVOutputFormat);

    @NoException
    public static native int av_sdp_create(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int av_sdp_create(@ByPtrPtr AVFormatContext aVFormatContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    @NoException
    public static native int av_sdp_create(@ByPtrPtr AVFormatContext aVFormatContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int av_sdp_create(@ByPtrPtr AVFormatContext aVFormatContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    @NoException
    public static native int av_seek_frame(AVFormatContext aVFormatContext, int i, @Cast({"int64_t"}) long j, int i2);

    @NoException
    @ByVal
    public static native AVRational av_stream_get_codec_timebase(@Const AVStream aVStream);

    @NoException
    @Cast({"int64_t"})
    public static native long av_stream_get_end_pts(@Const AVStream aVStream);

    @NoException
    public static native AVCodecParserContext av_stream_get_parser(@Const AVStream aVStream);

    @NoException
    @ByVal
    public static native AVRational av_stream_get_r_frame_rate(@Const AVStream aVStream);

    @NoException
    @Cast({"char*"})
    public static native BytePointer av_stream_get_recommended_encoder_configuration(@Const AVStream aVStream);

    @NoException
    @Cast({"uint8_t*"})
    public static native ByteBuffer av_stream_get_side_data(AVStream aVStream, @Cast({"AVPacketSideDataType"}) int i, IntBuffer intBuffer);

    @NoException
    @Cast({"uint8_t*"})
    public static native BytePointer av_stream_get_side_data(AVStream aVStream, @Cast({"AVPacketSideDataType"}) int i, IntPointer intPointer);

    @NoException
    @Cast({"uint8_t*"})
    public static native byte[] av_stream_get_side_data(AVStream aVStream, @Cast({"AVPacketSideDataType"}) int i, int[] iArr);

    @NoException
    @Cast({"uint8_t*"})
    public static native BytePointer av_stream_new_side_data(AVStream aVStream, @Cast({"AVPacketSideDataType"}) int i, int i2);

    @NoException
    public static native void av_stream_set_r_frame_rate(AVStream aVStream, @ByVal AVRational aVRational);

    @NoException
    public static native void av_stream_set_recommended_encoder_configuration(AVStream aVStream, @Cast({"char*"}) ByteBuffer byteBuffer);

    @NoException
    public static native void av_stream_set_recommended_encoder_configuration(AVStream aVStream, @Cast({"char*"}) BytePointer bytePointer);

    @NoException
    public static native void av_stream_set_recommended_encoder_configuration(AVStream aVStream, @Cast({"char*"}) byte[] bArr);

    @NoException
    public static native void av_url_split(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"char*"}) ByteBuffer byteBuffer2, int i2, @Cast({"char*"}) ByteBuffer byteBuffer3, int i3, IntBuffer intBuffer, @Cast({"char*"}) ByteBuffer byteBuffer4, int i4, String str);

    @NoException
    public static native void av_url_split(@Cast({"char*"}) ByteBuffer byteBuffer, int i, @Cast({"char*"}) ByteBuffer byteBuffer2, int i2, @Cast({"char*"}) ByteBuffer byteBuffer3, int i3, IntBuffer intBuffer, @Cast({"char*"}) ByteBuffer byteBuffer4, int i4, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native void av_url_split(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"char*"}) BytePointer bytePointer2, int i2, @Cast({"char*"}) BytePointer bytePointer3, int i3, IntPointer intPointer, @Cast({"char*"}) BytePointer bytePointer4, int i4, String str);

    @NoException
    public static native void av_url_split(@Cast({"char*"}) BytePointer bytePointer, int i, @Cast({"char*"}) BytePointer bytePointer2, int i2, @Cast({"char*"}) BytePointer bytePointer3, int i3, IntPointer intPointer, @Cast({"char*"}) BytePointer bytePointer4, int i4, @Cast({"const char*"}) BytePointer bytePointer5);

    @NoException
    public static native void av_url_split(@Cast({"char*"}) byte[] bArr, int i, @Cast({"char*"}) byte[] bArr2, int i2, @Cast({"char*"}) byte[] bArr3, int i3, int[] iArr, @Cast({"char*"}) byte[] bArr4, int i4, String str);

    @NoException
    public static native void av_url_split(@Cast({"char*"}) byte[] bArr, int i, @Cast({"char*"}) byte[] bArr2, int i2, @Cast({"char*"}) byte[] bArr3, int i3, int[] iArr, @Cast({"char*"}) byte[] bArr4, int i4, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int av_write_frame(AVFormatContext aVFormatContext, AVPacket aVPacket);

    @NoException
    public static native int av_write_trailer(AVFormatContext aVFormatContext);

    @NoException
    public static native int av_write_uncoded_frame(AVFormatContext aVFormatContext, int i, AVFrame aVFrame);

    @NoException
    public static native int av_write_uncoded_frame_query(AVFormatContext aVFormatContext, int i);

    @NoException
    public static native AVFormatContext avformat_alloc_context();

    @NoException
    public static native int avformat_alloc_output_context2(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer, AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @NoException
    public static native int avformat_alloc_output_context2(@ByPtrPtr AVFormatContext aVFormatContext, AVOutputFormat aVOutputFormat, String str, String str2);

    @NoException
    public static native int avformat_alloc_output_context2(@ByPtrPtr AVFormatContext aVFormatContext, AVOutputFormat aVOutputFormat, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @NoException
    public static native void avformat_close_input(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer);

    @NoException
    public static native void avformat_close_input(@ByPtrPtr AVFormatContext aVFormatContext);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avformat_configuration();

    @NoException
    public static native int avformat_find_stream_info(AVFormatContext aVFormatContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avformat_find_stream_info(AVFormatContext aVFormatContext, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avformat_flush(AVFormatContext aVFormatContext);

    @NoException
    public static native void avformat_free_context(AVFormatContext aVFormatContext);

    @NoException
    @Const
    public static native AVClass avformat_get_class();

    @NoException
    @Const
    public static native AVCodecTag avformat_get_mov_audio_tags();

    @NoException
    @Const
    public static native AVCodecTag avformat_get_mov_video_tags();

    @NoException
    @Const
    public static native AVCodecTag avformat_get_riff_audio_tags();

    @NoException
    @Const
    public static native AVCodecTag avformat_get_riff_video_tags();

    @NoException
    public static native int avformat_init_output(AVFormatContext aVFormatContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avformat_init_output(AVFormatContext aVFormatContext, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avformat_license();

    @NoException
    public static native int avformat_match_stream_specifier(AVFormatContext aVFormatContext, AVStream aVStream, String str);

    @NoException
    public static native int avformat_match_stream_specifier(AVFormatContext aVFormatContext, AVStream aVStream, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avformat_network_deinit();

    @NoException
    public static native int avformat_network_init();

    @NoException
    public static native AVStream avformat_new_stream(AVFormatContext aVFormatContext, @Const AVCodec aVCodec);

    @NoException
    public static native int avformat_open_input(@Cast({"AVFormatContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, AVInputFormat aVInputFormat, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

    @NoException
    public static native int avformat_open_input(@ByPtrPtr AVFormatContext aVFormatContext, String str, AVInputFormat aVInputFormat, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avformat_open_input(@ByPtrPtr AVFormatContext aVFormatContext, @Cast({"const char*"}) BytePointer bytePointer, AVInputFormat aVInputFormat, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avformat_query_codec(@Const AVOutputFormat aVOutputFormat, @Cast({"AVCodecID"}) int i, int i2);

    @NoException
    public static native int avformat_queue_attached_pictures(AVFormatContext aVFormatContext);

    @NoException
    public static native int avformat_seek_file(AVFormatContext aVFormatContext, int i, @Cast({"int64_t"}) long j, @Cast({"int64_t"}) long j2, @Cast({"int64_t"}) long j3, int i2);

    @NoException
    public static native int avformat_transfer_internal_stream_timing_info(@Const AVOutputFormat aVOutputFormat, AVStream aVStream, @Const AVStream aVStream2, @Cast({"AVTimebaseSource"}) int i);

    @NoException
    @Cast({"unsigned"})
    public static native int avformat_version();

    @NoException
    public static native int avformat_write_header(AVFormatContext aVFormatContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avformat_write_header(AVFormatContext aVFormatContext, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avio_accept(AVIOContext aVIOContext, @Cast({"AVIOContext**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avio_accept(AVIOContext aVIOContext, @ByPtrPtr AVIOContext aVIOContext2);

    @NoException
    public static native AVIOContext avio_alloc_context(@Cast({"unsigned char*"}) ByteBuffer byteBuffer, int i, int i2, Pointer pointer, Read_packet_Pointer_ByteBuffer_int read_packet_Pointer_ByteBuffer_int, Write_packet_Pointer_ByteBuffer_int write_packet_Pointer_ByteBuffer_int, Seek_Pointer_long_int seek_Pointer_long_int);

    @NoException
    public static native AVIOContext avio_alloc_context(@Cast({"unsigned char*"}) BytePointer bytePointer, int i, int i2, Pointer pointer, Read_packet_Pointer_BytePointer_int read_packet_Pointer_BytePointer_int, Write_packet_Pointer_BytePointer_int write_packet_Pointer_BytePointer_int, Seek_Pointer_long_int seek_Pointer_long_int);

    @NoException
    public static native AVIOContext avio_alloc_context(@Cast({"unsigned char*"}) byte[] bArr, int i, int i2, Pointer pointer, Read_packet_Pointer_byte___int read_packet_Pointer_byte___int, Write_packet_Pointer_byte___int write_packet_Pointer_byte___int, Seek_Pointer_long_int seek_Pointer_long_int);

    @NoException
    public static native int avio_check(String str, int i);

    @NoException
    public static native int avio_check(@Cast({"const char*"}) BytePointer bytePointer, int i);

    @NoException
    public static native int avio_close(AVIOContext aVIOContext);

    @NoException
    public static native int avio_close_dir(@Cast({"AVIODirContext**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avio_close_dir(@ByPtrPtr AVIODirContext aVIODirContext);

    @NoException
    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @ByPtrPtr @Cast({"uint8_t**"}) ByteBuffer byteBuffer);

    @NoException
    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @ByPtrPtr @Cast({"uint8_t**"}) BytePointer bytePointer);

    @NoException
    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @Cast({"uint8_t**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avio_close_dyn_buf(AVIOContext aVIOContext, @ByPtrPtr @Cast({"uint8_t**"}) byte[] bArr);

    @NoException
    public static native int avio_closep(@Cast({"AVIOContext**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avio_closep(@ByPtrPtr AVIOContext aVIOContext);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avio_enum_protocols(@ByPtrPtr @Cast({"void**"}) Pointer pointer, int i);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avio_enum_protocols(@Cast({"void**"}) PointerPointer pointerPointer, int i);

    @NoException
    public static native int avio_feof(AVIOContext aVIOContext);

    @NoException
    public static native String avio_find_protocol_name(String str);

    @NoException
    @Cast({"const char*"})
    public static native BytePointer avio_find_protocol_name(@Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native void avio_flush(AVIOContext aVIOContext);

    @NoException
    public static native void avio_free_directory_entry(@Cast({"AVIODirEntry**"}) PointerPointer pointerPointer);

    @NoException
    public static native void avio_free_directory_entry(@ByPtrPtr AVIODirEntry aVIODirEntry);

    @NoException
    public static native int avio_get_str(AVIOContext aVIOContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    @NoException
    public static native int avio_get_str(AVIOContext aVIOContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int avio_get_str(AVIOContext aVIOContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    @NoException
    public static native int avio_get_str16be(AVIOContext aVIOContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    @NoException
    public static native int avio_get_str16be(AVIOContext aVIOContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int avio_get_str16be(AVIOContext aVIOContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    @NoException
    public static native int avio_get_str16le(AVIOContext aVIOContext, int i, @Cast({"char*"}) ByteBuffer byteBuffer, int i2);

    @NoException
    public static native int avio_get_str16le(AVIOContext aVIOContext, int i, @Cast({"char*"}) BytePointer bytePointer, int i2);

    @NoException
    public static native int avio_get_str16le(AVIOContext aVIOContext, int i, @Cast({"char*"}) byte[] bArr, int i2);

    @NoException
    public static native int avio_handshake(AVIOContext aVIOContext);

    @NoException
    public static native int avio_open(@Cast({"AVIOContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, int i);

    @NoException
    public static native int avio_open(@ByPtrPtr AVIOContext aVIOContext, String str, int i);

    @NoException
    public static native int avio_open(@ByPtrPtr AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer, int i);

    @NoException
    public static native int avio_open2(@Cast({"AVIOContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, int i, @Const AVIOInterruptCB aVIOInterruptCB, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

    @NoException
    public static native int avio_open2(@ByPtrPtr AVIOContext aVIOContext, String str, int i, @Const AVIOInterruptCB aVIOInterruptCB, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avio_open2(@ByPtrPtr AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer, int i, @Const AVIOInterruptCB aVIOInterruptCB, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avio_open_dir(@Cast({"AVIODirContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

    @NoException
    public static native int avio_open_dir(@ByPtrPtr AVIODirContext aVIODirContext, String str, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avio_open_dir(@ByPtrPtr AVIODirContext aVIODirContext, @Cast({"const char*"}) BytePointer bytePointer, @ByPtrPtr AVDictionary aVDictionary);

    @NoException
    public static native int avio_open_dyn_buf(@Cast({"AVIOContext**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avio_open_dyn_buf(@ByPtrPtr AVIOContext aVIOContext);

    @NoException
    public static native int avio_pause(AVIOContext aVIOContext, int i);

    @NoException
    public static native int avio_printf(AVIOContext aVIOContext, String str);

    @NoException
    public static native int avio_printf(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avio_put_str(AVIOContext aVIOContext, String str);

    @NoException
    public static native int avio_put_str(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avio_put_str16be(AVIOContext aVIOContext, String str);

    @NoException
    public static native int avio_put_str16be(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avio_put_str16le(AVIOContext aVIOContext, String str);

    @NoException
    public static native int avio_put_str16le(AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avio_r8(AVIOContext aVIOContext);

    @NoException
    @Cast({"unsigned int"})
    public static native int avio_rb16(AVIOContext aVIOContext);

    @NoException
    @Cast({"unsigned int"})
    public static native int avio_rb24(AVIOContext aVIOContext);

    @NoException
    @Cast({"unsigned int"})
    public static native int avio_rb32(AVIOContext aVIOContext);

    @NoException
    @Cast({"uint64_t"})
    public static native long avio_rb64(AVIOContext aVIOContext);

    @NoException
    public static native int avio_read(AVIOContext aVIOContext, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, int i);

    @NoException
    public static native int avio_read(AVIOContext aVIOContext, @Cast({"unsigned char*"}) BytePointer bytePointer, int i);

    @NoException
    public static native int avio_read(AVIOContext aVIOContext, @Cast({"unsigned char*"}) byte[] bArr, int i);

    @NoException
    public static native int avio_read_dir(AVIODirContext aVIODirContext, @Cast({"AVIODirEntry**"}) PointerPointer pointerPointer);

    @NoException
    public static native int avio_read_dir(AVIODirContext aVIODirContext, @ByPtrPtr AVIODirEntry aVIODirEntry);

    @NoException
    public static native int avio_read_to_bprint(AVIOContext aVIOContext, AVBPrint aVBPrint, @Cast({"size_t"}) long j);

    @NoException
    @Cast({"unsigned int"})
    public static native int avio_rl16(AVIOContext aVIOContext);

    @NoException
    @Cast({"unsigned int"})
    public static native int avio_rl24(AVIOContext aVIOContext);

    @NoException
    @Cast({"unsigned int"})
    public static native int avio_rl32(AVIOContext aVIOContext);

    @NoException
    @Cast({"uint64_t"})
    public static native long avio_rl64(AVIOContext aVIOContext);

    @NoException
    @Cast({"int64_t"})
    public static native long avio_seek(AVIOContext aVIOContext, @Cast({"int64_t"}) long j, int i);

    @NoException
    @Cast({"int64_t"})
    public static native long avio_seek_time(AVIOContext aVIOContext, int i, @Cast({"int64_t"}) long j, int i2);

    @NoException
    @Cast({"int64_t"})
    public static native long avio_size(AVIOContext aVIOContext);

    @NoException
    @Cast({"int64_t"})
    public static native long avio_skip(AVIOContext aVIOContext, @Cast({"int64_t"}) long j);

    @NoException
    @Cast({"int64_t"})
    public static native long avio_tell(AVIOContext aVIOContext);

    @NoException
    public static native void avio_w8(AVIOContext aVIOContext, int i);

    @NoException
    public static native void avio_wb16(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    @NoException
    public static native void avio_wb24(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    @NoException
    public static native void avio_wb32(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    @NoException
    public static native void avio_wb64(AVIOContext aVIOContext, @Cast({"uint64_t"}) long j);

    @NoException
    public static native void avio_wl16(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    @NoException
    public static native void avio_wl24(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    @NoException
    public static native void avio_wl32(AVIOContext aVIOContext, @Cast({"unsigned int"}) int i);

    @NoException
    public static native void avio_wl64(AVIOContext aVIOContext, @Cast({"uint64_t"}) long j);

    @NoException
    public static native void avio_write(AVIOContext aVIOContext, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer, int i);

    @NoException
    public static native void avio_write(AVIOContext aVIOContext, @Cast({"const unsigned char*"}) BytePointer bytePointer, int i);

    @NoException
    public static native void avio_write(AVIOContext aVIOContext, @Cast({"const unsigned char*"}) byte[] bArr, int i);

    @NoException
    public static native void avio_write_marker(AVIOContext aVIOContext, @Cast({"int64_t"}) long j, @Cast({"AVIODataMarkerType"}) int i);

    @NoException
    public static native int avpriv_io_delete(String str);

    @NoException
    public static native int avpriv_io_delete(@Cast({"const char*"}) BytePointer bytePointer);

    @NoException
    public static native int avpriv_io_move(String str, String str2);

    @NoException
    public static native int avpriv_io_move(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @NoException
    @Deprecated
    public static native int url_feof(AVIOContext aVIOContext);

    static {
        Loader.load();
    }
}
