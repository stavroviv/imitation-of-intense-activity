package org.home.stavrov.utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundUtils {

    private static final float SAMPLE_RATE = 44100;// Amplitude (volume)
    private static final int AMPLITUDE = 15;
    private static final double[] NOTES = {
            261.63, // C4
            293.66, // D4
            329.63, // E4
            349.23, // F4
            392.00, // G4
            440.00, // A4
            466.16, // A#4/B♭4
            493.88, // B4
            523.25  // C5
    };

    // Duration of each chord in milliseconds
    private static final int CHORD_DURATION = 200; // in milliseconds
    private static final int REST_DURATION = 200;   // Duration between chords (rest)

    private SoundUtils() {

    }

    public static void main(String[] args) {
        playSound();
    }

    public static void playSound() {
        // Play the chord progression
        try {
            playChordProgression();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static void playChordProgression() throws LineUnavailableException {
        // Set up the audio format
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        SourceDataLine line = AudioSystem.getSourceDataLine(format);
        line.open(format);
        line.start();

        // Define the chords (in terms of frequencies)
        double[][] chordProgression = {
                {NOTES[1], NOTES[3], NOTES[5]},  // D minor (D, F, A)
                {NOTES[4], NOTES[6], NOTES[1]},  // G minor (G, B♭, D)
                {NOTES[5], NOTES[2], NOTES[7], NOTES[1]}, // A7 (A, C#, E, G)
                {NOTES[1], NOTES[3], NOTES[5], NOTES[7]}, // D7 (D, F#, A, C)
                {NOTES[6], NOTES[1], NOTES[3]}   // B♭ major (B♭, D, F)
        };

        // Generate and play the chord progression
        for (double[] chord : chordProgression) {
            playChordNotes(line, chord, CHORD_DURATION, SAMPLE_RATE, AMPLITUDE);
            try {
                Thread.sleep(REST_DURATION); // Pause between chords
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        line.drain();  // Ensure that all data is played before closing
        line.close();
    }

    private static void playChordNotes(SourceDataLine line, double[] frequencies, int duration, float sampleRate, int amplitude) {
        int numSamples = (int) (duration * sampleRate / 1000);
        byte[] buffer = new byte[numSamples];

        // Combine the sine waves of each frequency to form the chord
        for (int i = 0; i < numSamples; i++) {
            double sum = 0;
            for (double frequency : frequencies) {
                double angle = 2.0 * Math.PI * i / (sampleRate / frequency);
                sum += Math.sin(angle);
            }
            buffer[i] = (byte) ((sum / frequencies.length) * amplitude); // Average the frequencies
        }
        line.write(buffer, 0, buffer.length);
    }
}
