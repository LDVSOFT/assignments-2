package ru.spbau.mit.gui;

import ru.spbau.mit.options.BenchmarkOption;
import ru.spbau.mit.options.BenchmarkOptionFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ldvsoft on 05.05.16.
 */
class RunBenchmarkFrame extends JInternalFrame {
    private enum ChangingOption {
        CLIENTS_COUNT,
        REQUEST_SIZE,
        REQUEST_DELAY;

        private String getName() {
            switch (this) {
                case CLIENTS_COUNT:
                    return "Clients count";
                case REQUEST_SIZE:
                    return "Request size";
                case REQUEST_DELAY:
                    return "Request delay (ms)";
            }
            return null;
        }
    }

    private ChangingOption option = null;
    private JPanel optionsPanel;
    private JTextField requestsCountField;
    private JTextField clientsCountField;
    private JTextField requestSizeField;
    private JTextField requestsDelayField;

    private JPanel rangePanel;
    private JTextField startField;
    private JTextField stepField;
    private JTextField endField;

    private boolean isRemoteServer = false;
    private JTextField serverField;

    private class SelectParameterAction extends AbstractAction {
        private ChangingOption option;

        private SelectParameterAction(ChangingOption option) {
            this.option = option;
            putValue(NAME, option.getName());
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            setChangingOption(option);
        }
    }

    private class SelectServerAction extends AbstractAction {
        private boolean isRemote;

        private SelectServerAction(boolean isRemote) {
            this.isRemote = isRemote;
            this.putValue(NAME, isRemote ? "remote" : "local");
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            isRemoteServer = isRemote;
            serverField.setEnabled(isRemote);
        }
    }

    RunBenchmarkFrame() {
        super("Run benchmark", true, true, true, true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBorder(createBorder("Option"));

            JComboBox<BenchmarkOption> comboBox = new JComboBox<>();
            comboBox.setModel(new DefaultComboBoxModel<>(
                    BenchmarkOptionFactory.OPTIONS.toArray(new BenchmarkOption[]{})
            ));
            DefaultListCellRenderer labelRenderer = new DefaultListCellRenderer();
            comboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = (JLabel) labelRenderer.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );
                label.setText(value.getName());
                return label;
            });
            panel.add(comboBox, getConstraintsFor(0, 0, true));

            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
            add(panel, getConstraintsFor(0, 0, true));
        }
        {
            rangePanel = new JPanel();
            rangePanel.setLayout(new GridBagLayout());

            startField = new JTextField("0");
            stepField = new JTextField("1");
            endField = new JTextField("10");
            rangePanel.add(new JLabel("from"), getConstraintsFor(0, 0, false));
            rangePanel.add(startField, getConstraintsFor(0, 1, true));
            rangePanel.add(new JLabel("to"), getConstraintsFor(0, 2, false));
            rangePanel.add(endField, getConstraintsFor(0, 3, true));
            rangePanel.add(new JLabel("step"), getConstraintsFor(0, 4, false));
            rangePanel.add(stepField, getConstraintsFor(0, 5, true));
        }
        {
            optionsPanel = new JPanel();
            optionsPanel.setBorder(createBorder("Parameters"));
            GridBagLayout layout = new GridBagLayout();
            optionsPanel.setLayout(layout);
            ButtonGroup group = new ButtonGroup();
            {
                {
                    JLabel label = new JLabel("Request count");
                    optionsPanel.add(label, getConstraintsFor(0, 0, false));
                }
                {
                    requestsCountField = new JTextField("0");
                    optionsPanel.add(requestsCountField, getConstraintsFor(0, 1, true));
                }
            }
            for (ChangingOption option : ChangingOption.values()) {
                {
                    JRadioButton button = new JRadioButton(new SelectParameterAction(option));
                    group.add(button);
                    optionsPanel.add(button, getConstraintsFor(1 + option.ordinal(), 0, false));
                }
            }
            clientsCountField = new JTextField("0");
            requestSizeField = new JTextField("0");
            requestsDelayField = new JTextField("100");

            optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, optionsPanel.getPreferredSize().height));
            add(optionsPanel, getConstraintsFor(1, 0, true));
        }
        {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("Server location"));
            panel.setLayout(new GridBagLayout());
            ButtonGroup group = new ButtonGroup();

            {
                JRadioButton button = new JRadioButton(new SelectServerAction(false));
                button.setEnabled(true);
                group.add(button);
                GridBagConstraints constraints = getConstraintsFor(0, 0, false);
                constraints.gridwidth = 2;
                panel.add(button, constraints);
            }
            {
                JRadioButton button = new JRadioButton(new SelectServerAction(true));
                group.add(button);
                panel.add(button, getConstraintsFor(1, 0, false));

                serverField = new JTextField("localhost");
                serverField.setEnabled(false);
                panel.add(serverField, getConstraintsFor(1, 1, true));
            }

            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
            add(panel, getConstraintsFor(2, 0, true));
        }
        setMinimumSize(getPreferredSize());
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        pack();
    }

    private void setChangingOption(ChangingOption option) {
        optionsPanel.remove(clientsCountField);
        optionsPanel.remove(requestSizeField);
        optionsPanel.remove(requestsDelayField);
        optionsPanel.remove(rangePanel);

        optionsPanel.add(clientsCountField, getConstraintsFor(1, 1, true));
        optionsPanel.add(requestSizeField, getConstraintsFor(2, 1, true));
        optionsPanel.add(requestsDelayField, getConstraintsFor(3, 1, true));

        GridBagConstraints constraints;
        switch (option) {
            case CLIENTS_COUNT:
                optionsPanel.remove(clientsCountField);
                constraints = getConstraintsFor(1, 1, true);
                constraints.insets = new Insets(0, 0, 0, 0);
                optionsPanel.add(rangePanel, constraints);
                break;
            case REQUEST_SIZE:
                optionsPanel.remove(requestSizeField);
                constraints = getConstraintsFor(2, 1, true);
                constraints.insets = new Insets(0, 0, 0, 0);
                optionsPanel.add(rangePanel, constraints);
                break;
            case REQUEST_DELAY:
                optionsPanel.remove(requestsDelayField);
                constraints = getConstraintsFor(3, 1, true);
                constraints.insets = new Insets(0, 0, 0, 0);
                optionsPanel.add(rangePanel, constraints);
                break;
        }
        optionsPanel.invalidate();
        optionsPanel.validate();
        this.option = option;
    }

    private GridBagConstraints getConstraintsFor(int y, int x, boolean hFill) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(3, 3, 3, 3);
        if (hFill) {
            constraints.weightx = 0.5;
            constraints.fill = GridBagConstraints.HORIZONTAL;
        }
        return constraints;
    }

    private static Border createBorder(String caption) {
        return BorderFactory.createTitledBorder(caption);
    }
}
