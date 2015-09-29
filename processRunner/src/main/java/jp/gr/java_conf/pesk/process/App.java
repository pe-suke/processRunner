package jp.gr.java_conf.pesk.process;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.gr.java_conf.pesk.process.worker.ProcessRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  引数指定したフルパス\*.batをスレッドで起動する
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        logger.debug("スレッドプールを生成");

        if (args.length == 0) {
            logger.info("引数が設定されていません。処理を強制終了します。");
            return;
        }

        // スレッドプール
        ExecutorService executorService = Executors.newFixedThreadPool(args.length);

        // Future Map
        Map<String, Future<Integer>> futureMap = new HashMap<String, Future<Integer>>();

        for (String arg : args) {
            ProcessRunner processRunner = new ProcessRunner(arg);
            Future<Integer> future = executorService.submit(processRunner);

            // futureだけ先にリストに入れておく
            futureMap.put(arg, future);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }

        try {
            // エラー判定
            for (Map.Entry<String, Future<Integer>> entry : futureMap.entrySet()) {
                String arg = entry.getKey();
                Future<Integer> future = entry.getValue();

                Integer retValue = future.get();

                if (retValue == -1) {
                    logger.error("["+ arg + "]のスレッドでエラーが発生しました。");
                }
            }


            logger.info("処理が正常に終了しました。");

        } catch (ExecutionException e) {
            logger.error("Unexpected error occured");
        } catch (InterruptedException ex) {
            logger.error("Unexpected error occured");
        }

    }
}
