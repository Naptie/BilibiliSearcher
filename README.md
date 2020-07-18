# BilibiliSearcher

## 用途
通过关键词搜索视频，列出这些视频的信息以及UP主的信息，并保存到文件中。

## 注意
此程序用 **jdk14** 编写，编译运行前请确保 java 更新到此版本。

## 用法
* 下载代码，用 IntelliJ IDEA 打开此工程，点击标题栏中的 `Build -> Build Artifacts... -> Build`。
* 创建一个新文件夹，放入工程目录中 target 文件夹中的 JAR 文件，新建文本文档，输入以下内容：
```
@echo off
title Bilibili
chcp 65001 2>nul >nul
java -Dfile.encoding=UTF8 -jar bilibili-1.0.0.jar <keyword> <amount> <order> [debug] [path] [sex]
pause
```
* * 其中，<> 为必填项，[] 为选填项。  
* * `<keyword>` 为用于搜索的关键词，带空格的要用英文半角双引号包围。  
* * `<amount>` 为要获取的结果数量，比如10表示搜索10个结果。  
* * `<order>` 为排序，具体见下：
```
综合排序：totalrank；
最多点击：click；
最新发布：pubdate；
最多弹幕：dm；
最多收藏：stow；
最多评论：scores。
```
* * `[debug]` 为调试模式，要开启就填 debug，不开就随便填或不填。  
* * `[path]` 为日志文件的保存路径，默认为 JAR 文件所在文件夹。  
* * `[sex]` 为性别筛选，没有固定格式，比如 `male`，`man`，`boy`，`男`，都可以筛选出男UP；`female`，`woman`，`girl`，`女`，都可以筛选出女UP；`unset`，`unknown`，`保密`，都可以筛选出保密的UP；只要你打的这一串包含上述提到的两种或三种筛选信息，就可以实现把他们都筛选出来，比如 `man&woman`，`boygirl`，`男女`，都可以把男UP和女UP都筛选出来（三种等效于不填，比如 `boy&girl&unknown`）。  
* 保存关闭，将文件的后缀名 `txt` 改成 `bat`。
* 双击打开，运行程序。

## 借鉴
我查阅了 SocialSisterYi 整理的[B站API文档](https://github.com/SocialSisterYi/bilibili-API-collect)；引用了 [clyoudu-util](https://github.com/clyoudu/clyoudu-util) 的代码，用于输出表格。
