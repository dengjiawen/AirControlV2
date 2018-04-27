package main.java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import net.sourceforge.javaflacencoder.FLACFileWriter;

class SpeechTest implements GSpeechResponseListener {

    public static void main(String... args) {
        final Microphone mic = new Microphone(FLACFileWriter.FLAC);

        GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyAPdCA3W_b4McxwqZiNQKDvYk3Hh8zxYfI");

        duplex.setLanguage("en");

        JFrame frame = new JFrame("Jarvis Speech API DEMO");
        frame.setDefaultCloseOperation(3);
        JTextArea response = new JTextArea();
        response.setEditable(false);
        response.setWrapStyleWord(true);
        response.setLineWrap(true);

        final JButton record = new JButton("Record");
        final JButton stop = new JButton("Stop");
        stop.setEnabled(false);

        record.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new Thread(() -> {
                    try {
                        duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }).start();
                record.setEnabled(false);
                stop.setEnabled(true);
            }
        });
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mic.close();
                duplex.stopSpeechRecognition();
                record.setEnabled(true);
                stop.setEnabled(false);
            }
        });
        JLabel infoText = new JLabel(
                "Text Translation",

                SwingConstants.CENTER);
        frame.getContentPane().add(infoText);
        infoText.setAlignmentX(0.5F);
        JScrollPane scroll = new JScrollPane(response);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), 1));
        frame.getContentPane().add(scroll);
        JPanel recordBar = new JPanel();
        frame.getContentPane().add(recordBar);
        recordBar.setLayout(new BoxLayout(recordBar, BoxLayout.X_AXIS));
        recordBar.add(record);
        recordBar.add(stop);
        frame.setVisible(true);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        duplex.addResponseListener(new GSpeechResponseListener() {
            String old_text = "";

            public void onResponse(GoogleResponse gr) {
                String output;
                output = gr.getResponse();
                if (gr.getResponse() == null) {
                    this.old_text = response.getText();
                    if (this.old_text.contains("(")) {
                        this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
                    }
                    System.out.println("Paragraph Line Added");
                    this.old_text = (response.getText() + "\n");
                    this.old_text = this.old_text.replace(")", "").replace("( ", "");
                    response.setText(this.old_text);
                    return;
                }
                if (output.contains("(")) {
                    output = output.substring(0, output.indexOf('('));
                }
                if (!gr.getOtherPossibleResponses().isEmpty()) {
                    output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
                }
                System.out.println(output);
                response.setText("");
                response.append(this.old_text);
                response.append(output);
            }
        });
    }

    @Override
    public void onResponse(GoogleResponse paramGoogleResponse) {
        // Auto-generated method stub

    }
}