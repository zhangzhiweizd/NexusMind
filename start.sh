#!/bin/bash

# NexusMind 快速启动脚本

echo "======================================"
echo "  NexusMind 启动脚本"
echo "======================================"
echo ""

# 检查参数
ENV=${1:-dev}

echo "当前环境：$ENV"
echo ""

case $ENV in
    dev)
        echo "启动开发环境..."
        mvn spring-boot:run
        ;;
    prod)
        echo "启动生产环境..."
        mvn clean package -Pprod -DskipTests
        java -jar target/NexusMind-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
        ;;
    *)
        echo "未知环境：$ENV"
        echo "用法：./start.sh [dev|prod]"
        echo ""
        echo "示例:"
        echo "  ./start.sh dev    - 启动开发环境"
        echo "  ./start.sh prod   - 启动生产环境"
        exit 1
        ;;
esac
