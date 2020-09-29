package InitialDataSetting.Camera;

public enum Resolution {
    VGA(640,480),
    SVGA(800,600),
    XGA(1024,768), HD(1280,720), FHD(1920,1080);

    private int height;
    private int width;

    Resolution(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public String getName(){
        return height + " x " + width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
