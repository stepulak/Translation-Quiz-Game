package com.example.translationgame;

class VirtualCamera {
    public static float VIRTUAL_WIDTH = 1080;
    public static float VIRTUAL_HEIGHT = 1920;
    private float realWidth;
    private float realHeight;

    public VirtualCamera(float realWidth, float realHeight) {
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }
}
