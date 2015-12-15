package services;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by mdesousa on 10/12/2015.
 */

/**
 * This is a trash class
 * Adapting my code after my misundertanding of the message class
 * provided by my group
 */
public class Adapter {

    private Message msg;
    private String username;

    public Adapter(Message message){
        this.msg = message;
        parseMessage();
    }



    private InetAddress addr;
    private String ID;
    public Adapter(String ID)
    {
        this.ID = ID;
    }

    public InetAddress parse(){

        try {
             this.addr = InetAddress.getByName((ID.substring(ID.indexOf("@")+1)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return this.addr;
    }




    private void parseMessage(){
       this.username = msg.getData().substring(0,msg.getData().indexOf("@"));
    }

    public String getSender(){
        return this.username;
    }

}
