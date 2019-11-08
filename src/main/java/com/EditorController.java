package com;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorController extends Tab {

    @FXML
    public CodeArea text = new CodeArea();
    public Path path = Path.of(".");
    public EditorController() {

    }

    // for color
    private static final String[] keywordList = new String[] {
            "if", "else", "for", "while"
    };
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", keywordList) + ")\\b";
    private static final String OPERATOR_PATTERN = "[-+%==/*]";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<OP>" + OPERATOR_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
    );

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass = matcher.group("KEYWORD") != null ? "keyword" :
                                matcher.group("OP") != null ? "operator":
                                matcher.group("STRING") != null ? "string" : null;
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public EditorController getTabModel() {
        int end = text.getLength();
        text.setParagraphGraphicFactory(LineNumberFactory.get(text));//sets up line numbers
        Subscription cleanupWhenNoLongerNeedIt = text
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .subscribe(ignore -> text.setStyleSpans(0, computeHighlighting(text.getText())));
        text.replaceText(0, end, text.getText());
        return this;
    }
}

