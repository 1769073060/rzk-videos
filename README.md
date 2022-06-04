# rzk-videos

> 微信小程序后台api接口

# 部署流程

## maven打包后

### 把jar包和Dockerfile文件上传服务器放在同一个文件中

> docker build -t rzk-api .
> docker run -d --restart=always --name rzk-api -v /usr/local/logs:/home/jar-logs -p 8090:80 rzk-api

