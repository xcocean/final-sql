## 串行插入性能测试
### 2000 条
final-sql: <br>
final-sql批量插入耗时: 2487<br>
final-sql批量插入耗时: 2244<br>
final-sql批量插入耗时: 2320<br>
jpa: <br>
Jpa批量插入耗时: 5758<br>
Jpa批量插入耗时: 5265<br>
Jpa批量插入耗时: 5375<br>
mybatis:<br>
Mybatis批量插入耗时: 2775<br>
Mybatis批量插入耗时: 2503<br>
Mybatis批量插入耗时: 2416<br>
<hr>

### 10000 条
final-sql: <br>
final-sql批量插入耗时: 11536<br>
final-sql批量插入耗时: 11304<br>
jpa: <br>
Jpa批量插入耗时: 75082<br>
Jpa批量插入耗时: 76670<br>
mybatis:<br>
Mybatis批量插入耗时: 12608<br>
Mybatis批量插入耗时: 12365<br>
<hr>

### 100000 条
final-sql: <br>
final-sql批量插入耗时: 151931<br>
final-sql批量插入耗时: 292743<br>
jpa: <br>
放弃，15分钟查询数据库才4w条。串行大数据量插入太渣！
mybatis:<br>
Mybatis批量插入耗时: 151706<br>
Mybatis批量插入耗时: 293839<br>
<hr>

结论，jpa/hibernate太重，mybatis性能强大，比hibernate快了不止一点点。final-sql与mybatis比较性能不明显。