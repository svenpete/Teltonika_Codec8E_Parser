/** Attribute
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 11.05.2020
 */

package JDBC;


public class Attribute {
    String name = null;
    Type type;

    public Attribute(String name, Type type) {
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
