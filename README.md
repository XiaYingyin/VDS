# VDS系统开发文档

VDS使用前后端分离的架构，后端提供RESTful接口，前端通过调用相应的RESTful接口来完成相应的功能。其中前端使用Angular 2框架（需要注意这里Angular 2不是AngularJS，Angular 2框架是Google公司使用typescript语言对AngularJS框架的重写，是一个全新的框架和技术），后端使用Spring Boot框架，后台数据库使用PostgreSQL。下图为VDS的基本架构图：

![1586434782721](C:\Users\chaoy\AppData\Roaming\Typora\typora-user-images\1586434782721.png)

## 获取代码

VDS的代码托管在GitHub上，可以通过Git工具获取代码，由于包括子模块，因此必须添加`--recursive`选项：

```
git clone https://github.com/XiaYingyin/VDS.git --recursive
```

## 前端部分

前端使用Angular 2（版本为8.2），如下所示为环境配置教程：

1. 首先安装nodejs运行环境，因为angular 2依赖于nodejs，建议使用版本13.8.x：

Mac下建议使用brew安装：

```shell
brew install nodejs
```

windows下可以在[官网](https://nodejs.org/en/)下载二进制安装包，然后按照提示一步步安装即可。

Ubuntu下使用下面命令安装：

```shell
sudo apt-get install nodejs
```

2. 安装Angular CLI，Angular CLI 是一个命令行接口(Angular Command Line Interface)，用于实现自动化开发工作流程。能让开发者更容易搭建和运行Angular工程。

```
npm install -g @angular/cli
```

通过上述方法就可以完成对前端环境的搭建了。下面是运行前端程序的方法：

```shell
cd VDS/src/main/js		# 进入前端程序的根目录
npm install 	# 安装依赖包
ng serve --port <端口号> --host <ip>	# 启动前端程序
```

关于Angular 2的详细教程，可以参考这里：<https://www.runoob.com/angularjs2/angularjs2-tutorial.html>或者官方文档：<https://angular.io/cli>。

建议使用Visual Studio Code或者Jetbrains Webstorm来开发前端程序，这两款软件对Angular 2支持的很好。

## 后端部分

VDS的后端使用Spring Boot框架。

1. 安装Java运行环境：

运行后端程序需要安装Java环境，建议使用Java 8及以上版本，这里对于Java环境的安装不再赘述，可自行查找相关资料。

2. 安装maven工具：

在[官网](http://maven.apache.org/download.cgi)下载安装即可，建议使用3.3.9及以上版本。

3. 运行VDS后端程序的方法很简单，第一次启动会自动安装依赖，所以首次启动程序会比较慢。

```
cd VDS/
mvn spring-boot:run
```

建议使用Jetbrains IDEA或者Eclipse JEE来开发后端程序，这两款软件对Spring Boot有较好的支持。

## 后台数据库

后台数据库使用PostgreSQL，目前使用的版本为11。数据库的具体配置在后端的配置文件中：`src/main/resources/application.properties`。

