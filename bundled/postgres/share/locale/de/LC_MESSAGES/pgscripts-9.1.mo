��    �      $  �   ,
      �  K   �     �  f     
   r  >   }  >   �  =   �  -   9  C   g  A   �     �  #        *  (   E     n  <   �  9   �  6     H   9  E   �  B   �  9     C   E  9   �  4   �  E   �  =   >  .   |  ;   �  E   �  :   -  5   h  7   �  9   �  7     4   H  L   }  J   �  5     2   K  C   ~  7   �  2   �  2   -  J   `  :   �  5   �  G     0   d  <   �  0   �  M     J   Q  G   �  4   �  H     E   b  9   �  v   �  <   Y  I   �  @   �  5   !  4   W  1   �  ;   �  5   �  6   0  3   g  4   �  =   �  8     8   G  8   �  2   �  9   �  6   &  >   ]     �  /   �  <   �  #      #   9   ?   ]   %   �   #   �      �   3   !  &   ;!  5   b!  E   �!  I   �!  5   ("  I   ^"  5   �"  E   �"  F   $#  @   k#  >   �#  4   �#  D    $     e$  *   �$  8   �$  6   �$  %   %  (   D%  (   m%  8   �%  #   �%      �%     &  8   4&  4   m&  $   �&     �&  ,   �&  ,   '  ;   A'  9   }'     �'     �'     �'     �'  *   (  8   ;(  ,   t(  8   �(  #   �(  4   �(     3)  )   P)  7   z)     �)     �)  !   �)  +   �)     #*     4*     P*     m*     �*     �*  
   �*     �*     �*     �*  '   +  "   ++  2   N+  7   �+     �+  &   �+     �+     �+     �+     �+  :   ,     @,     B,  g  F,  l   �-     .  e   2.     �.  V   �.  V   �.  S   R/  6   �/  I   �/  G   '0     o0  "   �0     �0  )   �0     �0  E   1  B   U1  ?   �1  O   �1  L   (2  I   u2  B   �2  A   3  <   D3  D   �3  M   �3  9   4  ;   N4  J   �4  @   �4  B   5  =   Y5  6   �5  @   �5  5   6  2   E6  �   x6  �    7  7   �7  4   �7  S   �7  I   R8  6   �8  9   �8     9  A   �9  H   �9  m   :  4   �:  n   �:  8   *;  y   c;  s   �;  m   Q<  =   �<  Q   �<  N   O=  @   �=  �   �=  I   h>  L   �>  G   �>  F   G?  9   �?  6   �?  A   �?  ;   A@  <   }@  1   �@  7   �@  j   $A  @   �A  C   �A  :   B  4   OB  <   �B  9   �B  O   �B     KC  7   WC  C   �C  '   �C  (   �C  H   $D  +   mD  '   �D  #   �D  :   �D  -    E  D   NE  I   �E  S   �E  H   1F  S   zF  H   �F  M   G  M   eG  P   �G  N   H  D   SH  I   �H     �H  4   I  F   6I  L   }I  ,   �I  /   �I  7   'J  F   _J  /   �J  1   �J  -   K  ?   6K  =   vK  /   �K  +   �K  %   L  $   6L  F   [L  D   �L     �L     M     !M  !   6M  8   XM  H   �M  6   �M  J   N  0   \N  <   �N  *   �N  2   �N  D   (O     mO     �O  +   �O  7   �O     P  )   $P  2   NP  ,   �P     �P  -   �P  
   �P  %   �P  (   Q     ;Q  3   PQ  '   �Q  1   �Q  9   �Q  	   R  8   "R     [R     dR     fR     kR  =   �R     �R     �R        W   s   a   !   �   %      X      �   j       C   ~         l   o   �   ^   :   �       e               u   �       f   V          �   v      <   c   E   
   �       R   �   �   ,       )   g       K   �               D                     S       i      	   _       9   N   �           J   2   �       \   h      >   m   y   ?              /         $                     0           -   �       1   .       �   (   �   F               `           �   8   x   �      H       @           I   �       �           �       Z       &          {   �       ]              '   A       n   G   Y   �   |   }       �   �   M   �       *      =   U   t   d       T      Q          �   #   �      P   O   r   B   k   4   +      ;   w       �   p   b               7   q       L   �   "               z               �   �   �       �   �       [   5       6   3       
By default, a database with the same name as the current user is created.
 
Connection options:
 
If one of -d, -D, -r, -R, -s, -S, and ROLENAME is not specified, you will
be prompted interactively.
 
Options:
 
Read the description of the SQL command CLUSTER for details.
 
Read the description of the SQL command REINDEX for details.
 
Read the description of the SQL command VACUUM for details.
 
Report bugs to <pgsql-bugs@postgresql.org>.
       --lc-collate=LOCALE      LC_COLLATE setting for the database
       --lc-ctype=LOCALE        LC_CTYPE setting for the database
   %s [OPTION]... DBNAME
   %s [OPTION]... LANGNAME [DBNAME]
   %s [OPTION]... [DBNAME]
   %s [OPTION]... [DBNAME] [DESCRIPTION]
   %s [OPTION]... [ROLENAME]
   --help                          show this help, then exit
   --help                       show this help, then exit
   --help                    show this help, then exit
   --version                       output version information, then exit
   --version                    output version information, then exit
   --version                 output version information, then exit
   -D, --no-createdb         role cannot create databases
   -D, --tablespace=TABLESPACE  default tablespace for the database
   -E, --encoding=ENCODING      encoding for the database
   -E, --encrypted           encrypt stored password
   -F, --freeze                    freeze row transaction information
   -I, --no-inherit          role does not inherit privileges
   -L, --no-login            role cannot login
   -N, --unencrypted         do not encrypt stored password
   -O, --owner=OWNER            database user to own the new database
   -P, --pwprompt            assign a password to new role
   -R, --no-createrole       role cannot create roles
   -S, --no-superuser        role will not be superuser
   -T, --template=TEMPLATE      template database to copy
   -U, --username=USERNAME      user name to connect as
   -U, --username=USERNAME   user name to connect as
   -U, --username=USERNAME   user name to connect as (not the one to create)
   -U, --username=USERNAME   user name to connect as (not the one to drop)
   -W, --password               force password prompt
   -W, --password            force password prompt
   -Z, --analyze-only              only update optimizer statistics
   -a, --all                       vacuum all databases
   -a, --all                 cluster all databases
   -a, --all                 reindex all databases
   -c, --connection-limit=N  connection limit for role (default: no limit)
   -d, --createdb            role can create new databases
   -d, --dbname=DBNAME             database to vacuum
   -d, --dbname=DBNAME       database from which to remove the language
   -d, --dbname=DBNAME       database to cluster
   -d, --dbname=DBNAME       database to install language in
   -d, --dbname=DBNAME       database to reindex
   -e, --echo                      show the commands being sent to the server
   -e, --echo                   show the commands being sent to the server
   -e, --echo                show the commands being sent to the server
   -f, --full                      do full vacuuming
   -h, --host=HOSTNAME          database server host or socket directory
   -h, --host=HOSTNAME       database server host or socket directory
   -i, --index=INDEX         recreate specific index only
   -i, --inherit             role inherits privileges of roles it is a
                            member of (default)
   -i, --interactive         prompt before deleting anything
   -l, --list                show a list of currently installed languages
   -l, --locale=LOCALE          locale settings for the database
   -l, --login               role can login (default)
   -p, --port=PORT              database server port
   -p, --port=PORT           database server port
   -q, --quiet                     don't write any messages
   -q, --quiet               don't write any messages
   -r, --createrole          role can create new roles
   -s, --superuser           role will be superuser
   -s, --system              reindex system catalogs
   -t, --table='TABLE[(COLUMNS)]'  vacuum specific table only
   -t, --table=TABLE         cluster specific table only
   -t, --table=TABLE         reindex specific table only
   -v, --verbose                   write a lot of output
   -v, --verbose             write a lot of output
   -w, --no-password            never prompt for password
   -w, --no-password         never prompt for password
   -z, --analyze                   update optimizer statistics
 %s (%s/%s)  %s cleans and analyzes a PostgreSQL database.

 %s clusters all previously clustered tables in a database.

 %s creates a PostgreSQL database.

 %s creates a new PostgreSQL role.

 %s installs a procedural language into a PostgreSQL database.

 %s reindexes a PostgreSQL database.

 %s removes a PostgreSQL database.

 %s removes a PostgreSQL role.

 %s removes a procedural language from a database.

 %s: "%s" is not a valid encoding name
 %s: cannot cluster a specific table in all databases
 %s: cannot cluster all databases and a specific one at the same time
 %s: cannot reindex a specific index and system catalogs at the same time
 %s: cannot reindex a specific index in all databases
 %s: cannot reindex a specific table and system catalogs at the same time
 %s: cannot reindex a specific table in all databases
 %s: cannot reindex all databases and a specific one at the same time
 %s: cannot reindex all databases and system catalogs at the same time
 %s: cannot use the "freeze" option when performing only analyze
 %s: cannot use the "full" option when performing only analyze
 %s: cannot vacuum a specific table in all databases
 %s: cannot vacuum all databases and a specific one at the same time
 %s: clustering database "%s"
 %s: clustering of database "%s" failed: %s %s: clustering of table "%s" in database "%s" failed: %s %s: comment creation failed (database was created): %s %s: could not connect to database %s
 %s: could not connect to database %s: %s %s: could not get current user name: %s
 %s: could not obtain information about current user: %s
 %s: creation of new role failed: %s %s: database creation failed: %s %s: database removal failed: %s %s: language "%s" is already installed in database "%s"
 %s: language "%s" is not installed in database "%s"
 %s: language installation failed: %s %s: language removal failed: %s %s: missing required argument database name
 %s: missing required argument language name
 %s: only one of --locale and --lc-collate can be specified
 %s: only one of --locale and --lc-ctype can be specified
 %s: out of memory
 %s: query failed: %s %s: query was: %s
 %s: reindexing database "%s"
 %s: reindexing of database "%s" failed: %s %s: reindexing of index "%s" in database "%s" failed: %s %s: reindexing of system catalogs failed: %s %s: reindexing of table "%s" in database "%s" failed: %s %s: removal of role "%s" failed: %s %s: too many command-line arguments (first is "%s")
 %s: vacuuming database "%s"
 %s: vacuuming of database "%s" failed: %s %s: vacuuming of table "%s" in database "%s" failed: %s Are you sure? Cancel request sent
 Could not send cancel request: %s Database "%s" will be permanently removed.
 Enter it again:  Enter name of role to add:  Enter name of role to drop:  Enter password for new role:  Name Password encryption failed.
 Password:  Passwords didn't match.
 Please answer "%s" or "%s".
 Procedural Languages Role "%s" will be permanently removed.
 Shall the new role be a superuser? Shall the new role be allowed to create databases? Shall the new role be allowed to create more new roles? Trusted? Try "%s --help" for more information.
 Usage:
 n no out of memory
 pg_strdup: cannot duplicate null pointer (internal error)
 y yes Project-Id-Version: PostgreSQL 9.0
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2011-07-15 08:56+0000
PO-Revision-Date: 2013-03-08 00:23-0500
Last-Translator: Peter Eisentraut <peter_e@gmx.net>
Language-Team: German <peter_e@gmx.net>
Language: de
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
 
Wenn nichts anderes angegeben ist, dann wird eine Datenbank mit dem Namen
des aktuellen Benutzers erzeugt.
 
Verbindungsoptionen:
 
Wenn -d, -D, -r, -R, -s, -S oder ROLLENNAME nicht angegeben wird, dann wird
interaktiv nachgefragt.
 
Optionen:
 
Für weitere Informationen lesen Sie bitte die Beschreibung des
SQL-Befehls CLUSTER.
 
Für weitere Informationen lesen Sie bitte die Beschreibung des
SQL-Befehls REINDEX.
 
Für weitere Information lesen Sie bitte die Beschreibung des
SQL-Befehls VACUUM.
 
Berichten Sie Fehler an <pgsql-bugs@postgresql.org>.
       --lc-collate=LOCALE      LC_COLLATE-Einstellung für die Datenbank
       --lc-ctype=LOCALE        LC_CTYPE-Einstellung für die Datenbank
   %s [OPTION]... DBNAME
   %s [OPTION]... SPRACHE [DBNAME]
   %s [OPTION]... [DBNAME]
   %s [OPTION]... [DBNAME] [BESCHREIBUNG]
   %s [OPTION]... [ROLLENNAME]
   --help                          diese Hilfe anzeigen, dann beenden
   --help                       diese Hilfe anzeigen, dann beenden
   --help                    diese Hilfe anzeigen, dann beenden
   --version                       Versionsinformationen anzeigen, dann beenden
   --version                    Versionsinformationen anzeigen, dann beenden
   --version                 Versionsinformationen anzeigen, dann beenden
   -D, --no-createdb         Rolle kann keine Datenbanken erzeugen
   -D, --tablespace=TABLESPACE  Standard-Tablespace der Datenbank
   -E, --encoding=KODIERUNG     Kodierung für die Datenbank
   -E, --encrypted           verschlüssle das gespeicherte Passwort
   -F, --freeze                    Zeilentransaktionsinformationen einfrieren
   -I, --no-inherit          Rolle erbt keine Privilegien
   -L, --no-login            Rolle kann sich nicht anmelden
   -N, --unencrypted         verschlüssle das gespeicherte Passwort nicht
   -O, --owner=EIGENTÜMER       Eigentümer der neuen Datenbank
   -P, --pwprompt            weise der neuen Rolle ein Passwort zu
   -R, --no-createrole       Rolle kann keine Rollen erzeugen
   -S, --no-superuser        Rolle wird kein Superuser
   -T, --template=TEMPLATE      zu kopierende Template-Datenbank
   -U, --username=NAME          Datenbankbenutzername
   -U, --username=NAME       Datenbankbenutzername
   -U, --username=BENUTZER   Datenbankbenutzername für die Verbindung
                            (nicht der Name des neuen Benutzers)
   -U, --username=BENUTZER   Datenbankbenutzername für die Verbindung
                            (nicht der Name des zu löschenden Benutzers)
   -W, --password               Passwortfrage erzwingen
   -W, --password            Passwortfrage erzwingen
   -Z, --analyze-only              aktualisiere nur Statistiken für den Optimierer
   -a, --all                       führe Vacuum in allen Datenbanken aus
   -a, --all                 clustere alle Datenbanken
   -a, --all                 reindiziere alle Datenbanken
   -c, --connection-limit=N  Hochzahl an Verbindungen für Rolle
                            (Voreinstellung: keine Begrenzung)
   -d, --createdb            Rolle kann neue Datenbanken erzeugen
   -d, --dbname=DBNAME             führe Vacuum in dieser Datenbank aus
   -d, --dbname=DBNAME       Datenbank, aus der die Sprache gelöscht
                            werden soll
   -d, --dbname=DBNAME       zu clusternde Datenbank
   -d, --dbname=DBNAME       Datenbank, in der die Sprache installiert
                            werden soll
   -d, --dbname=DBNAME       zu reindizierende Datenbank
   -e, --echo                      zeige die Befehle, die an den Server
                                  gesendet werden
   -e, --echo                   zeige die Befehle, die an den Server
                               gesendet werden
   -e, --echo                zeige die Befehle, die an den Server
                            gesendet werden
   -f, --full                      führe volles Vacuum durch
   -h, --host=HOSTNAME          Name des Datenbankservers oder Socket-Verzeichnis
   -h, --host=HOSTNAME       Name des Datenbankservers oder Socket-Verzeichnis
   -i, --index=INDEX         erneuere nur einen bestimmten Index
   -i, --inherit             Rolle erbt alle Privilegien von Rollen, deren
                            Mitglied sie ist (Voreinstellung)
   -i, --interactive         frage nach, bevor irgendetwas gelöscht wird
   -l, --list                zeige Liste gegenwärtig installierter Sprachen
   -l, --locale=LOCALE          Lokale-Einstellungen für die Datenbank
   -l, --login               Rolle kann sich anmelden (Voreinstellung)
   -p, --port=PORT              Port des Datenbankservers
   -p, --port=PORT           Port des Datenbankservers
   -q, --quiet                     unterdrücke alle Mitteilungen
   -q, --quiet               unterdrücke alle Mitteilungen
   -r, --createrole          Rolle kann neue Rollen erzeugen
   -s, --superuser           Rolle wird Superuser
   -s, --system              reindiziere Systemkataloge
   -t, --table='TABELLE[(SPALTEN)]'
                                  führe Vacuum für diese Tabelle aus
   -t, --table=TABELLE       clustere nur eine bestimmte Tabelle
   -t, --table=TABELLE       reindiziere nur eine bestimmte Tabelle
   -v, --verbose                   erzeuge viele Meldungen
   -v, --verbose             erzeuge viele Meldungen
   -w, --no-password            niemals nach Passwort fragen
   -w, --no-password         niemals nach Passwort fragen
   -z, --analyze                   aktualisiere Statistiken für den Optimierer
 %s (%s/%s)  %s säubert und analysiert eine PostgreSQL-Datenbank.

 %s clustert alle vorher geclusterten Tabellen in einer Datenbank.

 %s erzeugt eine PostgreSQL-Datenbank.

 %s erzeugt eine neue PostgreSQL-Rolle.

 %s installiert eine prozedurale Sprache in einer PostgreSQL-Datenbank.

 %s reindiziert eine PostgreSQL-Datenbank.

 %s löscht eine PostgreSQL-Datenbank.

 %s löscht eine PostgreSQL-Rolle.

 %s löscht eine prozedurale Sprache aus einer Datenbank.

 %s: »%s« ist kein gültiger Kodierungsname
 %s: kann nicht eine bestimmte Tabelle in allen Datenbanken clustern
 %s: kann nicht alle Datenbanken und eine bestimmte gleichzeitig clustern
 %s: kann nicht einen bestimmten Index und Systemkataloge gleichzeitig reindizieren
 %s: kann nicht einen bestimmten Index in allen Datenbanken reindizieren
 %s: kann nicht eine bestimmte Tabelle und Systemkataloge gleichzeitig reindizieren
 %s: kann nicht eine bestimmte Tabelle in allen Datenbanken reindizieren
 %s: kann nicht alle Datenbanken und eine bestimmte gleichzeitig reindizieren
 %s: kann nicht alle Datenbanken und Systemkataloge gleichzeitig reindizieren
 %s: kann Option »freeze« nicht verwenden, wenn nur Analyze durchgeführt wird
 %s: kann Option »full« nicht verwenden, wenn nur Analyze durchgeführt wird
 %s: kann nicht eine bestimmte Tabelle in allen Datenbanken vacuumen
 %s: kann nicht alle Datenbanken und eine bestimmte gleichzeitig vacuumen
 %s: clustere Datenbank »%s«
 %s: Clustern der Datenbank »%s« fehlgeschlagen: %s %s: Clustern der Tabelle »%s« in Datenbank »%s« fehlgeschlagen: %s %s: Erzeugen des Kommentars ist fehlgeschlagen (Datenbank wurde erzeugt): %s %s: konnte nicht mit Datenbank %s verbinden
 %s: konnte nicht mit Datenbank %s verbinden: %s %s: konnte aktuellen Benutzernamen nicht ermitteln: %s
 %s: konnte Informationen über aktuellen Benutzer nicht ermitteln: %s
 %s: Erzeugen der neuen Rolle fehlgeschlagen: %s %s: Erzeugen der Datenbank ist fehlgeschlagen: %s %s: Löschen der Datenbank fehlgeschlagen: %s %s: Sprache »%s« ist bereits in Datenbank »%s« installiert
 %s: Sprache »%s« ist nicht in Datenbank »%s« installiert
 %s: Installation der Sprache fehlgeschlagen: %s %s: Löschen der Sprache fehlgeschlagen: %s %s: Datenbankname als Argument fehlt
 %s: Sprachenname als Argument fehlt
 %s: --locale und --lc-collate können nicht zusammen angegeben werden
 %s: --locale und --lc-ctype können nicht zusammen angegeben werden
 %s: Speicher aufgebraucht
 %s: Anfrage fehlgeschlagen: %s %s: Anfrage war: %s
 %s: reindiziere Datenbank »%s«
 %s: Reindizieren der Datenbank »%s« fehlgeschlagen: %s %s: Reindizieren des Index »%s« in Datenbank »%s« fehlgeschlagen: %s %s: Reindizieren der Systemkataloge fehlgeschlagen: %s %s: Reindizieren der Tabelle »%s« in Datenbank »%s« fehlgeschlagen: %s %s: Löschen der Rolle »%s« fehlgeschlagen: %s %s: zu viele Kommandozeilenargumente (das erste ist »%s«)
 %s: führe Vacuum in Datenbank »%s« aus
 %s: Vacuum der Datenbank »%s« fehlgeschlagen: %s %s: Vacuum der Tabelle »%s« in Datenbank »%s« fehlgeschlagen: %s Sind Sie sich sicher? Abbruchsanforderung gesendet
 Konnte Abbruchsanforderung nicht senden: %s Datenbank »%s« wird unwiderruflich gelöscht werden.
 Geben Sie es noch einmal ein:  Geben Sie den Namen der neuen Rolle ein:  Geben Sie den Namen der zu löschenden Rolle ein:  Geben Sie das Passwort der neuen Rolle ein:  Name Passwortverschlüsselung ist fehlgeschlagen.
 Passwort:  Passwörter stimmten nicht überein.
 Bitte antworten Sie »%s« oder »%s«.
 Prozedurale Sprachen Rolle »%s« wird unwiderruflich gelöscht werden.
 Soll die neue Rolle ein Superuser sein? Soll die neue Rolle Datenbanken erzeugen dürfen? Soll die neue Rolle weitere neue Rollen erzeugen dürfen? Vertraut? Versuchen Sie »%s --help« für weitere Informationen.
 Aufruf:
 n nein Speicher aufgebraucht
 pg_strdup: kann NULL-Zeiger nicht kopieren (interner Fehler)
 j ja 