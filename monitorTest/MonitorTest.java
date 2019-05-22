/*
 Учебная программа
 Остановка и выполнение потока

 Поток выводит сообщения в консоль
 При нажатии кнопки поток приостанавливается,
 при втором нажатии возобновляет работу
 */
package monitorTest;

import java.awt.event.ActionEvent;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author vvm
 */
public class MonitorTest {

    public static void main(String[] args) {
        final Task task = new Task();
        new Thread(task).start();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frmMainWindow = new JFrame();
                frmMainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frmMainWindow.setLocationRelativeTo(null);

                frmMainWindow.setSize(400, 400);

                final String PAUSE_TEXT = "Остановить";
                final String START_TEXT = "Запустить";
                JButton btnChangeThreadState = new JButton(PAUSE_TEXT);

                btnChangeThreadState.addActionListener((ActionEvent e) -> {
                    task.changeThreadState();
                    
                    JButton button = (JButton) e.getSource();
                    if (task.isRunning()) {
                        button.setText(PAUSE_TEXT);
                    } else {
                        button.setText(START_TEXT);
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(btnChangeThreadState);

                frmMainWindow.add(buttonPanel);
                frmMainWindow.setVisible(true);
            }
        });
    }
}

class Task implements Runnable {

    private boolean isRunning = true;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500);
                System.out.println("Поток выполняется");
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                continue;
            }

            synchronized (this) {
                if (!isRunning) {
                    System.out.println("Поток приостановлен");

                    while (!isRunning) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            //ex.printStackTrace();
                        }
                    }

                    System.out.println("Поток возобновил работу");
                }
            }
        }
    }

    public synchronized void changeThreadState() {
        isRunning = !isRunning;
        notifyAll();
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }
}
