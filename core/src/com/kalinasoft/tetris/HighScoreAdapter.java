package com.kalinasoft.tetris;

public interface HighScoreAdapter {
    boolean signIn();

    boolean add(long score);

    boolean show();
}
