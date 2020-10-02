/** Schema
 * <p>
 *     Version 1
 * </p>
 * Author: Sven Petersen
 * Change date: 11.07.2020
 */

package JDBC;

public class Schema {
    String name = null;

    public Schema(String name) {
        this.name = name;
    }

    public String create() {
        return "CREATE SCHEMA " + name + ";";
    }

    public String use() {
        return "USE " + name + ";";
    }

    public String drop() {
        return "DROP SCHEMA IF EXISTS " + name + ";";
    }

}
