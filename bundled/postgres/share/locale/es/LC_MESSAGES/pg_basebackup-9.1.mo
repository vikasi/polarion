��    E      D  a   l      �     �       !     -   <     j  5   |  A   �  >   �  6   3  5   j  >   �  O   �  L   /  [   |  D   �  ,   	  7   J	  3   �	  5   �	  @   �	  /   -
  8   ]
  D   �
  Z   �
  P   6  4   �  @   �  (   �  -   &  "   T  #   w  .   �  (   �  #   �  9     &   Q  2   x  #   �  &   �     �           4  *   U  +   �  4   �  /   �  0     %   B  +   h     �  A   �  #   �  9     &   Q  -   x  !   �  (   �  "   �  ,        A  -   T  ,   �  4   �  %   �  "   
  
   -  &   8     _  �  g           9  #   O  0   s     �  ;   �  K   �  B   ?  :   �  <   �  B   �  v   =  Q   �  b     L   i  <   �  8   �  7   ,  0   d  N   �  6   �  O     D   k  Z   �  P     K   \  N   �  0   �  7   (  ,   `  '   �  6   �  .   �  +     D   G  1   �  \   �  2     +   N  .   z  2   �  &   �  3     6   7  @   n  :   �  9   �  .   $  5   S  "   �  N   �  +   �  F   '   8   n   3   �   "   �   /   �   .   .!  ;   ]!     �!  ;   �!  -   �!  J   "  -   c"  *   �"     �"  2   �"     �"     >       
   =   4   D            	                       5       !   '   ;   )   8      %   (             B                      ?                    C                 :                    -       .                  <       0   $      9       &                1                 @            2   ,   6   7      "   *      A   E       +       3   /   #       
Connection options:
 
General options:
 
Options controlling the output:
 
Report bugs to <pgsql-bugs@postgresql.org>.
   %s [OPTION]...
   --help                   show this help, then exit
   --version                output version information, then exit
   -D, --pgdata=DIRECTORY   receive base backup into directory
   -F, --format=p|t         output format (plain, tar)
   -P, --progress           show progress information
   -U, --username=NAME      connect as specified database user
   -W, --password           force password prompt (should happen automatically)
   -Z, --compress=0-9       compress tar output with given compression level
   -c, --checkpoint=fast|spread
                           set fast or spread checkpointing
   -h, --host=HOSTNAME      database server host or socket directory
   -l, --label=LABEL        set backup label
   -p, --port=PORT          database server port number
   -v, --verbose            output verbose messages
   -w, --no-password        never prompt for password
   -x, --xlog               include required WAL files in backup
   -z, --gzip               compress tar output
 %s takes a base backup of a running PostgreSQL server.

 %s/%s kB (%d%%), %d/%d tablespace %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d tablespace %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s: COPY stream ended before last file was finished
 %s: can only write single tablespace to stdout, database has %i
 %s: could not access directory "%s": %s
 %s: could not close compressed file "%s": %s
 %s: could not close file "%s": %s
 %s: could not connect to server: %s %s: could not create compressed file "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create file "%s": %s
 %s: could not create symbolic link from "%s" to "%s": %s
 %s: could not get COPY data stream: %s %s: could not get WAL end position from server: %s %s: could not get backup header: %s %s: could not initiate base backup: %s %s: could not parse file mode
 %s: could not parse file size
 %s: could not read COPY data: %s %s: could not send base backup command: %s %s: could not set compression level %i: %s
 %s: could not set permissions on directory "%s": %s
 %s: could not set permissions on file "%s": %s
 %s: could not write to compressed file "%s": %s
 %s: could not write to file "%s": %s
 %s: directory "%s" exists but is not empty
 %s: final receive failed: %s %s: invalid checkpoint argument "%s", must be "fast" or "spread"
 %s: invalid compression level "%s"
 %s: invalid output format "%s", must be "plain" or "tar"
 %s: invalid tar block header size: %i
 %s: no WAL end position returned from server
 %s: no data returned from server
 %s: no start point returned from server
 %s: no target directory specified
 %s: only tar mode backups can be compressed
 %s: out of memory
 %s: received invalid directory (too long): %s %s: this build does not support compression
 %s: too many command-line arguments (first is "%s")
 %s: unrecognized link indicator "%c"
 %s: unsupported server version %s
 Password:  Try "%s --help" for more information.
 Usage:
 Project-Id-Version: pg_basebackup (PostgreSQL 9.1)
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2013-08-26 19:45+0000
PO-Revision-Date: 2013-08-30 12:28-0400
Last-Translator: Álvaro Herrera <alvherre@alvh.no-ip.org>
Language-Team: Spanish <pgsql-es-ayuda@postgresql.org>
Language: es
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=2; plural=(n != 1);
 
Opciones de conexión:
 
Opciones generales:
 
Opciones que controlan la salida:
 
Reporte errores a <pgsql-bugs@postgresql.org>.
   %s [OPCIÓN]...
   --help                   mostrar esta ayuda, luego salir
   --version                desplegar información de versión, luego salir
   -D, --pgdata=DIRECTORIO  recibir el respaldo base en directorio
   -F, --format=p|t         formato de salida (plano, tar)
   -P, --progress           mostrar información de progreso
   -U, --username=NOMBRE    conectarse con el usuario especificado
   -W, --password           forzar un prompt para la contraseña
                           (debería ser automático)
   -Z, --compress=0-9       comprimir salida tar con el nivel de compresión dado
   -c, --checkpoint=fast|spread
                           utilizar checkpoint rápido o extendido
   -h, --host=ANFITRIÓN     dirección del servidor o directorio del socket
   -l, --label=ETIQUETA     establecer etiqueta del respaldo
   -p, --port=PORT          número de port del servidor
   -v, --verbose            desplegar mensajes verbosos
   -w, --no-password        no pedir contraseña
   -x, --xlog               incluye los archivos WAL necesarios en el respaldo
   -z, --gzip               comprimir la salida de tar
 %s obtiene un respaldo base a partir de un servidor PostgreSQL en ejecución.

 %s/%s kB (%d%%), %d/%d tablespace %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d tablespace %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s: el flujo COPY terminó antes que el último archivo estuviera completo
 %s: sólo se puede escribir un tablespace a stdout, la base de datos tiene %i
 %s: no se pudo acceder al directorio «%s»: %s
 %s: no se pudo cerrar el archivo comprimido «%s»: %s
 %s: no se pudo cerrar el archivo «%s»: %s
 %s: no se pudo conectar al servidor: %s %s: no se pudo crear el archivo comprimido «%s»: %s
 %s: no se pudo crear el directorio «%s»: %s
 %s: no se pudo crear el archivo «%s»: %s
 %s: no se pudo crear un enlace simbólico desde «%s» a «%s»: %s
 %s: no se pudo obtener un flujo de datos COPY: %s %s: no se pudo obtener la posición del final del registro de transacciones del servidor: %s %s: no se pudo obtener la cabecera de respaldo: %s %s: no se pudo iniciar el respaldo base: %s %s: nose pudo interpretar el modo del archivo
 %s: no se pudo interpretar el tamaño del archivo
 %s: no fue posible leer datos COPY: %s %s: no se pudo enviar la orden de respaldo base: %s %s: no se pudo definir el nivel de compresión %i: %s
 %s: no se pudo definir los permisos en el directorio «%s»: %s
 %s: no se pudo definir los permisos al archivo «%s»: %s
 %s: no se pudo escribir al archivo comprimido «%s»: %s
 %s: no se pudo escribir al archivo «%s»: %s
 %s: el directorio «%s» existe pero no está vacío
 %s: la recepción final falló: %s %s: argumento de checkpoint «%s» no válido, debe ser «fast» o «spread»
 %s: valor de compresión «%s» no válido
 %s: formato de salida «%s» no válido, debe ser «plain» o «tar»
 %s: tamaño de bloque de cabecera de tar no válido: %i
 %s: el servidor no retornó un punto de fin de WAL
 %s: el servidor no retornó datos
 %s: el servidor no retornó un punto de inicio
 %s: no se especificó un directorio de salida
 %s: sólo los respaldos de modo tar pueden ser comprimidos
 %s: memoria agotada
 %s: se recibió directorio no válido (demasiado largo): %s %s: esta instalación no soporta compresión
 %s: demasiados argumentos en la línea de órdenes (el primero es «%s»)
 %s: indicador de enlace «%c» no reconocido
 %s: versión del servidor %s no soportada
 Contraseña:  Use «%s --help» para obtener más información.
 Empleo:
 