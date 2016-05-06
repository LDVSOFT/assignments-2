package ru.spbau.mit.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.spbau.mit.benchmark.BenchmarkRun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ldvsoft on 05.05.16.
 */
class GraphFrame extends JInternalFrame {
    private static final String SERVER_REQUEST = "Server request";
    private static final String SERVER_CLIENT = "Server client";
    private static final String CLIENT = "Client";
    private ChartPanel chartPanel;
    private JCheckBox showServerRequestTime;
    private JCheckBox showServerClientTime;
    private JCheckBox showClientTime;
    private BenchmarkRun.Result result;

    private Action showServerRequestTimeAction = new AbstractAction() {
        {
            putValue(NAME, "Server request");
            putValue(SHORT_DESCRIPTION, "Show average server request time");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };

    private Action showServerClientTimeAction = new AbstractAction() {
        {
            putValue(NAME, SERVER_CLIENT);
            putValue(SHORT_DESCRIPTION, "Show average server client time");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };

    private Action showClientTimeAction = new AbstractAction() {
        {
            putValue(NAME, CLIENT);
            putValue(SHORT_DESCRIPTION, "Show average client time");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    };

    private static final String SAY_I_DO = "Graph";
    private static final boolean I_DO = true;

    GraphFrame(BenchmarkRun.Result result) {
        super(SAY_I_DO, I_DO, I_DO, I_DO, I_DO); // ABBA-шмабба
        this.result = result;

        setLayout(new BorderLayout());

        chartPanel = new ChartPanel(null);
        add(chartPanel, BorderLayout.CENTER);

        JPanel selectionPanel = new JPanel();
        {
            selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.LINE_AXIS));
            selectionPanel.add(Box.createHorizontalGlue());
            {
                showServerRequestTime = new JCheckBox(showServerRequestTimeAction);
                showServerRequestTime.setSelected(true);
                selectionPanel.add(showServerRequestTime);
            }
            selectionPanel.add(Box.createHorizontalStrut(10));
            {
                showServerClientTime = new JCheckBox(showServerClientTimeAction);
                showServerClientTime.setSelected(true);
                selectionPanel.add(showServerClientTime);
            }
            selectionPanel.add(Box.createHorizontalStrut(10));
            {
                showClientTime = new JCheckBox(showClientTimeAction);
                showClientTime.setSelected(true);
                selectionPanel.add(showClientTime);
            }
            selectionPanel.add(Box.createHorizontalGlue());
        }
        add(selectionPanel, BorderLayout.PAGE_END);
        setMinimumSize(new Dimension(700, 500));
        pack();

        refresh();
    }

    private void refresh() {
        XYSeriesCollection collection = new XYSeriesCollection();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        if (showServerRequestTime.isSelected()) {
            XYSeries series = new XYSeries(SERVER_REQUEST);
            result.getServerRequestTimes().forEach(series::add);
            collection.addSeries(series);
            renderer.setSeriesPaint(collection.getSeriesIndex(SERVER_REQUEST), Color.RED);
        }
        if (showServerClientTime.isSelected()) {
            XYSeries series = new XYSeries(SERVER_CLIENT);
            result.getServerClientTimes().forEach(series::add);
            collection.addSeries(series);
            renderer.setSeriesPaint(collection.getSeriesIndex(SERVER_CLIENT), Color.ORANGE);
        }
        if (showClientTime.isSelected()) {
            XYSeries series = new XYSeries(CLIENT);
            result.getClientTimes().forEach(series::add);
            collection.addSeries(series);
            renderer.setSeriesPaint(collection.getSeriesIndex(CLIENT), Color.BLUE);
        }
        String caption = String.format("%s (%s)", result.getOptionName(), result.getChangingParamName());
        setTitle("Graph: " + caption);
        JFreeChart chart = ChartFactory.createXYLineChart(caption, result.getChangingParamName(), "ms", collection);
        chart.getXYPlot().setRenderer(renderer);
        chartPanel.setChart(chart);
    }
}
