public class DataPage {
    private byte[] data;
    private int dataSize;

    public DataPage(byte[] data){
        this.data = data;
        this.dataSize = data.length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
}
