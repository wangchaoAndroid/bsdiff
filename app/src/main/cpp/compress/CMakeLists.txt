
#生成so动态库

# 查找当前目录下的所有源文件，并保存到 DIR_LIB_SRCS 变量
aux_source_directory(. DIR_LIB_SRCS)

# 生成链接库
ADD_LIBRARY(Compress SHARED ${DIR_LIB_SRCS})

# 导入当前目录下的所有头文件
include_directories(.)

find_library(log-lib log)

target_link_libraries(Compress ${log-lib} ${jpeg-turbo} )

