/** Table
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

import java.util.ArrayList;

public class Table {
    String sqlStatement;
    String name = null;
    ArrayList<Attribut> attributes = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    // fügt Attribute der Arrayliste hiznu
    public Table addAttr(String name, Type type) {
        attributes.add(new Attribut(name, type));
        return this;
    }

    public void dropTable() {
        sqlStatement += "DROP TABLE IF EXISTS " + name + ";";
    }

    public Table addPrimaryKey(String name, Type type) {
        attributes.add(new PrimaryKey(name, type));
        return this;
    }

    public Table addForeignKey(String name, Type type, Table tableReference, Attribut attributeReference,
                               boolean isPrimaryKey, Constraint onDelte, Constraint onUpdate) {
        attributes.add(new ForeignKey(name, type, tableReference, attributeReference, isPrimaryKey, onDelte, onUpdate));
        return this;
    }

    public String create() {
        String sqlStatement = "CREATE TABLE IF NOT EXISTS " + name + "(";
        String sqlStatementPrimaryKey = "";

        for (int i = 0; i < attributes.size(); i++) {
            sqlStatement += attributes.get(i).create();

            if (i < attributes.size() - 1)
            {
                sqlStatement += ", ";
            } else {
                sqlStatementPrimaryKey = attributes.get(0).createPrimaryKey(sqlStatementPrimaryKey);

                sqlStatement += ", PRIMARY KEY (" + sqlStatementPrimaryKey + "));";

            }

        }
        return sqlStatement;
    }

    public String getSqlstatement() {
        return sqlStatement;
    }
}
