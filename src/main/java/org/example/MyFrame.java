package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.*;

// TODO: check is serialization is working and add deserialization
//TODO: check if the code fits and if there is no abundance
public class MyFrame extends Frame {

    private final static int COUNT_CIRCLES = 10;
    private final int WIDTH = 640;
    private final int HEIGHT = 640;
    private final int DIAMETER = Math.min(HEIGHT, WIDTH) / COUNT_CIRCLES;
    private final static SerializationType SER_TYPE = SerializationType.JSON;
    private final static PresetType STARTING_PARAMS = PresetType.LOADED;
    private final static String PREFIX = "temp-";
    private final static String SUF_DEF = ".ser";
    private final static String SUF_JSON = ".json";
    private static final String SUF;

    static {
        if (SerializationType.JSON == SER_TYPE)
            SUF = SUF_JSON;
        else SUF = SUF_DEF;
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private int COUNTER = 1;
    private final static String FILEPATH = "C:\\PJATK\\Homework\\GUI\\HOMEWORK\\HW_1\\";

    public MyFrame() {
        super();

        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        displayCircles(g);
    }


    private Color calculateColor(PresetType color) {
        return switch (color) {
            case RANDOM -> new Color(
                    (float) Math.random(),
                    (float) Math.random(),
                    (float) Math.random()
            );
            case ORANGE -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

    private void drawCircles(Graphics g, PresetType presetType) {
        for (int i = 0; i < COUNT_CIRCLES; i++) {
            for (int j = 0; j < COUNT_CIRCLES; j++) {
                g.setColor(calculateColor(presetType));
                g.fillOval(DIAMETER * j, DIAMETER * i, DIAMETER, DIAMETER);
            }
        }
    }

    @SneakyThrows
    private void serializeDef(Color color, String sample) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILEPATH + sample))) {
            oos.writeObject(color);
        }
        log("Serialized default: " + sample);
    }

    private String getSerSample() {
        String sample = PREFIX + COUNTER + SUF;
        COUNTER++;
        return sample;
    }

    // TODO: optimize serialization def and json, ++ deserialization
    // TODO: check if json mapping works
    @SneakyThrows
    private Color deserializeDef(String sample) {
        Color color;
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(FILEPATH + sample))) {
            color = (Color) oos.readObject();
        }
        log("Deserialized default: " + sample);
        return color;
    }

    private String getDeSerSample() {
        String sample = PREFIX + COUNTER + SUF;
        COUNTER++;
        return sample;
    }

    private void drawAndSerialize(Graphics g) {
        for (int i = 0; i < COUNT_CIRCLES; i++) {
            for (int j = 0; j < COUNT_CIRCLES; j++) {
                Color randomColor = new Color(
                        (float) Math.random(),
                        (float) Math.random(),
                        (float) Math.random());

                serialize(randomColor);

                g.setColor(randomColor);
                g.fillOval(DIAMETER * j, DIAMETER * i, DIAMETER, DIAMETER);
            }
        }
    }

    private void serialize(Color color) {
        String sample = getSerSample();
        if (SER_TYPE == SerializationType.JSON) {
            serializeJSON(color, sample);
        } else {
            serializeDef(color, sample);
        }
    }

    @SneakyThrows
    private void serializeJSON(Color color, String sample) {
        MyColor wrapper = new MyColor(color);
        mapper.writeValue(new File(FILEPATH + sample), wrapper);
        log("Serialized JSON: " + sample);
    }


    private void drawFromFile(Graphics g) {
        for (int i = 0; i < COUNT_CIRCLES; i++) {
            for (int j = 0; j < COUNT_CIRCLES; j++) {
                Color loadedColor = deserialize();

                g.setColor(loadedColor);
                g.fillOval(DIAMETER * j, DIAMETER * i, DIAMETER, DIAMETER);
            }
        }
    }

    private Color deserialize() {
        String sample = getDeSerSample();
        return switch (SER_TYPE) {
            case JSON -> deserializeJSON(sample);
            case DEFAULT -> deserializeDef(sample);
        };
    }

    private void log(String text) {
        System.out.println(text);
    }

    @SneakyThrows
    private Color deserializeJSON(String sample) {
        MyColor wrapper = mapper.readValue(new File(FILEPATH + sample), MyColor.class);
        log("Deserialized JSON: " + sample);
        return new Color(wrapper.getR(), wrapper.getG(), wrapper.getB(), wrapper.getA());
    }

    private void drawRandom(Graphics g) {
        drawCircles(g, PresetType.RANDOM);
    }

    private void drawOrange(Graphics g) {
        drawCircles(g, PresetType.ORANGE);
    }

    private void displayCircles(Graphics g) {
        switch (STARTING_PARAMS) {
            case RANDOM -> drawRandom(g);
            case ORANGE -> drawOrange(g);
            case RANDOM_SAVABLE -> drawAndSerialize(g);
            case LOADED -> drawFromFile(g);
        }
    }

}
