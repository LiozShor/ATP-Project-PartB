package IO;

public class SimpleDecompressorInputStream {

    public byte[] decompress(byte[] b) {
        if (b == null || b.length == 0) {
            return new byte[0];
        }

        // Calculate the size of the decompressed array
        int size = 0;
        for (int i = 1; i < b.length; i += 2) {
            size += b[i];
        }

        return getBytes(b, size);
    }

    private byte[] getBytes(byte[] b, int size) {
        byte[] decompressed = new byte[size];
        int index = 0;

        for (int i = 0; i < b.length; i += 2) {
            for (int j = 0; j < b[i + 1]; j++) {
                decompressed[index++] = b[i];
            }
        }

        return decompressed;
    }
}
