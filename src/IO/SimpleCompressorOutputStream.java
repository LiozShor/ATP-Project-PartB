package IO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleCompressorOutputStream extends OutputStream {

    private OutputStream out;
    private List<Integer> buffer;

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out;
        this.buffer = new ArrayList<>();
    }

    @Override
    public void write(int b) throws IOException {
        // Accumulate bytes in a buffer until we have enough for compression
        buffer.add(b);
        if (buffer.size() >= 255) { // When buffer reaches maximum size for compression
            compressBuffer();
        }
    }

    @Override
    public void close() throws IOException {
        // Flush remaining bytes in buffer before closing
        if (!buffer.isEmpty()) {
            compressBuffer();
        }
        out.close();
    }

    private void compressBuffer() throws IOException {
        if (buffer.isEmpty()) {
            return;
        }

        int count = 1;
        int lastValue = buffer.get(0);
        for (int i = 1; i < buffer.size(); i++) {
            int currentValue = buffer.get(i);
            if (currentValue == lastValue) {
                count++;
            } else {
                out.write((byte) count);
                out.write((byte) lastValue);
                lastValue = currentValue;
                count = 1;
            }
        }
        // Write the last count and byte if needed
        out.write((byte) count);
        out.write((byte) lastValue);

        buffer.clear(); // Clear the buffer after compression
    }
}
