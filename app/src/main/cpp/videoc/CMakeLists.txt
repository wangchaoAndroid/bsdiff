
#生成so动态库

# 查找当前目录下的所有源文件，并保存到 DIR_LIB_SRCS 变量
aux_source_directory(. DIR_LIB_SRCS)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/jniLibs/${ANDROID_ABI})
set(PRE_BUILD_DIR ${PROJECT_SOURCE_DIR}/src/main/cpp/prebuild/${ANDROID_ABI})
set(DIR_THIRD_SRCS ./include )

# 生成链接库
ADD_LIBRARY(videoc SHARED ${DIR_LIB_SRCS})
ADD_LIBRARY(avcodec SHARED IMPORTED)
ADD_LIBRARY(avcodec-58 SHARED IMPORTED)
ADD_LIBRARY(avformat-58 SHARED IMPORTED)
ADD_LIBRARY(avformat SHARED IMPORTED)
ADD_LIBRARY(avutil SHARED IMPORTED)
ADD_LIBRARY(avutil-56 SHARED IMPORTED)
ADD_LIBRARY(swscale-5 SHARED IMPORTED)
ADD_LIBRARY(swscale SHARED IMPORTED)
find_library(log-lib log)

target_link_libraries(videoc
            avcodec
            avcodec-58
            avformat
            avformat-58
            avutil
            avutil-56
            swscale-5
            swscale
            ${log-lib})

target_include_directories(videoc
        PRIVATE
        ${DIR_THIRD_SRCS})


set_target_properties(avcodec
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libavcodec.so)


set_target_properties(avcodec-58
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libavcodec-58.so)

set_target_properties(avformat
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libavformat.so)

set_target_properties(avformat-58
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libavformat-58.so)






set_target_properties(avutil-56
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libavutil-56.so)



set_target_properties(avutil
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libavutil.so)



set_target_properties(swscale-5
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libswscale-5.so)




set_target_properties(swscale
            PROPERTIES IMPORTED_LOCATION
            ${PRE_BUILD_DIR}/libswscale.so)

