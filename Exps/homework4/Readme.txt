注意，想要正确使用本项目有以下几点注意：
1. 本项目用IDEA+JDK20.0.1+maven构建，请尽量使用同样的配置环境；

2. 本项目的java文件有三个，最完整的运行顺序是WebServer -> IndexFiles -> Searcher
    如果想要从零开始，请先清除Scholar-Search-Engine\src\main\resources\indexes目录下的索引文件，否则IndexFiles会生成新的索引文件，这会导致Searcher搜索时搜到双倍的结果；
    如果您想直接开始，请单独运行Searcher.java，因为我已经为测试数据生成好了索引；

3. 测试文件存储在Scholar-Search-Engine\src\main\resources\sources下；

4. WebServer的爬取以及后续的索引和检索都是针对https://aclanthology.org/中ACL会议2023年的所有论文特例化的，因此想要爬取和检索其他内容，需要您对项目进行自定义的修改；