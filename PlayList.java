//import java.util.Scanner;

public class PlayList {

   private SongNode head;
   private SongNode tail;
private SongNode current;

public SongNode getHead() {
    return head;
}

public SongNode getCurrent() {
    return current;
}
public SongNode gettail()
{
    return tail;
}
    PlayList()
    {
        head=null;
        tail=null;
       current=null;
    }
    /*Add songs to the Playlist */
    public void addSong(String songName, String filePath)
    {
        if (containsFilePath(filePath)) {
        System.out.println("This file already exists in the playlist.");
        return;
    }

        SongNode node=new SongNode(songName, filePath);
        if(node!=null)
        {
        
        if(head==null)
        {
            current=node;
            tail=node;
            head=node;
            node.next=node;
            node.prev=node;
        }
        else
        {
            if(tail!=null)
            tail.next=node;
            node.prev=tail;
            node.next=head;
            head.prev=node;
            tail=node;

        }
    }
    System.out.println("Song Added");

    }
    public String getCurrentSongName() {
        return current != null ? current.songName : "No song playing";
    }
    // plays current song
    public void playCurrent()
    {
        if(current!=null)
        System.out.println("Now Playing " + current.songName);
        else
        System.out.println("Playlist is empty");
    }

    //plays next song
    public void nextSong()
    {
        if(current!=null)
        {
          current=current.next;
          playCurrent();
        }
        else
        {
            System.out.println("Playlist is empty");
        }
    }

    // plays previous song
    public void prevSong()
    {
        if(current!=null)
        {
            current=current.prev;
            playCurrent();
        }
        else
        System.out.println("Playlist is empty");
    }
    // Insert song at beginning
    public void insertBegin(String songName, String filePath)
    {
        if (containsFilePath(filePath)) {
        System.out.println("This file already exists in the playlist.");
        return;
    }

        SongNode node=new SongNode(songName, filePath);
        if(head==null)
        {
            head=node;
            tail=node;
            current=node;
            node.next=node;
            node.prev=node;
        }
        else
        {
            node.next=head;
            head.prev=node;
            tail.next=node;
            node.prev=tail;
            head=node;
        }
        System.out.println("Inserted");
    }
    // Insert after the current song
    public void insertCurrent(String songName, String filePath)
    {
        if (containsFilePath(filePath)) {
        System.out.println("This file already exists in the playlist.");
        return;
    }

        SongNode node=new SongNode(songName, filePath);
        if(current==null)
        {
            head=node;
            tail=node;
            current=node;
            node.next=node;
            node.prev=node;
        }
        else if(current!=tail)
        {
            node.next=current.next;
            current.next.prev=node;
            current.next=node;
            node.prev=current;
        }
        else
        {
            addSong(songName,filePath);
        }
        System.out.println("Inserted");
        
    }
    // display playlist

    public String showPlaylist()
    {
        StringBuilder sb = new StringBuilder();
        SongNode temp=head;
        long count=0;
        if(temp==null)
         return "PlayList is Empty";
        while(temp!=head || count==0)
        {
        sb.append(temp.songName);
        if (temp == current) sb.append(" (Current)");
        sb.append("\n");
            count++;
            temp=temp.next;

        }
        return sb.toString();
    }
    // searching and playing a song
    public boolean searchAndPlay(String song)
    {
        SongNode temp=head;
        int flag=0;
        int found=0;
        while(temp!=head || flag==0)
        {
            flag=1;
            if(temp.songName.equalsIgnoreCase(song))
            {
                found=1;
            current=temp;
            }
            temp=temp.next;

        }
        if(found==0)
        return false;
        else
        {
        playCurrent();
        return true;
    }
    
}

public boolean containsFilePath(String filePath) {
    if (head == null) return false;

    SongNode temp = head;
    int flag = 0;

    while (temp != head || flag == 0) {
        flag = 1;
        if (temp.filePath.equals(filePath)) {
            return true;
        }
        temp = temp.next;
    }

    return false;
}


 public boolean deletebyName(String name)
    {
        if(name.equals(""))
        {
        System.out.println("field empty");
        return false;
        }
        SongNode temp=head;
        int found=0;
        int flag=0;
        if(temp==null)
        {
        System.out.println("Nothing to delete");
        return false;
        }
            
        
        else
        {
            while(temp!=head || flag==0 )
            {
                flag=1;
                if(temp.songName.equalsIgnoreCase(name))
                {
                    found=1;
                    break;
                }
                temp=temp.next;
            }
            if(found==0)
            {
            System.out.println("No song found");
            return false;
            }
            else
            {
            if (temp == head && temp == tail) {
            head = null;
             tail = null;
             return true;
           }
                if(temp==head)
                head=head.next;
                else if(temp==tail)
                tail=tail.prev;
                temp.prev.next=temp.next;
                temp.next.prev=temp.prev;

                
            }
        }
        return true;

    }

public String getCurrentSongFilePath() {
    return current != null ? current.filePath : null;
}

}
