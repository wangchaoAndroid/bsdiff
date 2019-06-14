package com.test.jnihelloworld.bean;

/**
 * Created by 80004024 on 2019/5/14.
 */

public class TaskResult {
    public int code;

    public String result;

    public String index;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "code=" + code +
                ", result='" + result + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}
