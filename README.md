# ConsineTextCheck
程序暂且支持 .docx 格式的文本。

测试文件夹中A是原始文本，B是抄袭文本。

上传页面中：先上传的是原始文本。后上传的是抄袭文本。

程序运行结果集：A、B重复片段在各自文中的位置标记，在抄袭文本中写入抄袭文本对原文本的相似率。

程序为一次性使用，若多次使用，需重新运行部署。

Java版本为1.8、maven为3.0以上，开发工具为idea。

项目为springboot+Layui。maven作为依赖jar包管理工具。

在浏览器输入http://localhost:8080/fileLoad 。

余弦查重思路：
      1、对两份合同读取内容，结果集：两串字符串。
      2、分别对字符串，按 句号、问号、感叹号、分号进行分割。
      3、分割后的片段逐个取词，统计词频，统计所有语句中的每个词在当前句子出现的次数，
         例如：
            第一句：你好，我是小王，我是个程序员”
                    你好1,我2，是2，小王1，个1，程序员1，设计师0。
            第二句：你好，我是设计师
                    你好1,我1，是1，小王0，个0，程序员0，设计师1。
      4、组合词频向量，第一句(1,2,2,1,1,1,0),第二句(1,1,1,0,0,0,1)。
      5、将两个词频向量带入余弦计算公式。
      6、根据得到的结果得出两个句子的相似度，值越大表示相似度越高。
