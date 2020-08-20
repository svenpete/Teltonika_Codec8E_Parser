/** ForeignKey
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

public class ForeignKey extends Attribut {

    Table tableReference;
    Attribut attributeReference;
    boolean isPrimaryKey;
    Constraint onDelete;
    Constraint onUpdate;


    public ForeignKey(String name, Type type, Table tableReference, Attribut attributeReference, boolean isPrimaryKey,
                      Constraint onDelete, Constraint onUpdate) {
        super(name, type);
        this.tableReference = tableReference;
        this.attributeReference = attributeReference;
        this.isPrimaryKey = isPrimaryKey;
        this.onDelete = onDelete;
        this.onUpdate = onUpdate;
    }


    /**
     * @return calling methode "create" of super class Attribute
     */
    public String create() {
        return super.create() + ", "
                + "FOREIGN KEY (" + name + ") REFERENCES "
                + tableReference.name
                + "(" + attributeReference.name + ") "
                + onDelete.getSqlConstraint()
                + onUpdate.getSqlConstraint();
    }


    /**
     * creating sql statement for primary key
     * @return sqlStatementPrimaryKey
     */
    @Override
    public String createPrimaryKey(String sqlStatementPrimaryKey) {
        if (isPrimaryKey)
            return sqlStatementPrimaryKey.isEmpty() ? name : sqlStatementPrimaryKey + ", name";
        else
            return sqlStatementPrimaryKey;
    }


}
