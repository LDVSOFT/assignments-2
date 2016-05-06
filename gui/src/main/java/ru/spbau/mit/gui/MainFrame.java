package ru.spbau.mit.gui;

import ru.spbau.mit.benchmark.BenchmarkRun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by ldvsoft on 05.05.16.
 */
public final class MainFrame extends JFrame {
    private JDesktopPane desktopPane;
    private RunBenchmarkFrame runBenchmarkFrame;

    private Action openAction = new AbstractAction() {
        {
            putValue(NAME, "Open");
            putValue(SHORT_DESCRIPTION, "Open graphs from saved file");
            putValue(MNEMONIC_KEY, KeyEvent.VK_O);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            SwingUtilities.invokeLater(() -> {
                BenchmarkRun.Result result = new BenchmarkRun.Result("trololo", "xxx");
                result.commitPoint(1, 1, 2, 3);
                result.commitPoint(2, 4, 5, 6);
                result.commitPoint(3, 7, 8, 9);
                GraphFrame frame = new GraphFrame(result);
                desktopPane.add(frame);
                frame.setVisible(true);
            });
        }
    };

    private Action runBenchmarkAction = new AbstractAction() {
        {
            putValue(NAME, "Run benchmark");
            putValue(SHORT_DESCRIPTION, "Run benchmark");
            putValue(MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            runBenchmarkFrame.setVisible(true);
        }
    };

    private Action quitAction = new AbstractAction() {
        {
            putValue(NAME, "Quit");
            putValue(SHORT_DESCRIPTION, "Quit application");
            putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            dispose();
        }
    };

    public MainFrame() {
        super("Servers benchmark");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        {
            JMenu menu = new JMenu("File");
            menu.add(openAction);
            menu.add(runBenchmarkAction);
            menu.addSeparator();
            menu.add(quitAction);
            menuBar.add(menu);
        }
        setJMenuBar(menuBar);
        desktopPane = new JDesktopPane();
        add(desktopPane);
        setMinimumSize(new Dimension(300, 300));
        setSize(new Dimension(300, 300));
        //pack();

        runBenchmarkFrame = new RunBenchmarkFrame();
        desktopPane.add(runBenchmarkFrame);
    }
}
