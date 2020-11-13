package cn.com.wlfdj.anyshare;

public class LoginInfo
{
	private final String HOST="10.35.100.55";
	private final int PORT=2081;
	private final String USERNAME="wyn";
	private final String PASSWORD="LJwyn11";
	public LoginInfo() 
	{
//		File xml=new File("./anyshare_info.xml");
//		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder=factory.newDocumentBuilder();
//		Document doc=builder.parse(xml);
//		XMLParse doc=new XMLParse("./anyshare_info.xml");
//		host=doc.getNodeValue("host", 0);
//		port=Integer.parseInt(doc.getNodeValue("port", 0));
//		userName=doc.getNodeValue("username", 0);
//		password=doc.getNodeValue("password", 0);
	}
	public String getHost()
	{
		return HOST;
	}
	public int getPort()
	{
		return PORT;
	}
	public String getUserName()
	{
		return USERNAME;
	}
	public String getPassword()
	{
		return PASSWORD;
	}
}
