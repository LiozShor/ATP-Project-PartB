package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SimpleDecompressorInputStream extends InputStream {

    private InputStream in;
    private byte[] decompressedData;
    private int currentPosition;

    public SimpleDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        if (currentPosition >= decompressedData.length) {
            return -1; // End of stream
        }
        return decompressedData[currentPosition++];
    }

    @Override
    public int read(byte[] b) throws IOException {
        ArrayList<Byte> decompressed = new ArrayList<>();
        int index = 0;
        int value;
        while ((value = in.read()) != -1) {
            byte count = (byte) value;
            byte val = (byte) in.read();
            for (int j = 0; j < Byte.toUnsignedInt(count); j++) {
                if (index >= b.length) {
                    return index; // Prevent overflow in case of mismatched compression
                }
                b[index++] = val;
            }
        }
        return index;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
