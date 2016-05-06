package ru.spbau.mit;

import ru.spbau.mit.gui.MainFrame;

import javax.swing.*;

/**
 * Created by ldvsoft on 05.05.16.
 */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
