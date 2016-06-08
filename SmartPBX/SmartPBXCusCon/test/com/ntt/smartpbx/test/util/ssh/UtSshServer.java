package com.ntt.smartpbx.test.util.ssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import org.apache.log4j.Logger;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.util.OsUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.shell.ProcessShellFactory.TtyOptions;



/***
 * SSHサーバの機能を提供する。（UTでローカルホストに接続させるための実装）
 */
public class UtSshServer {
    
    /** ロガーインスタンス */
    public static Logger log = Logger.getLogger(UtSshServer.class);
    
    /** SSHサーバクラス */
    private SshServer sshd = null;
    
    /** 待ち受けポート番号*/
    private int port = 10022;
    
    /**
     * コンストラクタ
     */
    @SuppressWarnings("unused")
    private UtSshServer(){
        
    }
    
    /**
     * コンストラクタ
     * @param bindPort  待ち受けポート番号
     */
    public UtSshServer(int bindPort){
        this.setPort(bindPort);
    }

    
    
    /**
     * SSHサーバを開始する
     * @throws IOException 
     */
    public synchronized void startup() throws IOException{
        
        log.info("start to init ssh server...");
        
        
        sshd = SshServer.setUpDefaultServer();
        //ポート番号設定
        sshd.setPort(port);
        
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
        
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(
                    String username, String password, ServerSession session) {
                // ここでユーザ認証を行う
                return true;
            }
        });
        
        // sshのログインシェルを設定
        EnumSet<TtyOptions> options;
        String[] command;
        if (OsUtils.isWin32()) {
            options = EnumSet.of(TtyOptions.ONlCr, TtyOptions.Echo, TtyOptions.ICrNl);
            command = new String[]{"cmd"};
        } else {
            options = EnumSet.of(TtyOptions.ONlCr);
            command = new String[]{"/bin/sh", "-i", "-l"};
        }
        sshd.setShellFactory(new ProcessShellFactory(command, options));

        // scpの設定
        sshd.setCommandFactory(new ScpCommandFactory());

        // sftpの設定
        sshd.setSubsystemFactories(new ArrayList<NamedFactory<Command>>(1) {
            private static final long serialVersionUID = 3630596645645274510L;
            {
                add(new SftpSubsystem.Factory());
            }
        });

        // TODO 以下が修正必要
        //sshd.setIoServiceFactory(new MinaServiceFactory());

        try {
            sshd.start();
        } catch (IOException e) {
            log.error("fail to start up ssh server!!  :" + e.getMessage());
            throw e;
        }
        
        log.info("complete to start ssh server!");
    }
    
    
    /**
     * SSHサーバを終了する
     */
    public synchronized void shutdown(){
        
        log.info("stopping ssh server.");

        try {
            if( null != sshd){
                sshd.stop();
            }
        } catch (InterruptedException e) {
            log.error("error is happened when stopping ssh server.  :" + e.getMessage());
        }

        log.info("ssh server is stopped.");
    }

    /**
     * @param port セットする port
     */
    public void setPort(int port) {
        this.port = port;
    }

}


//(C) NTT Communications  2015  All Rights Reserved
