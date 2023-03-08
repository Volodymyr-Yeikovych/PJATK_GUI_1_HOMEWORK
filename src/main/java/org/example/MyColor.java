package org.example;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class MyColor {

    private int r;
    private int g;
    private int b;
    private int a;

    public MyColor(Color color) {
       this.r = color.getRed();
       this.g = color.getGreen();
       this.b = color.getBlue();
       this.a = color.getAlpha();
    }

    public MyColor() {

    }
}
