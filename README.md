# 数据库设计文档生成工具

[套娃借鉴](https://github.com/heartsuit/db2word/tree/mysql) <br>
[mysql+postgres合集](https://github.com/wang-shaobiao/db2word-demo.git)
## 依赖介绍：

Apache iText 是一个开源 Java 库，支持 PDF 文档的开发和转换。<br>
iText是著名的开放源码的站点sourceforge一个项目，是用于生成PDF文档的一个java类库。通过iText不仅可以生成PDF或rtf的文档，而且可以将XML、Html文件转化为PDF文件。<br>

[iText官网](https://itextpdf.com/en)

老项目使用：<br>
itext<com.lowagie><br>
itext-asian<com.itextpdf><br>
itext-rtf<com.lowagie><br>
新项目变更：<br>
待完善<br>

## gradle项目搭建-多moudle
- db2word-mysql
- db2word-postgresql

## 1.实现--db2word-mysql

### 1.1 生成
http://localhost/ams/getDb/dbName
E:data/dbDetail.doc

### 1.2 参考SQL
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
实现类似mysql
### 2.1 生成
http://localhost/ams/getDb/dbName
E:data/dbDetail.doc

### 2.2 参考SQL
- 查询所有表名

```sql
select relname as table_name,(select description from pg_description where objoid=oid and objsubid=0) 
as table_comment 
from pg_class where relkind ='r' and relname NOT LIKE 'pg%' AND relname NOT LIKE 'sql_%' -- and relowner='16404'
order by table_name;
```
可以增加条件 `relowner =?` 判断所有者 对应表为 `pg_authid`

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
where attstattarget=-1 and attrelid = (select oid from pg_class where relname ='ok' -- and relowner='16404'));
```
最好加上releowner，要不有可能会有表名相同的情况



## 3.主要实现逻辑
- 1. 创建com.lowagie.text.Document类对象
     `Document document = new Document(PageSize.A4)`
     也可以多加参数，来标明页边距

- 2. 创建书写器和document对象和输出流关联
     `RtfWriter2.getInstance(document, new FileOutputStream("E:/data/dbDetail.doc"));`
     pdf可以使用
     `PdfWriter pdfWriter = PdfWriter.getInstance(document, bao);`<com.lowagie.text.PDF.PDFWriter>
     还可以加入权限控制

- 3. 打开文档 document.open();
- 4. 创建段落Paragraph，创建字体Font
- 5. 文档中添加内容
    - document.add(table)
        - table.addCell(cell)
            - cell.setHeader(true);表头开始
            - ce.ll.setBackgroundColor(new Color(176,196,222)); 背景颜色
            - cell.endHeaders();表头结束
    - document.add(jpeg)
    - ...
- 6 .关闭文档 document.close();



## Reference


https://github.com/BeliveYourSelf/lv617DbTest<br>
https://www.cnblogs.com/nami/p/4112339.html<br>
https://www.csdn.net/tags/MtTaMg2sMTg1MTQ2LWJsb2cO0O0O.html
