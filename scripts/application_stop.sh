#!/bin/bash
# 이전 스프링부트 애플리케이션 종료 (예시)
PID=$(pgrep -f 'java.*your-app.jar')
if [ -n "$PID" ]; then
  echo "Stopping application with PID $PID"
  kill -15 $PID
  sleep 10
else
  echo "No running application found"
fi
