package jp.gr.java_conf.pesk.process.worker;

import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessRunner implements Callable<Integer>{

    private static final Logger logger = LoggerFactory.getLogger(ProcessRunner.class);

    private String processArgs;

    /**
     * コンストラクタ
     * */
    public ProcessRunner(String processArgs) {
        this.setProcessArgs(processArgs);
    }

    /**
     * @return processArgs
     */
    public String getProcessArgs() {
        return processArgs;
    }

    /**
     * @param processArgs セットする processArgs
     */
    public void setProcessArgs(String processArgs) {
        this.processArgs = processArgs;
    }

    public Integer call() throws Exception {
        int rtn = -1;
        ProcessBuilder processBuilder = new ProcessBuilder(splitArgs(getProcessArgs()));

        logger.info("スレッドを開始します。["+getProcessArgs()+"]");
        Process process = processBuilder.start();

        int ret = process.waitFor();
        System.out.println("ret=" + ret);
        process.waitFor(); // 後待ち
        int exitValue = process.exitValue();

        if (exitValue != 0) {
            logger.error("スレッド呼び出し異常終了[" + exitValue + "]" + "["+ getProcessArgs() +"]");
            System.out.println("スレッド呼び出し異常終了[" + exitValue + "]" + "["+ getProcessArgs()+"]");
        } else {
            logger.info("スレッド呼び出し正常終了"+ "["+ getProcessArgs() +"]");
            System.out.println("スレッド呼び出し正常終了"+ "["+ getProcessArgs() +"]");
            rtn = 0;
        }

        return rtn;
    }

    public String[] splitArgs(String args) {
        return StringUtils.split(args, " ");
    }

}
