# table-mapper
JAVA Table Mapper Annotation Driven with primitives type handling

### File types handled
- Excel xlsx
- Excel 1997-2003

### Download
```xml
<dependency>
   <groupId>com.github.btheu.table-mapper</groupId>
   <artifactId>table-mapper</artifactId>
   <version>0.1.0</version>
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
