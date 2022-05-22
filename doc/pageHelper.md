## 查询的分页使用

```java
FinalPageHelper.startPage(2,10);// 开始分页，依赖于方言的实现
List<MyUser> select = finalSql.select(new MyUser());
PageInfo pageInfo = FinalPageHelper.getPageInfo();// 获取分页返回的total信息

System.out.println(pageInfo);
System.out.println(select.size());
```