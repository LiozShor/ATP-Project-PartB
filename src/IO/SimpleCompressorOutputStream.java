package IO;

public class SimpleCompressorOutputStream {

    public byte[] compress(byte[] b) {
        if (b == null || b.length == 0) {
            return new byte[0];
        }

        // Calculate the size of the compressed array
        int size = 0;
        byte current = b[0];
        int count = 1;
        for (int i = 1; i < b.length; i++) {
            if (b[i] == current && count < 255) { // count is limited to 255
                count++;
            } else {
                size += 2; // Increase size for a new pair (byte + count)
                current = b[i];
                count = 1;
            }
        }
        size += 2; // Account for the last run

        byte[] compressed = new byte[size];
        current = b[0];
        count = 1;
        int index = 0;
        for (int i = 1; i < b.length; i++) {
            if (b[i] == current && count < 255) { // count is limited to 255
                count++;
            } else {
                compressed[index++] = current;
                compressed[index++] = (byte) count;
                current = b[i];
                count = 1;
            }
        }
        compressed[index++] = current;
        compressed[index] = (byte) count; // Last run

        return compressed;
    }
}
