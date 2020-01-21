package de.kleindev.tftpserver.objects;

import de.kleindev.tftpserver.utils.CheckSum;
import de.kleindev.tftpserver.utils.LogManager;

import java.net.*;
import java.io.*;


public class TFTPserverRRQ extends Thread {

	protected DatagramSocket sock;
	protected InetAddress host;
	protected int port;
	protected FileInputStream source;
	protected TFTPpacket req;
	protected int timeoutLimit=10;
	protected String fileName;
	protected String filename_new;
	protected String folder = "/var/tftp/default/";

	// initialize read request
	public TFTPserverRRQ(TFTPread request) throws TftpException {
		try {
			req = request;
			//open new socket with random port num for tranfer
			sock = new DatagramSocket();
			sock.setSoTimeout(1000);
			fileName = request.fileName();

			host = request.getAddress();
			port = request.getPort();
			
			//create file object in parent folder
			//TODO Handle folder
			File srcFile;
			LogManager.log(LogType.DEBUG, "Requested file: "+fileName, true);
			if(!fileName.startsWith("/var/tftp/"))
				srcFile = new File(folder+fileName);
			else srcFile = new File(fileName);
			filename_new = srcFile.getAbsolutePath();
			LogManager.log(LogType.DEBUG, "Sending file: "+srcFile.getAbsolutePath(), true);
			/*System.out.println("procce checking");*/
			//check file
			if (srcFile.exists() && srcFile.isFile() && srcFile.canRead()) {
				source = new FileInputStream(srcFile);
				this.start(); //open new thread for transfer
			} else
				throw new TftpException("access violation");

		} catch (Exception e) {
			TFTPerror ePak = new TFTPerror(1, e.getMessage()); // error code 1
			try {
				ePak.send(host, port, sock);
			} catch (Exception f) {
			}

			System.out.println("Client start failed:  " + e.getMessage());
		}
	}
	//everything is fine, open new thread to transfer file
	public void run() {
		int bytesRead = TFTPpacket.maxTftpPakLen;
		// handle read request
		if (req instanceof TFTPread) {
			try {
				for (int blkNum = 1; bytesRead == TFTPpacket.maxTftpPakLen; blkNum++) {
					TFTPdata outPak = new TFTPdata(blkNum, source);
					LogManager.log(LogType.DEBUG, "send block no. " + outPak.blockNumber(), true);
					bytesRead = outPak.getLength();
					LogManager.log(LogType.DEBUG, "bytes sent:  " + bytesRead, true);
					outPak.send(host, port, sock);
					LogManager.log(LogType.DEBUG, "current op code  " + outPak.get(0), true);

					//wait for the correct ack. if incorrect, retry up to 10 times
					while (timeoutLimit!=0) { 
						try {
							TFTPpacket ack = TFTPpacket.receive(sock);
							LogManager.log(LogType.DEBUG, ack.toString(), true);
							if (!(ack instanceof TFTPack)){throw new Exception("Client failed");}
							TFTPack a = (TFTPack) ack;

							if(a.blockNumber()!=blkNum){ //check ack
								throw new SocketTimeoutException("last packet lost, resend packet");}
							LogManager.log(LogType.DEBUG, "confirm blk num " + a.blockNumber()+" from "+a.getPort(), true);
							break;
						} 
						catch (SocketTimeoutException t) {//resend last packet
							System.out.println("Resent blk " + blkNum);
							timeoutLimit--;
							outPak.send(host, port, sock);
						}
					} // end of while
					if(timeoutLimit==0){throw new Exception("connection failed");}
				}
				System.out.println("Transfer completed.(Client " +host +")" );
				System.out.println("Filename: "+fileName + "\nSHA1 checksum: "+ CheckSum.getChecksum(folder+fileName)+"\n");
			} catch (Exception e) {
				TFTPerror ePak = new TFTPerror(1, e.getMessage());

				try {
					ePak.send(host, port, sock);
				} catch (Exception f) {
				}

				System.out.println("Client failed:  " + e.getMessage());
			}
		}
	}
}