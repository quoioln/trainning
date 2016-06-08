package com.ntt.smartpbx.test.util.ami;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/***
 * AMIサーバの機能を提供する。（UTでローカルホストに接続させるための実装）
 */
public class UtAmiServer {

    /** ロガーインスタンス */
    public static Logger log = Logger.getLogger(UtAmiServer.class);

    /** 待ち受けポート番号*/
    private int port = 15038;

    /** ログイン済みか */
    private boolean isLogined = false;

    /** タイムアウト*/
    @SuppressWarnings("unused")
    private static final int TIMEOUT = 10;

    /** サーバソケットインスタンス*/
    private ServerSocket serverSocket = null;

    /** スレッド実施クラスのインスタンス */
    private AmiServerThread amiThread = new AmiServerThread();
    /** スレッドインスタンス */
    private Thread thread = null;

    /** ログインコマンド */
    private static final String COMMAND_LOGIN = "Action: Login";
    /** リロードコマンド */
    private static final String COMMAND_RELOAD = "Command: module reload";

    /**
     * コンストラクタ
     */
    @SuppressWarnings("unused")
    private UtAmiServer(){

    }

    /**
     * コンストラクタ
     * @param bindPort  待ち受けポート番号
     */
    public UtAmiServer(int bindPort){
        this.setPort(bindPort);
    }


    /**
     * AMIサーバ起動する
     * @throws IOException 
     */
    public void startup() throws IOException{

        log.info("start to init the AMI server...");


        try {
            serverSocket = new ServerSocket( port );
            log.debug("socket opened");
        } catch (IOException e1) {
            log.error("Server Start Fail !!  :" + e1.getMessage() );
            throw e1;
        }

        // 待ち受けスレッドのスタート
        log.debug("try starting thread...");
        thread = new Thread(amiThread);
        thread.start();

        log.info("complete to start the AMI server!");
    }


    /**
     * AMIサーバを停止する
     */
    public void shutdown(){

        log.info("stopping the AMI server.");

        // AMIスレッドの停止
        amiThread.isRunning = false;

        try {
            if ( null != serverSocket ){
                serverSocket.close();
            }

        } catch (IOException e){ 
            log.error("error is happened when stopping the AMI server.  :" + e.getMessage());
        }

        log.info("the AMI server is stopped.");
    }


    private List<String> handleRequest(List<String> requestList){
        log.debug("[AMI Stub Server] handle request... ");
        List<String> responseList = new ArrayList<>();

        if( null == requestList ){
            String message = "Recieved message is null !!!";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        for( String line : requestList ){
            if( null == line ){ continue; }

            if( isLogined && line.contains(COMMAND_RELOAD) ){
                log.info("[AMI stub server] recieve reload message.");
                responseList = getReloadResponse();
                break;

            }else if( line.contains(COMMAND_LOGIN) ){
                log.info("[AMI stub server] recieve login message.");
                responseList = getLoginResponse();
                // ログイン状態に更新
                isLogined = true;
                break;
            }

        }


        return responseList;
    }


    /**
     * ログイン要求に対する応答文を作成する
     * @return
     */
    private List<String> getLoginResponse(){

        List<String> responseList = new ArrayList<>();

        responseList.add("Asterisk Call Manager/1.3");
        responseList.add("Response: Success");
        responseList.add("Message: Authentication accepted");
        responseList.add("Event: FullyBooted");
        responseList.add("Privilege: system,all");
        responseList.add("Status: Fully Booted");
        responseList.add("");

        return responseList;

    }

    /**
     * リロード要求に対する応答文を作成する
     * @return
     */
    private List<String> getReloadResponse(){

        List<String> responseList = new ArrayList<>();

        responseList.add("Response: Follows");
        responseList.add("Privilege: Command");
        responseList.add("--END COMMAND--");
        responseList.add("Event: ChannelReload");
        responseList.add("Privilege: system,all");
        responseList.add("ChannelType: SIP");
        responseList.add("ReloadReason: RELOAD (Channel module reload)");
        responseList.add("Registry_Count: 0");
        responseList.add("Peer_Count: 18");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: limit");
        responseList.add("Value: 20");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: pass");
        responseList.add("Value: FJnbv");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: flag_use_dial_zero");
        responseList.add("Value: 2");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: VOIPGWDOMAIN");
        responseList.add("Value: voipgw.smartpbx.jp");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: TESTCALL");
        responseList.add("Value: 1234");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: VMPLAY");
        responseList.add("Value: *5");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: PFBINTERNATIONAL");
        responseList.add("Value: 010");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: CancelTimer");
        responseList.add("Value: 60");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: CBCCancelTimer");
        responseList.add("Value: 60");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: SLCancelTimer");
        responseList.add("Value: 30");
        responseList.add("Uniqueid: none");
        responseList.add("Event: VarSet");
        responseList.add("Privilege: dialplan,all");
        responseList.add("Channel: none");
        responseList.add("Variable: forwardlimit");
        responseList.add("Value: +++++++");
        responseList.add("Uniqueid: none");
        responseList.add("");

        return responseList;

    }


    /**
     * @param port セットする port
     */
    public void setPort(int port) {
        this.port = port;
    }


    /**
     * サーバサイドのリクエストを受けつけるスレッドクラス
     */
    private class AmiServerThread implements Runnable {

        /** スレッドの実行状態 */
        public boolean isRunning = true;

        @Override
        public void run() {
            log.debug("[AMI Stub Server] thread start.");
            
            
            this.isRunning = true;
            while(isRunning) {

                try{

                    log.debug("[AMI Stub Server] wainting request....");
                    Socket socket = serverSocket.accept();

                    //受信したメッセージを処理するスレッドの立ち上げ
                    new Thread( new SocketThread(socket) ).start();


                }catch (Exception e) {
                    log.warn("[AMI Stub Server] fail to handle request !!  :" + e.getMessage());
                    break;
                }
            } // while

            log.debug("[AMI Stub Server] thread end.");
        }

    }


    /**
     * 受信したメッセージを処理するスレッドクラス
     */
    private class SocketThread implements Runnable {

        private Socket socket;

        /**
         * コンストラクタ
         * @param socket
         */
        public SocketThread(Socket socket){
            this.socket = socket;
            log.debug("connected from :" + socket.getRemoteSocketAddress() );
        }
        
        /**
         * ソケットをclose
         * @param socket
         */
        private void closeSocket(Socket socket){
            try {
                if( null != socket ){
                    socket.close();
                }
            } catch (IOException e) {
                log.warn("close socket error  :" + e.getMessage() );
            }
        }
        
        
        /***
         * 受信メッセージを取り出す
         * @param in
         * @return
         * @throws IOException
         */
        private List<String> getRecievedMessage(BufferedReader in) throws IOException{
            
            String rcvLine = null;
            List<String> requestList = new ArrayList<>();

            rcvLine = in.readLine();
            while ( rcvLine != null ) {
                log.debug("[AMI Stub Server] 受信: " + rcvLine );
                requestList.add(rcvLine);
                rcvLine = in.readLine();

                if( !in.ready()  ){
                    log.debug("[AMI Stub Server] Receive Done.");
                    break;
                }
                if( null == rcvLine){
                    log.debug("[AMI Stub Server] Receive Done. (if the end of the stream has been reached )");
                    break;
                }
                
            }
            
            return requestList;
        }
        
        /***
         * メッセージを送信する。
         * @param in
         * @return
         * @throws IOException
         */
        private void sendMessage(List<String> responseList) throws IOException{
            
            if( null != responseList && !responseList.isEmpty() ){
                log.debug("[AMI Stub Server] prepare to send message... ");
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                for( String sendLine : responseList ){
                    log.debug("[AMI Stub Server] 送信: " + sendLine );
                    out.writeBytes(sendLine);
                    out.write('\n'); // コレを忘れてすごいハマった。
                }
                out.write("".getBytes());
                out.flush();
                //out.close();  //NG
                
                log.debug("[AMI Stub Server] send message complete !!   Check Client");
            }
            
        }

        @Override
        public void run() {

            BufferedReader in =null;
            try{
                log.debug("[AMI Stub Server] prepare to recieve message... ");
                
                List<String> requestList = null;
                List<String> responseList = null;
                
                //ログインの処理
                
                // メッセージの受信
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                requestList = getRecievedMessage(in); 
                // 受信メッセージの解釈
                responseList = handleRequest(requestList);
                // メッセージ返信
                sendMessage(responseList);
                
                
                //リロードの処理
                
                requestList = getRecievedMessage(in); 
                // 受信メッセージの解釈
                responseList = handleRequest(requestList);
                // メッセージ返信
                sendMessage(responseList);
                

            }catch (Exception e) {
                log.warn("[AMI Stub Server] fail to handle request !!  :" + e.getMessage());
            }finally{
                closeSocket(socket);
            }
        }

    }


}

//(C) NTT Communications  2015  All Rights Reserved
