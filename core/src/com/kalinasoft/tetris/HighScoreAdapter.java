package com.kalinasoft.tetris;

import java.util.Locale;

public interface HighScoreAdapter {
    boolean signIn();
    boolean checkReady();
    boolean add(long score);

    boolean show();

    void toast();

    Locale getLocale();
}
