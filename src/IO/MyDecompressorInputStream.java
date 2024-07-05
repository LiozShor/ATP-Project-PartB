package IO;

import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() {
        return 0;
    }

    @Override
    public int read(byte[] b) {
        byte[] decompressed = decompress(b);
        for (int i = 0; i < decompressed.length; i++) {
            b[i] = decompressed[i];
        }
        return decompressed.length;
    }

    private byte[] decompress(byte[] b) {
        int size = 0;
        for (int i = 0; i < b.length; i += 2) {
            size += b[i + 1];
        }
        byte[] decompressed = new byte[size];
        int index = 0;
        for (int i = 0; i < b.length; i += 2) {
            for (int j = 0; j < b[i + 1]; j++) {
                decompressed[index++] = b[i];
            }
        }
        return decompressed;
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
