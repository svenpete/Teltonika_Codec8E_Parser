/** Type
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

public enum Type{
    BOOLEAN("BOOLEAN "),
    INTEGER("INTEGER "),
    DECIMAL("DECIMAL(10,2) "),
    FLOAT("FLOAT "),
    VARCHAR("VARCHAR(255) "),
    TINY_VARCHAR("VARCHAR(4) "),
    DATE("DATE "),
    TIME("TIME "),
    TEXT("TEXT "),
    TIMESTAMP("TIMESTAMP "),
    INTEGER_AUTO_INCREMENT("INTEGER AUTO_INCREMENT "),
    DEFAULT( "NOT NULL DEFAULT ");

     String sqlTypeString;

    Type(String sqlTypeString) {
        this.sqlTypeString = sqlTypeString;
    }


    public String getSqlTypeString() {
        return sqlTypeString;
    }

    public Type Default(String value ){
        String str = this.sqlTypeString + Type.DEFAULT.sqlTypeString + value;
        this.sqlTypeString = str;

        return this;
    }


}


