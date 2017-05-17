/*
If you used any functions specific to the H2 database (all functions: http://www.h2database.com/html/functions.html ) in your report queries, 
those functions may not be available in PostgreSQL and your reports will throw exceptions. We recommend that you modify the queries in your 
reports to use functions compatible with PostgreSQL. However, if that is not possible (when the rendering fails in history because of a missing 
function, for example) you can deploy your own function in Polarion's PostgreSQL database.

Here is an example of an error, thrown when the proper "curdate" function is not defined, and occurring after updating to a Polarion version that uses PostgreSQL:
    "ERROR: function CURDATE() does not exist Hint: No function matches the given name and argument types. You might need to add explicit type casts."

Steps for adding "curdate" function (to map from H2 to PostgreSQL):

    1. In your installation's file system, navigate to [POLARION_HOME]/polarion/plugins/com.polarion.platform.sql_*
    2. Copy the file h2_functions_mapping.sql, paste it to [POLARION_HOME]/polarion/configuration and open it in a text or SQL editor application.
    3. If the function is not already defined in this file, add your own definition of the function from H2. 
    For example:
       CREATE OR REPLACE FUNCTION POLARION.CURDATE()
       returns date AS
       $$ SELECT current_date $$ LANGUAGE SQL; 

    Function definitions must always end with a semicolon (;) to be successfully deployed, and always prefix the names of mapped functions with the schema name 
    "polarion", otherwise Polarion will not always take them into consideration. 

    4. Enable the property com.polarion.platform.sql.h2CompatibilityMode=true in the polarion.properties system configuration file (see Polarion Help for the location if necessary).
    5. Restart the Polarion server.

    TIP: For easy debugging before deploying a function to this file, try to manually create it in the PostgreSQL database. Use the psql utility to connect to 
    the 'polarion' database as the 'polarion' user: psql -p 5433 -U polarion polarion.

    For information on creating PostgreSQL functions, see: http://www.postgresql.org/docs/current/static/sql-createfunction.html 
    To check the definition of a function in H2, see: http://www.h2database.com/html/functions.html

You can add any other function to the [POLARION_HOME]/polarion/configuration/h2_functions_mapping.sql file, and it will be deployed on the next restart 
(when system property com.polarion.platform.sql.h2CompatibilityMode is enabled).
*/

CREATE OR REPLACE FUNCTION POLARION.CURDATE()
returns date AS
$$ SELECT current_date $$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION POLARION.YEAR(timestamp) 
returns double precision AS $$ 
SELECT EXTRACT(YEAR FROM $1::timestamp);
$$
LANGUAGE SQL;

CREATE OR REPLACE FUNCTION POLARION.ROUND(real,integer) 
returns numeric AS $$ 
SELECT round($1::numeric, $2);
$$
LANGUAGE SQL;
