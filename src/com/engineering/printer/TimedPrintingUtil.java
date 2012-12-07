package com.engineering.printer;

import java.io.IOException;

import com.trilead.ssh2.Connection;

public class TimedPrintingUtil {
	private static Connection connection;
	private static TimedPrintingUtil instance;
	private static CommandConnection mConn;
	private static ErrorCallback mCb;
	private static final String TO_PRINT = "to_print";
	private static final String SETUP_SH = "curl -L https://raw.github.com/emish/cets_autoprint/master/setup.sh | sh";

	public synchronized static TimedPrintingUtil getInstance(Connection conn,ErrorCallback cb) throws IOException{
		if (instance == null || conn != connection){
			instance = new TimedPrintingUtil(conn);
		}
		instance.mCb = cb;
		return instance;
	}

	private TimedPrintingUtil(Connection conn) throws IOException{
		mConn = new CommandConnection(conn);
		setup();
	}

	private void setup() throws IOException{
		String returnV = mConn.execWithReturn("echo | screen -ls");
		System.out.println(returnV);
		String firstL = returnV.split(" ")[0];
		//System.out.println(firstL.equals("No"));
		if(firstL.equals("No")){
			mConn.execWithoutReturnPty("cd ~");
			mConn.execWithoutReturnPty("mkdir to_print");
			System.out.println(mConn.execWithReturnPty("git clone https://github.com/emish/cets_autoprint.git autoprint"));
			mConn.execWithoutReturnPty("screen -i");
			System.out.println("Screen Done!");
<<<<<<< HEAD
			System.out.println(mConn.execWithReturnPty(SETUP_SH));
			mConn.execWithoutReturnPty("screen -i");
			System.out.println("Screen Done again!");
			mConn.execWithoutReturnPty("python ~/autoprint/autoprint.py");
=======
			System.out.println(mConn.execWithReturnPty("python ~/autoprint/autoprint.py"));
>>>>>>> 2281f6d44b9f63edfdf8fefda7f28d94aed29c22
			System.out.println("Python done!");
			System.out.println(mConn.execWithReturnPty("screen -d"));
			//mConn.closeSession();
		}
	}
	
	public synchronized boolean addToPrintList(String filename){
		try {
			String home = mConn.execWithReturn("echo ~");
			String target = home + "/" + TO_PRINT;
			//String pdfFilename = filename + ".pdf";
			//mConn.execWithReturn("unoconv -o " + pdfFilename + " " + filename);
			System.out.println("Going to cp"+ filename + " " + target);
			mConn.execWithReturn("cp " + filename + " " + target);
		} catch (IOException e) {
			mCb.error();
		}
		return true;
	}

	public CommandConnection getmConn() {
		return mConn;
	}

}
