package IO;

public class SimpleCompressorOutputStream {
    byte[] b;
    public SimpleCompressorOutputStream(byte[] b) {
        this.b = b;
    }

        public byte[] compress(byte[] b) {
            if (b == null || b.length == 0) {
                return new byte[0];
            }

            // Calculate the size of the compressed array
            int size = 0;
            byte current = b[0];
            int count = 1;
            for (int i = 1; i < b.length; i++) {
                if (b[i] == current) {
                    count++;
                } else {
                    size++;
                    current = b[i];
                    count = 1;
                }
            }
            size++; // Account for the last run

            byte[] compressed = new byte[size];
            current = b[0];
            count = 1;
            int index = 0;
            for (int i = 1; i < b.length; i++) {
                if (b[i] == current) {
                    count++;
                } else {
                    compressed[index++] = (byte) count;
                    current = b[i];
                    count = 1;
                }
            }
            compressed[index] = (byte) count; // Last run

            return compressed;
        }

//        int size = 0;
//        byte current = b[0];
//        int count = 1;
//        for (int i = 1; i < b.length; i++) {
//            if (b[i] == current) {
//                count++;
//            } else {
//                size += 2;
//                current = b[i];
//                count = 1;
//            }
//        }
//        size += 2;
//        byte[] compressed = new byte[size];
//        current = b[0];
//        count = 1;
//        int index = 0;
//        for (int i = 1; i < b.length; i++) {
//            if (b[i] == current) {
//                count++;
//            } else {
//                compressed[index++] = current;
//                compressed[index++] = (byte) count;
//                current = b[i];
//                count = 1;
//            }
//        }
//        compressed[index++] = current;
//        compressed[index] = (byte) count;
//        return compressed;
//    }

}
