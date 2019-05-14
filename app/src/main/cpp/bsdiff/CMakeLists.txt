
#生成so动态库
include_directories(
./bzip2
)
# 查找当前目录下的所有源文件，并保存到 DIR_LIB_SRCS 变量
aux_source_directory(. DIR_LIB_SRCS)
aux_source_directory(./bzip2 DIR_BZIP2_LIB_SRCS)
list(APPEND DIR_LIB_SRCS ${DIR_BZIP2_LIB_SRCS})
# 生成链接库
ADD_LIBRARY(bsdiffcore SHARED ${DIR_LIB_SRCS})

find_library(log-lib log)

target_link_libraries(bsdiffcore ${log-lib})

