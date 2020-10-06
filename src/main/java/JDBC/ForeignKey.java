/** ForeignKey
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 11.05.2020
 */

package JDBC;

public class ForeignKey extends Attribute {

    Table tableReference;
    Attribute attributeReference;
    boolean isPrimaryKey;
    Constraint onDelete;
    Constraint onUpdate;


    public ForeignKey(String name, Type type, Table tableReference, Attribute attributeReference,
                      Constraint onDelete, Constraint onUpdate) {
        super(name, type);
        this.tableReference = tableReference;
        this.attributeReference = attributeReference;

        this.onDelete = onDelete;
        this.onUpdate = onUpdate;
    }


    /** This method creates a foreign key.
     * @return a string with a foreign_key and constraints.
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
     * This method creates a primary key.
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
