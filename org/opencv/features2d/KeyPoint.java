package org.opencv.features2d;

import org.opencv.core.Point;

public class KeyPoint {
    public float angle;
    public int class_id;
    public int octave;
    public Point pt;
    public float response;
    public float size;

    public KeyPoint(float x, float y, float _size, float _angle, float _response, int _octave, int _class_id) {
        this.pt = new Point((double) x, (double) y);
        this.size = _size;
        this.angle = _angle;
        this.response = _response;
        this.octave = _octave;
        this.class_id = _class_id;
    }

    public KeyPoint() {
        this(0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0, -1);
    }

    public KeyPoint(float x, float y, float _size, float _angle, float _response, int _octave) {
        this(x, y, _size, _angle, _response, _octave, -1);
    }

    public KeyPoint(float x, float y, float _size, float _angle, float _response) {
        this(x, y, _size, _angle, _response, 0, -1);
    }

    public KeyPoint(float x, float y, float _size, float _angle) {
        this(x, y, _size, _angle, 0.0f, 0, -1);
    }

    public KeyPoint(float x, float y, float _size) {
        this(x, y, _size, -1.0f, 0.0f, 0, -1);
    }

    public String toString() {
        return "KeyPoint [pt=" + this.pt + ", size=" + this.size + ", angle=" + this.angle + ", response=" + this.response + ", octave=" + this.octave + ", class_id=" + this.class_id + "]";
    }
}
