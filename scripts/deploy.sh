#!/bin/bash

APP_DIR=/home/ec2-user/app
JAR_NAME=$(ls $APP_DIR/build/libs/*.jar | tail -n 1)

echo "Using java: $(which java)" >> $APP_DIR/app.log
echo "Java version:" >> $APP_DIR/app.log
java -version >> $APP_DIR/app.log 2>&1

echo "Stopping existing app..."
sudo pkill -f 'java -jar' || true

echo "Starting new app..."
nohup java -jar $JAR_NAME > $APP_DIR/app.log 2>&1 &
