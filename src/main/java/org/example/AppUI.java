package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppUI extends JFrame {
    private JTextField pathField;
    private JCheckBox logCheckBox;
    private JLabel statusLabel; // Новая метка для статуса

    public AppUI() {
        setTitle("PlantUML ASCII Generator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        pathField = new JTextField();

        JButton browseButton = new JButton("Обзор...");
        browseButton.setPreferredSize(new Dimension(200, 25)); // Установка размера кнопки

        logCheckBox = new JCheckBox("Включить логирование");

        JButton processButton = new JButton("Обработать");
        processButton.setHorizontalAlignment(SwingConstants.CENTER); // Центрирование текста на кнопке
        processButton.setPreferredSize(new Dimension(380, 25)); // Установка размера кнопки

        statusLabel = new JLabel(""); // Инициализация метки для статуса
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER); // Центрирование текста на метке

        panel.add(pathField);
        panel.add(browseButton);
        panel.add(logCheckBox);
        panel.add(processButton);
        panel.add(statusLabel); // Добавление метки для статуса

        add(panel);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int result = fileChooser.showOpenDialog(AppUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    Path selectedPath = fileChooser.getSelectedFile().toPath();
                    pathField.setText(selectedPath.toString());
                }
            }
        });

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pathStr = pathField.getText();
                if (pathStr.isEmpty()) {
                    JOptionPane.showMessageDialog(AppUI.this, "Пожалуйста, выберите файл или директорию.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Path inputPath = Paths.get(pathStr);
                boolean outputLogEnabled = logCheckBox.isSelected();

                try (FileProcessor processor = new FileProcessor(inputPath.getParent(), outputLogEnabled)) {
                    processor.process(inputPath);
                    statusLabel.setText("Обработка завершена"); // Установка текста метки после успешной обработки
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AppUI.this, "Critical error: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    statusLabel.setText("Обработка завершена с ошибкой"); // Установка текста метки при ошибке
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppUI appUI = new AppUI();
            appUI.setVisible(true);
        });
    }
}
