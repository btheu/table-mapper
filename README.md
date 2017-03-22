# table-mapper
JAVA Table Mapper Annotation Driven

### Gére les fichiers:
- Excel xlsx
- Excel 1997-2003


@SheetAll

@Sheet("Feuil1")

@Sheet({"Feuil1","Feuil2"})


# Exemple:

    List<MonTableauSheetAll> parseExcel = Tableur2Pojo.parseExcel(inputStream, MonTableauSheetAll.class);


    @Data
    // Pas de @Sheet => Chargement uniquement sur la premiere feuille
    public class MonTableauSheet1 {
    
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


# A faire:
- *.csv
