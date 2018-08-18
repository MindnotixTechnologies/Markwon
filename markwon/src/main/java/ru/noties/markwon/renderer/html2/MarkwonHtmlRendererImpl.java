package ru.noties.markwon.renderer.html2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

import ru.noties.markwon.SpannableBuilder;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.html.api.HtmlTag;
import ru.noties.markwon.html.api.MarkwonHtmlParser;
import ru.noties.markwon.renderer.html2.tag.TagHandler;

class MarkwonHtmlRendererImpl extends MarkwonHtmlRenderer {

    private final Map<String, TagHandler> tagHandlers;

    MarkwonHtmlRendererImpl(@NonNull Map<String, TagHandler> tagHandlers) {
        this.tagHandlers = tagHandlers;
    }

    @Override
    public void render(
            @NonNull final SpannableConfiguration configuration,
            @NonNull final SpannableBuilder builder,
            @NonNull MarkwonHtmlParser parser) {

        final int length = builder.length();

        parser.flushInlineTags(length, new MarkwonHtmlParser.FlushAction<HtmlTag.Inline>() {
            @Override
            public void apply(@NonNull List<HtmlTag.Inline> tags) {
                TagHandler handler;
                for (HtmlTag.Inline inline : tags) {
                    handler = tagHandler(inline.name());
                    if (handler != null) {
                        handler.handle(configuration, builder, inline);
                    }
                }
            }
        });

        parser.flushBlockTags(length, new MarkwonHtmlParser.FlushAction<HtmlTag.Block>() {
            @Override
            public void apply(@NonNull List<HtmlTag.Block> tags) {
                TagHandler handler;
                for (HtmlTag.Block block : tags) {
                    handler = tagHandler(block.name());
                    if (handler != null) {
                        handler.handle(configuration, builder, block);
                    } else {
                        // see if any of children can be handled
                        apply(block.children());
                    }
                }
            }
        });

        parser.reset();
    }

    @Nullable
    @Override
    public TagHandler tagHandler(@NonNull String tagName) {
        return tagHandlers.get(tagName);
    }
}