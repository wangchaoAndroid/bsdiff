package com.test.jnihelloworld;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.test.jnihelloworld.bean.TaskResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by 80004024 on 2019/5/14.
 *  多线程异步回调结果
 */

public class MutiUpload {
    public static final String TAG = "MutiUpload";
    // 工作线程池
    private ExecutorService mExecutors = new ThreadPoolExecutor(10,10,0, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000));

    //处理结果的线程池
    private ExecutorService mResultExecutors =new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000));

    public void upload(final OnCompleteListener listener){


        mResultExecutors.execute(new Runnable() {
            @Override
            public void run() {
                List<TaskResult> failTask = new ArrayList<>();
                final List<Future<TaskResult>> futureList = new ArrayList<>();
                //开线程池干活
                for (int i = 0; i < 10; i++) {
                    TaskResult taskResult =new TaskResult();
                    Future<TaskResult> submit = mExecutors.submit(new MyTask(taskResult,"index" + i), taskResult);
                    futureList.add(submit);
                }
                Log.e(TAG,"-->" + futureList.size());
                for(Future<TaskResult> future : futureList){
                    try {
                        TaskResult taskResult = future.get();

                        if(taskResult.code != 200){
                            failTask.add(taskResult);
                        }
                        Log.e(TAG,"-->" + taskResult.toString());
                    } catch (InterruptedException e) {
                        Log.e(TAG,"-->InterruptedException" );
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        Log.e(TAG,"-->ExecutionException" );
                        e.printStackTrace();
                    }
                }
                if(failTask.isEmpty()){
                    listener.onComplete();
                }else {
                    listener.onFail(failTask);
                }
                failTask.clear();
                failTask = null;
            }
        });
    }

    public class MyTask implements Runnable{

        private TaskResult mTaskResult;

        private String mTaskUrl;




        public MyTask(TaskResult taskResult,String taskUrl) {
            mTaskResult = taskResult;
            mTaskUrl = taskUrl;
        }

        @Override
        public void run() {
            SystemClock.sleep(5000);
            if(mTaskUrl.equals("index2") ||  mTaskUrl.equals("index5")){ // 模拟失败场景
                mTaskResult.code = 400;
                mTaskResult.result = "fail";
            }else {
                mTaskResult.code = 200;
                mTaskResult.result = "success";
            }

            mTaskResult.index = mTaskUrl;
        }
    }

    public interface OnCompleteListener{
        void onComplete();

        void onFail(List<TaskResult> taskResults);
    }
}
