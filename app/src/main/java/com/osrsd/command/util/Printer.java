package com.osrsd.command.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.osrsd.App;
import com.osrsd.cache.def.SoundEffectDefinition;
import com.osrsd.cache.def.SpriteDefinition;
import com.osrsd.cache.util.Serializable;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.stream.IntStream;

@Slf4j
public class Printer {

    public static void printContent(Serializable serializable) {
        final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        final File file = new File(String.format("%s/%s.json", String.format("%s/dump", App.defaultPath()), serializable.getPath()));
        if (file.exists() && !file.delete()) {
            throw new RuntimeException(String.format("Could not delete file at %s.", file.getPath()));
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(serializable.getDefinitions(), fileWriter);
        } catch (IOException e) {
            log.error(String.format("There was a problem when printing content to %s.", serializable.getPath()), e);
        }
    }

    public static void printImage(Serializable serializable) {
        final File file = new File(String.format("%s/dump%s", App.defaultPath(), serializable.getPath()));
        if (!file.exists() && !file.mkdir()) {
            throw new RuntimeException(String.format("Couldn't create directory at %s.", file.getPath()));
        }
        serializable.getDefinitions().stream().filter(definition -> definition instanceof SpriteDefinition)
                .forEach(definition -> {
                    SpriteDefinition sprite = (SpriteDefinition) definition;
                    IntStream.range(0, sprite.getFrames().length).forEach(frame -> {
                        final File image = new File(file.getPath(), String.format("%s_%s.png", sprite.getId(), frame));
                        if (image.exists() && !image.delete()) {
                            throw new RuntimeException(String.format("Could not delete file at %s.", image.getPath()));
                        }
                        try {
                            ImageIO.write(sprite.getFrames()[frame], "png", image);
                        } catch (IOException e) {
                            log.error(String.format("There was a problem when printing image to %s.", serializable.getPath()), e);
                        }
                    });
                });
    }

    public static void printAudio(Serializable serializable) {
        final File file = new File(String.format("%s/dump%s", App.defaultPath(), serializable.getPath()));
        if (!file.exists() && !file.mkdir()) {
            throw new RuntimeException(String.format("Couldn't create directory at %s.", file.getPath()));
        }
        serializable.getDefinitions().stream().filter(definition -> definition instanceof SoundEffectDefinition)
                .forEach(definition -> {
                    SoundEffectDefinition soundEffect = (SoundEffectDefinition) definition;
                    final File audio = new File(file.getPath(), String.format("%s.wav", soundEffect.getId()));
                    if (audio.exists() && !audio.delete()) {
                        throw new RuntimeException(String.format("Could not delete file at %s.", audio.getPath()));
                    }
                    byte[] data = soundEffect.mix();
                    AudioFormat format = new AudioFormat(22050, 8, 1, true, false);
                    AudioInputStream stream = new AudioInputStream(new ByteArrayInputStream(data), format, data.length);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, out);
                        DataOutputStream dos = new DataOutputStream(new FileOutputStream(audio));
                        dos.write(out.toByteArray());
                        dos.flush();
                        dos.close();
                    } catch (IOException e) {
                        log.error(String.format("There was a problem when printing audio to %s.", serializable.getPath()), e);
                    }
                });
    }

}
