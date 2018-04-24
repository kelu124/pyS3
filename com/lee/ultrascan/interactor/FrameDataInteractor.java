package com.lee.ultrascan.interactor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.lee.ultrascan.library.GrayFrame;
import com.lee.ultrascan.model.FrameSequence;
import com.lee.ultrascan.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import rx.Observable;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class FrameDataInteractor {
    private static final String FRAME_IMAGE_CACHE_DIR = "UltraScan/image";
    private static final String FRAME_VIDEO_CACHE_DIR = "UltraScan/image";
    private static final SimpleDateFormat SAVE_FILE_TIME_FORMAT = new SimpleDateFormat("_yyyyMMdd_HHmmss_", Locale.US);
    private static final String VIDEO_FOMAT = "mp4";
    private static final double VIDEO_FRAME_RATE = 12.0d;
    private static final int VIDEO_HEIGHT = 600;
    private static final int VIDEO_WIDTH = 800;
    public static final String frameImageExtension = ".jpg";
    public static final String frameVideoExtension = ".mp4";
    private Context context;
    private FrameSequence mFrameSequence = new FrameSequence();

    class C10061 implements Observable$OnSubscribe<Integer> {
        C10061() {
        }

        public void call(Subscriber<? super Integer> subscriber) {
            try {
                int size = FrameDataInteractor.this.frameSequenceSize();
                if (size != 0) {
                    GrayFrame firstFrame = FrameDataInteractor.this.mFrameSequence.getFrame(0);
                    File file = FileUtils.openFile(FrameDataInteractor.getFrameVideosFileDir(), ("record" + FrameDataInteractor.this.getDateTimeString()) + FrameDataInteractor.frameVideoExtension);
                    AndroidFrameConverter converter = new AndroidFrameConverter();
                    FFmpegFrameRecorder recorder = FFmpegFrameRecorder.createDefault(file, firstFrame.getWidth(), firstFrame.getHeight());
                    recorder.setFormat(FrameDataInteractor.VIDEO_FOMAT);
                    recorder.setFrameRate(FrameDataInteractor.VIDEO_FRAME_RATE);
                    recorder.setVideoQuality(0.0d);
                    recorder.start();
                    Bitmap outBitmap = Bitmap.createBitmap(firstFrame.getWidth(), firstFrame.getHeight(), Config.ARGB_8888);
                    for (int i = 0; i < size; i++) {
                        subscriber.onNext(Integer.valueOf(i + 1));
                        GrayFrame grayFrame = FrameDataInteractor.this.mFrameSequence.getFrame(i);
                        if (outBitmap.getWidth() != grayFrame.getWidth() || outBitmap.getHeight() != grayFrame.getHeight()) {
                            outBitmap.recycle();
                            outBitmap = Bitmap.createBitmap(grayFrame.getWidth(), grayFrame.getHeight(), Config.ARGB_8888);
                        }
                        grayFrame.getPixels(outBitmap);
                        recorder.record(converter.convert(outBitmap));
                    }
                    outBitmap.recycle();
                    recorder.stop();
                    subscriber.onCompleted();
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }
        }
    }

    public FrameDataInteractor(Context context) {
        this.context = context.getApplicationContext();
    }

    public synchronized void getSize() {
        this.mFrameSequence.size();
    }

    public synchronized void addFrame(GrayFrame grayFrame) {
        this.mFrameSequence.add(grayFrame);
    }

    public synchronized GrayFrame getFrame(int index) {
        return this.mFrameSequence.getFrame(index);
    }

    public synchronized GrayFrame getLast() {
        return this.mFrameSequence.getLast();
    }

    public synchronized int frameSequenceSize() {
        return this.mFrameSequence.size();
    }

    public synchronized void clearFrames() {
        this.mFrameSequence.clear();
    }

    public synchronized File saveFrame(Bitmap bitmap) throws IOException {
        File file;
        file = FileUtils.openFile(getFrameImagesFileDir(), ("frame" + getDateTimeString()) + frameImageExtension);
        bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
        return file;
    }

    public synchronized Observable<Integer> saveFrameAsVideoFile() {
        return Observable.create(new C10061()).subscribeOn(Schedulers.io());
    }

    public static Bitmap loadFrameImageFromFile(String filename) throws IOException {
        Bitmap imageBitmap = BitmapFactory.decodeFile(FileUtils.openFile(getFrameImagesFileDir(), filename).getAbsolutePath());
        if (imageBitmap != null) {
            return imageBitmap;
        }
        throw new IOException("decode:" + filename + " failed!");
    }

    public static File getFrameImagesFileDir() throws IOException {
        return getExternalStorageDir("UltraScan/image");
    }

    public static File getFrameVideosFileDir() throws IOException {
        return getExternalStorageDir("UltraScan/image");
    }

    private static File getExternalStorageDir(@NonNull String subDir) throws IOException {
        File sdCardDir = FileUtils.getExternalStorageDir();
        if (sdCardDir == null) {
            throw new IOException("getExternalStorageDir failed!");
        }
        File targetDir = new File(sdCardDir, subDir);
        if (targetDir.isDirectory() || targetDir.mkdirs()) {
            return targetDir;
        }
        throw new IOException("create dir failed!");
    }

    protected String getDateTimeString() {
        return SAVE_FILE_TIME_FORMAT.format(Calendar.getInstance().getTime());
    }
}
