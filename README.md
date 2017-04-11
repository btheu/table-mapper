# table-mapper
JAVA Table Mapper Annotation Driven

### File types handled
- Excel xlsx
- Excel 1997-2003

### Features
- Primitives types conversions
- Default value
- Column name with regular expression
- Date formatter for parsing date string value

### Supports


| Type | Read | Write | Update |
|---|--------|--------|---|
| CSV | No | Yes | No |
| Excel | Yes | No | No |


### Download
```xml
<dependency>
   <groupId>com.github.btheu.table-mapper</groupId>
   <artifactId>table-mapper</artifactId>
   <version>0.3.0</version>
</dependency>
```

### Main annotations
	@SheetAll
	
	@Sheet("Feuil1")
	
	@Sheet({"Feuil1","Feuil2"})

	@Column("Table Column Name")

### Sample
```java
    InputStream document = ...

    List<PojoTable> parseExcel = TableMapper.parseExcel(document, PojoTable.class);
```

![alt tag](https://raw.githubusercontent.com/btheu/table-mapper/master/media/sample.png)

```java
    // No @Sheet => Handle the first sheet only by default
    public class PojoTable {
    
       @Column(value = "Numéro de rue", type = CellType.INT)
       private int numero;
       
       @Column("La rue")
       private String rue;
       
       @Column("La ville")
       private String ville;
       
       @Column(value = "Date création", type = CellType.DATE)
       private Date création;
       
       @Column(value = "Cout", type = CellType.BIG_DECIMAL)
       private BigDecimal cout;
    
    }
```

### Roadmap:
- Handle csv file as input
- Handle Excel file as simple output
- Handle csv file as output
