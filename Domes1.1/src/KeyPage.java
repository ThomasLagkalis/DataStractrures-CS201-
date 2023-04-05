import java.nio.ByteBuffer;

public class KeyPage implements Comparable<KeyPage>{
    private int key;
    private int pageIndex;

    public KeyPage(int key, int pageIndex){
        this.key = key;
        this.pageIndex =pageIndex;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public byte[] toByteArray(){
        ByteBuffer bb =  ByteBuffer.allocate(8);
        bb.putInt(this.key);
        bb.putInt(this.pageIndex);
        return bb.array();
    }

    @Override
    public int compareTo(KeyPage pair) {
        if (this.getKey() == pair.getKey()) return 0;
        else if (this.getKey() > pair.getKey()) return 1;
        else return -1;
    }
}
