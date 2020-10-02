/** Table
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 11.07.2020
 */

package JDBC;

import java.util.ArrayList;

public class Table {
    String sqlStatement;
    String name = null;
    ArrayList<Attribute> attributes = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    // fügt Attribute der Arrayliste hiznu
    public Table addAttr(String name, Type type) {
        attributes.add(new Attribute(name, type));
        return this;
    }

    public void dropTable() {
        sqlStatement += "DROP TABLE IF EXISTS " + name + ";";
    }

    public Table addPrimaryKey(String name, Type type) {
        attributes.add(new PrimaryKey(name, type));
        return this;
    }

    public Table addForeignKey(String columnName, Type columnType, Table tableReference, Attribute attributeReference,
                               boolean isPrimaryKey, Constraint onDelte, Constraint onUpdate) {
        attributes.add(new ForeignKey(columnName, columnType, tableReference, attributeReference, isPrimaryKey, onDelte, onUpdate));
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

    /**
     * This method creates a table with a composite primary key.
     * @param CompositePrimaryKey
     * @return
     */
    public String create(String CompositePrimaryKey) {
        String sqlStatement = "CREATE TABLE IF NOT EXISTS " + name + "(";
        String sqlStatementPrimaryKey = "";

        for (int i = 0; i < attributes.size(); i++) {
            sqlStatement += attributes.get(i).create();

            if (i < attributes.size() - 1)
            {
                sqlStatement += ", ";
            } else {
                sqlStatementPrimaryKey = attributes.get(0).createPrimaryKey(sqlStatementPrimaryKey);

                sqlStatement += ", PRIMARY KEY (" + sqlStatementPrimaryKey + ", " + CompositePrimaryKey + "));";

            }

        }
        return sqlStatement;
    }

    public String getSqlstatement() {
        return sqlStatement;
    }
}
