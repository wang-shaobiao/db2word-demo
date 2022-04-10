# 数据库设计文档生成工具

- [mysql+postgres合集(老版iText应用demo)](https://github.com/wang-shaobiao/db2word-demo/tree/first)<br>
- [iText7应用生成pdf](https://github.com/wang-shaobiao/db2word-demo/tree/second)<br>
iText7具体应用见分支**second-postgresql**

## 结果展示：
![image](https://user-images.githubusercontent.com/24486746/162610993-ce498b11-bf22-4e65-a606-896ee86c000a.png)



## 依赖介绍：

Apache iText 是一个开源 Java 库，支持 PDF 文档的开发和转换。<br>
iText是著名的开放源码的站点sourceforge一个项目，是用于生成PDF文档的一个java类库。通过iText不仅可以生成PDF或rtf的文档，而且可以将XML、Html文件转化为PDF文件。<br>

[iText官网](https://itextpdf.com/en)

* 老项目使用：<br>
itext<com.lowagie><br>
itext-asian<com.itextpdf><br>
itext-rtf<com.lowagie><br>
* iText7：<br>

  * 方式1：单独依赖
com.itextpdf:kernel:7.0.4
com.itextpdf:io
com.itextpdf:layout
com.itextpdf:forms
com.itextpdf:pdfa
com.itextpdf:pdftest
  * 方式2：全家桶
com.itextpdf:itext7-core:7.0.4

[iText7应用生成pdf](https://github.com/wang-shaobiao/db2word-demo/tree/second)<br>

## gradle项目搭建-多moudle
- db2word-mysql
- db2word-postgresql

## 1.实现--db2word-mysql

- 查询所有表名

```sql
SELECT table_name, table_comment FROM information_schema.TABLES WHERE table_schema='zaservice';
```
![image](https://user-images.githubusercontent.com/24486746/162043004-4ab60ae1-e6ff-4d4c-98fe-3ea2436b07dd.png)

- 查询每个表的字段信息

```sql
SELECT
        COLUMN_NAME 字段名称,
        COLUMN_TYPE 字段类型,
        COLUMN_DEFAULT 默认值,
        CHARACTER_MAXIMUM_LENGTH AS 最大长度,
        (CASE WHEN is_nullable = 'NO' THEN
                        '否' ELSE
                        '是' END
        ) AS 是否可空,
        (CASE WHEN column_key = 'PRI' THEN
                        '是' ELSE
                        '否' END
        ) AS 是否主键,
        EXTRA 其他,
        COLUMN_COMMENT 字段说明
FROM
        INFORMATION_SCHEMA.COLUMNS
WHERE
 table_schema='zaservice' AND
        table_name = 'sys_log'

```
![image](https://user-images.githubusercontent.com/24486746/162042900-818e6eea-0f4a-4b3b-a962-b7d8f0e9bd94.png)

- 简单展示表字段信息
  `SHOW FULL FIELDS FROM live_gateway.t_authorize`
  效果和上面第二点类似
![image](https://user-images.githubusercontent.com/24486746/162043083-f044158b-4295-45ce-a3a9-64e81412d3c2.png)

## 2.实现--db2word-postgresql

[pgsql系统表参考](https://www.csdn.net/tags/MtTaMg2sMTg1MTQ2LWJsb2cO0O0O.html)
- 查询所有表名

```sql
select relname as table_name,(select description from pg_description where objoid=oid and objsubid=0) 
as table_comment 
from pg_class where relkind ='r' and relname NOT LIKE 'pg%' AND relname NOT LIKE 'sql_%' 
and relnamespace=(select oid from pg_namespace where nspname='cif' )
order by table_name;
```
可以增加条件 `relowner =?` 判断所有者 对应表为 `pg_authid`<br>
**更新：**<br>
发现relowner不太行，还是会有多表情况，需要用schema来区分，`pg_class.relnamespace` 对应表`pg_namespace.oid` <br>

- 查询每个表的字段信息
```sql
select
a.attname as 字段名称,
format_type(a.atttypid,a.atttypmod) as 类型,
(case when atttypmod-4>0 then atttypmod-4 else 0 end) as 长度,
(case 
when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='p')>0 then 'PRI' 
when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='u')>0 then 'UNI'
when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='f')>0 then 'FRI'
else '' end) as 索引,
(case when a.attnotnull=true then 'NO' else 'YES' end) as 允许为空,
col_description(a.attrelid,a.attnum) as 说明
from pg_attribute a
where attstattarget=-1 and attrelid = (select oid from pg_class where relname ='ok' 
and relnamespace=(select oid from pg_namespace where nspname='cif' )
));
```
最好加上`releowner`，要不有可能会有表名相同的情况<br>
**更新：**<br>
发现relowner不太行，还是会有多表情况，需要用schema来区分，`pg_class.relnamespace` 对应表`pg_namespace.oid` <br>


## 3.主要实现
表格 页码 页眉 水印 分页  插入图片...

# References
[官方API](https://api.itextpdf.com/iText7/java/7.2.0/)
https://www.cnblogs.com/fonks/p/15090635.html
https://ld246.com/article/1506562801846
https://www.bbsmax.com/A/rV57P1NqdP/
https://blog.csdn.net/weiruiwei/article/details/89084807
