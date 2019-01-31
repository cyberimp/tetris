package com.kalinasoft.tetris;

public interface HighScoreAdapter {
    boolean signIn();
    boolean checkReady();
    boolean add(long score);

    boolean show();
}
