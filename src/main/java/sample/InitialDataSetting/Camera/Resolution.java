package sample.InitialDataSetting.Camera;

public enum Resolution {
    VGA(640,480),
    SVGA(800,600),
    SVGATWO(864,600),
    XGA(1024,768), HD(1280,720), FHD(1920,1080);

    private int height;
    private int width;

    Resolution(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public String getName(){
        return width + " x " + height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
