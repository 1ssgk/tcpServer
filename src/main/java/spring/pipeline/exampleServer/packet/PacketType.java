package spring.pipeline.exampleServer.packet;

public final class PacketType {
    private PacketType() { }
    public static final byte HeartBeat = 0;
    public static final byte In = 1;
    public static final byte Out = 2;

    public static final String[] names = { "HeartBeat", "In", "Out", };

    public static String name(int e) { return names[e]; }

}
