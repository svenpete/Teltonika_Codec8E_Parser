/** Attribut
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;


public class Attribut {
    String name = null;
    Type type;

    public Attribut(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String create() {
        return name + " " + type.getSqlTypeString();
    }

    public String createPrimaryKey(String sqlStatementPrimaryKeys) {
        return sqlStatementPrimaryKeys;
    }

}
