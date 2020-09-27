/** Constraint
 * <p>
 *     Version 1
 * </p>
 * Änderungsdatum 11.05.2020
 */

package JDBC;

public enum Constraint {
    UPDATE_CASCADE(" ON UPDATE CASCADE"),
    DELETE_RESTRICT(" ON DELETE RESTRICT"),
    ON_DELETE_SET_NULL(" ON DELETE SET NULL");

    private String sqlConstraint;

    Constraint(String sqlConstraint) {
        this.sqlConstraint = sqlConstraint;
    }

    public String getSqlConstraint() {
        return sqlConstraint;
    }


}
