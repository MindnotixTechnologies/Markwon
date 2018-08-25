package ru.noties.markwon;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

@SuppressWarnings({"unused", "WeakerAccess"})
public class UrlProcessorAndroidAssets implements UrlProcessor {


    static final String MOCK = "https://android.asset/";
    static final String BASE = "file:///android_asset/";

    private final UrlProcessorRelativeToAbsolute assetsProcessor
            = new UrlProcessorRelativeToAbsolute(MOCK);

    private final UrlProcessor processor;

    public UrlProcessorAndroidAssets() {
        this(null);
    }

    public UrlProcessorAndroidAssets(@Nullable UrlProcessor parent) {
        this.processor = parent;
    }

    @NonNull
    @Override
    public String process(@NonNull String destination) {
        final String out;
        final Uri uri = Uri.parse(destination);
        if (TextUtils.isEmpty(uri.getScheme())) {
            out = assetsProcessor.process(destination).replace(MOCK, BASE);
        } else {
            if (processor != null) {
                out = processor.process(destination);
            } else {
                out = destination;
            }
        }
        return out;
    }
}
