package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        byte[] compressed = compress(b);
        out.write(compressed);
    }

    private byte[] compress(byte[] b) {
        ArrayList<Byte> compressed = new ArrayList<>();
        byte current = b[0];
        int count = 1;
        for (int i = 1; i < b.length; i++) {
            if (b[i] == current) {
                count++;
            } else {
                compressed.add(current);
                compressed.add((byte) count);
                current = b[i];
                count = 1;
            }
        }
        compressed.add(current);
        compressed.add((byte) count);
        return toByteArray(compressed);
    }

    private byte[] toByteArray(ArrayList<Byte> compressed) {
        byte[] arr = new byte[compressed.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = compressed.get(i);
        }
        return arr;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
