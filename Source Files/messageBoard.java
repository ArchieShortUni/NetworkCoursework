import java.util.Vector;

public class messageBoard {

    public static Vector<message> messages = new Vector<message>();

    public messageBoard(){}


    public void addMessage(message m){
        boolean alreadyHave = false;
        for(message toMatch:messages){
            if(toMatch.getId().equals(m.getId())){
                alreadyHave = true;
            }
        }
        if(alreadyHave == false){messages.add(m);}}


    public message getMessageByRow(int row){return messages.elementAt(row);}

    public Vector<String> getMessageByFilter(Vector<String> filters){
        Vector<String> IDv = new Vector<String>();

        for(message m :messages){
            Boolean matching = true;
            for(String filter:filters){
                String temp = filter;
                temp =temp.replaceAll("\\s.*", "");

                String time = filters.elementAt(0).substring(5).trim();
                time = time.replaceAll("\\s.*", "");
                int since = Integer.parseInt(time);
                int currentTime = (int) (System.currentTimeMillis() /1000);

                if( !(Integer.parseInt(m.getTime()) >since ) && (Integer.parseInt(m.getTime() ) < currentTime)){
                    matching = false;
                }
                if(temp.equals("From:")){
                    if(!m.getSender().equals(filter.substring(temp.length()+1))){matching = false;}
                }
                if(temp.equals("To:")){
                    if(!m.getReceiver().equals(filter.substring(temp.length()+1))){matching = false;}
                }
                if(temp.equals("Subject:")){
                    if(!m.getSubject().equals(filter.substring(temp.length()+1))){matching = false;}
                }
                if(temp.equals("Topic:")){
                    if(!m.getTopic().equals(filter.substring(temp.length()+1))){matching = false;}
                }
            }


            if(matching){
                IDv.add(m.getId());
            }}
        return IDv;
    }
    public message getMessageByID(String ID){
        message mess = null;

        for(message m : messages){
            if(m.getId().equals(ID)){
                mess = m;
            }
        }
        return mess;


    }
    public Vector<message> getMessages(){return messages;}

}
