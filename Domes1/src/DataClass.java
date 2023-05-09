import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataClass {
    private int key;
    private String data;

    public DataClass(int key, String data){
        this.key =  key;
        this.data = data;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getKey() {
        return key;
    }

    public String getData() {
        return data;
    }

    public byte[] toByteArray(int chunkSize){
        /*This method takes parameter the chunk size of the data processed by the app
         * and converts the data of this instance to a ByteArray.*/
        ByteBuffer bb = ByteBuffer.allocate(chunkSize);
        bb.putInt(this.key);
        bb.put(this.data.getBytes(StandardCharsets.US_ASCII));
        return bb.array();
    }

}
