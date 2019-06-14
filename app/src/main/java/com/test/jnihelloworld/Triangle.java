package com.test.jnihelloworld;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 80004024 on 2019/5/24.
 */

public class Triangle {

    public FloatBuffer vertexBuffer;

    //设置每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    //设置三角形顶点数组
    static float triangleCoords[] = {   //默认按逆时针方向绘制
            0.0f, 1.0f, 0.0f, // 顶点
            -1.0f, -0.0f, 0.0f, // 左下角
            1.0f, -0.0f, 0.0f  // 右下角
    };

    // 设置三角形颜色和透明度（r,g,b,a）
    float color[] = {0.0f, 1.0f, 0f, 1.0f};//绿色不透明

    public Triangle(){
        // 初始化顶点字节缓冲区，用于存放形状的坐标
        ByteBuffer bb = ByteBuffer.allocateDirect(
                //(每个浮点数占用4个字节
                triangleCoords.length * 4);
        //设置使用设备硬件的原生字节序
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        // 把坐标都添加到FloatBuffer中
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }

}



