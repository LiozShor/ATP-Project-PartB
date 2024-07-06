package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyDecompressorInputStream extends InputStream {

    private InputStream in;

    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        byte[] compressed = new byte[in.available()];
        int bytesRead = in.read(compressed);
        byte[] decompressed = decompress(compressed, bytesRead);
        System.arraycopy(decompressed, 0, b, 0, decompressed.length);
        return decompressed.length;
    }

    private byte[] decompress(byte[] b, int length) throws IOException {
        ArrayList<Byte> decompressed = new ArrayList<>();
        for (int i = 0; i < length; i += 2) {
            byte value = b[i];
            byte count = b[i + 1];
            for (int j = 0; j < count; j++) {
                decompressed.add(value);
            }
        }
        return toByteArray(decompressed);
    }

    private byte[] toByteArray(ArrayList<Byte> decompressed) {
        byte[] arr = new byte[decompressed.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = decompressed.get(i);
        }
        return arr;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
