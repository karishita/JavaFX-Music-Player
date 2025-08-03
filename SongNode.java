public class SongNode {

    String songName;
    String filePath;
    SongNode next;
    SongNode prev;
    SongNode(String songName, String filePath)
    {
        this.songName=songName;
        this.filePath=filePath;
        this.next=null;
        this.prev=null;
    }
    @Override
    public String toString() {
    return songName;
   }

}
