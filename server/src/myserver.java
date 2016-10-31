import java.sql.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 
public class myserver{
 
    public static final int ServerPort = 9999;
    public static final String ServerIP = "xxx.xxx.xxx.xxxx";
    
    public void startserver(Connection con){
    	ServerSocket serversocket = null;
    	Socket socket = null;


    	try
    	{
    		System.out.println("Server Start");
    		serversocket = new ServerSocket(9999);
    		while(true)
    		{
    			socket = serversocket.accept();
    			System.out.println(socket.getInetAddress() + " connected.");
    			Server s = new Server(socket, con);
    			s.start();
    		}
    	}
    	catch(IOException e)
    	{
    		
    	}
    	
    }
 

 
    public static void main(String[] args) {
 
    	Connection dbcon = null;
    	
    	
    	try
    	{
    		dbcon = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "1234");	
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
      new myserver().startserver(dbcon);
 
    }
    
    
    class Server extends Thread
    {
   
    	Socket socket;
    	BufferedReader in;
    	PrintWriter out;
    	java.sql.Statement stm;
    	ResultSet rs;
    	Connection dbcon;
    	PreparedStatement pst;
    	
    	Server(Socket socket, Connection con)
    	{
    		dbcon = con;
    		System.out.println(this.toString()+ " thread starts.");
    		this.socket = socket;
    		try
    		{
    			 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	         out = new PrintWriter(socket.getOutputStream(), true);
    		}
    		catch(IOException e)
    		{
    			
    		}

    		
    		
    	}
    	
    	public void run()
    	{
    		
    		try
    		{
        		stm = dbcon.createStatement();
    			stm.execute("use SEproject");

    		String sql="";

             String temp;
             String close = "connection finish";
             String signup = "signup";
             while (true)
             {
                 try {
               
                     String str = in.readLine();
                     if(str!=null)
                     {
                     	System.out.println(str);
                     	if(close.equals(str))
                         {
                     		System.out.println("Connection Closed.");
                         	break;
                    
                         }
                     	else if(signup.equals(str))
                     	{
                     		String k[] = new String[4]; // id, pw, nic, depart
                     		for(int a=0; a<4; a++)
                     			{
                     			k[a] = in.readLine();
                     			}
                     		System.out.println(k);
                     		
                     		String j = "Select * From project where id=?";
                     			pst = dbcon.prepareStatement(j);
                     			pst.setString(1, k[0]);
                     			//j = j.concat(k[0]);
                     			//j = j.concat("'");
                     			try
                     			{
                     				rs = pst.executeQuery();
                     			}
                     			catch(SQLException sqex)
                     			{
                     				System.out.println(sqex.getMessage());
                     			}
                     			
                     			if(!rs.next()) // no redundant id value, valid insertion.
                     			{
                     				j = "insert into project values(?,?,?,?)";
                     				pst = dbcon.prepareStatement(j);
                
                     				pst.setString(1, k[0]);
                     				pst.setString(2, k[1]);
                     				pst.setString(3, k[2]);
                     				pst.setString(4, k[3]);
                     				pst.executeUpdate();
                     				System.out.println("insertion success");
                     				out.println("success");
                     				
                     			}
                     			else
                     			{
                     				out.println("fail");
                     			}
                     			
                 
                     			
                     	}
                     	else if(str.equals(new String("login")))
                     			{
                     				String m,l;
                     				m = in.readLine();
                     				l = in.readLine();
                     			int result = login(m, l);
                     			if(result==1)
                     			{
                     				                                                                                                                                   System.out.println("Login success");
                     				out.println("success");
                     				
                     			}
                     			else
                     			{
                     				System.out.println("Login fail");
                     				out.println("fail");
                     			}
                     			
                     			} 
                     	else if(str.equals(new String("addcomment")))
                     	{
                     		System.out.println("addcomment");
                     		String uid = in.readLine();
                     		String cont = in.readLine();
                     		String tablenum = in.readLine();
                     		addcomment(uid, cont, Integer.parseInt(tablenum));
                     	}
                     	else if(str.equals(new String("reqcomment")))
                     	{
                     		String tablenum = in.readLine();
                     		System.out.println(tablenum);
                     			commentset cs[];
                     			cs = reqcom(Integer.parseInt(tablenum));
                     			out.println(String.valueOf(cs.length));
                     			for(int j=0; j<cs.length; j++)
                     			{
                     				if(cs[j]!=null)
                     				{
                     					out.println(cs[j].id);
                     					out.println(cs[j].content);
                     				}
                     			}
                     		
                     		
                     	}
                     	//System.out.println("S: Received: '" + str + "'");
                     	
                     }
                     //out.println("Server Received " + str);
                 } 
                 catch (Exception e) {
                     System.out.println("S: Error");
                     e.printStackTrace();
                 }
                 finally {
                 }
             }
    		}
		catch (SQLException sqex)
		{
			System.out.println("Error??");
			System.out.println(sqex.getMessage());
		}
             
               
    	}
    	
    	public int login(String i, String k)
    	{
    		String j = "Select * From project where id=? and password=? ";
    		
    		try
    		{
    			pst = dbcon.prepareStatement(j);
    			pst.setString(1, i);
    			pst.setString(2, k);
    			rs = pst.executeQuery();
    			if(rs.next())
        		{
        			return 1;
        		}
        		else
        		{
        			return 0;
        		}
    		}
    		catch(Exception e)
    		{
    			System.out.println(e.getMessage());
    			return -1;
    		}
	
    	}
    	
    	public void addcomment(String k, String l, int p)
    	{
    		String j = "Insert into comment" + String.valueOf(p) + " (userid, context, password)  values(?,?,?)";
    		try
    		{
    			pst = dbcon.prepareStatement(j);
    			pst.setString(1, k);
    			pst.setString(2, l);
    			pst.setString(3, "0");
    			pst.execute();
    		}
    		catch(Exception e)
    		{
    			
    		}
    	}
    	
    	
    	public commentset[] reqcom(int k)
    	{
    		
    		int counter = 0 ;
    		//String j = "Select context from comment" + String.valueOf(k);
    		String j = "Select * from comment" + String.valueOf(k);
    		try
    		{
    			pst = dbcon.prepareStatement(j);
    			rs = pst.executeQuery();
    			while(rs.next())
    			{
    				if(counter==50)
    				{
    					break;
    				}
    				counter++;
    			}
    			rs.beforeFirst();
    			commentset cs[] = new commentset[counter];
    			counter = 0;
    			while(rs.next())
    			{
    				String a, b, c, d;
    				a = rs.getString(1);
    				b = rs.getString(2);
    				c = rs.getString(3);
    				d = rs.getString(4);
    				cs[counter] = new commentset(a,b,c,d);
    				if(counter==50)
    				{
    					break;
    				}
    				counter++;
    			}
    			return cs;
    			
    		}
    		catch(Exception e)
    		{
    			return null;
    		}
    	}
    	
    	
    }
 
}


class commentset
{
	public String index;
	public String id;
	public String content;
	public String password;
	
	public commentset(String a,String b,String c,String d)
	{
		index =  a;
		id = b;
		content = c;
		password = d;
	}
	
	public String toString()
	{
		return "index : " + index + " id : " + id + "content : " + content + "password : " + password;
	}
}